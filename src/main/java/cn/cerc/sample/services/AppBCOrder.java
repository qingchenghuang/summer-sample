package cn.cerc.sample.services;

import cn.cerc.jbean.core.CustomService;
import cn.cerc.jbean.core.DataValidateException;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.BuildQuery;
import cn.cerc.jdb.mysql.SqlQuery;

public class AppBCOrder extends CustomService {
	public boolean searchBClist() {
		Record headIn = getDataIn().getHead();
		BuildQuery bq = new BuildQuery(this);
		String name = headIn.getString("Name_");
		if (headIn.hasValue("Name_")) {
			bq.byLink(new String[] { "TBNo_", "UserName_", "TBType_" }, name);
		}
		if (headIn.hasValue("UID_")) {
			bq.byField("UID_", headIn.getInt("UID_"));
		}
		bq.byField("TBType_", "BC");
		bq.add("select * from %s ", "e_tranh");
		bq.open();
		getDataOut().appendDataSet(bq.getDataSet());
		return true;
	}

	// 查询相应单号 里的商品列表
	public boolean search() throws DataValidateException {
		Record headIn = getDataIn().getHead();
		String code = headIn.getString("Code_");
		String tbno = headIn.getString("TBNo_");
		String name = headIn.getString("Name_");
		SqlQuery sql4 = new SqlQuery(this);
		sql4.add("select * from %s ", "e_tranh");
		sql4.add("where TBNo_= '%s' ", tbno);
		sql4.open();
		if (sql4.eof()) {
			sql4.append();
			sql4.setField("TBType_", "BC");
			sql4.setField("TBNo_", tbno);
			sql4.setField("TBDate_", headIn.getDateTime("TBDate_"));
			sql4.setField("UserName_", name);
			sql4.post();
		}
		if (code != null && !"".equals(code)) {
			SqlQuery sql5 = new SqlQuery(this);
			sql5.add("select * from %s ", "e_tranb");
			sql5.add("where TBNo_= '%s' and Code_='%s'", tbno, code);
			sql5.open();
			SqlQuery sql = new SqlQuery(this);
			sql.add("select * from %s ", "e_product");
			sql.add("where Code_= '%s' ", code);
			sql.open();
			if (sql.getDouble("Stock_") >= 1) {
				if (sql5.eof()) {
					SqlQuery sql2 = new SqlQuery(this);
					sql2.add("select * from %s", "e_tranb");
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
				} else {
					sql5.edit();
					Double number = sql5.getDouble("Num_") + 1;
					sql5.setField("Num_", number);
					sql5.setField("Total_", number * sql5.getDouble("Price_"));
					sql5.post();
				}
				if (!sql.eof()) {
					sql.edit();
					sql.setField("Stock_", sql.getDouble("Stock_") - 1);
					sql.post();
				}
			} else {
				getDataOut().getHead().setField("msg", String.format("%s库存数量不足！" ,sql.getString("Name_")));
			}
		}
		SqlQuery sql3 = new SqlQuery(this);
		sql3.add("select * from %s", "e_tranb");
		sql3.add("where TBNo_='%s'", tbno);
		sql3.open();
		getDataOut().appendDataSet(sql3);
		return true;
	}
    
	//修改订单商品信息
	public boolean modifyproduct() throws DataValidateException{
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
		if(sql2.getDouble("Stock_")+sql.getDouble("Num_")-number>=0){
			sql2.edit();
			sql.edit();
			sql2.setField("Stock_",sql2.getDouble("Stock_")-number+sql.getDouble("Num_"));
			sql2.post();
			sql.setField("Num_", number);
			sql.setField("Price_", price);
			sql.setField("Total_", number*price);
			sql.post();
			getDataOut().appendDataSet(sql);
			getDataOut().getHead().setField("msg", String.format("%s商品信息修改成功！", sql2.getString("Name_")));
		}else{
			getDataOut().appendDataSet(sql);
			getDataOut().getHead().setField("msg", String.format("%s商品库存不足，修改失败！", sql2.getString("Name_")));
			//DataValidateException.stopRun(String.format("%s 库存数量不足", productname), sql2.getDouble("Stock_")+sql.getDouble("Num_")-number<0);
		}
		
		return true;
	}
	
