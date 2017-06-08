package cn.cerc.sample.services;

import cn.cerc.jbean.core.CustomService;
import cn.cerc.jbean.core.DataValidateException;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.BuildQuery;
import cn.cerc.jdb.mysql.SqlQuery;

public class AppAEOrder extends CustomService {
	public boolean searchAE() {
		Record headIn = getDataIn().getHead();
		BuildQuery bq = new BuildQuery(this);
		String name = headIn.getString("Name_");
		if (headIn.hasValue("Name_")) {
			bq.byLink(new String[] { "TBNo_", "UserName_", "TBType_" }, name);
		}
		if (headIn.hasValue("UID_")) {
			bq.byField("UID_", headIn.getInt("UID_"));
		}
		bq.byField("TBType_", "AE");
		bq.add("select * from %s ", "e_tranh");
		bq.open();
		getDataOut().appendDataSet(bq.getDataSet());
		return true;
	}

	public boolean AppendOrderH() throws DataValidateException {
		Record headIn = getDataIn().getHead();
		String tbno = headIn.getString("TBNo_");
		String name = headIn.getString("Name_");
		String date = headIn.getString("TBDate_");
		SqlQuery sql = new SqlQuery(this);
		sql.add("select * from %s", "e_tranh");
		sql.add("where TBNo_='%s'", tbno);
		sql.open();
		DataValidateException.stopRun(String.format("%s 盘点单号已存在！", tbno),
				!sql.eof());
		sql.append();
		sql.setField("TBType_", "AE");
		sql.setField("TBNo_", tbno);
		sql.setField("TBDate_", date);
		sql.setField("UserName_", name);
		sql.post();
		getDataOut().appendDataSet(sql);
		return true;
	}
	//查询要修改商品信息
	public boolean modifysearchproduct(){
		Record headIn = getDataIn().getHead();
		String tbno = headIn.getString("TBNo_");
    	String productname = headIn.getString("Name_");
    	SqlQuery sql = new SqlQuery(this);
    	sql.add("select * from %s","e_tranb");
    	sql.add("where TBNo_='%s' and Name_='%s' ",tbno,productname);
        sql.open();
        getDataOut().appendDataSet(sql);
		return true;
	}
	//修改单内商品信息
	public boolean modifyproduct(){
		Record headIn=getDataIn().getHead();
		String tbno = headIn.getString("TBNo_");
		String productname = headIn.getString("Name_");
		Double number = headIn.getDouble("Num_");
		Double price=headIn.getDouble("Price_");
		SqlQuery sql2=new SqlQuery(this);
		sql2.add("select * from %s","e_product");
		sql2.add("where Name_='%s' ",productname);
		sql2.open();
		SqlQuery sql=new SqlQuery(this);
		sql.add("select * from %s","e_tranb");
		sql.add("where TBNo_='%s' and Name_='%s' ",tbno,productname);
		sql.open();
		sql.edit();
		if(number>0){
			sql2.edit();
			sql2.setField("Stock_", number);
			sql2.post();
			sql.setField("Num_", number);
		}
		sql.setField("Price_", price);
		sql.setField("Total_", number*price);
		sql.post();
		getDataOut().appendDataSet(sql);
		return true;
	}
	public boolean searchOrderB() {
		Record headIn = getDataIn().getHead();
		String code = headIn.getString("Code_");
		String tbno = headIn.getString("TBNo_");
	//	String name = headIn.getString("Name_");
		if (code != null && !"".equals(code)) {
			SqlQuery sql5 = new SqlQuery(this);
			sql5.add("select * from %s ", "e_tranb");
			sql5.add("where TBNo_= '%s' and Code_='%s'", tbno, code);
			sql5.open();
			SqlQuery sql = new SqlQuery(this);
			sql.add("select * from %s ", "e_product");
			sql.add("where Code_= '%s' ", code);
			sql.open();
			if(sql5.eof()){
				SqlQuery sql2 = new SqlQuery(this);
				sql2.add("select * from %s", "e_tranb");
				sql2.open();
				sql2.append();
				sql2.setField("TBNo_", tbno);
				sql2.setField("Code_", code);
				sql2.setField("Name_", sql.getString("Name_"));
				sql2.setField("Unit_", sql.getString("Unit_"));
				sql2.setField("Num_", 0);
				sql2.setField("Inventory_",sql.getDouble("Stock_"));
				sql2.setField("Price_", sql.getDouble("Price_"));
				sql2.setField("Total_", sql.getDouble("Price_"));
				sql2.post();
			}
		}
		SqlQuery sql3 = new SqlQuery(this);
		sql3.add("select * from %s", "e_tranb");
		sql3.add("where TBNo_='%s'", tbno);
		sql3.open();
		getDataOut().appendDataSet(sql3);
		return true;
	}
	//删除单内商品信息
	public boolean delete(){
		Record headIn=getDataIn().getHead();
    	String tbno = headIn.getString("TBNo_");
    	String productname = headIn.getString("ProductName_");
    	SqlQuery sql = new SqlQuery(this);
    	sql.add("select * from %s","e_tranb");
    	sql.add("where TBNo_='%s' and Name_='%s' ",tbno,productname);
    	sql.open();
    	SqlQuery sql2 = new SqlQuery(this);
    	sql2.add("select * from %s","e_product");
    	sql2.add("where Name_='%s'",productname);
    	sql2.open();
    	if(!sql.eof()){
    		sql2.edit();
    		sql2.setField("Stock_",sql.getDouble("Inventory_"));
    		sql2.post();
    		sql.delete();
    	}
    	SqlQuery sql3=new SqlQuery(this);
 		sql3.add("select * from %s","e_tranb");
 		sql3.add("where TBNo_='%s'",tbno);
 		sql3.open();
        getDataOut().appendDataSet(sql3);
		return true;
	}
	
