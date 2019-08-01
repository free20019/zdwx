package mvc.controllers;


import helper.DownloadAct;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.service.userServer1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(value = "/user1")
public class userAction1 {
	private userServer1 userServer;
	
	public userServer1 getUserServer() {
		return userServer;
	}
	@Autowired
	public void setUserServer(userServer1 userServer) {
		this.userServer = userServer;
	}
	@RequestMapping("/finduser")
	@ResponseBody
	public String finduser(@RequestParam("info") String info) {
		String msg = userServer.finduser(info);
	    return msg;
	}
	@RequestMapping("finduserexcle")
	@ResponseBody
	public String finduserexcle(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("info") String info) throws IOException{
		String a[] = {"用户名","密码","用户姓名","权限名"};//导出列明
		String b[] = {"USER_NAME","USER_PWD","REAL_NAME","POWER_NAME"};//导出map中的key
		String gzb = "用户管理";//导出sheet名和导出的文件名
		String msg = userServer.finduser(info);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/adduser")
	@ResponseBody
	public String adduser(@RequestParam("postData") String postData) {
		String msg = userServer.adduser(postData);
	    return msg;
	}
	@RequestMapping("/edituser")
	@ResponseBody
	public String edituser(@RequestParam("postData") String postData) {
		String msg = userServer.edituser(postData);
	    return msg;
	}
	@RequestMapping("/deluser")
	@ResponseBody
	public String deluser(@RequestParam("postData") String postData) {
		String msg = userServer.deluser(postData);
	    return msg;
	}
	@RequestMapping("/findpower")
	@ResponseBody
	public String findpower(@RequestParam("info") String info) {
		String msg = userServer.findpower(info);
	    return msg;
	}
	@RequestMapping("findpowerexcle")
	@ResponseBody
	public String findpowerexcle(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("info") String info) throws IOException{
		String a[] = {"权限名","权限"};//导出列明
		String b[] = {"POWER_NAME","POWER_INFO"};//导出map中的key
		String gzb = "权限管理";//导出sheet名和导出的文件名
		String msg = userServer.findpower(info);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/addpower")
	@ResponseBody
	public String addpower(@RequestParam("postData") String postData) {
		String msg = userServer.addpower(postData);
	    return msg;
	}
	@RequestMapping("/editpower")
	@ResponseBody
	public String editpower(@RequestParam("postData") String postData) {
		String msg = userServer.editpower(postData);
	    return msg;
	}
	@RequestMapping("/delpower")
	@ResponseBody
	public String delpower(@RequestParam("postData") String postData) {
		String msg = userServer.delpower(postData);
	    return msg;
	}
}