	// //查询相应单号 里的商品列表
	public boolean searchOrderB() {
		Record headIn = getDataIn().getHead();
		String code = headIn.getString("Code_");
		String tbno = headIn.getString("TBNo_");
		if (code != null && !"".equals(code)) {
			SqlQuery sql5 = new SqlQuery(this);
			sql5.add("select * from %s ", "e_tranb");
			sql5.add("where TBNo_= '%s' and Code_='%s'", tbno, code);
			sql5.open();
			SqlQuery sql = new SqlQuery(this);
			sql.add("select * from %s ", "e_product");
			sql.add("where Code_= '%s' ", code);
			sql.open();
			if (sql.getDouble("Stock_") >= 1) {
				if (sql5.eof()) {
					SqlQuery sql2 = new SqlQuery(this);
					sql2.add("select * from %s", "e_tranb");
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
				} else {
					sql5.edit();
					Double number = sql5.getDouble("Num_") + 1;
					sql5.setField("Num_", number);
					sql5.setField("Total_", number * sql5.getDouble("Price_"));
					sql5.post();
				}
				if (!sql.eof()) {
					sql.edit();
					sql.setField("Stock_", sql.getDouble("Stock_") - 1);
					sql.post();
				}
			} else {
				getDataOut().getHead().setField("msg", String.format("%s库存数量不足！" ,sql.getString("Name_")));
			}

		}
		SqlQuery sql3 = new SqlQuery(this);
		sql3.add("select * from %s", "e_tranb");
		sql3.add("where TBNo_='%s'", tbno);
		sql3.open();
		getDataOut().appendDataSet(sql3);
		return true;
	}

	// 删除销售单内商品信息
	public boolean delete() {
		Record headIn = getDataIn().getHead();
		String tbno = headIn.getString("TBNo_");
		String productname = headIn.getString("ProductName_");
		SqlQuery sql = new SqlQuery(this);
		sql.add("select * from %s", "e_tranb");
		sql.add("where TBNo_='%s' and Name_='%s' ", tbno, productname);
		sql.open();
		SqlQuery sql2 = new SqlQuery(this);
    	sql2.add("select * from %s","e_product");
    	sql2.add("where Name_='%s'",productname);
    	sql2.open();
    	sql2.edit();
    	sql2.setField("Stock_", sql2.getDouble("Stock_")+sql.getDouble("Num_"));
    	sql2.post();
    	sql.delete();
    	SqlQuery sql3=new SqlQuery(this);
 		sql3.add("select * from %s","e_tranb");
 		sql3.add("where TBNo_='%s'",tbno);
 		sql3.open();
        getDataOut().appendDataSet(sql3);
		return true;
	}

	// 添加单头
	public boolean AppendOrderH() throws DataValidateException {
		Record headIn = getDataIn().getHead();
		String tbno = headIn.getString("TBNo_");
		String name = headIn.getString("Name_");
		String date = headIn.getString("TBDate_");
		SqlQuery sql = new SqlQuery(this);
		sql.add("select * from %s", "e_tranh");
		sql.add("where TBNo_='%s'", tbno);
		sql.open();
		DataValidateException.stopRun(String.format("%s 进货单号已存在！", tbno),
				!sql.eof());
		sql.append();
		sql.setField("TBType_", "BC");
		sql.setField("TBNo_", tbno);
		sql.setField("TBDate_", date);
		sql.setField("UserName_", name);
		sql.post();
		getDataOut().appendDataSet(sql);
		return true;
	}
	//删除销售单
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
        		sql3.edit();
        		SqlQuery sql4=new SqlQuery(this);
            	sql4.add("select * from %s","e_tranb");
            	sql4.add("where TBNo_='%s' and Code_='%s'",tbno,sql2.getString("Code_"));
            	sql4.open();
            	sql3.setField("Stock_", sql3.getDouble("Stock_")+sql4.getDouble("Num_"));
            	sql3.post();
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
    	bq.byField("TBType_", "BC");
    	bq.add("select * from %s ", "e_tranh");
		bq.open();
		getDataOut().appendDataSet(bq.getDataSet());
		return true;
	}
	
}
