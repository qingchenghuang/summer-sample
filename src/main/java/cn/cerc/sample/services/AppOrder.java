package cn.cerc.sample.services;

import cn.cerc.jbean.core.CustomService;
import cn.cerc.jbean.core.DataValidateException;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.BuildQuery;
import cn.cerc.jdb.mysql.SqlQuery;

public class AppOrder extends CustomService {
	//查询所有商品列表
	public boolean searchList() {
		Record headIn = getDataIn().getHead();
		BuildQuery bq = new BuildQuery(this);
		System.out.println(headIn.getString("Name_"));
		if (headIn.hasValue("Name_")) {
			bq.byLink(new String[] {"Name_", "Code_", "Unit_" },
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
   ////查询相应单号 里的商品列表
	public boolean searchOrderB() throws DataValidateException {
		Record headIn = getDataIn().getHead();
		String code = headIn.getString("Code_");
		String tbno = headIn.getString("TBNo_");
		if(code!=null && !"".equals(code)){
			SqlQuery sql5 = new SqlQuery(this);
			sql5.add("select * from %s ", "e_tranb");
			sql5.add("where TBNo_= '%s' and Code_='%s'", tbno ,code);
			sql5.open();
			SqlQuery sql = new SqlQuery(this);
			sql.add("select * from %s ", "e_product");
			sql.add("where Code_= '%s' ", code);
			sql.open();
			if(sql5.eof()){
					SqlQuery sql2=new SqlQuery(this);
					sql2.add("select * from %s","e_tranb");
					sql2.open();
					sql2.append();
					sql2.setField("TBNo_", tbno);
					sql2.setField("Code_", code);
					sql2.setField("Name_", sql.getString("Name_"));
					sql2.setField("Unit_", sql.getString("Unit_"));
					sql2.setField("Num_", 1);
					sql2.setField("Price_", sql.getDouble("Price_"));
					sql2.setField("Total_", sql.getDouble("Price_"));
					sql2.post();
			}else{
					sql5.edit();
					Double number=sql5.getDouble("Num_")+1;
					sql5.setField("Num_", number);
					sql5.setField("Total_", number*sql5.getDouble("Price_"));
					sql5.post();
			}
			if(!sql.eof()){
				sql.edit();
				sql.setField("Stock_", sql.getDouble("Stock_")+1);
				sql.post();
			}
		}
		SqlQuery sql3=new SqlQuery(this);
		sql3.add("select * from %s","e_tranb");
		sql3.add("where TBNo_='%s'",tbno);
		sql3.open();
		getDataOut().appendDataSet(sql3);
		return true;
	}
	//查询相应单号 里的商品列表
	public boolean search() throws DataValidateException {
		Record headIn = getDataIn().getHead();
		// DataValidateException.stopRun(String.format("%s 单号不存在!",headIn.getString("TBNo_")),
		// !headIn.hasValue("TBNo_"));
		// DataValidateException.stopRun("商品Code不存在!",
		// !headIn.hasValue("Code_"));
		String code = headIn.getString("Code_");
		String tbno = headIn.getString("TBNo_");
		String name =headIn.getString("Name_");
		SqlQuery sql4 = new SqlQuery(this);
		sql4.add("select * from %s ", "e_tranh");
		sql4.add("where TBNo_= '%s' ", tbno);
		sql4.open();
		if(sql4.eof()){
			sql4.append();
			sql4.setField("TBType_", "AB");
			sql4.setField("TBNo_", tbno);
			sql4.setField("TBDate_", headIn.getDateTime("TBDate_"));
			sql4.setField("UserName_", name);
			sql4.post();
		}
		if(code!=null && !"".equals(code)){
			SqlQuery sql5 = new SqlQuery(this);
			sql5.add("select * from %s ", "e_tranb");
			sql5.add("where TBNo_= '%s' and Code_='%s'", tbno ,code);
			sql5.open();
			SqlQuery sql = new SqlQuery(this);
			sql.add("select * from %s ", "e_product");
			sql.add("where Code_= '%s' ", code);
			sql.open();
			if(sql5.eof()){
					SqlQuery sql2=new SqlQuery(this);
					sql2.add("select * from %s","e_tranb");
					sql2.open();
					sql2.append();
					sql2.setField("TBNo_", tbno);
					sql2.setField("Code_", code);
					sql2.setField("Name_", sql.getString("Name_"));
					sql2.setField("Unit_", sql.getString("Unit_"));
					sql2.setField("Num_", 1);
					sql2.setField("Price_", sql.getDouble("Price_"));
					sql2.setField("Total_", sql.getDouble("Price_"));
					sql2.post();
			}else{
					sql5.edit();
					Double number=sql5.getDouble("Num_")+1;
					sql5.setField("Num_", number);
					sql5.setField("Total_", number*sql5.getDouble("Price_"));
					sql5.post();
			}
			if(!sql.eof()){
				sql.edit();
				sql.setField("Stock_", sql.getDouble("Stock_")+1);
				sql.post();
			}
		}
		SqlQuery sql3=new SqlQuery(this);
		sql3.add("select * from %s","e_tranb");
		sql3.add("where TBNo_='%s'",tbno);
		sql3.open();
		getDataOut().appendDataSet(sql3);
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
	//修改订单商品信息
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
		sql2.edit();
		SqlQuery sql=new SqlQuery(this);
		sql.add("select * from %s","e_tranb");
		sql.add("where TBNo_='%s' and Name_='%s' ",tbno,productname);
		sql.open();
		sql.edit();
		Double oldnumber=sql.getDouble("Num_");
		sql2.setField("Stock_",sql2.getDouble("Stock_")+number-oldnumber);
		sql2.post();
		sql.setField("Num_", number);
		sql.setField("Price_", price);
		sql.setField("Total_", number*price);
		sql.post();
		getDataOut().appendDataSet(sql);
		return true;
	}
    public boolean modify(){
    	Record headIn = getDataIn().getHead();
    	String tbno = headIn.getString("TBNo_");
    	String productname = headIn.getString("ProductName_");
    	SqlQuery sql = new SqlQuery(this);
    	sql.add("select * from %s","e_tranb");
    	sql.add("where TBNo_='%s' and Name_='%s' ",tbno,productname);
        sql.open();
        sql.edit();
        Double number=sql.getDouble("Num_")+1;
        sql.setField("Num_", number);
        sql.setField("Total_", number*sql.getDouble("Price_"));
        sql.post();
        SqlQuery sql3=new SqlQuery(this);
		sql3.add("select * from %s","e_tranb");
		sql3.add("where TBNo_='%s'",tbno);
		sql3.open();
        getDataOut().appendDataSet(sql3);
    	return true;
    }
    //删除进货单内商品信息
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
    	sql2.edit();
    	sql2.setField("Stock_", sql2.getDouble("Stock_")-sql.getDouble("Num_"));
    	sql2.post();
    	sql.delete();
    	SqlQuery sql3=new SqlQuery(this);
 		sql3.add("select * from %s","e_tranb");
 		sql3.add("where TBNo_='%s'",tbno);
 		sql3.open();
        getDataOut().appendDataSet(sql3);
    	return true;
    }
    //查询所有进货单信心列表
    public boolean searchAB(){
    	Record headIn=getDataIn().getHead();
    	BuildQuery bq=new BuildQuery(this);
    	String name=headIn.getString("Name_");
    	if(headIn.hasValue("Name_")){
    		bq.byLink(new String[]{"TBNo_","UserName_","TBType_"},name);
    	}
    	
    	if (headIn.hasValue("UID_")) {
			bq.byField("UID_", headIn.getInt("UID_"));
		}
    	bq.byField("TBType_", "AB");
    	bq.add("select * from %s ", "e_tranh");
		bq.open();
		getDataOut().appendDataSet(bq.getDataSet());
    	return true;
    }
    
    //删除进货单
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
        	sql.delete();
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
        		sql3.edit();
        		SqlQuery sql4=new SqlQuery(this);
            	sql4.add("select * from %s","e_tranb");
            	sql4.add("where TBNo_='%s' and Code_='%s'",tbno,sql2.getString("Code_"));
            	sql4.open();
            	sql3.setField("Stock_", sql3.getDouble("Stock_")-sql4.getDouble("Num_"));
            	sql3.post();
            	sql2.delete();
        	}	
    	}
    	BuildQuery bq=new BuildQuery(this);
    	if(headIn.hasValue("Name_")){
    		bq.byLink(new String[]{"TBNo_","UserName_","TBType_"},name);
    	}
    	
