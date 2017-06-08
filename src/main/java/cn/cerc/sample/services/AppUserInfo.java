package cn.cerc.sample.services;

import cn.cerc.jbean.core.CustomService;
import cn.cerc.jbean.core.DataValidateException;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.BuildQuery;
import cn.cerc.jdb.mysql.SqlQuery;

public class AppUserInfo extends CustomService {
	public boolean search() {
		Record headIn=getDataIn().getHead();
		BuildQuery bq=new BuildQuery(this);
		if(headIn.hasValue("Name_")){
			bq.byLink(new String[]{"Name_","Code_","Sex_"}, headIn.getString("Name_"));
		}
		if(headIn.hasValue("UID_")){
			bq.byField("UID_",headIn.getInt("UID_") );
		}
		bq.add("select * from %s","e_test");
		bq.open();
		getDataOut().appendDataSet(bq.getDataSet());
		return true;
	}

	public boolean deleteuser() throws DataValidateException {
		Record headIn=getDataIn().getHead();
		DataValidateException.stopRun("UID不存在", !headIn.hasValue("UID_"));
		int uid=headIn.getInt("UID_");
		SqlQuery sql = new SqlQuery(this);
		sql.add("select * from %s","e_test");
		sql.add("where UID_=%d",uid);
		sql.open();
		DataValidateException.stopRun(String.format("%d 您要删除的记录不存在", uid),sql.eof());
		getDataOut().getHead().setField("Name_", sql.getString("Name_"));
		sql.delete();
		return true;
	}
	
	public boolean append() throws DataValidateException{
		Record headIn=getDataIn().getHead();
		String code=headIn.getString("Code_");
		String name=headIn.getString("Name_");
		int age=headIn.getInt("Age_");
		String sex=headIn.getString("Sex_");
		SqlQuery sql=new SqlQuery(this);
		sql.add("select * from %s","e_test");
		sql.add("where Name_='%s'",name);
		sql.open();
		DataValidateException.stopRun(String.format("%s 用户名称已存在不允许重复!",name),!sql.eof());
		sql.append();
		sql.setField("Code_", code);
		sql.setField("Name_", name);
		sql.setField("Age_", age);
		sql.setField("Sex_", sex);
		sql.post();
		return true;
	}
	
	public boolean update() throws DataValidateException{
		Record headIn=getDataIn().getHead();
		DataValidateException.stopRun("UID不存在!", !headIn.hasValue("UID_"));
		int uid=headIn.getInt("UID_");
		String name=headIn.getString("Name_");
		int age=headIn.getInt("Age_");
		String sex=headIn.getString("Sex_");
		SqlQuery sql=new SqlQuery(this);
		sql.add("select * from %s","e_test");
		sql.add("where Name_='%s'",name);
		sql.open();
		DataValidateException.stopRun(String.format("%s 用户名称已存在，不允许修改！",name), !sql.eof());
		SqlQuery sql2=new SqlQuery(this);
		sql2.add("select * from %s","e_test");
		sql2.add("where UID_=%d",uid);
		sql2.open();
		DataValidateException.stopRun(String.format("%s 您要修改的记录不存在，不允许修改", uid), sql2.eof());
		sql2.edit();
		sql2.setField("Name_", name);
		sql2.setField("Sex_", sex);
		sql2.setField("Age_", age);
		sql2.post();
		return true;
	}
}
