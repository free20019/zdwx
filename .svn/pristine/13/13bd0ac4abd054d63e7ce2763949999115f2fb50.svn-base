package mvc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import helper.JacksonUtil;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class WXServer1 {
	protected JdbcTemplate jdbcTemplate2 = null;
	
	public JdbcTemplate getJdbcTemplate2() {
		return jdbcTemplate2;
	}
	@Autowired
	public void setJdbcTemplate2(JdbcTemplate jdbcTemplate2) {
		this.jdbcTemplate2 = jdbcTemplate2;
	}
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	public String findvhicinfo(String vhic) {
		String sql = null;
		String wxxx = "select a.*,rm.RM_MALFUNCTION RM_MALFUNCTION1,rt.rt_type,ra.ra_addr,us.real_name shry,uw.real_name shry_name from(select r.*"
				+ ",u.real_name,u.user_name user_name1 from TB_REPAIR_RECORD r,"
				+ " TB_USER_WX u where r.user_id=u.user_id and vehi_no = '"+vhic+"') a"
				+ " left join TB_REPAIR_MALFUNCTION rm on a.rm_id=rm.rm_id left join TB_USER_WX us on a.RR_ASSESSOR=us.user_id"
				+ " left join Tb_User_Wx uw on a.RR_ASSESSOR=uw.USER_ID "
				+ " left join TB_REPAIR_TYPE rt on a.rt_id=rt.rt_id left join TB_REPAIR_ADDR ra on a.ra_id=ra.ra_id"
				+ " order by rr_time desc";
		List<Map<String, Object>> list =null;
		sql = "select * from VW_VEHICLE where vehi_no = '"+vhic+"'";
		list = jdbcTemplate2.queryForList(sql);
		List<Map<String, Object>> l = jdbcTemplate2.queryForList(wxxx);
		for (int i = 0; i < l.size(); i++) {
			if(String.valueOf(l.get(i).get("RR_STATE")).equals("0")){
				l.get(i).put("SHJG", "通过");
			}else if(String.valueOf(l.get(i).get("RR_STATE")).equals("1")){
				l.get(i).put("SHJG", "不通过");
			}else {
				l.get(i).put("SHJG", "待审核");
			}
		}
		System.out.println(wxxx);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vhicinfo", list);
		map.put("wxxx", l);
		return jacksonUtil.toJson(map);
	}
	public String vhicinfo(String vhic) {
		List<Map<String, Object>> l =null;
		String wxxx = "select a.*,rm.RM_MALFUNCTION RM_MALFUNCTION1,rt.rt_type,ra.ra_addr,us.real_name shry,uw.real_name shry_name from(select r.*"
				+ ",u.real_name,u.user_name user_name1 from TB_REPAIR_RECORD r,"
				+ " TB_USER_WX u where r.user_id=u.user_id and vehi_no = '"+vhic+"') a"
				+ " left join TB_REPAIR_MALFUNCTION rm on a.rm_id=rm.rm_id left join TB_USER_WX us on a.RR_ASSESSOR=us.user_id"
				+ " left join Tb_User_Wx uw on a.RR_ASSESSOR=uw.USER_ID "
				+ " left join TB_REPAIR_TYPE rt on a.rt_id=rt.rt_id left join TB_REPAIR_ADDR ra on a.ra_id=ra.ra_id"
				+ " order by rr_time desc";
		l = jdbcTemplate2.queryForList(wxxx);
		System.out.println("导出数据的sql:"+wxxx);
		for (int i = 0; i < l.size(); i++) {
			l.get(i).put("SXSJ", String.valueOf(l.get(i).get("RR_TIME")));
			l.get(i).put("WXSJ", String.valueOf(l.get(i).get("RR_TIME_END")));
			l.get(i).put("SHSJ", String.valueOf(l.get(i).get("RR_AUDITIME")));
			if(String.valueOf(l.get(i).get("RR_STATE")).equals("0")){
				l.get(i).put("SHJG", "通过");
			}else if(String.valueOf(l.get(i).get("RR_STATE")).equals("1")){
				l.get(i).put("SHJG", "不通过");
			}else {
				l.get(i).put("SHJG", "待审核");
			}
		}
		return jacksonUtil.toJson(l);
	}
	public String wxcc(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String STIME = String.valueOf(paramMap.get("STIME"));
		String ETIME = String.valueOf(paramMap.get("ETIME"));
		String QK = String.valueOf(paramMap.get("QK"));//区块
		String GS = String.valueOf(paramMap.get("GS"));//公司
		String CPHM = String.valueOf(paramMap.get("CPHM"));//车牌
		String RY = String.valueOf(paramMap.get("RY"));//人员
		String WXLX = String.valueOf(paramMap.get("WXLX"));//维修类型
		String ZDLX = String.valueOf(paramMap.get("ZDLX"));//终端类型
		String sql = "select * from(select vehi_no,count(1) c from TB_REPAIR_RECORD t,TB_USER_WX u,TB_REPAIR_TYPE r where"
				+ " t.user_id=u.user_id and t.rt_id=r.rt_id and rr_time >=to_date('"+STIME+" 00:00:00','yyyy-mm-dd hh24:mi:ss')"
				+ " and rr_time <=to_date('"+ETIME+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
				if(RY!=null&&!RY.equals("null")&&RY.length()>0&&!RY.equals("所有人员")){
					sql += " and u.user_name = '"+RY+"'";
				}
				if(WXLX!=null&&!WXLX.equals("null")&&WXLX.length()>0&&!WXLX.equals("所有维修类型")){
					sql += " and rt_type = '"+WXLX+"'";
				}
								
				sql += " group by vehi_no)a left join vw_vehicle v on a.vehi_no = v.vehi_no where 1=1";
				
				if(CPHM!=null&&!CPHM.equals("null")&&CPHM.length()>0){
					sql += " and a.vehi_no = '"+CPHM+"'";
				}
				if(GS!=null&&!GS.equals("null")&&GS.length()>0&&!GS.equals("所有公司")){
					sql += " and comp_name = '"+GS+"'";
				}
				if(ZDLX!=null&&!ZDLX.equals("null")&&ZDLX.length()>0&&!ZDLX.equals("所有终端")){
					sql += " and mt_name = '"+ZDLX+"'";
				}
				if(QK!=null&&!QK.equals("null")&&QK.length()>0&&!QK.equals("所有区块")){
					sql += " and owner_name = '"+QK+"'";
				}
				System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
	public String findwxmx(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String STIME = String.valueOf(paramMap.get("STIME"));
		String ETIME = String.valueOf(paramMap.get("ETIME"));
		String QK = String.valueOf(paramMap.get("QK"));//区块
		String GS = String.valueOf(paramMap.get("GS"));//公司
		String CPHM = String.valueOf(paramMap.get("CPHM"));//车牌
		String RY = String.valueOf(paramMap.get("RY"));//人员
		String WXLX = String.valueOf(paramMap.get("WXLX"));//维修类型
		String ZDLX = String.valueOf(paramMap.get("ZDLX"));//终端类型
		String sql="select al.*，trm.RM_MALFUNCTION,us.real_name shry,v.vehi_no,v.mdt_no,v.sim_num,V.MT_NAME,V.OWNER_NAME,"
				+ "V.COMP_NAME,v.MDT_SUB_TYPE,uw.real_name shry_name from (select t.*,u.real_name wxry,rt.rt_type,a.ra_addr"
				+ " from TB_REPAIR_RECORD t,TB_USER_WX u,TB_REPAIR_TYPE rt,TB_REPAIR_ADDR a"
				+ " where t.user_id=u.user_id and t.rt_id=rt.rt_id and t.ra_id=a.ra_id "
				+ " and rr_time >=to_date('"+STIME+" 00:00:00','yyyy-mm-dd hh24:mi:ss')"
				+ " and rr_time <=to_date('"+ETIME+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
//		String wxxx = "select a.*,rm.RM_MALFUNCTION RM_MALFUNCTION1,rt.rt_type,ra.ra_addr,us.real_name shry from(select r.*"
//				+ ",u.real_name,u.user_name user_name1 from TB_REPAIR_RECORD r,"
//				+ " tb_user u where r.user_id=u.user_id and vehi_no = '"+vhic+"') a"
//				+ " left join TB_REPAIR_MALFUNCTION rm on a.rm_id=rm.rm_id left join tb_user us on a.RR_ASSESSOR=us.user_id"
//				+ " left join TB_REPAIR_TYPE rt on a.rt_id=rt.rt_id left join TB_REPAIR_ADDR ra on a.ra_id=ra.ra_id"
//				+ " order by rr_time desc";
//		List<Map<String, Object>> l = jdbcTemplate.queryForList(wxxx);
		if(RY!=null&&!RY.equals("null")&&RY.length()>0&&!RY.equals("所有人员")){
			sql += " and u.user_name = '"+RY+"'";
		}
		if(WXLX!=null&&!WXLX.equals("null")&&WXLX.length()>0&&!WXLX.equals("所有维修类型")){
			sql += " and rt_type = '"+WXLX+"'";
		}
		
		sql+= ") al left join TB_REPAIR_MALFUNCTION trm on al.rm_id = trm.rm_id"
				+ " left join Tb_User_Wx uw on al.RR_ASSESSOR=uw.USER_ID "
				+ " left join TB_USER_WX us on al.RR_ASSESSOR=us.user_id left join vw_vehicle v"
				+ " on al.vehi_no = v.vehi_no where 1=1";			
		
		if(CPHM!=null&&!CPHM.equals("null")&&CPHM.length()>0&&!CPHM.equals("所有车辆")){
			sql += " and al.vehi_no = '"+CPHM+"'";
		}
		if(GS!=null&&!GS.equals("null")&&GS.length()>0&&!GS.equals("所有公司")){
			sql += " and comp_name = '"+GS+"'";
		}
		if(ZDLX!=null&&!ZDLX.equals("null")&&ZDLX.length()>0&&!ZDLX.equals("所有终端")){
			sql += " and mt_name = '"+ZDLX+"'";
		}
		if(QK!=null&&!QK.equals("null")&&QK.length()>0&&!QK.equals("所有区块")){
			sql += " and owner_name = '"+QK+"'";
		}
		System.out.println("审核语句sql="+sql);
		List<Map<String, Object>> l  = jdbcTemplate2.queryForList(sql);
		for (int i = 0; i < l.size(); i++) {
			l.get(i).put("SXSJ", String.valueOf(l.get(i).get("RR_TIME")));
			l.get(i).put("SHSJ", String.valueOf(l.get(i).get("RR_AUDITIME")));
			if(String.valueOf(l.get(i).get("RR_STATE")).equals("0")){
				l.get(i).put("SHJG", "通过");
			}else if(String.valueOf(l.get(i).get("RR_STATE")).equals("1")){
				l.get(i).put("SHJG", "不通过");
			}else {
				l.get(i).put("SHJG", "待审核");
			}
		}
		return jacksonUtil.toJson(l);
	}
	public String wxsh(String postData,HttpServletRequest request){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String type = String.valueOf(paramMap.get("type"));
		String id = String.valueOf(paramMap.get("id"));
		String note = String.valueOf(paramMap.get("note"));
		String RR_ASSESSOR = String.valueOf(request.getSession().getAttribute("user_id"));
		String sql = "update TB_REPAIR_RECORD set RR_ASSESSOR = '"+RR_ASSESSOR+"',RR_STATE = '"+type+"',note = '"+note+"',RR_AUDITIME=sysdate where RR_ID = '"+id+"'";
		System.out.println(sql);
		int c = jdbcTemplate2.update(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(c>0){
			map.put("info", "审核成功");
		}else{
			map.put("info", "审核失败");
		}
		return jacksonUtil.toJson(map);
	}
	/**
	 * 维修记录新增
	 */
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
		String rr_id = "";
		int count = 0;
		String sql = "";
		if(type.equals("0")){
			rr_id = String.valueOf(request.getSession().getAttribute("RR_ID"));
			sql = "update TB_REPAIR_RECORD set vehi_no='"+vehi_id+"',rt_id='"+rt_id+"',rr_cost='"+rr_cost+"'"
					+ ",ra_id='"+ra_id+"',rc_content='"+rc_content+"',rr_time=to_date('"+rr_time+"','yyyy-mm-dd hh24:mi:ss'),"
					+ "rr_time_end=to_date('"+rr_time_end+"','yyyy-mm-dd hh24:mi:ss'),rm_id='"+rm_id+"',"
					+ "rm_malfunction='"+rm_malfunction+"',tcss='"+tcss+"',user_id='"+user_id+"'"
					+ ",vehi_name='"+vehi_name+"',vehi_phone='"+vehi_phone+"' where rr_id ='"+rr_id+"'";
		}else if(type.equals("1")){
			rr_id = findMaxId()+"";
			sql = "insert into TB_REPAIR_RECORD (rr_id,vehi_no,rt_id,rr_cost,ra_id,rc_content,rr_time,"
					+ "rr_time_end,rm_id,rm_malfunction,tcss,user_id,vehi_name,vehi_phone) values ('"+rr_id+"','"+vehi_id+"','"+rt_id+"','"+rr_cost+"',"
					+ "'"+ra_id+"','"+rc_content+"',to_date('"+rr_time+"','yyyy-mm-dd hh24:mi:ss'),"
					+ "to_date('"+rr_time_end+"','yyyy-mm-dd hh24:mi:ss'),'"+rm_id+"','"+rm_malfunction+"','"+tcss+"','"+user_id+"'"
					+ ",'"+vehi_name+"','"+vehi_phone+"')";
		}
	
		System.out.println(sql);
		count = jdbcTemplate2.update(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(count>0){
			map.put("info", "添加成功");
		}else{
			map.put("info", "添加失败");
		}
		return jacksonUtil.toJson(map);
	}
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
		String rr_id = String.valueOf(paramMap.get("rr_id")).equals("null")?"":String.valueOf(paramMap.get("rr_id"));
		String sql =  "update TB_REPAIR_RECORD set vehi_no='"+vehi_id+"',rt_id='"+rt_id+"',rr_cost='"+rr_cost+"'"
				+ ",ra_id='"+ra_id+"',rc_content='"+rc_content+"',rr_time=to_date('"+rr_time+"','yyyy-mm-dd hh24:mi:ss'),"
				+ "rr_time_end=to_date('"+rr_time_end+"','yyyy-mm-dd hh24:mi:ss'),rm_id='"+rm_id+"',"
				+ "rm_malfunction='"+rm_malfunction+"',tcss='"+tcss+"',user_id='"+user_id+"'"
				+ ",vehi_name='"+vehi_name+"',vehi_phone='"+vehi_phone+"' where rr_id ='"+rr_id+"'";
		System.out.println(sql);
		int count = jdbcTemplate2.update(sql);
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
		int count = jdbcTemplate2.update(sql);
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
		String sql = "select RR_ID from TB_REPAIR_RECORD  order by to_number(RR_ID) desc";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
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
		String GJZ = String.valueOf(paramMap.get("GJZ"));//关键字
		String sql = "select al.*,trm.RM_MALFUNCTION,us.real_name shry,trp.hfjg,trp.jfyy,trp.hfr,trp.hfsj,trp.id,comp_name"
				+ " from (select t.*,u.real_name wxry,rt.rt_type,a.ra_addr"
				+ " from TB_REPAIR_RECORD t,TB_USER_WX u,TB_REPAIR_TYPE rt,TB_REPAIR_ADDR a"
				+ " where t.user_id=u.user_id and t.rt_id=rt.rt_id and t.ra_id=a.ra_id "
				+ " and rr_time >=to_date('"+STIME+" 00:00:00','yyyy-mm-dd hh24:mi:ss')"
				+ " and rr_time <=to_date('"+ETIME+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		sql+= ") al left join TB_REPAIR_MALFUNCTION trm on al.rm_id = trm.rm_id"
				+ " left join TB_USER_WX us on al.RR_ASSESSOR=us.user_id"
				+ " left join TB_REPAIR_PHONE trp on trp.rr_id=al.rr_id"
				+ " left join vw_vehicle v on al.vehi_no=v.vehi_no where 1=1";
		if(HFJG!=null&&!HFJG.equals("")&&HFJG.length()>0){
			sql += " and HFJG = '"+HFJG+"'";
		}else{
			sql += " and HFJG is NULL";
		}
		if(GJZ!=null&&!GJZ.equals("")&&GJZ.length()>0){
			sql += " and (hfjg like '%"+GJZ+"%' or jfyy like '%"+GJZ+"%' or al.vehi_no like '%"+GJZ+"%' )";
		}
//		System.out.println(sql);
//		String sql = "select al.*,trm.RM_MALFUNCTION,us.real_name shry from (select t.*,u.real_name wxry"
//				+ ",rt.rt_type,v.vehi_no,v.mdt_no,v.sim_num,V.MT_NAME,V.OWNER_NAME,V.COMP_NAME,a.ra_addr,rth.hfjg,rth.jfyy,rth.hfr, rth.hfsj, rth.id"
//				+ " from TB_REPAIR_RECORD t,tb_user u,TB_REPAIR_TYPE rt,vw_vehicle v,TB_REPAIR_ADDR a,TB_REPAIR_PHONE rth"
//				+ " where t.user_id=u.user_id and t.rt_id=rt.rt_id and t.vehi_id = v.vehi_id and t.ra_id=a.ra_id and t.rr_id=rth.rr_id"
//				+ " and rr_time >=to_date('"+STIME+" 00:00:00','yyyy-mm-dd hh24:mi:ss')"
//				+ " and rr_time <=to_date('"+ETIME+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
//		if(HFJG!=null&&!HFJG.equals("null")&&HFJG.length()>0){
//			sql += " and HFJG = '"+HFJG+"'";
//		}
//		if(GJZ!=null&&!GJZ.equals("null")&&GJZ.length()>0){
//			sql += " and (hfjg = '"+GJZ+"' or jfyy = '"+GJZ+"' or vehi_no = '"+GJZ+"' )";
//		}
//		sql+= ") al left join TB_REPAIR_MALFUNCTION trm on al.rm_id = trm.rm_id"
//				+ " left join tb_user us on al.RR_ASSESSOR=us.user_id";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("SXSJ", String.valueOf(list.get(i).get("RR_TIME")));
		}
		return jacksonUtil.toJson(list);
	}
	public String dhhf(String postData,HttpServletRequest request){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String HFJG = String.valueOf(paramMap.get("HFJG"));//回访结果
		String HFYY = String.valueOf(paramMap.get("HFYY"));//关键字
		String ID = String.valueOf(paramMap.get("ID"));//关键字
		String user_id = String.valueOf(request.getSession().getAttribute("user_id"));
		List<Map<String, Object>> list = jdbcTemplate2.queryForList("select * from TB_REPAIR_PHONE where rr_id = '"+ID+"'");
		int count = 0;
		if(list.size()>0){
			String sql = " update TB_REPAIR_PHONE set hfjg = '"+HFJG+"',JFYY = '"+HFYY+"' where rr_id = '"+ ID+"'";
			count = jdbcTemplate2.update(sql);
		}else{
			String sql = " insert into TB_REPAIR_PHONE values ('"+UUID.randomUUID()+
					"','"+ID+"','"+HFJG+"','"+HFYY+"','"+user_id+"',sysdate)";
			count = jdbcTemplate2.update(sql);
		}
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "回访成功");
		}else{
			map.put("info", "回访失败");
		}
		return jacksonUtil.toJson(map);
	}
}
