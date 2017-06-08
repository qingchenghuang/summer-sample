package cn.cerc.sample.services;

import cn.cerc.jbean.core.CustomService;
import cn.cerc.jdb.core.DataSet;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.BuildQuery;
import cn.cerc.jdb.mysql.SqlQuery;

public class AppInventory extends CustomService {
	public boolean search() {
		Record headIn = getDataIn().getHead();
		String start = headIn.getString("Start_");
		String end = headIn.getString("End_");
		String productname = headIn.getString("ProductName_");
		Double abnumber;
		Double bcnumber;
		Double aenumber;
		DataSet ds = new DataSet();
		SqlQuery sql = new SqlQuery(this);
		sql.add("select * from %s", "e_product");
		sql.open();
		while (sql.fetch()) {
//			if(sql2.locate("Code_", "P001")){
//				sql2.get
//			}
			abnumber=0.0; bcnumber=0.0; aenumber=0.0;
			SqlQuery sql2 = new SqlQuery(this);
			sql2.add("select * from %s", "e_tranb");
			if (productname == null || "".equals(productname)) {
				sql2.add("where Code_='%s'",sql.getString("Code_"));
			}else{
				sql2.add("where Code_='%s' and Name_ like '%%%s%%'",sql.getString("Code_"),productname);
			}
			sql2.open();
			if (!sql2.eof()) {
				while (sql2.fetch()) {
					SqlQuery sql3 = new SqlQuery(this);
					sql3.add("select * from %s", "e_tranh");
					sql3.add(
							"where TBNo_='%s' and TBDate_>='%s' and TBDate_<='%s'",
							sql2.getString("TBNo_"), start, end);
					sql3.open();
					if (!sql3.eof()) {
						if (sql3.getString("TBType_") == "AB"
								|| "AB".equals(sql3.getString("TBType_"))) {
							abnumber += sql2.getDouble("Num_");
						}
						if (sql3.getString("TBType_") == "BC"
								|| "BC".equals(sql3.getString("TBType_"))) {
							bcnumber += sql2.getDouble("Num_");
						}
						if (sql3.getString("TBType_") == "AE"
								|| "AE".equals(sql3.getString("TBType_"))) {
							aenumber += sql2.getDouble("Num_");
						}
					}
				}
				ds.append();
				ds.setField("Name_", sql.getString("Name_"));
				ds.setField("ABNumber_", abnumber);
				ds.setField("BCNumber_", bcnumber);
				ds.setField("AENumber_", aenumber);
				ds.post();
			}
		}
		getDataOut().appendDataSet(ds);
		return true;
	}
	
	
    public boolean searchInventory(){
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
}
