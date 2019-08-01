package mvc.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;



import helper.JacksonUtil;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class userServer1 {
	protected JdbcTemplate jdbcTemplate2 = null;

	public JdbcTemplate getJdbcTemplate2() {
		return jdbcTemplate2;
	}
	@Autowired
	public void setJdbcTemplate2(JdbcTemplate jdbcTemplate2) {
		this.jdbcTemplate2 = jdbcTemplate2;
	}

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	public String finduser(String info){
		String sql = "select * from TB_USER_WX u,tb_user_power p where u.note=p.id and user_name like 'wx%'";	
		if(info!=null&&info.length()>0){
			sql += " and (user_name like '%"+info+"%' or user_pwd like '%"+info+"%' or"
					+ " real_name like '%"+info+"%' or p.power_name like '%"+info+"%')";
		}
		sql += " order by to_number(user_id) desc";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
	public String adduser(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String user_name = String.valueOf(paramMap.get("user_name"));
		String user_pwd = String.valueOf(paramMap.get("user_pwd"));
		String real_name = String.valueOf(paramMap.get("real_name"));
		String note = String.valueOf(paramMap.get("note"));
		int user_id = findMaxId("TB_USER_WX","user_id");
		int count = 0;
		String sql = "insert into TB_USER_WX (user_name,user_pwd,real_name,note,ba_id,comp_id,user_id) values "
			+ " ('"+user_name+"','"+user_pwd+"','"+real_name+"','"+note+"','11037','11037001','"+user_id+"')";
			count = jdbcTemplate2.update(sql);
		System.out.println(sql);
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(count>0){
			map.put("info", "添加成功");
		}else{
			map.put("info", "添加失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String edituser(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String user_name = String.valueOf(paramMap.get("user_name"));
		String user_pwd = String.valueOf(paramMap.get("user_pwd"));
		String real_name = String.valueOf(paramMap.get("real_name"));
		String note = String.valueOf(paramMap.get("note"));
		String user_id = String.valueOf(paramMap.get("user_id"));
		int count = 0;
		String sql = "update TB_USER_WX set user_name='"+user_name+"',user_pwd='"+user_pwd+
				"',real_name='"+real_name+"',note='"+note+"' where user_id='"+user_id+"'";
		count = jdbcTemplate2.update(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(count>0){
			map.put("info", "修改成功");
		}else{
			map.put("info", "修改失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String deluser(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String id = String.valueOf(paramMap.get("id"));
		String[] ids = id.split(",");
		String is = "";
		int count = 0;
		for (int i = 0; i < ids.length; i++) {
			is += "'"+ids[i]+"',";
		}
		String sql ="delete from TB_USER_WX where user_id in ("+is.substring(0, is.length()-1)+")";
		count = jdbcTemplate2.update(sql);
		System.out.println(sql);
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "删除成功");
		}else{
			map.put("info", "删除失败");
		}
		return jacksonUtil.toJson(map);
	}
	public int findMaxId(String table,String id){
		int id1 = 1;
		String sql = "select "+id+" from "+table+"  order by to_number("+id+") desc";
		System.out.println("findMaxId sql="+sql);
		List<Map<String, Object>> list = null;
		list = jdbcTemplate2.queryForList(sql);
		if(list.size()>0){
			id1 = Integer.parseInt(list.get(0).get(id).toString())+1;
		}
		return id1;
	}
	public String findpower(String info){
		List<Map<String, Object>> list = null;
		String sql = "select * from tb_user_power p where 1=1";
		if(info!=null&&info.length()>0){
			sql += " and power_name like '%"+info+"%'";
		}
		list = jdbcTemplate2.queryForList(sql);
		System.out.println("权限搜索:sql="+sql);
		return jacksonUtil.toJson(list);
	}
	public String addpower(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String power_name = String.valueOf(paramMap.get("power_name"));
		String power_but = String.valueOf(paramMap.get("power_but"));
		String power_info = String.valueOf(paramMap.get("power_info"));
		String power = qxym(power_info);
		int id = findMaxId("tb_user_power","id");
		String sql = "insert into tb_user_power (id,power_name,power,power_but,power_info) values("
				+ "'"+id+"','"+power_name+"','"+power+"','"+power_but+"','"+power_info+"')";
//		System.out.println(sql);
		int count = jdbcTemplate2.update(sql);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(count>0){
			map.put("info", "添加成功");
		}else{
			map.put("info", "添加失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String editpower(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String power_name = String.valueOf(paramMap.get("power_name"));
		String power_but = String.valueOf(paramMap.get("power_but"));
		String power_info = String.valueOf(paramMap.get("power_info"));
		String id = String.valueOf(paramMap.get("id"));
		String power = qxym(power_info);
		String sql = "update tb_user_power set power_name='"+power_name+"',power='"+power+"'"
				+ ",power_but='"+power_but+"',power_info='"+power_info+"' where  id='"+id+"'";
//		System.out.println(sql);
		int count = jdbcTemplate2.update(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(count>0){
			map.put("info", "修改成功");
		}else{
			map.put("info", "修改失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String delpower(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String id = String.valueOf(paramMap.get("id"));
		String[] ids = id.split(",");
		String is = "";
		for (int i = 0; i < ids.length; i++) {
			is += "'"+ids[i]+"',";
		}
		String sql = "delete from tb_user_power where id in ("+is.substring(0, is.length()-1)+")";
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
	public String qxym(String power_info){
		String ym = "[{"+
				"id: \"wxManagement\", name: \"维修管理\", icon: \"icon-wxgl\", displayState: true, children: [";
				if(power_info.indexOf("维修记录")>=0){
					ym += "{id: \"wxServer\", name: \"维修记录\", icon: \"icon-wxjl\", active: true, src: \"./app/html/wxServer_fcz.html\"},";
				}
				if(power_info.indexOf("维修车次")>=0){
					ym += "{id: \"wxTrips\", name: \"维修车次\", icon: \"icon-wxcc\", src: \"./app/html/wxTrips_fcz.html\"},";
				}
				if(power_info.indexOf("维修审核")>=0){
					ym += "{id: \"shMaintain\", name: \"维修审核\", icon: \"icon-wxsh\", src: \"./app/html/shMaintain_fcz.html\"},";
				}
				if(power_info.indexOf("电话回访")>=0){
					ym += "{id: \"dhHappen\", name: \"电话回访\", icon: \"icon-dhhf\", src: \"./app/html/dhHappen_fcz.html\"},";
				}
				ym = ym.substring(0,ym.length()-1);
			ym += "]},";
		ym+="{"+
			"id: \"whManagement\", name: \"维护管理\", icon: \"icon-whgl\", children: [";
		if(power_info.indexOf("地点维护")>=0){
			ym += "{id: \"ddMaintain\", name: \"地点维护\", icon: \"icon-ddwh\", src: \"./app/html/ddMaintain_fcz.html\"},";
		}
		if(power_info.indexOf("类型维护")>=0){
			ym += "{id: \"lxMaintain\", name: \"类型维护\", icon: \"icon-wxlx\", src: \"./app/html/lxMaintain_fcz.html\"},";
		}
		if(power_info.indexOf("故障维护")>=0){
			ym += "{id: \"gzMaintain\", name: \"故障维护\", icon: \"icon-gzwh\", src: \"./app/html/gzMaintain_fcz.html\"},";
		}
		if(power_info.indexOf("内容维护")>=0){
			ym += "{id: \"nrMaintain\", name: \"内容维护\", icon: \"icon-wxjl\", src: \"./app/html/nrMaintain_fcz.html\"},";
		}
		if(power_info.indexOf("地点维护")>=0||power_info.indexOf("类型维护")>=0||power_info.indexOf("故障维护")>=0){
			ym = ym.substring(0,ym.length()-1);	
		}
		ym += "]},";
		if(power_info.indexOf("用户管理")>=0){
			ym += "{id: \"userInfo\", name: \"用户管理\", icon: \"icon-yhgl\", src: \"./app/html/userInfo_fcz.html\"},";
		}
		if(power_info.indexOf("权限管理")>=0){
			ym += "{id: \"competence\", name: \"权限管理\", icon: \"icon-qxgl\", src: \"./app/html/competence_fcz.html\"},";
		}
//		if(power_info.indexOf("用户管理")>=0||power_info.indexOf("权限管理")>=0){
			ym = ym.substring(0,ym.length()-1);	
//		}
		ym += "]";
	return ym;
	}
}
