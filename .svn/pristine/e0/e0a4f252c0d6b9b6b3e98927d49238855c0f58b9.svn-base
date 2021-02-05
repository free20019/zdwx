package mvc.controllers;


import helper.DownloadAct;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.service.CommonServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 公共基础类，通用方法
 * 公司、分公司、车辆等下拉框
 */
@Controller
@RequestMapping(value = "/common")
public class CommonAction {
	private CommonServer commonService;

	public CommonServer getCommonServer() {
		return commonService;
	}

	@Autowired
	public void setCommonServer(CommonServer commonService) {
		this.commonService = commonService;
	}

	@RequestMapping(value = "/ssjk")
	@ResponseBody
	public String ssjk(HttpServletRequest request,@RequestParam("postData") String postData
			) {
		String msg = commonService.ssjk(postData);
		return msg;
	}
	//区块
	@RequestMapping("/findqk")
	@ResponseBody
	public String findqk(){
		String msg = commonService.findqk();
		return msg;
	}
	//公司
	@RequestMapping("findba")
	@ResponseBody
	public String findba(){
		String msg = commonService.findba();
		return msg;
	}
	//分公司
	@RequestMapping("/findcomp")
	@ResponseBody
	public String fingcomp(@RequestParam("ba_id") String ba_id){
		String msg = commonService.findcomp(ba_id);
		return msg;
	}
	@RequestMapping("/findcompowner")
	@ResponseBody
	public String findcompowner(@RequestParam("owner_name") String owner_name){
		String msg = commonService.findcompowner(owner_name);
		return msg;
	}
	//车辆
	@RequestMapping("/findvhic")
	@ResponseBody
	public String findvhic(@RequestParam("ba_id") String ba_id,@RequestParam("comp_id") String comp_id){
		String msg = commonService.findvhic(ba_id, comp_id);
		return msg;
	}
	@RequestMapping("/findvhicgs")
	@ResponseBody
	public String findvhicgs(@RequestParam("comp_name") String comp_name){
		String msg = commonService.findvhicgs(comp_name);
		return msg;
	}
	@RequestMapping("/findcomp_tj")
	@ResponseBody
	public String findcomp_tj(@RequestParam("comp_name") String comp_name){
		String msg = commonService.findcomp_tj(comp_name);
		return msg;
	}
	@RequestMapping("/findvhictj")
	@ResponseBody
	public String findvhictj(@RequestParam("vhic") String vhic){
		String msg = commonService.findvhictj(vhic);
		return msg;
	}
	@RequestMapping("/findwxry")
	@ResponseBody
	public String findwxry(HttpServletRequest request){
		String msg = commonService.findwxry(request);
		return msg;
	}
	@RequestMapping("/findzdlx")
	@ResponseBody
	public String findzdlx(){
		String msg = commonService.findzdlx();
		return msg;
	}
	@RequestMapping("/findzdzlx")
	@ResponseBody
	public String findzdzlx() {
		String msg = commonService.findzdzlx();
	  return msg;
	}
	@RequestMapping("/findwxlx")
	@ResponseBody
	public String findwxlx(){
		String msg = commonService.findwxlx();
		return msg;
	}
	@RequestMapping(value = "/upload")
	@ResponseBody
	public String upload(HttpServletRequest request,HttpServletResponse response) {
		String msg = "";
	  msg = commonService.upload(request, response);
	  return msg;
	}
	@RequestMapping(value = "/query_pic")
	@ResponseBody
	public synchronized String query_pic(HttpServletRequest request,HttpServletResponse response,@RequestParam("type") String type,@RequestParam("info") int info,@RequestParam("rr_id") String rr_id) {
		String msg = "";
		commonService.query_pic(request, response, type, rr_id, info);
	    return msg;
	}
}
