package cn.cerc.sample.services;

import cn.cerc.jbean.core.CustomService;
import cn.cerc.jbean.core.DataValidateException;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.BuildQuery;
import cn.cerc.jdb.mysql.SqlQuery;

public class AppProductInfo extends CustomService {
	public boolean search() {
		Record headIn = getDataIn().getHead();
		BuildQuery bq = new BuildQuery(this);
		if (headIn.hasValue("Name_")) {
			bq.byLink(new String[] { "Name_", "Code_", "Unit_" },
					headIn.getString("Name_"));
		}
		if (headIn.hasValue("UID_")) {
			bq.byField("UID_", headIn.getInt("UID_"));
		}
		bq.add("select * from %s", "e_product");
		bq.open();
		getDataOut().appendDataSet(bq.getDataSet());
		return true;
	}

	public boolean delete() throws DataValidateException {
		Record headIn = getDataIn().getHead();
		DataValidateException.stopRun("UID不存在！", !headIn.hasValue("UID_"));
		int uid = headIn.getInt("UID_");
		SqlQuery sql = new SqlQuery(this);
		sql.add("select * from %s", "e_product");
		sql.add("where UID_=%d", uid);
		sql.open();
		DataValidateException.stopRun(String.format("%d 您要删除的商品不存在，请核查！", uid),
				sql.eof());
		String code=sql.getString("Code_");
		SqlQuery sql2 = new SqlQuery(this);
		sql2.add("select * from %s", "e_tranb");
		sql2.add("where Code_='%s'",code);
		sql2.open();
		if(sql2.size()>0){
			getDataOut().appendDataSet(sql);
			DataValidateException.stopRun(String.format("%s 商品还存有单据信息无法删除，请先删除单据信息！", sql2.getString("Name_")), !sql2.eof());
		}
		getDataOut().getHead().setField("Name_", sql.getField("Name_"));
		if(sql2.size()==0){
			sql.delete();
		}
		//getDataOut().setField("Name_", sql.getField("Name_"));
		
		return true;
	}

	public boolean append() throws DataValidateException {
		Record headIn = getDataIn().getHead();
		String code = headIn.getString("Code_");
		String name = headIn.getString("Name_");
		String unit = headIn.getString("Unit_");
		String status = headIn.getString("Status_");
		String description = headIn.getString("Description_");
		Double price = headIn.getDouble("Price_");
		Double weight = headIn.getDouble("Weight_");
		int stock = headIn.getInt("Stock_");
		SqlQuery sql2 = new SqlQuery(this);
		sql2.add("select * from %s", "e_product");
		sql2.add("where Code_='%s'", code);
		sql2.open();
		DataValidateException.stopRun(String.format("%s 商品代码已存在不允许重复！", code),
				!sql2.eof());
		SqlQuery sql = new SqlQuery(this);
		sql.add("select * from %s", "e_product");
		sql.add("where Name_='%s'", name);
		sql.open();
		DataValidateException.stopRun(String.format("%s 商品名称已存在不允许重复!", name),
				!sql.eof());
		sql.append();
		sql.setField("Code_", code);
		sql.setField("Name_", name);
		sql.setField("Unit_", unit);
		sql.setField("Stock_", stock);
		sql.setField("Status_", status);
		sql.setField("Description_", description);
		sql.setField("Price_", price);
		sql.setField("Weight_", weight);
		sql.post();
		return true;
	}

	public boolean update() throws DataValidateException {
		Record headIn = getDataIn().getHead();
		DataValidateException.stopRun("UID不存在!", !headIn.hasValue("UID_"));
		int uid = headIn.getInt("UID_");
		String name = headIn.getString("Name_");
		String unit = headIn.getString("Unit_");
		Double price = headIn.getDouble("Price_");
		Double stock = headIn.getDouble("Stock_");
		Double weight = headIn.getDouble("Weight_");
		String description = headIn.getString("Description_");
		SqlQuery sql = new SqlQuery(this);
		sql.add("select * from %s", "e_product");
		sql.add("where Name_='%s'", name);
		sql.open();
		DataValidateException.stopRun(String.format("%s 商品名称已存在，不允许修改！", name),
				!sql.eof());
		SqlQuery sql2 = new SqlQuery(this);
		sql2.add("select * from %s", "e_product");
		sql2.add("where UID_=%d", uid);
		sql2.open();
		DataValidateException.stopRun(
				String.format("%s 您要修改的记录不存在，不允许修改", uid), sql2.eof());
		sql2.edit();
		sql2.setField("Name_", name);
		sql2.setField("Unit_", unit);
		sql2.setField("Price_", price);
		sql2.setField("Stock_", stock);
		sql2.setField("Weight_", weight);
		sql2.setField("Description_", description);
		sql2.post();
		return true;
	}
}
