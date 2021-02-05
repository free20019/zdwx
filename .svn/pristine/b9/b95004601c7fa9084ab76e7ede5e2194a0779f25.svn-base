package mvc.controllers;


import helper.DownloadAct;
import helper.JacksonUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.service.CommonServer;
import mvc.service.LoginServer;
import mvc.service.WHServer;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(value = "/wh")
public class WHAction {
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	private WHServer whServer;

	public WHServer getWhServer() {
		return whServer;
	}
	@Autowired
	public void setWhServer(WHServer whServer) {
		this.whServer = whServer;
	}
	@RequestMapping("/find")
	@ResponseBody
	public String find(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = whServer.find(postData);
	    return msg;
	}
	@RequestMapping("findexcle")
	@ResponseBody
	public String findwxccexcle(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("postData") String postData) throws IOException{
		postData = java.net.URLDecoder.decode(postData, "UTF-8");
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String table = String.valueOf(paramMap.get("table"));
		String a[] = {""};
		String b[] = {""};
		String gzb = "";
		if(table.equals("TB_REPAIR_ADDR")){
			a[0]="维修地点";
			b[0]="RA_ADDR";
			gzb = "地点维护";
		}else if(table.equals("TB_REPAIR_MALFUNCTION")){
			a[0]="故障现象";
			b[0]="RM_MALFUNCTION";
			gzb = "故障维护";
		}else{
			a[0]="维修类型";
			b[0]="RT_TYPE";
			gzb = "类型维护";
		}
//		String a[] = {"区块","公司","车牌号码","维修次数"};//导出列明
//		String b[] = {"OWNER_NAME","COMP_NAME","VEHI_NO","C"};//导出map中的key
//		String gzb = "维修车次";//导出sheet名和导出的文件名
		String msg = whServer.find(postData);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/save")
	@ResponseBody
	public String save(HttpServletRequest request,@RequestParam("postData") String postData){
		String msg = whServer.save(postData);
		return msg;
	}
	@RequestMapping("/edit")
	@ResponseBody
	public String edit(HttpServletRequest request,@RequestParam("postData") String postData){
		String msg = whServer.edit(postData);
		return msg;
	}
	@RequestMapping("/del")
	@ResponseBody
	public String del(HttpServletRequest request,@RequestParam("postData") String postData){
		String msg = whServer.del(postData);
		return msg;
	}
	@RequestMapping("/find1")
	@ResponseBody
	public String find1() {
		String msg = whServer.find1();
	    return msg;
	}
}