	//删除盘点单
		public boolean deleteOrder() throws DataValidateException{
			Record headIn=getDataIn().getHead();
	    	String tbno=headIn.getString("TBNo_");
	    	String name=headIn.getString("Name_");
	    	if(name==null || "".equals(name)){
	    		SqlQuery sql=new SqlQuery(this);
	        	sql.add("select * from %s","e_tranh");
	        	sql.add("where TBNo_='%s'",tbno);
	        	sql.open();
	        	DataValidateException.stopRun("您要删除的记录不存在！", sql.eof());
	        	SqlQuery sql2=new SqlQuery(this);
	        	sql2.add("select * from %s","e_tranb");
	        	sql2.add("where TBNo_='%s'",tbno);
	        	sql2.open();
	        	while(sql2.fetch()){
	        		String code=sql2.getString("Code_");
	        		SqlQuery sql3=new SqlQuery(this);
	        		sql3.add("select * from %s","e_product");
	        		sql3.add("where Code_='%s'",code);
	        		sql3.open();
	        		SqlQuery sql4=new SqlQuery(this);
	            	sql4.add("select * from %s","e_tranb");
	            	sql4.add("where TBNo_='%s' and Code_='%s'",tbno,sql2.getString("Code_"));
	            	sql4.open();
	            	if(sql4.getDouble("Num_")!=0){
	            		sql3.edit();
	            		sql3.setField("Stock_",sql4.getDouble("Inventory_"));
	            		sql3.post();
	            	}
	            	sql2.delete();
	        	}
	        	sql.delete();
	    	}
	    	BuildQuery bq=new BuildQuery(this);
	    	if(headIn.hasValue("Name_")){
	    		bq.byLink(new String[]{"TBNo_","UserName_","TBType_"},name);
	    	}
	    	
	    	if (headIn.hasValue("UID_")) {
				bq.byField("UID_", headIn.getInt("UID_"));
			}
	    	bq.byField("TBType_", "AE");
	    	bq.add("select * from %s ", "e_tranh");
			bq.open();
			getDataOut().appendDataSet(bq.getDataSet());
			return true;
		}
}
