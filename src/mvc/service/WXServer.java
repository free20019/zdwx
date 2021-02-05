package mvc.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helper.JacksonUtil;
import helper.SystemConfig;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class WXServer {
	protected JdbcTemplate jdbcTemplate = null;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	private String qx(String view_type){
		String[] a = view_type.split(";");
		if(a.length==0){
			return "";
		}
		String sql = " and (";
		for (int i = 0; i < a.length; i++) {
			if (i == a.length - 1) {
				sql += " v.comp_id='" + a[i] + "' or v.ba_id='"
						+ a[i] + "' or v.owner_id='"+ a[i] + "')";
			} else {
				sql += " v.comp_id='" + a[i] + "' or v.ba_id='"
						+ a[i] + "' or  v.owner_id='"+ a[i] + "' or ";
			}
		}
		return sql;
	}
	public String findvhicinfo(String vhic,String power_name,String username,String view_type) {
		String sql = "select distinct * from VW_VEHICLE where vehi_no = '"+vhic+"'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		String wxxx = "select distinct  a.*,rm.RM_MALFUNCTION RM_MALFUNCTION1,rt.rt_type,ra.ra_addr,us.real_name shry,g.*,p.partsandnumber from(select r.*"
				+ ",u.real_name,u.user_name user_name1,v.VEHI_NO vehi_no1 from TB_REPAIR_RECORD r,"
				+ " tb_user_wx u,vw_vehicle v where r.user_id=u.user_id and (r.vehi_id = v.vehi_id or r.vehi_no = v.VEHI_NO)"
				+ " and (r.vehi_no = '"+vhic+"' or v.VEHI_NO = '"+vhic+"')";
//		if(!power_name.equals("管理员")){
//			wxxx += " and u.user_name = '"+username+"'";
//		}
		wxxx += qx(view_type);
		wxxx += " ) a left join TB_REPAIR_MALFUNCTION rm on a.rm_id=rm.rm_id left join tb_user_wx us on a.RR_ASSESSOR=us.user_id"
				+ " left join TB_REPAIR_TYPE rt on a.rt_id=rt.rt_id left join TB_REPAIR_ADDR ra on a.ra_id=ra.ra_id"
				+ " left join tb_taxi_gzfx g on g.vehicle_no=a.vehi_no"
				+" left join (select a.repair_id,to_char(wm_concat(b.parts_name||'*'||a.use_number)) partsandnumber from TB_REPAIR_PARTS_USE a, TB_REPAIR_PARTS b where (a.parts_id = b.parts_id or a.parts_name = b.parts_name) group by a.repair_id) p on a.rr_id=p.repair_id"
				+ " order by rr_time desc";
		List<Map<String, Object>> l = jdbcTemplate.queryForList(wxxx);
		for (int i = 0; i < l.size(); i++) {
			l.get(i).put("VEHI_NO", String.valueOf(l.get(i).get("VEHI_NO1")).equals("null")?l.get(i).get("VEHI_NO"):l.get(i).get("VEHI_NO1"));
			Map<String, Object> vehicle1=l.get(i);
			vehicle1.put("GZ", findgz(vehicle1));
		}
		System.out.println(wxxx);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vhicinfo", list);
		map.put("wxxx", l);
		return jacksonUtil.toJson(map);
	}
	public String vhicinfo(String vhic,String power_name,String username,String view_type) {
		String wxxx = "select distinct a.*,rm.RM_MALFUNCTION RM_MALFUNCTION1,rt.rt_type,ra.ra_addr,us.real_name shry,g.* from(select r.*"
				+ ",u.real_name,u.user_name user_name1 from TB_REPAIR_RECORD r,"
				+ " tb_user_wx u,vw_vehicle v where r.user_id=u.user_id and (r.vehi_id = v.vehi_id or r.vehi_no = v.VEHI_NO)"
				+ " and (r.vehi_no = '"+vhic+"' or v.VEHI_NO = '"+vhic+"')";
//		if(!power_name.equals("管理员")){
//			wxxx += " and u.user_name = '"+username+"'";
//		}
		wxxx += qx(view_type);
		wxxx += ") a left join TB_REPAIR_MALFUNCTION rm on a.rm_id=rm.rm_id left join tb_user_wx us on a.RR_ASSESSOR=us.user_id"
				+ " left join TB_REPAIR_TYPE rt on a.rt_id=rt.rt_id left join TB_REPAIR_ADDR ra on a.ra_id=ra.ra_id"
				+ " left join tb_taxi_gzfx g on g.vehicle_no=a.vehi_no"
				+ " order by rr_time desc";
		List<Map<String, Object>> l = jdbcTemplate.queryForList(wxxx);
		System.out.println(wxxx);
		for (int i = 0; i < l.size(); i++) {
			l.get(i).put("SXSJ", String.valueOf(l.get(i).get("RR_TIME")));
			l.get(i).put("WXSJ", String.valueOf(l.get(i).get("RR_TIME_END")));
			l.get(i).put("SHSJ", l.get(i).get("RR_AUDITIME")==null?"":String.valueOf(l.get(i).get("RR_AUDITIME")));
			if(String.valueOf(l.get(i).get("RR_STATE")).equals("0")){
				l.get(i).put("SHJG", "通过");
			}else{
				l.get(i).put("SHJG", "不通过");
			}
			Map<String, Object> vehicle1=l.get(i);
			vehicle1.put("GZ", findgz(vehicle1));
		}
		return jacksonUtil.toJson(l);
	}
	public String wxcc(String postData,String power_name,String username,String view_type){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String STIME = String.valueOf(paramMap.get("STIME"));
		String ETIME = String.valueOf(paramMap.get("ETIME"));
		String QK = String.valueOf(paramMap.get("QK"));//区块
		String GS = String.valueOf(paramMap.get("GS"));//公司
		String CPHM = String.valueOf(paramMap.get("CPHM"));//车牌
		String RY = String.valueOf(paramMap.get("RY"));//人员
		String WXLX = String.valueOf(paramMap.get("WXLX"));//维修类型
		String ZDLX = String.valueOf(paramMap.get("ZDLX"));//终端类型
		String sql = "select * from(select t.vehi_no,count(1) c from TB_REPAIR_RECORD t,tb_user_wx u,TB_REPAIR_TYPE r"
				+ ",vw_vehicle v where"
				+ " t.user_id=u.user_id and t.rt_id=r.rt_id and (t.vehi_id = v.vehi_id or t.vehi_no = v.VEHI_NO)"
				+ " and rr_time >=to_date('"+STIME+" 00:00:00','yyyy-mm-dd hh24:mi:ss')"
				+ " and rr_time <=to_date('"+ETIME+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
//				if(!power_name.equals("管理员")){
//					sql += " and u.user_name = '"+username+"'";
//				}
				if(RY!=null&&!RY.equals("null")&&RY.length()>0){
					sql += " and user_name = '"+RY+"'";
				}
				if(WXLX!=null&&!WXLX.equals("null")&&WXLX.length()>0){
					sql += " and rt_type = '"+WXLX+"'";
				}
				if(CPHM!=null&&!CPHM.equals("null")&&CPHM.length()>0){
					sql += " and v.vehi_no = '"+CPHM+"'";
				}
				if(GS!=null&&!GS.equals("null")&&GS.length()>0){
					sql += " and comp_name = '"+GS+"'";
				}
				if(ZDLX!=null&&!ZDLX.equals("null")&&ZDLX.length()>0){
					sql += " and mt_name = '"+ZDLX+"'";
				}
				if(QK!=null&&!QK.equals("null")&&QK.length()>0){
					sql += " and owner_name = '"+QK+"'";
				}
				sql += qx(view_type);
				sql += " group by t.vehi_no) a left join vw_vehicle v on a.vehi_no = v.vehi_no";
				System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
	public String findwxmx(String postData,String power_name,String username,String view_type){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String STIME = String.valueOf(paramMap.get("STIME"));
		String ETIME = String.valueOf(paramMap.get("ETIME"));
		String QK = String.valueOf(paramMap.get("QK"));//区块
		String GS = String.valueOf(paramMap.get("GS"));//公司
		String CPHM = String.valueOf(paramMap.get("CPHM"));//车牌
		String RY = String.valueOf(paramMap.get("RY"));//人员
		String WXLX = String.valueOf(paramMap.get("WXLX"));//维修类型
		String ZDLX = String.valueOf(paramMap.get("ZDLX"));//终端类型
		String ZDZLX = String.valueOf(paramMap.get("ZDZLX"));//终端子类型
		String sql = "select al.*,trm.RM_MALFUNCTION,us.real_name shry,a.ra_addr,p.partsandnumber from (select t.*,u.real_name wxry,"
				+ "rt.rt_type,v.vehi_no vehi_no1,v.mdt_no,v.sim_num,V.MT_NAME,V.OWNER_NAME,V.COMP_NAME,v.MDT_SUB_TYPE"
				+ " from TB_REPAIR_RECORD t,tb_user_wx u,TB_REPAIR_TYPE rt, vw_vehicle v"
				+ " where t.user_id=u.user_id and t.rt_id=rt.rt_id  and (t.vehi_id = v.vehi_id or t.vehi_no = v.VEHI_NO) "
				+ " and rr_time >=to_date('"+STIME+"','yyyy-mm-dd hh24:mi:ss')"
				+ " and rr_time <=to_date('"+ETIME+"','yyyy-mm-dd hh24:mi:ss')";
		if(!power_name.equals("管理员")){
			sql += " and u.user_name = '"+username+"'";
		}
		if(ZDZLX!=null&&!ZDZLX.equals("null")&&ZDZLX.length()>0&&!ZDZLX.equals("0")){
			sql += " and v.MDT_SUB_TYPE = '"+ZDZLX+"'";
		}
		sql += qx(view_type);
		if(RY!=null&&!RY.equals("null")&&RY.length()>0){
			sql += " and u.user_name = '"+RY+"'";
		}
		if(WXLX!=null&&!WXLX.equals("null")&&WXLX.length()>0){
			sql += " and rt_type = '"+WXLX+"'";
		}
		if(CPHM!=null&&!CPHM.equals("null")&&CPHM.length()>0){
			sql += " and (t.vehi_no = '"+CPHM+"' or v.vehi_no = '"+CPHM+"')";
		}
		if(GS!=null&&!GS.equals("null")&&GS.length()>0){
			sql += " and comp_name = '"+GS+"'";
		}
		if(ZDLX!=null&&!ZDLX.equals("null")&&ZDLX.length()>0){
			sql += " and mt_name = '"+ZDLX+"'";
		}
		if(QK!=null&&!QK.equals("null")&&QK.length()>0){
			sql += " and owner_name = '"+QK+"'";
		}
		sql+= ") al left join TB_REPAIR_MALFUNCTION trm on al.rm_id = trm.rm_id"
				+ " left join tb_user_wx us on al.RR_ASSESSOR=us.user_id"
				+ " left join TB_REPAIR_ADDR a on al.ra_id=a.ra_id"
				+ " left join (select a.repair_id,to_char(wm_concat(b.parts_name||'*'||a.use_number)) partsandnumber from TB_REPAIR_PARTS_USE a, TB_REPAIR_PARTS b where (a.parts_id = b.parts_id or a.parts_name = b.parts_name) group by a.repair_id) p on al.rr_id=p.repair_id"
				+ " order by al.rr_time desc";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("SXSJ", String.valueOf(list.get(i).get("RR_TIME")).equals("null")?"":list.get(i).get("RR_TIME").toString());
			list.get(i).put("WXSJ", String.valueOf(list.get(i).get("RR_TIME_END")).equals("null")?"":list.get(i).get("RR_TIME_END").toString());
			list.get(i).put("STATE", String.valueOf(list.get(i).get("RR_STATE")).equals("0")?"通过":String.valueOf(list.get(i).get("RR_STATE")).equals("1")?"不通过":"");
			list.get(i).put("SHSJ", String.valueOf(list.get(i).get("RR_AUDITIME")).equals("null")?"":list.get(i).get("RR_AUDITIME").toString());
			list.get(i).put("VEHI_NO", String.valueOf(list.get(i).get("VEHI_NO1")).equals("null")?list.get(i).get("VEHI_NO"):list.get(i).get("VEHI_NO1"));
			list.get(i).put("PAY_STATUS", String.valueOf(list.get(i).get("PAY_STATUS")).equals("0")?"未交":String.valueOf(list.get(i).get("PAY_STATUS")).equals("1")?"已交":"");

		}
		return jacksonUtil.toJson(list);
	}
	public String wxsh(String postData,HttpServletRequest request){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String type = String.valueOf(paramMap.get("type"));
		String id = String.valueOf(paramMap.get("id"));
		String note = String.valueOf(paramMap.get("note"));
		String RR_ASSESSOR = String.valueOf(request.getSession().getAttribute("user_id"));
		String sql = "update TB_REPAIR_RECORD set RR_ASSESSOR = '"+RR_ASSESSOR+"',RR_STATE = '"+type+"',note = '"+note+"',RR_AUDITIME=sysdate where RR_ID = '"+id+"'";
		System.out.println(sql);
		int c = jdbcTemplate.update(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(c>0){
			map.put("info", "审核成功");
		}else{
			map.put("info", "审核失败");
		}
		return jacksonUtil.toJson(map);
	}

	public String paywxmx(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String id = String.valueOf(paramMap.get("id"));
		String[] ids = id.split(",");
		String is = "";
		for (int i = 0; i < ids.length; i++) {
			is += "'"+ids[i]+"',";
		}
		String sql = "update TB_REPAIR_RECORD set PAY_STATUS='1' where RR_ID in ("+is.substring(0, is.length()-1)+")";
		System.out.println(sql);
		int count = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "操作成功");
		}else{
			map.put("info", "操作失败");
		}
		return jacksonUtil.toJson(map);
	}

	/**
	 * 维修记录新增
	 */
	@Transactional
	public String addwxjl(String postData,HttpServletRequest request){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String vehi_id = String.valueOf(paramMap.get("vehi_id")).equals("null")?"":String.valueOf(paramMap.get("vehi_id"));//车牌id
		String vehi_name = String.valueOf(paramMap.get("vehi_name")).equals("null")?"":String.valueOf(paramMap.get("vehi_name"));//车牌id
		String vehi_phone = String.valueOf(paramMap.get("vehi_phone")).equals("null")?"":String.valueOf(paramMap.get("vehi_phone"));//车牌id
		String rt_id = String.valueOf(paramMap.get("rt_id")).equals("null")?"":String.valueOf(paramMap.get("rt_id"));//维修类型id
		String rr_cost = String.valueOf(paramMap.get("rr_cost")).equals("null")?"":String.valueOf(paramMap.get("rr_cost"));//维修费用
		String ra_id = String.valueOf(paramMap.get("ra_id")).equals("null")?"":String.valueOf(paramMap.get("ra_id"));//维修地点
		String rc_content = String.valueOf(paramMap.get("rc_content")).equals("null")?"":String.valueOf(paramMap.get("rc_content"));//维修内容
		String rr_time = String.valueOf(paramMap.get("rr_time")).equals("null")?"":String.valueOf(paramMap.get("rr_time"));//送修时间
		String rr_time_end = String.valueOf(paramMap.get("rr_time_end")).equals("null")?"":String.valueOf(paramMap.get("rr_time_end"));//完修时间
		String rm_id = String.valueOf(paramMap.get("rm_id")).equals("null")?"":String.valueOf(paramMap.get("rm_id"));//故障类型
		String rm_malfunction = String.valueOf(paramMap.get("rm_malfunction")).equals("null")?"":String.valueOf(paramMap.get("rm_malfunction"));//故障现象
		String tcss = String.valueOf(paramMap.get("tcss")).equals("null")?"":String.valueOf(paramMap.get("tcss"));//客户满意度
		String user_id = String.valueOf(paramMap.get("user_id")).equals("null")?"":String.valueOf(paramMap.get("user_id"));//维修用户id
		String type = String.valueOf(paramMap.get("type")).equals("null")?"":String.valueOf(paramMap.get("type"));//类型  0有图片  1无图片

		String other_content = String.valueOf(paramMap.get("other_content")).equals("null")?"":String.valueOf(paramMap.get("other_content"));//其他内容
		String parts_total_price = String.valueOf(paramMap.get("parts_total_price")).equals("null")?"":String.valueOf(paramMap.get("parts_total_price"));//配件价格
		String labor_cost = String.valueOf(paramMap.get("labor_cost")).equals("null")?"":String.valueOf(paramMap.get("labor_cost"));//工时费工
		String pay_method = String.valueOf(paramMap.get("pay_method")).equals("null")?"":String.valueOf(paramMap.get("pay_method"));//支付方式 (现金，支付宝，微信，支票)
		String billing_method = String.valueOf(paramMap.get("billing_method")).equals("null")?"":String.valueOf(paramMap.get("billing_method"));//开票方式（发票，收据）
		String invoice_number = String.valueOf(paramMap.get("invoice_number")).equals("null")?"":String.valueOf(paramMap.get("invoice_number"));//发票编号
		String partsandnumber = String.valueOf(paramMap.get("partsandnumber")).equals("null")?"":String.valueOf(paramMap.get("partsandnumber"));//配件和数量

		String rr_id = "";
		int count = 0;
		String sql = "";
		if(type.equals("0")){
			rr_id = String.valueOf(request.getSession().getAttribute("RR_ID"));
			sql = "update TB_REPAIR_RECORD set vehi_no='"+vehi_id+"',rt_id='"+rt_id+"',rr_cost='"+rr_cost+"'"
					+ ",ra_id='"+ra_id+"',rc_content='"+rc_content+"',rr_time=to_date('"+rr_time+"','yyyy-mm-dd hh24:mi:ss'),"
					+ "rr_time_end=to_date('"+rr_time_end+"','yyyy-mm-dd hh24:mi:ss'),rm_id='"+rm_id+"',"
					+ "rm_malfunction='"+rm_malfunction+"',tcss='"+tcss+"',user_id='"+user_id+"'"
					+ ",vehi_name='"+vehi_name+"'" +
					",vehi_phone='"+vehi_phone+"'" +

					",other_content='"+other_content+"'" +
					",parts_total_price='"+parts_total_price+"'" +
					",labor_cost='"+labor_cost+"'" +
					",pay_method='"+pay_method+"'" +
					",billing_method='"+billing_method+"'" +
					",invoice_number='"+invoice_number+"'" +

					" where rr_id ='"+rr_id+"'";
		}else if(type.equals("1")){
			rr_id = findMaxId()+"";
			sql = "insert into TB_REPAIR_RECORD (rr_id,vehi_no,rt_id,rr_cost,ra_id,rc_content,rr_time,"
					+ "rr_time_end,rm_id,rm_malfunction,tcss,user_id,vehi_name,vehi_phone,other_content,parts_total_price,labor_cost,pay_method,billing_method,invoice_number) values ('"+rr_id+"','"+vehi_id+"','"+rt_id+"','"+rr_cost+"',"
					+ "'"+ra_id+"','"+rc_content+"',to_date('"+rr_time+"','yyyy-mm-dd hh24:mi:ss'),"
					+ "to_date('"+rr_time_end+"','yyyy-mm-dd hh24:mi:ss'),'"+rm_id+"','"+rm_malfunction+"','"+tcss+"','"+user_id+"'"
					+ ",'"+vehi_name+"','"+vehi_phone+"','"+other_content+"','"+parts_total_price+"','"+labor_cost+"','"+pay_method+"','"+billing_method+"','"+invoice_number+"')";
		}
		System.out.println(sql);
		try {
			count = jdbcTemplate.update(sql);

			if(!StringUtils.isEmpty(partsandnumber)){
				String[] arr = partsandnumber.split(",");
				for (int i = 0; i < arr.length; i++) {
					jdbcTemplate.update("insert into TB_REPAIR_PARTS_USE (REPAIR_ID,PARTS_NAME,USE_NUMBER,PARTS_PRICE)" +
							" values ('"+rr_id+"','"+arr[i].split("\\*",-1)[0]+"','"+arr[i].split("\\*",-1)[1]+"',(select parts_price from TB_REPAIR_PARTS where PARTS_NAME='"+arr[i].split("\\*",-1)[0]+"' and rownum=1))");
				}
			}
		}catch (Exception e){
			System.out.println("addwxjl:"+e);
			throw new RuntimeException("事物回滚");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if(count>0){
			map.put("info", "添加成功");
		}else{
			map.put("info", "添加失败");
		}
		return jacksonUtil.toJson(map);
	}

	@Transactional
	public String editwxjl(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String vehi_id = String.valueOf(paramMap.get("vehi_id")).equals("null")?"":String.valueOf(paramMap.get("vehi_id"));//车牌id
		String vehi_name = String.valueOf(paramMap.get("vehi_name")).equals("null")?"":String.valueOf(paramMap.get("vehi_name"));//车牌id
		String vehi_phone = String.valueOf(paramMap.get("vehi_phone")).equals("null")?"":String.valueOf(paramMap.get("vehi_phone"));//车牌id
		String rt_id = String.valueOf(paramMap.get("rt_id")).equals("null")?"":String.valueOf(paramMap.get("rt_id"));//维修类型id
		String rr_cost = String.valueOf(paramMap.get("rr_cost")).equals("null")?"":String.valueOf(paramMap.get("rr_cost"));//维修费用
		String ra_id = String.valueOf(paramMap.get("ra_id")).equals("null")?"":String.valueOf(paramMap.get("ra_id"));//维修地点
		String rc_content = String.valueOf(paramMap.get("rc_content")).equals("null")?"":String.valueOf(paramMap.get("rc_content"));//维修内容
		String rr_time = String.valueOf(paramMap.get("rr_time")).equals("null")?"":String.valueOf(paramMap.get("rr_time"));//送修时间
		String rr_time_end = String.valueOf(paramMap.get("rr_time_end")).equals("null")?"":String.valueOf(paramMap.get("rr_time_end"));//完修时间
		String rm_id = String.valueOf(paramMap.get("rm_id")).equals("null")?"":String.valueOf(paramMap.get("rm_id"));//故障类型
		String rm_malfunction = String.valueOf(paramMap.get("rm_malfunction")).equals("null")?"":String.valueOf(paramMap.get("rm_malfunction"));//故障现象
		String tcss = String.valueOf(paramMap.get("tcss")).equals("null")?"":String.valueOf(paramMap.get("tcss"));//客户满意度
		String user_id = String.valueOf(paramMap.get("user_id")).equals("null")?"":String.valueOf(paramMap.get("user_id"));//维修用户id

		String other_content = String.valueOf(paramMap.get("other_content")).equals("null")?"":String.valueOf(paramMap.get("other_content"));//其他内容
		String parts_total_price = String.valueOf(paramMap.get("parts_total_price")).equals("null")?"":String.valueOf(paramMap.get("parts_total_price"));//配件价格
		String labor_cost = String.valueOf(paramMap.get("labor_cost")).equals("null")?"":String.valueOf(paramMap.get("labor_cost"));//工时费工
		String pay_method = String.valueOf(paramMap.get("pay_method")).equals("null")?"":String.valueOf(paramMap.get("pay_method"));//支付方式 (现金，支付宝，微信，支票)
		String billing_method = String.valueOf(paramMap.get("billing_method")).equals("null")?"":String.valueOf(paramMap.get("billing_method"));//开票方式（发票，收据）
		String invoice_number = String.valueOf(paramMap.get("invoice_number")).equals("null")?"":String.valueOf(paramMap.get("invoice_number"));//发票编号
		String partsandnumber = String.valueOf(paramMap.get("partsandnumber")).equals("null")?"":String.valueOf(paramMap.get("partsandnumber"));//配件和数量


		String rr_id = String.valueOf(paramMap.get("rr_id")).equals("null")?"":String.valueOf(paramMap.get("rr_id"));
		String sql = "update TB_REPAIR_RECORD set vehi_no='"+vehi_id+"',rt_id='"+rt_id+"',rr_cost='"+rr_cost+"'"
				+ ",ra_id='"+ra_id+"',rc_content='"+rc_content+"',rr_time=to_date('"+rr_time+"','yyyy-mm-dd hh24:mi:ss'),"
				+ "rr_time_end=to_date('"+rr_time_end+"','yyyy-mm-dd hh24:mi:ss'),rm_id='"+rm_id+"',"
				+ "rm_malfunction='"+rm_malfunction+"',tcss='"+tcss+"',user_id='"+user_id+"'"
				+ ",vehi_name='"+vehi_name+"'" +
				",vehi_phone='"+vehi_phone+"'" +

				",other_content='"+other_content+"'" +
				",parts_total_price='"+parts_total_price+"'" +
				",labor_cost='"+labor_cost+"'" +
				",pay_method='"+pay_method+"'" +
				",billing_method='"+billing_method+"'" +
				",invoice_number='"+invoice_number+"'" +

				" where rr_id ='"+rr_id+"'";
		System.out.println(sql);
		int count = 0;

		try {
			count = jdbcTemplate.update(sql);
			jdbcTemplate.update("delete from TB_REPAIR_PARTS_USE where REPAIR_ID='"+rr_id+"'");
			if(!StringUtils.isEmpty(partsandnumber)){
				String[] arr = partsandnumber.split(",");
				for (int i = 0; i < arr.length; i++) {
				    String insert="insert into TB_REPAIR_PARTS_USE (REPAIR_ID,PARTS_NAME,USE_NUMBER,PARTS_PRICE)" +
                            " values ('"+rr_id+"','"+arr[i].split("\\*",-1)[0]+"','"+arr[i].split("\\*",-1)[1]+"',(select parts_price from TB_REPAIR_PARTS where PARTS_NAME='"+arr[i].split("\\*",-1)[0]+"' and rownum=1))";
                    jdbcTemplate.update("insert into TB_REPAIR_PARTS_USE (REPAIR_ID,PARTS_NAME,USE_NUMBER,PARTS_PRICE)" +
                            " values ('"+rr_id+"','"+arr[i].split("\\*",-1)[0]+"','"+arr[i].split("\\*",-1)[1]+"',(select parts_price from TB_REPAIR_PARTS where PARTS_NAME='"+arr[i].split("\\*",-1)[0]+"' and rownum=1))");
                }
			}
		}catch (Exception e){
			System.out.println("addwxjl:"+e);
			throw new RuntimeException("事物回滚");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		if(count>0){
			map.put("info", "修改成功");
		}else{
			map.put("info", "修改失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String delwxjl(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String id = String.valueOf(paramMap.get("id"));
		String[] ids = id.split(",");
		String is = "";
		for (int i = 0; i < ids.length; i++) {
			is += "'"+ids[i]+"',";
		}
		String sql = "delete from TB_REPAIR_RECORD where RR_ID in ("+is.substring(0, is.length()-1)+")";
		System.out.println(sql);
		int count = jdbcTemplate.update(sql);
		jdbcTemplate.update("delete from TB_REPAIR_PARTS_USE where REPAIR_ID in ("+is.substring(0, is.length()-1)+")");
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "删除成功");
		}else{
			map.put("info", "删除失败");
		}
		return jacksonUtil.toJson(map);
	}
	/**
	 * 查询表中id最大的
	 */
	public int findMaxId(){
		int id = 1;
		String sql = "select max(to_number(RR_ID)) RR_ID from TB_REPAIR_RECORD";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(list.size()>0){
			id = Integer.parseInt(list.get(0).get("RR_ID").toString())+1;
		}
		return id;
	}
	public String finddhhf(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String STIME = String.valueOf(paramMap.get("STIME"));
		String ETIME = String.valueOf(paramMap.get("ETIME"));
		String HFJG = String.valueOf(paramMap.get("HFJG"));//回访结果
		String ECHF = String.valueOf(paramMap.get("ECHF"));
		String GJZ = String.valueOf(paramMap.get("GJZ"));//关键字
		String sql = "select al.*,trm.RM_MALFUNCTION,trp.hfjg, trp.jfyy,trp.hfr,trp.hfsj,trp.id,trp.real_name,"
				+ " comp_name,trp.echf from (select t.*, u.real_name wxry, rt.rt_type, a.ra_addr from TB_REPAIR_RECORD t,"
				+ " tb_user_wx       u, TB_REPAIR_TYPE   rt, TB_REPAIR_ADDR   a where t.user_id = u.user_id and"
				+ " t.rt_id = rt.rt_id and t.ra_id = a.ra_id and rr_time >= to_date('"+STIME+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')"
				+ " and rr_time <= to_date('"+ETIME+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')) al left join"
				+ " TB_REPAIR_MALFUNCTION trm on al.rm_id = trm.rm_id left join (select trp1.*,wx.real_name from"
				+ " TB_REPAIR_PHONE trp1,tb_user_wx wx where trp1.hfr=wx.user_id) trp on trp.rr_id = al.rr_id left join vw_vehicle v"
				+ " on al.vehi_no = v.vehi_no where 1 = 1";
		if(HFJG!=null&&!HFJG.equals("null")&&HFJG.length()>0){
			sql += " and HFJG = '"+HFJG+"'";
		}
		if(ECHF!=null&&!ECHF.equals("null")&&ECHF.length()>0){
			sql += " and ECHF = '"+ECHF+"'";
		}
		if(GJZ!=null&&!GJZ.equals("null")&&GJZ.length()>0){
			sql += " and (hfjg like '%"+GJZ+"%' or jfyy like '%"+GJZ+"%' or al.vehi_no like '%"+GJZ+"%' )";
		}
		sql += " order by rr_time desc";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("SJ", String.valueOf(list.get(i).get("RR_TIME")));
		}
		return jacksonUtil.toJson(list);
	}
	public String dhhf(String postData,HttpServletRequest request){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String HFJG = String.valueOf(paramMap.get("HFJG"));//回访结果
		String HFYY = String.valueOf(paramMap.get("HFYY"));//关键字
		String ID = String.valueOf(paramMap.get("ID"));//关键字
		String ECHF = String.valueOf(paramMap.get("ECHF"));//二次回访
		String user_id = String.valueOf(request.getSession().getAttribute("user_id"));
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from TB_REPAIR_PHONE where rr_id = '"+ID+"'");
		int count = 0;
		if(list.size()>0){
			String sql = " update TB_REPAIR_PHONE set hfjg = '"+HFJG+"',JFYY = '"+HFYY+"',ECHF = '"+ECHF+"' where rr_id = '"+ ID+"'";
			count = jdbcTemplate.update(sql);
		}else{
			String sql = " insert into TB_REPAIR_PHONE values ('"+UUID.randomUUID()+
					"','"+ID+"','"+HFJG+"','"+HFYY+"','"+user_id+"',sysdate,'"+ECHF+"')";
			count = jdbcTemplate.update(sql);
		}
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "回访成功");
		}else{
			map.put("info", "回访失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String findfaultTypeHistory(String postData,
			HttpServletRequest request) {
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String TIME = String.valueOf(paramMap.get("TIME"));//时间
		String CPHM = String.valueOf(paramMap.get("CPHM"));//车牌号码
		String TYPE = String.valueOf(paramMap.get("TYPE"));//故障类型
		String tj="";
        if(CPHM!=null&&!CPHM.isEmpty()&&!CPHM.equals("null")&&CPHM.length()>0&&!CPHM.equals("所有车辆")){
            tj += " and al.vehicle_no='"+CPHM+"'";
        }
        if(TIME!=null&&!TIME.isEmpty()&&!TIME.equals("null")&&TIME.length()>0){
            tj +=" and al.db_time =to_date('"+TIME+"','yyyy-MM-dd')";
        }
        if(TYPE!=null&&!TYPE.isEmpty()&&!TYPE.equals("null")&&TYPE.length()>0&&!TYPE.equals("全部")){
            List type=Arrays.asList(TYPE.split(","));
            tj +=" and ( 1=0";
            if(type.contains("有定位无营运")){
                tj +=" or al.NO_GPS='1'";
            }
            if(type.contains("有营运无定位")){
                tj +=" or al.NO_JJQ='1'";
            }
            if(type.contains("有抓拍无定位无营运")){
                tj +=" or al.NO_GPS_JJQ='1'";
            }
            if(type.contains("7天无定位无营运")){
                tj +=" or al.SEVEN_GPS_JJQ='1'";
            }
            if(type.contains("空重车无变化")){
                tj +=" or al.EMPTY_HEAVY='1'";
            }
            if(type.contains("视频黑屏")){
                tj +=" or al.SCREEN_BLACK='1'";
            }
            if(type.contains("视频移位")){
                tj +=" or al.MOVE_ON='1'";
            }
            if(type.contains("视频断线")){
                tj +=" or al.BREAK_OFF='1'";
            }
            tj +=")";

        }else{
        	 tj +=" and (al.NO_GPS='1' or al.NO_JJQ='1' or al.NO_GPS_JJQ='1' or al.SEVEN_GPS_JJQ='1'"
        	 		+ " or al.EMPTY_HEAVY='1' or al.SCREEN_BLACK='1' or al.MOVE_ON='1' or al.BREAK_OFF='1')";
        }
        String sql = "select al.*,v.comp_name,to_char(db_time,'yyyy-MM-dd') time,g.area_name from tb_taxi_gzfx_history@db69 al" +
                " left join vw_vehicle v on al.vehicle_no=v.vehi_no  " +
                " left join  (select plate_number,area_name from tb_global_vehicle where BUSINESS_SCOPE_NAME='客运：出租车客运。' AND STATUS_NAME='营运' AND PLATE_NUMBER LIKE '浙A%') g on al.vehicle_no=g.plate_number " +
                "where 1=1";
        sql +=tj;
        sql +="order by db_time desc";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        if(list.size()>0){
			list=findDetail(list);
		}
        return jacksonUtil.toJson(list);
	}
	//将查询出的0,1字段转化为无故障，故障
	private List<Map<String, Object>> findDetail(List<Map<String, Object>> list) {
		for(int i=0;i<list.size();i++){
			if(String.valueOf(list.get(i).get("NO_GPS")).equals("1")){
				list.get(i).put("NO_GPS","故障");
			}else if(String.valueOf(list.get(i).get("NO_GPS")).equals("0")){
				list.get(i).put("NO_GPS","无故障");
			}else{
				list.get(i).put("NO_GPS","");
			}
			if(String.valueOf(list.get(i).get("NO_JJQ")).equals("1")){
				list.get(i).put("NO_JJQ","故障");
			}else if(String.valueOf(list.get(i).get("NO_JJQ")).equals("0")){
				list.get(i).put("NO_JJQ","无故障");
			}else{
				list.get(i).put("NO_JJQ","");
			}
			if(String.valueOf(list.get(i).get("NO_GPS_JJQ")).equals("1")){
				list.get(i).put("NO_GPS_JJQ","故障");
			}else if(String.valueOf(list.get(i).get("NO_GPS_JJQ")).equals("0")){
				list.get(i).put("NO_GPS_JJQ","无故障");
			}else{
				list.get(i).put("NO_GPS_JJQ","");
			}
			if(String.valueOf(list.get(i).get("SEVEN_GPS_JJQ")).equals("1")){
				list.get(i).put("SEVEN_GPS_JJQ","故障");
			}else if(String.valueOf(list.get(i).get("SEVEN_GPS_JJQ")).equals("0")){
				list.get(i).put("SEVEN_GPS_JJQ","无故障");
			}else{
				list.get(i).put("SEVEN_GPS_JJQ","");
			}
			if(String.valueOf(list.get(i).get("EMPTY_HEAVY")).equals("1")){
				list.get(i).put("EMPTY_HEAVY","故障");
			}else if(String.valueOf(list.get(i).get("EMPTY_HEAVY")).equals("0")){
				list.get(i).put("EMPTY_HEAVY","无故障");
			}else{
				list.get(i).put("EMPTY_HEAVY","");
			}
			if(String.valueOf(list.get(i).get("SCREEN_BLACK")).equals("1")){
				list.get(i).put("SCREEN_BLACK","故障");
			}else if(String.valueOf(list.get(i).get("SCREEN_BLACK")).equals("0")){
				list.get(i).put("SCREEN_BLACK","无故障");
			}else{
				list.get(i).put("SCREEN_BLACK","");
			}
			if(String.valueOf(list.get(i).get("MOVE_ON")).equals("1")){
				list.get(i).put("MOVE_ON","故障");
			}else if(String.valueOf(list.get(i).get("SCREEN_BLACK")).equals("0")){
				list.get(i).put("MOVE_ON","无故障");
			}else{
				list.get(i).put("MOVE_ON","");
			}
			if(String.valueOf(list.get(i).get("BREAK_OFF")).equals("1")){
				list.get(i).put("BREAK_OFF","故障");
			}else if(String.valueOf(list.get(i).get("SCREEN_BLACK")).equals("0")){
				list.get(i).put("BREAK_OFF","无故障");
			}else{
				list.get(i).put("BREAK_OFF","");
			}
		}
		return list;
	}
	public String findgz(Map<String, Object> vehicle1){
		String gz="";
		//有定位无营运
		if (String.valueOf(vehicle1.get("NO_GPS")).equals("1")){
			gz +="有定位无营运;";
		}	
		//有营运无定位
		if (String.valueOf(vehicle1.get("NO_JJQ")).equals("1")) {
			gz +="有营运无定位;";
		}
		//有抓拍无定位无营运
		if (String.valueOf(vehicle1.get("NO_GPS_JJQ")).equals("1")) {
			gz +="有抓拍无定位无营运;";
		}
		//7天无定位无营运
		if (String.valueOf(vehicle1.get("SEVEN_GPS_JJQ")).equals("1")) {
			gz +="7天无定位无营运;";
		}
		//空重车无变化
		if (String.valueOf(vehicle1.get("EMPTY_HEAVY")).equals("1")) {
			gz +="空重车无变化;";
		}
		return gz;
	}


    public String findwwzsfqkybb(String postData) {
        Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        String vehicle = String.valueOf(paramMap.get("vehicle"));
        String month = String.valueOf(paramMap.get("month"));
        String company = String.valueOf(paramMap.get("company"));
        String tj ="";
        if(vehicle != null && !vehicle.isEmpty() && !vehicle.equals("null") && vehicle.length() > 0){
			tj +=" and r.vehi_no='"+vehicle+"'";
		}
		if(month != null && !month.isEmpty() && !month.equals("null") && month.length() > 0){
			tj +=" and to_char(r.rr_time,'yyyy-mm')='"+month+"'";
		}
		if(company != null && !company.isEmpty() && !company.equals("null") && company.length() > 0){
			tj +=" and v.comp_name='"+company+"'";
		}

		String sql = "select to_char(r.rr_time,'yyyy-mm-dd') day," +
				" sum(decode(r.billing_method,'发票', LABOR_COST, '')) LABOR_COST_FP," +
				" sum(decode(r.billing_method,'收据', LABOR_COST, '')) LABOR_COST_SJ," +
				" sum(decode(r.billing_method,'发票', PARTS_TOTAL_PRICE, '')) PARTS_TOTAL_PRICE_FP," +
				" sum(decode(r.billing_method,'收据', PARTS_TOTAL_PRICE, '')) PARTS_TOTAL_PRICE_SJ," +
				" sum(PARTS_TOTAL_PRICE+LABOR_COST) acount" +
				" from tb_repair_record r ,VW_VEHICLE v where r.vehi_no=v.vehi_no and (r.LABOR_COST is not null or r.PARTS_TOTAL_PRICE is not null )";
		sql +=tj;
		sql += "group by to_char(r.rr_time,'yyyy-mm-dd') order by  to_char(r.rr_time, 'yyyy-mm-dd') desc";
        System.out.println(sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

	public String findywxqktj(String postData) {
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String vehicle = String.valueOf(paramMap.get("vehicle"));
		String month = String.valueOf(paramMap.get("month"));
		String company = String.valueOf(paramMap.get("company"));
		String tj ="";
		if(vehicle != null && !vehicle.isEmpty() && !vehicle.equals("null") && vehicle.length() > 0){
			tj +=" and r.vehi_no='"+vehicle+"'";
		}
		if(month != null && !month.isEmpty() && !month.equals("null") && month.length() > 0){
			tj +=" and to_char(r.rr_time,'yyyy-mm')='"+month+"'";
		}
		if(company != null && !company.isEmpty() && !company.equals("null") && company.length() > 0){
			tj +=" and v.comp_name='"+company+"'";
		}

		List<Map<String, Object>> partsList = jdbcTemplate.queryForList("select a.PARTS_ID, a.PARTS_NAME, a.PARTS_PRICE from TB_REPAIR_PARTS a,TB_REPAIR_PARTS_USE b where  (a.parts_id = b.parts_id or a.parts_name = b.parts_name) order by PARTS_ID");
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		for (int i = 0; i < partsList.size(); i++) {
			map.put("m"+i,partsList.get(i).get("PARTS_NAME"));
		}



		String sql = "select to_char(r.rr_time,'yyyy-mm-dd') as 日期";
//		for (int i = 0; i < partsList.size(); i++) {
//			sql +=" ,sum(decode(u.parts_name,'"+partsList.get(i).get("PARTS_NAME")+"', u.USE_NUMBER, '')) as \""+partsList.get(i).get("PARTS_NAME")+"(数量)\""+
////				  " ,sum(decode(u.parts_name,'"+partsList.get(i).get("PARTS_NAME")+"', u.USE_NUMBER*p.PARTS_PRICE, '')) as \""+partsList.get(i).get("PARTS_NAME")+"(金额)\"";
//				  " ,sum(decode(u.parts_name,'"+partsList.get(i).get("PARTS_NAME")+"', p.PARTS_PRICE, '')) as \""+partsList.get(i).get("PARTS_NAME")+"(金额)\"";
//		}

		for (int i = 0; i < map.size(); i++) {
			sql +=" ,sum(decode(u.parts_name,'"+partsList.get(i).get("PARTS_NAME")+"', u.USE_NUMBER, '')) as \"m"+i+"(数量)\""+
				  " ,sum(decode(u.parts_name,'"+partsList.get(i).get("PARTS_NAME")+"',  u.USE_NUMBER*u.PARTS_PRICE, '')) as \"m"+i+"(金额)\"";
		}
		sql +=	" ,sum(u.USE_NUMBER*p.PARTS_PRICE) as 合计" +
				" from tb_repair_record r ,VW_VEHICLE v, TB_REPAIR_PARTS p, TB_REPAIR_PARTS_USE u" +
				" where r.vehi_no=v.vehi_no and r.rr_id=u.repair_id and (p.parts_id=u.parts_id or p.parts_name=u.parts_name)";
		sql +=tj;
		sql += "group by to_char(r.rr_time,'yyyy-mm-dd') order by  to_char(r.rr_time, 'yyyy-mm-dd') desc";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		List<LinkedHashMap<String, Object>> list_new = new ArrayList<LinkedHashMap<String, Object>>();
        LinkedHashMap<String, Object> map_new = null;
		for (int i = 0; i < list.size(); i++) {
            map_new = new LinkedHashMap<String, Object>();
            map_new.put("日期",list.get(i).get("日期"));
            for (int j = 0; j < map.size(); j++) {
                map_new.put(map.get("m"+j).toString()+"(数量)",list.get(i).get("m"+j+"(数量)"));
                map_new.put(map.get("m"+j).toString()+"(金额)",list.get(i).get("m"+j+"(金额)"));
            }
            map_new.put("合计",list.get(i).get("合计"));
            list_new.add(map_new);
		}
		return jacksonUtil.toJson(list_new);
	}
}
