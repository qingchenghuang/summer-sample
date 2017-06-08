package cn.cerc.sample.forms;

import cn.cerc.jbean.client.LocalService;
import cn.cerc.jbean.form.IPage;
import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.other.utils;
import cn.cerc.jmis.form.AbstractForm;
import cn.cerc.jmis.page.JspPage;
import cn.cerc.jpage.fields.DateField;
import cn.cerc.jpage.fields.ItField;
import cn.cerc.jpage.fields.OperaField;
import cn.cerc.jpage.fields.StringField;
import cn.cerc.jpage.grid.AbstractGrid;
import cn.cerc.jpage.grid.DataGrid;
import cn.cerc.jpage.other.OperaPages;

public class AEOrder extends AbstractForm {

	@Override
	public IPage execute() throws Exception {
		String name = getRequest().getParameter("name");
		String tbno = getRequest().getParameter("tbno");
		JspPage jspPage = new JspPage(this, "common/aeorder_aelist.jsp");
		LocalService svr = new LocalService(this);
		if (tbno != null) {
			svr.setService("AppAEOrder.deleteOrder");
			svr.getDataIn().getHead().setField("Name_", name);
			svr.getDataIn().getHead().setField("TBNo_", tbno);
			if (!svr.exec()) {
				jspPage.add("msg", jspPage.getMessage());
			} else {
				jspPage.add("msg", String.format("%s 删除成功!", tbno));

			}
		} else {
			svr.setService("AppAEOrder.searchAE");
			Record head = svr.getDataIn().getHead();
			head.setField("Name_", name);
			if (!svr.exec()) {
				jspPage.add("msg", jspPage.getMessage());
			}
		}
		AbstractGrid grid = new DataGrid(this, null);
		grid.setDataSet(svr.getDataOut());
		new ItField(grid);
		new StringField(grid, "单号", "TBNo_", 7);
		new StringField(grid, "人物", "UserName_", 4);
		new DateField(grid, "创建日期", "TBDate_").setWidth(8);
		OperaField opera = new OperaField(grid);
		OperaField opera2 = new OperaField(grid);
		opera.setName("操作");
		opera.setValue("删除");
		opera.createUrl((ds, url) -> {
			url.setSite("AEOrder.OrderInfo");
			url.addParam("tbno", ds.getString("TBNo_"));
			url.addParam("name", ds.getString("UserName_"));
			url.addParam("date", ds.getDate("TBDate_").toString());
		});
		opera2.setName("操作");
		opera2.setValue("修改");
		opera2.createUrl((ds, url) -> {
			url.setSite("AEOrder.modifyOrderInfo");
			url.addParam("tbno", ds.getString("TBNo_"));
			url.addParam("name", ds.getString("UserName_"));
			url.addParam("date", ds.getDate("TBDate_").toString());
		});

		jspPage.add("page", new OperaPages(this, grid.getPages()));
		jspPage.add("dataGrid", grid);
		jspPage.add("name", name);
		return jspPage;
	}

