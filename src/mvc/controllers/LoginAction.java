package mvc.controllers;

import helper.DownloadAct;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



import javax.servlet.http.HttpServletResponse;

import mvc.service.LoginServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(value = "/login")
public class LoginAction {
	private LoginServer loginServer;

	public LoginServer getLoginServer() {
		return loginServer;
	}
	@Autowired
	public void setLoginServer(LoginServer loginServer) {
		this.loginServer = loginServer;
	}

	@RequestMapping("/login")
	@ResponseBody
	public String query_pic(HttpServletRequest request,@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("value") String value) {
		String msg = loginServer.login(username,password,value,request);	
	    return msg;
	}
	@RequestMapping("/index")
	@ResponseBody
	public String index(HttpServletRequest request) {
		String msg = loginServer.index(request);
	    return msg;
	}
	@RequestMapping("/findlogin")
	@ResponseBody
	public String findlogin(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = loginServer.findlogin(postData);
	    return msg;
	}
	
	@RequestMapping("findloginexcle")
	@ResponseBody
	public String findloginexcle(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("postData") String postData) throws IOException{
		String a[] = {"公司","账号","用户姓名","登录时间"};//导出列明
		String b[] = {"COMP_NAME","USER_NAME","REAL_NAME","LOGIN_TIME"};//导出map中的key
		String gzb = "平台使用情况";//导出sheet名和导出的文件名
		postData = java.net.URLDecoder.decode(postData, "UTF-8");
		String msg = loginServer.findlogin(postData);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping(value = "/xt2")
	@ResponseBody
	public String xt2(HttpServletRequest request) {
		String msg = "{}";
		Object userObject = request.getSession().getAttribute("username");
		System.out.println(userObject);
		if(null == userObject){
			msg = "{\"code\":510}";
		}else{
			String user = (String)userObject;
			if(user.isEmpty()){
				msg = "{\"code\":510}";
			}else{
				msg = "{\"code\":520}";
			}
		}
		//logger.info(msg);
		return msg;
	}
}
