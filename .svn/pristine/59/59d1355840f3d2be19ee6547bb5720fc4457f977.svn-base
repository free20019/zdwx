package mvc.controllers;


import helper.DownloadAct;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.service.WXServer1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(value = "/wx1")
public class WXAction1 {
	private WXServer1 wxServer;
	public WXServer1 getWxServer() {
		return wxServer;
	}
	@Autowired
	public void setWxServer(WXServer1 wxServer) {
		this.wxServer = wxServer;
	}
	@RequestMapping("/vhicinfo")
	@ResponseBody
	public String findvhicinfo(@RequestParam("vhic") String vhic){
		String msg = wxServer.findvhicinfo(vhic);
		return msg;
	}
	@RequestMapping("vhicinfoexcle")
	@ResponseBody
	public String findqkbexcle(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("vhic") String vhic) throws IOException{
		vhic = java.net.URLDecoder.decode(vhic, "gbk-2312");
		String a[] = {"车牌号码","送修时间","完修时间","故障类型","故障现象","维修地点","维修类型"
				,"维修内容","维修费用","维修审核","审核人","审核时间","客户满意度"};//导出列明
		String b[] = {"VEHI_NO","SXSJ","WXSJ","RM_MALFUNCTION1","RM_MALFUNCTION","RA_ADDR","RT_TYPE",
				"RC_CONTENT","RR_COST","SHJG","SHRY_NAME","SHSJ","TCSS"};//导出map中的key
		String gzb = "维修记录";//导出sheet名和导出的文件名
		String msg = wxServer.vhicinfo(vhic);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/findwxcc")
	@ResponseBody
	public String findwxcc(@RequestParam("postData") String postData){
		String msg = wxServer.wxcc(postData);
		return msg;
	}
	@RequestMapping("findwxccexcle")
	@ResponseBody
	public String findwxccexcle(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("postData") String postData) throws IOException{
		String a[] = {"区块","公司","车牌号码","终端类型","终端子类型","维修次数"};//导出列明
		String b[] = {"OWNER_NAME","COMP_NAME","VEHI_NO","MT_NAME","MDT_SUB_TYPE","C"};//导出map中的key
		String gzb = "维修车次";//导出sheet名和导出的文件名
		postData = java.net.URLDecoder.decode(postData, "UTF-8");
		String msg = wxServer.wxcc(postData);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("findshexcle")
	@ResponseBody
	public String findshexcle(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("postData") String postData) throws IOException{
		String a[] = {"送修时间","车牌号码","终端类型","终端子类型","业务区块","公司","故障现象","维修内容","维修地点","维修类型","维修人姓名","维修审核","审核人姓名","审核时间","维修费用","审核原因"};//导出列明
		String b[] = {"SXSJ","VEHI_NO","MT_NAME","MDT_SUB_TYPE","OWNER_NAME","COMP_NAME","RM_MALFUNCTION","RC_CONTENT","RA_ADDR","RT_TYPE","WXRY","SHJG","SHRY_NAME","SHSJ","RR_COST","NOTE"};//导出map中的key
		String gzb = "维修审核";//导出sheet名和导出的文件名
		postData = java.net.URLDecoder.decode(postData, "UTF-8");
		String msg = wxServer.findwxmx(postData);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("finddhexcle")
	@ResponseBody
	public String finddhexcle(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("postData") String postData) throws IOException{
		System.out.println("回访导出");
		String a[] = {"送修时间","车牌号码","联系人","联系电话","公司","故障现象","维修内容","维修地点","维修类型","维修人姓名","维修费用","回访结果","回访意见"};//导出列明
		String b[] = {"SXSJ","VEHI_NO","VEHI_NAME","VEHI_PHONE","COMP_NAME","RM_MALFUNCTION","RC_CONTENT","RA_ADDR","RT_TYPE","WXRY","RR_COST","HFJG","JFYY"};//导出map中的key
		String gzb = "电话回访";//导出sheet名和导出的文件名
		postData = java.net.URLDecoder.decode(postData, "UTF-8");
		String msg = wxServer.finddhhf(postData);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/findwxmx")
	@ResponseBody
	public String findwxmx(@RequestParam("postData") String postData){
		String msg = wxServer.findwxmx(postData);
		return msg;
	}
	@RequestMapping("/wxsh")
	@ResponseBody
	public String wxsh(@RequestParam("postData") String postData,HttpServletRequest request){
		String msg = wxServer.wxsh(postData,request);
		return msg;
	}
	@RequestMapping("/addwxjl")
	@ResponseBody
	public String addwxjl(@RequestParam("postData") String postData,HttpServletRequest request){
		String msg = wxServer.addwxjl(postData,request);
		return msg;
	}
	@RequestMapping("/editwxjl")
	@ResponseBody
	public String editwxjl(@RequestParam("postData") String postData){
		String msg = wxServer.editwxjl(postData);
		return msg;
	}
	@RequestMapping("/delwxjl")
	@ResponseBody
	public String delwxjl(@RequestParam("postData") String postData){
		String msg = wxServer.delwxjl(postData);
		return msg;
	}
	@RequestMapping("/finddhhf")
	@ResponseBody
	public String finddhhf(@RequestParam("postData") String postData){
		String msg = wxServer.finddhhf(postData);
		return msg;
	}
	@RequestMapping("/dhhf")
	@ResponseBody
	public String dhhf(@RequestParam("postData") String postData,HttpServletRequest request){
		String msg = wxServer.dhhf(postData,request);
		return msg;
	}
}
