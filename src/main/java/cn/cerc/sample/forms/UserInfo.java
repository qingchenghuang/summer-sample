package cn.cerc.sample.forms;

import javax.swing.JOptionPane;

import cn.cerc.jbean.client.LocalService;
import cn.cerc.jbean.form.IPage;
import cn.cerc.jdb.core.DataSet;
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

public class UserInfo extends AbstractForm {

	@Override
	public IPage execute() throws Exception {
		JspPage jspPage = new JspPage(this, "common/userInfo.jsp");
		LocalService svr = new LocalService(this);
		String name = getRequest().getParameter("name");
		Record head = svr.getDataIn().getHead();
		head.setField("Name_", name);
		svr.setService("AppUserInfo.search");
		if (!svr.exec()) {
			jspPage.setMessage(jspPage.getMessage());
		}
		AbstractGrid grid = new DataGrid(this, null);
		grid.setDataSet(svr.getDataOut());
		new ItField(grid);
		new StringField(grid, "姓名", "Name_", 7);
		new StringField(grid, "性别", "Sex_", 5);
		new DoubleField(grid, "年龄", "Age_", 5);
		OperaField opera2 = new OperaField(grid);
		opera2.setName("查看");
		opera2.setValue("修改");
		opera2.setWidth(5);
		opera2.createUrl((ds, url) -> {
			url.setSite("UserInfo.updateuser");
			url.addParam("uid", ds.getString("UID_"));
		});
		OperaField opera = new OperaField(grid);
		opera.setName("操作");
		opera.setValue("删除");
		opera.setWidth(5);
		//int n=JOptionPane.showConfirmDialog(null, "确认删除吗?","确认对话框",JOptionPane.YES_NO_OPTION);
		opera.createUrl((ds, url) -> {
			url.setSite("UserInfo.deleteuser");
			url.addParam("uid", ds.getString("UID_"));
		});
		// while(svr.getDataOut().fetch()){
		// System.out.println(svr.getDataOut().getString("Name_"));
		// }
		// getUserGrid(svr.getDataOut(), jspPage,name);
		String msg = (String) getRequest().getSession().getAttribute("msg");
		jspPage.add("page", new OperaPages(this, grid.getPages()));
		jspPage.add("dataGrid", grid);
		jspPage.add("msg", msg);
		jspPage.add("name", name);
		getRequest().getSession().setAttribute("msg", "");
		return jspPage;
	}
   //判断字符串是否为数字
	public final static boolean isNumber(String s){
		if(s!=null ||"".equals(s.trim())){
			return s.matches("^[0.0-9.0]*$");
		}else
			return false;
	}

	public IPage append() throws Exception {
		JspPage jspPage = new JspPage(this, "common/userInfo_append.jsp");
		String submit = getRequest().getParameter("submit");
		if (submit != null) {
			LocalService svr = new LocalService(this);
			svr.setService("AppUserInfo.append");
			Record head = svr.getDataIn().getHead();
			String code = getRequest().getParameter("code");
			String name = getRequest().getParameter("name");
			String age = getRequest().getParameter("age");
			String sex = getRequest().getParameter("sex");
			boolean flag = false;
			if(!isNumber(age)){
				flag=true;
			}
			if (code == null || code.equals("")) {
				flag = true;
			}
			if (name == null || name.equals("")) {
				flag = true;
			}
			if (age == null || age.equals("")) {
				flag = true;
			}
			if (sex == null || sex.equals("")) {
				flag = true;
			}
			if (flag) {
				jspPage.add("msg", "您输入的值存在空值或年龄不是数字，请确认！");
			} else {
				head.setField("Code_", code);
				head.setField("Name_", name);
				head.setField("Age_", getRequest().getParameter("age"));
				head.setField("Sex_", sex);
				if (!svr.exec()) {
					jspPage.add("msg", svr.getMessage());
				} else
					jspPage.add("msg", "用户信息添加成功！");
			}
		}
		return jspPage;
	}
	
	public IPage deleteuser() throws Exception {
		JspPage jspPage=new JspPage(this,"common/userInfo_delete.jsp");
		String submit=getRequest().getParameter("submit");
		String msg = "";
		String uid = getRequest().getParameter("uid");
		if(submit !=null){
			if (uid != null || "".equals(uid)) {
				LocalService svr = new LocalService(this);
				svr.setService("AppUserInfo.deleteuser");
				svr.getDataIn().getHead().setField("UID_", uid);
				if (!svr.exec()) {
					msg = svr.getMessage();
				} else {
					String name = svr.getDataOut().getHead().getString("Name_");
					msg = String.format("用户 %s 删除成功", name);
					getRequest().getSession().setAttribute("msg", msg);
					return new RedirectPage(this, "UserInfo");
				}
			} else {
				msg = "您要删除的用户记录不存在！,请核查";
			}
		}else{
			if (uid != null || "".equals(uid)) {
				LocalService svr=new LocalService(this);
				svr.setService("AppUserInfo.search");
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
	public IPage updateuser() {
		JspPage jspPage = new JspPage(this, "common/userInfo_modify.jsp");
		String submit = getRequest().getParameter("submit");
		String uid = getRequest().getParameter("uid");
		if (submit != null) {
			LocalService svr = new LocalService(this);
			Record head = svr.getDataIn().getHead();
			String name = getRequest().getParameter("name");
			String age = getRequest().getParameter("age");
			String sex = getRequest().getParameter("sex");
			boolean flag = false;
			if (name == null || "".equals(name)) {
				flag = true;
			}
			if (age == null || "".equals(age)) {
				flag = true;
			}
			if (sex == null || "".equals(sex)) {
				flag = true;
			}
			if (flag) {
				jspPage.add("msg", "您输入的值存在空值，请确认！");
			} else {
				svr.setService("AppUserInfo.update");
				head.setField("UID_", uid);
				head.setField("Name_", getRequest().getParameter("name"));
				head.setField("Age_", getRequest().getParameter("age"));
				head.setField("Sex_", getRequest().getParameter("sex"));
				if (!svr.exec()) {
					jspPage.add("msg", svr.getMessage());
				} else
					jspPage.add("msg", "用户资料修改成功！");
			}
		}
		if (uid != null || !"".equals(uid)) {
			LocalService svr = new LocalService(this);
			svr.setService("AppUserInfo.search");
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
