package cn.cerc.sample.forms;

import cn.cerc.jbean.client.LocalService;
import cn.cerc.jbean.form.IPage;
import cn.cerc.jdb.core.Record;
import cn.cerc.jmis.form.AbstractForm;
import cn.cerc.jmis.page.JspPage;
import cn.cerc.jmis.page.RedirectPage;
import cn.cerc.jpage.fields.DoubleField;
import cn.cerc.jpage.fields.ItField;
import cn.cerc.jpage.fields.OperaField;
import cn.cerc.jpage.fields.StringField;
import cn.cerc.jpage.grid.AbstractGrid;
import cn.cerc.jpage.grid.DataGrid;
import cn.cerc.jpage.other.OperaPages;

public class Product extends AbstractForm {

	@Override
	public IPage execute() throws Exception {
		JspPage jspPage = new JspPage(this, "common/product.jsp");
		LocalService svr = new LocalService(this);
		String name = getRequest().getParameter("name");
		Record head = svr.getDataIn().getHead();
		head.setField("Name_", name);
		svr.setService("AppProductInfo.search");
		if (!svr.exec()) {
			jspPage.setMessage(svr.getMessage());
		}
		AbstractGrid grid = new DataGrid(this, null);
		grid.setDataSet(svr.getDataOut());
		new ItField(grid);
		new StringField(grid, "商品代码", "Code_", 6);
		new StringField(grid, "商品名称", "Name_", 10);
		new StringField(grid, "单位", "Unit_", 6);
		new StringField(grid,"价格","Price_",6);
		new StringField(grid,"重量kg","Weight_",6);
		new StringField(grid,"描述","Description_",15);
		OperaField opera = new OperaField(grid);
		opera.setName("操作");
		opera.setValue("删除");
		opera.createUrl((ds, url) -> {
			url.setSite("Product.delete");
			url.addParam("uid", ds.getString("UID_"));
		});
		OperaField opera2=new OperaField(grid);
		opera2.setName("查看");
		opera2.setValue("修改");
		opera2.createUrl((ds,url)->{
			url.setSite("Product.updateproduct");
			url.addParam("uid", ds.getString("UID_"));
		});
		jspPage.add("page", new OperaPages(this, grid.getPages()));
		jspPage.add("dataGrid", grid);
		jspPage.add("name", name);
		String msg = (String) getRequest().getSession().getAttribute("msg");
		jspPage.add("msg", msg);
		getRequest().getSession().setAttribute("msg", "");
		return jspPage;

	}

	public IPage delete() throws Exception {
		String msg = "";
		JspPage jspPage=new JspPage(this,"common/product_delete.jsp");
		String uid = getRequest().getParameter("uid");
		String submit=getRequest().getParameter("submit");
		if(submit!=null){
			if (uid != null || !"".equals(uid)) {
				LocalService svr = new LocalService(this);
				svr.setService("AppProductInfo.delete");
				svr.getDataIn().getHead().setField("UID_", uid);
				if (!svr.exec()) {
					msg = svr.getMessage();
					jspPage.add("data", svr.getDataOut().getCurrent());
				} else {
					String name = svr.getDataOut().getHead().getString("Name_");
					msg = String.format("商品 %s 删除成功！", name);
					getRequest().getSession().setAttribute("msg", msg);
					return new RedirectPage(this, "Product");
				}
			} else {
				msg = "您要删除的商品记录不存在！,请核查";
			}
		}else{
			if (uid != null || "".equals(uid)) {
				LocalService svr=new LocalService(this);
				svr.setService("AppProductInfo.search");
				svr.getDataIn().getHead().setField("UID_", uid);
				if(!svr.exec()){
					msg=svr.getMessage();
				}
				if(!svr.getDataOut().eof()){
					jspPage.add("data", svr.getDataOut().getCurrent());
				}else{
					msg="您要删除的记录不存在,请核查！";
				}
			} else {
				msg = "您要删除的用户记录不存在！,请核查";
			}
		}
		
		getRequest().getSession().setAttribute("msg", msg);
		return jspPage;
	}

	// 判断字符串是否为数字
	public final static boolean isNumber(String s) {
		if (s != null || "".equals(s.trim())) {
			return s.matches("^[0.0-9.0]*$");
		} else
			return false;
	}

