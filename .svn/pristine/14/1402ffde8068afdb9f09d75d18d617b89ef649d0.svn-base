package mvc.service;

import helper.JacksonUtil;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class LoginServer {
	protected JdbcTemplate jdbcTemplate = null;
	protected JdbcTemplate jdbcTemplate2 = null;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

	public JdbcTemplate getJdbcTemplate2() {
		return jdbcTemplate2;
	}
	@Autowired
	public void setJdbcTemplate2(JdbcTemplate jdbcTemplate2) {
		this.jdbcTemplate2 = jdbcTemplate2;
	}


	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();


	public String login(String username, String password,String value,HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
//		if(!"wx".equals(username.substring(0,2))){
//			map.put("info", "1");
//			return jacksonUtil.toJson(map);
//		}
		List<Map<String, Object>> list = null;
		String sql= "select * from TB_USER_WX u,tb_user_power p where u.note=p.id and user_name = ? and user_pwd = ?";
		System.out.println(sql);		
		if(value.equals("0")){
			list = jdbcTemplate.queryForList(sql,username,password);
			map.put("value", "0");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
			String insert="insert into tb_login_history (USER_ID,LOGIN_TIME) values ('"+list.get(0).get("USER_ID")+"',to_date('"+date+"','yyyy-mm-dd hh24:mi:ss'))";
			System.out.println("login insert="+insert);		
			jdbcTemplate.update(insert);
		}else{
			list = jdbcTemplate2.queryForList(sql,username,password);
			map.put("value", "1");
		}
		if(list.size()>0){
			map.put("info", "0");
			request.getSession().setAttribute("username", list.get(0).get("user_name"));
			request.getSession().setAttribute("user_id", list.get(0).get("user_id"));
			request.getSession().setAttribute("power", list.get(0).get("power"));
			request.getSession().setAttribute("power_but", list.get(0).get("power_but"));
			request.getSession().setAttribute("power_info", list.get(0).get("power_info"));
			request.getSession().setAttribute("power_name", list.get(0).get("power_name"));
			if(!value.equals("1"))
				request.getSession().setAttribute("view_type", String.valueOf(list.get(0).get("view_type")).split("\\|").length>2?String.valueOf(list.get(0).get("view_type")).split("\\|")[2]:"");
		}else{
			map.put("info", "1");
		}
		System.out.println(request.getSession().getAttribute("view_type"));
		return jacksonUtil.toJson(map);
	}


	public String index(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("info", request.getSession().getAttribute("power"));
		map.put("power_but", request.getSession().getAttribute("power_but"));
		return jacksonUtil.toJson(map);
	}
	
	public String findlogin(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String STIME = String.valueOf(paramMap.get("STIME"));
		String ETIME = String.valueOf(paramMap.get("ETIME"));
		String GS = String.valueOf(paramMap.get("GS"));//公司
		String sql = "select u.*,c.comp_name,to_char(h.login_time,'yyyy-MM-dd HH24:mi:ss') login_time from TB_USER_WX u,tb_company c,tb_login_history h "
				+ " where u.user_id=h.user_id and u.ba_id=c.ba_id";
		if(STIME!=null&&!STIME.equals("null")&&STIME.length()>0){
			sql += " and h.login_time >=to_date('"+STIME+" 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		}
		if(ETIME!=null&&!ETIME.equals("null")&&ETIME.length()>0){
			sql += " and h.login_time <=to_date('"+ETIME+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		if(GS!=null&&!GS.equals("null")&&GS.length()>0&&!GS.equals("所有公司")){
			sql += " and c.comp_name = '"+GS+"'";
		}
		sql +="order by h.login_time desc";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
}
