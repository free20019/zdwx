package mvc.controllers;


import helper.DownloadAct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helper.JacksonUtil;
import mvc.service.CommonServer;
import mvc.service.LoginServer;
import mvc.service.WHServer;
import mvc.service.WXServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(value = "/wx")
public class WXAction {
	private WXServer wxServer;
	public WXServer getWxServer() {
		return wxServer;
	}
	@Autowired
	public void setWxServer(WXServer wxServer) {
		this.wxServer = wxServer;
	}

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

	@RequestMapping("/vhicinfo")
	@ResponseBody
	public String findvhicinfo(@RequestParam("vhic") String vhic,HttpServletRequest request){
		String power_name = String.valueOf(request.getSession().getAttribute("power_name"));
		String username = String.valueOf(request.getSession().getAttribute("username"));
		String view_type = String.valueOf(request.getSession().getAttribute("view_type"));
		String msg = wxServer.findvhicinfo(vhic,power_name,username,view_type);
		return msg;
	}
	@RequestMapping("vhicinfoexcle")
	@ResponseBody
	public String findqkbexcle(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("vhic") String vhic) throws IOException{
		String a[] = {"车牌号码","送修时间","完修时间","故障类型","故障现象","维修地点","维修类型"
				,"维修内容","维修费用","当前故障类型","维修审核","审核人","审核时间","客户满意度"};//导出列明
		String b[] = {"VEHI_NO","SXSJ","WXSJ","RM_MALFUNCTION1","RM_MALFUNCTION","RA_ADDR","RT_TYPE",
				"RC_CONTENT","RR_COST","GZ","SHJG","RR_ASSESSOR","SHSJ","TCSS"};//导出map中的key
		String gzb = "维修记录";//导出sheet名和导出的文件名
		String view_type = String.valueOf(request.getSession().getAttribute("view_type"));
		String power_name = String.valueOf(request.getSession().getAttribute("power_name"));
		String username = String.valueOf(request.getSession().getAttribute("username"));
		String msg = wxServer.vhicinfo(vhic,power_name,username,view_type);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/findwxcc")
	@ResponseBody
	public String findwxcc(HttpServletRequest request,@RequestParam("postData") String postData){
		String power_name = String.valueOf(request.getSession().getAttribute("power_name"));
		String username = String.valueOf(request.getSession().getAttribute("username"));
		String view_type = String.valueOf(request.getSession().getAttribute("view_type"));
		String msg = wxServer.wxcc(postData,power_name,username,view_type);
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
		String power_name = String.valueOf(request.getSession().getAttribute("power_name"));
		String username = String.valueOf(request.getSession().getAttribute("username"));

		String view_type = String.valueOf(request.getSession().getAttribute("view_type"));
		String msg = wxServer.wxcc(postData,power_name,username,view_type);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/findwxmx")
	@ResponseBody
	public String findwxmx(HttpServletRequest request,@RequestParam("postData") String postData){
		String power_name = String.valueOf(request.getSession().getAttribute("power_name"));
		String username = String.valueOf(request.getSession().getAttribute("username"));
		String view_type = String.valueOf(request.getSession().getAttribute("view_type"));
		String msg = wxServer.findwxmx(postData,power_name,username,view_type);
		return msg;
	}

	@RequestMapping("/paywxmx")
	@ResponseBody
	public String paywxmx(@RequestParam("postData") String postData){
		String msg = wxServer.paywxmx(postData);
		return msg;
	}

	@RequestMapping("findwxmxexcle")
	@ResponseBody
	public String findwxmxexcle(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("postData") String postData) throws IOException{
		String a[] = {"送修时间","完修时间","车牌号码","终端类型","终端子类型","业务区块","公司","故障现象","维修内容","维修地点","维修类型","维修人姓名","维修审核","审核人姓名","审核时间","维修费用","审核原因","交费状态"};//导出列明
		String b[] = {"SXSJ","WXSJ","VEHI_NO","MT_NAME","MDT_SUB_TYPE","OWNER_NAME","COMP_NAME","RM_MALFUNCTION","RC_CONTENT","RA_ADDR","RT_TYPE","WXRY","STATE","SHRY","SHSJ","RR_COST","NOTE","PAY_STATUS"};//导出map中的key
		String gzb = "维修明细";//导出sheet名和导出的文件名
		postData = java.net.URLDecoder.decode(postData, "UTF-8");
		String power_name = String.valueOf(request.getSession().getAttribute("power_name"));
		String username = String.valueOf(request.getSession().getAttribute("username"));
		String view_type = String.valueOf(request.getSession().getAttribute("view_type"));
		String msg = wxServer.findwxmx(postData,power_name,username,view_type);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
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
		String msg = "";
		try {
			msg =wxServer.addwxjl(postData,request);
		}catch (Exception e){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("info", "添加失败");
			msg = jacksonUtil.toJson(map);
		}
		return msg;
	}

	@RequestMapping("/editwxjl")
	@ResponseBody
	public String editwxjl(@RequestParam("postData") String postData){
		String msg = "";
		try {
			msg =wxServer.editwxjl(postData);
		}catch (Exception e){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("info", "修改失败");
			msg = jacksonUtil.toJson(map);
		}
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
	@RequestMapping("finddhhfexcle")
	@ResponseBody
	public String finddhhfexcle(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("postData") String postData) throws IOException{
		String a[] = {"送修时间","车牌号码","联系人","联系电话","公司","故障现象","维修内容","维修地点","维修类型","维修人姓名","维修费用","回访人","回访结果","回访意见"};//导出列明
		String b[] = {"SJ","VEHI_NO","VEHI_NAME","VEHI_PHONE","COMP_NAME","RM_MALFUNCTION","RC_CONTENT","RA_ADDR","RT_TYPE","WXRY","RR_COST","REAL_NAME","HFJG","JFYY"};//导出map中的key
		String gzb = "维修车次";//导出sheet名和导出的文件名
		postData = java.net.URLDecoder.decode(postData, "UTF-8");
		String msg = wxServer.finddhhf(postData);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/dhhf")
	@ResponseBody
	public String dhhf(@RequestParam("postData") String postData,HttpServletRequest request){
		String msg = wxServer.dhhf(postData,request);
		return msg;
	}
	
	@RequestMapping("/findfaultTypeHistory")
	@ResponseBody
	public String findfaultTypeHistory(@RequestParam("postData") String postData,HttpServletRequest request){
		String msg = wxServer.findfaultTypeHistory(postData,request);
		return msg;
	}
	@RequestMapping("findfaultTypeHistorydc")
	@ResponseBody
	public String findfaultTypeHistorydc(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("postData") String postData) throws IOException{
		String a[] = {"车牌号","业户","时间","有定位无营运","有营运无定位","有抓拍无定位无营运","7天无定位无营运","空重车无变化","视频黑屏","视频移位","视频断线"};//导出列明
	    String b[] = {"VEHICLE_NO","COMP_NAME","TIME","NO_GPS","NO_JJQ","NO_GPS_JJQ","SEVEN_GPS_JJQ","EMPTY_HEAVY","SCREEN_BLACK","MOVE_ON","BREAK_OFF"};//导出map中的key
		String gzb = "主机故障历史记录";//导出sheet名和导出的文件名
		postData = java.net.URLDecoder.decode(postData, "UTF-8");
		String msg = wxServer.findfaultTypeHistory(postData,request);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}

    @RequestMapping("/findwwzsfqkybb")
    @ResponseBody
    public String findwwzsfqkybb(@RequestParam("postData") String postData){
        String msg = wxServer.findwwzsfqkybb(postData);
        return msg;
    }
    @RequestMapping("findwwzsfqkybbexcle")
    @ResponseBody
    public String findwwzsfqkybbexcle(HttpServletRequest request,
                                HttpServletResponse response,@RequestParam("postData") String postData) throws IOException{
        String a[] = {"配件（发票）","配件（收据）","安装费（发票）","安装费（收据）","合计"};//导出列明
        String b[] = {"PARTS_TOTAL_PRICE_FP","PARTS_TOTAL_PRICE_SJ","LABOR_COST_FP","LABOR_COST_SJ","ACOUNT"};//导出map中的key
        String gzb = "维修站收费情况月报";//导出sheet名和导出的文件名
        postData = java.net.URLDecoder.decode(postData, "UTF-8");
        String msg = wxServer.findwwzsfqkybb(postData);
        List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
        DownloadAct.download(request,response,a,b,gzb,list);
        return null;
    }

	@RequestMapping("/findywxqktj")
	@ResponseBody
	public String findywxqktj(@RequestParam("postData") String postData){
		String msg = wxServer.findywxqktj(postData);
		return msg;
	}
	@RequestMapping("findywxqktjexcle")
	@ResponseBody
	public String findywxqktjexcle(HttpServletRequest request,
									  HttpServletResponse response,@RequestParam("postData") String postData) throws IOException{
		List<String> a = new ArrayList<String>();//导出列明
		List<String> b = new ArrayList<String>();//导出map中的key
		String gzb = "月维修情况统计";//导出sheet名和导出的文件名
		postData = java.net.URLDecoder.decode(postData, "UTF-8");
		String msg = wxServer.findywxqktj(postData);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		if(list.size()>0){
			for (Map.Entry<String, Object> entry : list.get(0).entrySet()) {
				a.add(entry.getKey());
				b.add(entry.getKey());
			}
		}
		DownloadAct.download(request,response,a.toArray(new String[a.size()]),b.toArray(new String[b.size()]),gzb,list);
		return null;
	}
}
