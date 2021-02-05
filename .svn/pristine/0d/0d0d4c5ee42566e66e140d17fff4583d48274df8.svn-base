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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

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
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class WHServer {
	protected JdbcTemplate jdbcTemplate = null;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	public String find(String postData) {
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String table = String.valueOf(paramMap.get("table"));
		String whfs = String.valueOf(paramMap.get("whfs"));
		String whnr = String.valueOf(paramMap.get("whnr"));
		String sql = "select * from "+table+" where 1=1 ";
		if(table.equals("TB_REPAIR_ADDR")){
			if(!whfs.equals("null")){
				sql += " and ra_addr like '%"+whfs+"%'";
			}
			sql += " order by to_number(RA_ID) desc";
		}else if(table.equals("TB_REPAIR_MALFUNCTION")){
			if(!whfs.equals("null")){
				sql += " and RM_MALFUNCTION like '%"+whfs+"%'";
			}
			sql += " order by to_number(RM_ID) desc";
		}else if(table.equals("TB_REPAIR_TYPE")){
			if(!whfs.equals("null")){
				sql += " and rt_type like '%"+whfs+"%'";
			}
			sql += " order by RT_TYPE";
		}else if(table.equals("TB_REPAIR_CONTENT")){
			if(!whfs.equals("null")){
				sql += " and RC_CONTENT like '%"+whfs+"%'";
			}
			if(!whnr.equals("null")){
				sql += " and RC_MODE like '%"+whnr+"%'";
			}
			sql += " order by to_number(RC_ID) desc";
		}
		System.out.println(sql+"  "+table);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
	public String save(String postData) {
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String table = String.valueOf(paramMap.get("table"));
		String whfs = String.valueOf(paramMap.get("whfs"));
		String whnr = String.valueOf(paramMap.get("whnr"));
		int id = 0;
		String sql = "insert into ";
		if(table.equals("TB_REPAIR_ADDR")){
			id = findMaxId("TB_REPAIR_ADDR", "RA_ID");
			sql += table +"(RA_ID,RA_ADDR) values ( '"+id+"','"+whfs+"')";
		}else if(table.equals("TB_REPAIR_TYPE")){
			id = findMaxId("TB_REPAIR_TYPE", "RT_ID");
			sql += table +"(RT_ID,RT_TYPE) values ( '"+id+"','"+whfs+"')";
		}else if(table.equals("TB_REPAIR_CONTENT")){
			id = findMaxId("TB_REPAIR_CONTENT", "RC_ID");
			sql += table +"(RC_ID,RC_CONTENT,RC_MODE) values ( '"+id+"','"+whfs+"','"+whnr+"')";
		}else if(table.equals("TB_REPAIR_MALFUNCTION")){
			id = findMaxId("TB_REPAIR_MALFUNCTION", "RM_ID");
			sql += table +"(RM_ID,RM_MALFUNCTION) values ( '"+id+"','"+whfs+"')";
		}
		int c = jdbcTemplate.update(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(c>0){
			map.put("info", "添加成功");
		}else{
			map.put("info", "添加失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String edit(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String table = String.valueOf(paramMap.get("table"));
		String whfs = String.valueOf(paramMap.get("whfs"));
		String whnr = String.valueOf(paramMap.get("whnr"));
		String id = String.valueOf(paramMap.get("id"));
		String sql = "update "+table+" set ";
		if(table.equals("TB_REPAIR_ADDR")){
			sql += " RA_ADDR ='"+whfs+"' where RA_ID ='"+id+"'";
		}else if(table.equals("TB_REPAIR_TYPE")){
			sql += " RT_TYPE ='"+whfs+"' where RT_ID ='"+id+"'";
		}else if(table.equals("TB_REPAIR_CONTENT")){
			sql += " RC_CONTENT ='"+whfs+"',RC_MODE='"+whnr+"' where RC_ID ='"+id+"'";
		}else if(table.equals("TB_REPAIR_MALFUNCTION")){
			sql += " RM_MALFUNCTION ='"+whfs+"' where RM_ID ='"+id+"'";
		}
		int c = jdbcTemplate.update(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(c>0){
			map.put("info", "修改成功");
		}else{
			map.put("info", "修改失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String del(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String table = String.valueOf(paramMap.get("table"));
		String id = String.valueOf(paramMap.get("id"));
		String[] ids = id.split(",");
		String is = "";
		for (int i = 0; i < ids.length; i++) {
			is += "'"+ids[i]+"',";
		}
		String sql = "delete from "+table+" where ";
		if(table.equals("TB_REPAIR_ADDR")){
			sql += "RA_ID in ("+is.substring(0, is.length()-1)+")";
		}else if(table.equals("TB_REPAIR_TYPE")){
			sql += "RT_ID in ("+is.substring(0, is.length()-1)+")";
		}else if(table.equals("TB_REPAIR_CONTENT")){
			sql += "RC_ID in ("+is.substring(0, is.length()-1)+")";
		}else if(table.equals("TB_REPAIR_MALFUNCTION")){
			sql += "RM_ID in ("+is.substring(0, is.length()-1)+")";
		}
		System.out.println(sql);
		int count = jdbcTemplate.update(sql);
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
	public int findMaxId(String table,String ids){
		int id = 1;
		String sql = "select "+ids+" from "+table +"  order by to_number("+ids+") desc";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(list.size()>0){
			id = Integer.parseInt(list.get(0).get(ids).toString())+1;
		}
		return id;
	}
	public String find1(){
		String sql = "select distinct rc_mode from TB_REPAIR_CONTENT t";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
	}

	public String findpjgl(String postData) {
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String keyword = String.valueOf(paramMap.get("keyword"));
		String tj ="";
		if(keyword != null && !keyword.isEmpty() && !keyword.equals("null") && keyword.length() > 0){
			tj +=" and a.PARTS_NAME like '%"+keyword+"%'";
		}

		String sql = "select * from tb_repair_parts a where 1=1";
		sql +=tj;
		sql += " order by PARTS_ID desc";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
	}

	public String addpjgl(String postData) {
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String parts_name = String.valueOf(paramMap.get("parts_name"));
		String parts_price = String.valueOf(paramMap.get("parts_price"));
		String sql = "insert into tb_repair_parts(parts_name,parts_price,parts_id) values('"+parts_name+"','"+parts_price+"','"+System.currentTimeMillis()+"')";
		System.out.println(sql);
		int count = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "添加成功");
		}else{
			map.put("info", "添加失败");
		}
		return jacksonUtil.toJson(map);
	}

	public String updatepjgl(String postData) {
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String parts_name = String.valueOf(paramMap.get("parts_name"));
		String parts_price = String.valueOf(paramMap.get("parts_price"));
		String parts_id = String.valueOf(paramMap.get("parts_id"));
		String sql = "update tb_repair_parts set" +
				" parts_name='"+parts_name+"'," +
				" parts_price='"+parts_price+"'" +
				" where parts_id='"+parts_id+"'";
		System.out.println(sql);
		int count = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "修改成功");
		}else{
			map.put("info", "修改失败");
		}
		return jacksonUtil.toJson(map);
	}

	public String deletepjgl(String postData) {
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String id = String.valueOf(paramMap.get("id"));String[] ids = id.split(",");
		String is = "";
		for (int i = 0; i < ids.length; i++) {
			is += "'"+ids[i]+"',";
		}
		String sql = "delete from tb_repair_parts  where parts_id in ("+is.substring(0, is.length()-1)+")";
		System.out.println(sql);
		int count = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "删除成功");
		}else{
			map.put("info", "删除失败");
		}
		return jacksonUtil.toJson(map);
	}
}