	// 修改单
	public IPage modifyOrderInfo() throws Exception {
		JspPage jspPage = new JspPage(this, "common/aeorder_modify.jsp");
		String tbno = getRequest().getParameter("tbno");
		String code = getRequest().getParameter("code");
		String name = getRequest().getParameter("name");
		String date = getRequest().getParameter("date");
		LocalService svr = new LocalService(this);
		svr.setService("AppAEOrder.searchOrderB");
		svr.getDataIn().getHead().setField("Code_", code);
		svr.getDataIn().getHead().setField("TBNo_", tbno);
		svr.getDataIn().getHead().setField("Name_", name);
		svr.getDataIn().getHead()
				.setField("TBDate_", getRequest().getParameter("date"));
		if (!svr.exec()) {
			jspPage.add("msg", svr.getMessage());
		}
		AbstractGrid grid = new DataGrid(this, null);
		grid.setDataSet(svr.getDataOut());
		Double sum = 0.00;
		while (svr.getDataOut().fetch()) {
			sum += svr.getDataOut().getDouble("Total_");
		}
		new ItField(grid);
		new StringField(grid, "商品名称", "Name_", 6);
		new StringField(grid, "当前数量", "Inventory_", 4);
		new StringField(grid, "实际数量", "Num_", 4);
		new StringField(grid, "单价", "Price_", 4);
		new StringField(grid, "合计", "Total_", 4);
		OperaField opera3 = new OperaField(grid);
		opera3.setName("操作");
		opera3.setValue("修改");
		opera3.setWidth(4);
		opera3.createUrl((ds, url) -> {
			url.setSite("AEOrder.modifyproduct");
			url.addParam("tbno", tbno);
			url.addParam("productname", ds.getString("Name_"));
			url.addParam("date", getRequest().getParameter("date"));
		});
		OperaField opera2 = new OperaField(grid);
		opera2.setName("操作");
		opera2.setValue("删除");
		opera2.setWidth(4);
		opera2.createUrl((ds, url) -> {
			url.setSite("AEOrder.appendOrderB");
			url.addParam("productname", ds.getString("Name_"));
			url.addParam("tbno", tbno);
			url.addParam("name", name);
			url.addParam("code", code);
			url.addParam("delete", "1");
			url.addParam("date", getRequest().getParameter("date"));
		});
		jspPage.add("page", new OperaPages(this, grid.getPages()));
		jspPage.add("dataGrid", grid);
		jspPage.add("tbno", tbno);
		jspPage.add("name", name);
		jspPage.add("total", sum);
		jspPage.add("date", date);
		return jspPage;
	}

	// 查看销售单信息
	public IPage OrderInfo() throws Exception {
		JspPage jspPage = new JspPage(this, "common/aeorder_Info.jsp");
		String tbno = getRequest().getParameter("tbno");
		String username = getRequest().getParameter("name");
		LocalService svr = new LocalService(this);
		svr.setService("AppOrder.search");
		svr.getDataIn().getHead().setField("TBNo_", tbno);
		if (!svr.exec()) {
			jspPage.add("msg", jspPage.getMessage());
		}
		AbstractGrid grid = new DataGrid(this, null);
		grid.setDataSet(svr.getDataOut());
		Double sum = 0.00;
		while (svr.getDataOut().fetch()) {
			sum += svr.getDataOut().getDouble("Total_");
		}
		new ItField(grid);
		new StringField(grid, "商品名称", "Name_", 6);
		new StringField(grid,"当前数量","Inventory_",4);
		new StringField(grid, "实际数量", "Num_", 4);
		new StringField(grid, "单价", "Price_", 4);
		new StringField(grid, "合计", "Total_", 4);
		jspPage.add("page", new OperaPages(this, grid.getPages()));
		jspPage.add("dataGrid", grid);
		jspPage.add("tbno", tbno);
		jspPage.add("name", username);
		jspPage.add("total", sum);
		jspPage.add("date", getRequest().getParameter("date"));
		return jspPage;
	}

	// 选中添加商品列表
	public IPage appendproductList() throws Exception {
		JspPage jspPage = new JspPage(this, "common/aeorder_product.jsp");
		LocalService svr = new LocalService(this);
		String tbno = getRequest().getParameter("tbno");
		String name = getRequest().getParameter("name");
		String productname = getRequest().getParameter("productname");
		svr.setService("AppOrder.searchList");
		svr.getDataIn().getHead().setField("Name_", productname); // 用户名和搜索名重复
		if (!svr.exec()) {
			jspPage.add("msg", svr.getMessage());
		}
		AbstractGrid grid = new DataGrid(this, null);
		grid.setDataSet(svr.getDataOut());
		new ItField(grid);
		new StringField(grid, "商品代码", "Code_", 5);
		new StringField(grid, "商品名称", "Name_", 7);
		new StringField(grid, "单位", "Unit_", 5);
		new StringField(grid, "价格", "Price_", 5);
		new StringField(grid, "库存", "Stock_", 5);
		new StringField(grid, "重量kg", "Weight_", 5);
		new StringField(grid, "描述", "Description_", 11);
		OperaField opera = new OperaField(grid);
		opera.setName("操作");
		opera.setValue("添加");
		opera.createUrl((ds, url) -> {
			url.setSite("AEOrder.appendOrderB");
			url.addParam("code", ds.getString("Code_"));
			url.addParam("tbno", tbno);
			url.addParam("name", name);
			url.addParam("date", getRequest().getParameter("date"));
		});
		jspPage.add("productname", productname);
		jspPage.add("page", new OperaPages(this, grid.getPages()));
		jspPage.add("dataGrid", grid);
		return jspPage;
	}