	public IPage append() {
		JspPage jspPage = new JspPage(this, "common/product_append.jsp");
		String submit = getRequest().getParameter("submit");
		if (submit != null) {
			LocalService svr = new LocalService(this);
			svr.setService("AppProductInfo.append");
			Record head = svr.getDataIn().getHead();
			String code = getRequest().getParameter("code");
			String name = getRequest().getParameter("name");
			String unit = getRequest().getParameter("unit");
			String stock = getRequest().getParameter("stock");
			String price = getRequest().getParameter("price");
			String weight = getRequest().getParameter("weight");
			String description = getRequest().getParameter("description");
			//String status = getRequest().getParameter("status");
			boolean flag = false;
			if (code == null || code.equals("")) {
				flag = true;
			}
			if (name == null || name.equals("")) {
				flag = true;
			}
			if (unit == null || unit.equals("")) {
				flag = true;
			}
			if (!isNumber(price)) {
				flag = true;
			}
			if (!isNumber(weight)) {
				flag = true;
			}
			if (stock == null || stock.equals("")) {
				flag = true;
			}
//			if (status == null || "".equals(status)) {
//				flag = true;
//			}
			if (flag) {
				jspPage.add("msg", "您输入的值存在空值或数字格式不正确，请确认！");
			} else {
				head.setField("Code_", code);
				head.setField("Name_", name);
				head.setField("Unit_", unit);
				head.setField("Description_", description);
				//head.setField("Status_", status);
				head.setField("Price_", getRequest().getParameter("price"));
				head.setField("Weight_", getRequest().getParameter("weight"));
				head.setField("Stock_", getRequest().getParameter("stock"));
				if (!svr.exec()) {
					jspPage.add("msg", svr.getMessage());
				} else {
					jspPage.add("msg", "商品信息添加成功！");
				}
			}
		}
		return jspPage;
	}
    public IPage updateproduct(){
    	JspPage jspPage=new JspPage(this,"common/product_modify.jsp");
    	String submit=getRequest().getParameter("submit");
    	String uid=getRequest().getParameter("uid");
    	if(submit !=null){
    		LocalService svr=new LocalService(this);
    		Record head=svr.getDataIn().getHead();
    		String code = getRequest().getParameter("code");
			String name = getRequest().getParameter("name");
			String unit = getRequest().getParameter("unit");
			String stock = getRequest().getParameter("stock");
			String price = getRequest().getParameter("price");
			String weight = getRequest().getParameter("weight");
			String description = getRequest().getParameter("description");
			//String status = getRequest().getParameter("status");
			boolean flag = false;
			if (code == null || code.equals("")) {
				flag = true;
			}
			if (name == null || name.equals("")) {
				flag = true;
			}
			if (unit == null || unit.equals("")) {
				flag = true;
			}
			if (!isNumber(price)) {
				flag = true;
			}
			if (!isNumber(weight)) {
				flag = true;
			}
			if (stock == null || stock.equals("")) {
				flag = true;
			}
//			if (status == null || "".equals(status)) {
//				flag = true;
//			}
			if (flag) {
				jspPage.add("msg", "您输入的值存在空值或数字格式不正确，请确认！");
			}else{
				svr.setService("AppProductInfo.update");
				head.setField("UID_",uid );
				head.setField("Name_", getRequest().getParameter("name"));
				head.setField("Unit_", getRequest().getParameter("unit"));
				head.setField("Code_", getRequest().getParameter("code"));
				head.setField("Price_", getRequest().getParameter("price"));
				head.setField("Stock_", getRequest().getParameter("stock"));
				head.setField("Weight_", getRequest().getParameter("weight"));
				head.setField("Description_", description);
			//	head.setField("Status_", getRequest().getParameter("status"));
				if (!svr.exec()) {
					jspPage.add("msg", svr.getMessage());
				} else
					jspPage.add("msg", "商品资料修改成功！");
			}
			
    	}
    	if (uid != null || !"".equals(uid)) {
			LocalService svr = new LocalService(this);
			svr.setService("AppProductInfo.search");
			svr.getDataIn().getHead().setField("UID_", uid);
			if (!svr.exec()) {
				jspPage.add("msg", svr.getMessage());
			}
			if (!svr.getDataOut().eof()) {
				jspPage.add("data", svr.getDataOut().getCurrent());
			} else {
				jspPage.add("msg", "您要修改的记录不存在,请核查!");
			}
		} else {
			jspPage.add("msg", "您要修改的记录不存在,请核查!");
		}
    	return jspPage;
    }
	@Override
	public boolean logon() {
		return true;
	}
}
