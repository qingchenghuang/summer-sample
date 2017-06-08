package cn.cerc.sample.forms;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jxl.write.DateTime;
import cn.cerc.jbean.client.LocalService;
import cn.cerc.jbean.form.IPage;
import cn.cerc.jmis.form.AbstractForm;
import cn.cerc.jmis.page.JspPage;
import cn.cerc.jpage.fields.ItField;
import cn.cerc.jpage.fields.StringField;
import cn.cerc.jpage.grid.AbstractGrid;
import cn.cerc.jpage.grid.DataGrid;
import cn.cerc.jpage.other.OperaPages;

public class Inventory extends AbstractForm {

	@Override
	public IPage execute() throws Exception {
		JspPage jspPage=new JspPage(this,"common/inventory.jsp");
		LocalService svr = new LocalService(this);
		svr.setService("AppInventory.searchInventory");
		svr.getDataIn().getHead().setField("Name_", getRequest().getParameter("name"));
		if (!svr.exec()) {
			jspPage.add("msg",svr.getMessage());
		}
		AbstractGrid grid = new DataGrid(this, null);
		grid.setDataSet(svr.getDataOut());
		new ItField(grid);
		new StringField(grid, "商品代码", "Code_", 5);
		new StringField(grid, "商品名称", "Name_", 7);
		new StringField(grid, "单位", "Unit_", 5);
		new StringField(grid,"价格","Price_",5);
		new StringField(grid,"库存","Stock_",4);
		new StringField(grid,"重量kg","Weight_",5);
		new StringField(grid,"描述","Description_",11);
		jspPage.add("page", new OperaPages(this, grid.getPages()));
		jspPage.add("dataGrid", grid);
		return jspPage;
	}
	
	public IPage AllOrder() throws Exception{
		JspPage jspPage= new JspPage(this,"common/allorder.jsp");
		String submit =getRequest().getParameter("submit");
		LocalService svr=new LocalService(this);
		String productname=getRequest().getParameter("productname");
		String start=getRequest().getParameter("start");
		String end=getRequest().getParameter("end");
		if(submit==null || "".equals(submit)){
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
			 Date date = new Date();//获取当前时间    
			 Calendar calendar = Calendar.getInstance();    
			 calendar.setTime(date);    
			 calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间    
			 calendar.getTime();//获取一个月前的时间    
		     start=	sdf.format(calendar.getTime());
		     calendar.add(Calendar.MONTH, +1);
		     end=sdf.format(calendar.getTime());
		}
		svr.setService("AppInventory.search");
		svr.getDataIn().getHead().setField("Start_", start);
		svr.getDataIn().getHead().setField("End_", end);
		svr.getDataIn().getHead().setField("ProductName_", productname);
		if (!svr.exec()) {
			jspPage.add("msg", svr.getMessage());
		}
		AbstractGrid grid = new DataGrid(this, null);
		grid.setDataSet(svr.getDataOut());
		new ItField(grid);
		new StringField(grid, "商品名称", "Name_", 7);
		new StringField(grid, "进货数量", "ABNumber_", 4);
		new StringField(grid, "销售数量", "BCNumber_", 4);
		new StringField(grid, "盘点数量", "AENumber_", 5);
		jspPage.add("page", new OperaPages(this, grid.getPages()));
		jspPage.add("dataGrid", grid);
		jspPage.add("start", start);
	    jspPage.add("end", end);
	    jspPage.add("productname", productname);
		return jspPage;
		
	}
	
	@Override
	public boolean logon(){
		return true;
	}
}