    	if (headIn.hasValue("UID_")) {
			bq.byField("UID_", headIn.getInt("UID_"));
		}
    	bq.byField("TBType_", "AB");
    	bq.add("select * from %s", "e_tranh");
		bq.open();
		getDataOut().appendDataSet(bq.getDataSet());
    	return true;
    }
    public boolean AppendOrderH() throws DataValidateException{
    	Record headIn=getDataIn().getHead();
    	String tbno=headIn.getString("TBNo_");
    	String name=headIn.getString("Name_");
    	String date=headIn.getString("TBDate_");
    	SqlQuery sql=new SqlQuery(this);
    	sql.add("select * from %s","e_tranh");
    	sql.add("where TBNo_='%s'",tbno);
    	sql.open();
    	DataValidateException.stopRun(String.format("%s 进货单号已存在！", tbno), !sql.eof());
    	sql.append();
    	sql.setField("TBType_", "AB");
    	sql.setField("TBNo_", tbno);
    	sql.setField("TBDate_", date);
    	sql.setField("UserName_", name);
    	sql.post();
    	getDataOut().appendDataSet(sql);
    	return true;
    }
    
   public boolean modifyOrderH(){
		Record headIn = getDataIn().getHead();
		String tbno = headIn.getString("TBNo_");
		String name = headIn.getString("Name_");
		String date = headIn.getString("TBDate_");
		SqlQuery sql = new SqlQuery(this);
		sql.add("select * from %s", "e_tranh");
		sql.add("where TBNo_='%s'", tbno);
		sql.open();
		sql.edit();
		sql.setField("TBDate_", date);
    	sql.setField("UserName_", name);
    	sql.post();
    	getDataOut().appendDataSet(sql);
		return true;
   }
}