	public IPage appendOrderB() throws Exception {
		JspPage jspPage = new JspPage(this, "common/aeorder_append.jsp");
		String code = getRequest().getParameter("code");
		String tbno = getRequest().getParameter("tbno");
		String name = getRequest().getParameter("name");
		String delete = getRequest().getParameter("delete");
		String productname = getRequest().getParameter("productname");
		LocalService svr = new LocalService(this);
		if (delete != null && !"".equals(delete)) {
			svr.setService("AppAEOrder.delete");
			svr.getDataIn().getHead().setField("TBNo_", tbno);
			svr.getDataIn().getHead().setField("ProductName_", productname);
		} else {
			svr.setService("AppAEOrder.searchOrderB");
			svr.getDataIn().getHead().setField("Code_", code);
			svr.getDataIn().getHead().setField("TBNo_", tbno);
			svr.getDataIn().getHead().setField("Name_", name);
			svr.getDataIn().getHead()
					.setField("TBDate_", getRequest().getParameter("date"));
		}
		if (tbno != null || "".equals(tbno)) {
			if (!svr.exec()) {
				jspPage.add("msg", svr.getMessage());
			}
		}
		AbstractGrid grid = new DataGrid(this, null);
		grid.setDataSet(svr.getDataOut());
		Double sum = 0.00;
		while (svr.getDataOut().fetch()) {
			sum += svr.getDataOut().getDouble("Total_");
		}
		new ItField(grid);
		new StringField(grid, "商品名称", "Name_", 6);
		new StringField(grid, "当前数量", "Inventory_", 4);
		new StringField(grid, "实际数量", "Num_", 4);
		new StringField(grid, "单价", "Price_", 4);
		new StringField(grid, "合计", "Total_", 4);

		OperaField opera3 = new OperaField(grid);
		opera3.setName("操作");
		opera3.setValue("修改");
		opera3.setWidth(4);
		opera3.createUrl((ds, url) -> {
			url.setSite("AEOrder.modifyproduct");
			url.addParam("tbno", tbno);
			url.addParam("productname", ds.getString("Name_"));
			url.addParam("date", getRequest().getParameter("date"));
		});
		OperaField opera2 = new OperaField(grid);
		opera2.setName("操作");
		opera2.setValue("删除");
		opera2.setWidth(4);
		opera2.createUrl((ds, url) -> {
			url.setSite("AEOrder.appendOrderB");
			url.addParam("productname", ds.getString("Name_"));
			url.addParam("tbno", tbno);
			url.addParam("name", name);
			url.addParam("code", code);
			url.addParam("delete", "1");
			url.addParam("date", getRequest().getParameter("date"));
		});
		jspPage.add("page", new OperaPages(this, grid.getPages()));
		jspPage.add("dataGrid", grid);
		jspPage.add("tbno", tbno);
		jspPage.add("name", name);
		jspPage.add("total", sum);
		jspPage.add("date", getRequest().getParameter("date"));
		return jspPage;
	}

	// 要修改订单商品信息
	public IPage modifyproduct() {
		JspPage jspPage = new JspPage(this, "common/aeorder_modifyproduct.jsp");
		String submit = getRequest().getParameter("submit");
		String tbno = getRequest().getParameter("tbno");
		String productname = getRequest().getParameter("productname");
		String date = getRequest().getParameter("date");
		if (submit != null) {
			String number = getRequest().getParameter("number");
			String price = getRequest().getParameter("price");
			boolean flag = false;
			if (number == null || number.equals("")) {
				flag = true;
			}
			if (price == null || price.equals("")) {
				flag = true;
			}
			if (!utils.isNumeric(price)) {
				flag = true;
			}
			if (!utils.isNumeric(number)) {
				flag = true;
			}
			if (flag) {
				jspPage.add("msg", "您输入的值存在空值或数字格式不正确，请确认！");
			} else {
				LocalService svr = new LocalService(this);
				svr.setService("AppAEOrder.modifyproduct");
				svr.getDataIn().getHead().setField("TBNo_", tbno);
				svr.getDataIn().getHead().setField("Name_", productname);
				svr.getDataIn().getHead()
						.setField("Num_", getRequest().getParameter("number"));
				svr.getDataIn().getHead()
						.setField("Price_", getRequest().getParameter("price"));
				if (!svr.exec()) {
					jspPage.add("msg", svr.getMessage());
				} else {
					jspPage.add("data", svr.getDataOut().getCurrent());
					jspPage.add("msg", "商品信息修改成功！");
					jspPage.add("date",
							getRequest().getSession().getAttribute("date"));
				}
			}
		} else {
			getRequest().getSession().setAttribute("date", date);
			LocalService svr = new LocalService(this);
			svr.setService("AppAEOrder.modifysearchproduct");
			svr.getDataIn().getHead().setField("TBNo_", tbno);
			svr.getDataIn().getHead().setField("Name_", productname);
			if (!svr.exec()) {
				jspPage.add("msg", svr.getMessage());
			}
			if (!svr.getDataOut().eof()) {
				jspPage.add("data", svr.getDataOut().getCurrent());
				jspPage.add("date", date);
			} else {
				jspPage.add("msg", "您要修改的记录不存在,请核查!");
			}
		}
		return jspPage;
	}

	// 添加单头
	public IPage appendOrderH() throws Exception {
		JspPage jspPage = new JspPage(this, "common/aeorder_append.jsp");
		LocalService svr = new LocalService(this);
		String submit = getRequest().getParameter("submit");
		if (submit != null) {
			boolean flag = false;
			String tbno = getRequest().getParameter("tbno");
			String name = getRequest().getParameter("name");
			String date = getRequest().getParameter("date");
			if (tbno == null || tbno.equals("")) {
				flag = true;
			}
			if (name == null || name.equals("")) {
				flag = true;
			}
			if (date == null || date.equals("")) {
				flag = true;
			}
			if (flag) {
				jspPage.add("msg", "您输入的值存在空值或数字格式不正确，请确认！");
			} else {
				svr.setService("AppAEOrder.AppendOrderH");
				svr.getDataIn().getHead().setField("TBNo_", tbno);
				svr.getDataIn().getHead().setField("Name_", name);
				svr.getDataIn().getHead()
						.setField("TBDate_", getRequest().getParameter("date"));
				if (!svr.exec()) {
					jspPage.add("msg", svr.getMessage());
				}
				if (!svr.getDataOut().eof()) {
					jspPage.add("data", svr.getDataOut().getCurrent());
					jspPage.add("tbno", svr.getDataOut().getCurrent()
							.getString("TBNo_"));
					jspPage.add("date",
							svr.getDataOut().getCurrent().getDate("TBDate_"));
					jspPage.add("name", svr.getDataOut().getCurrent()
							.getString("UserName_"));
					jspPage.add("msg", "单头信息添加成功!");
				} else {
					jspPage.add("msg", "单头信息已存在!");
				}
			}
		}
		return jspPage;
	}

	@Override
	public boolean logon() {
		return true;
	}
}
