package mvc.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helper.JacksonUtil;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class CommonServer1 {    
	protected JdbcTemplate jdbcTemplate2 = null;
	public JdbcTemplate getJdbcTemplate2() {
		return jdbcTemplate2;
	}
	@Autowired
	public void setJdbcTemplate2(JdbcTemplate jdbcTemplate2) {
		this.jdbcTemplate2 = jdbcTemplate2;
	}
	

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

	public String ssjk(String postData) {
		Map<String,Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String,Object>>() {});
		String gsmc = String.valueOf(paramMap.get("gsmc"));
		Map<String, Object> resultmap = new HashMap<String, Object>();
		return jacksonUtil.toJson(resultmap);
	}
	/**
	 * 公共基础类，通用方法
	 * 公司、分公司、车辆等下拉框
	 */
	//区块下拉框
	public String findqk(){
		String sql = "select owner_id,owner_name from TB_OWNER";
		List<Map<String, Object>> list = list = jdbcTemplate2.queryForList(sql);
		System.out.println("下拉区块sql语句="+sql);	
		List al = new ArrayList();
		Map map1 = new HashMap();
		map1.put("name", "所有区块");
		map1.put("id", "0");
		al.add(map1);
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("owner_name"));
			map.put("id", list.get(i).get("owner_id"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	//内容下拉框
	public String findnr(){
		String sql = "select RC_ID,RC_CONTENT from TB_REPAIR_CONTENT";
		List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
		System.out.println("下拉nr sql语句="+sql);
		List al = new ArrayList();
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("RC_MODE"));
			map.put("id", list.get(i).get("RC_ID"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	//公司下拉框
	public String findba(){
		String sql = "select ba_id,ba_name from TB_BUSI_AREA order by NLSSORT(ba_name,'NLS_SORT = SCHINESE_PINYIN_M')";
		List<Map<String, Object>> list =jdbcTemplate2.queryForList(sql);
		List al = new ArrayList();
		Map map1 = new HashMap();
		map1.put("name", "所有公司");
		map1.put("id", "0");
		al.add(map1);
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("ba_name"));
			map.put("id", list.get(i).get("ba_id"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	//分公司下拉框
	public String findcomp(String ba_id){
		String sql ="select comp_id,comp_name from TB_COMPANY where 1=1";
		if(ba_id!=null&&!ba_id.isEmpty()){
			sql += " and ba_id = '"+ba_id+"'";
		}
		sql += " order by comp_name";
		List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
		List al = new ArrayList();
		Map map1 = new HashMap();
		map1.put("name", "所有公司");
		map1.put("id", "0");
		al.add(map1);
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("comp_name"));
			map.put("id", list.get(i).get("comp_id"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	public String findcompowner(String owner_name){
		String sql = "select comp_id,comp_name from TB_COMPANY c,tb_owner o where c.owner_id=o.owner_id ";
		if(owner_name!=null&&!owner_name.isEmpty()){
			sql += " and owner_name = '"+owner_name+"'";
		}
		sql += " order by comp_name";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
		List al = new ArrayList();
		Map map1 = new HashMap();
		map1.put("name", "所有公司");
		map1.put("id", "0");
		al.add(map1);
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("comp_name"));
			map.put("id", list.get(i).get("comp_id"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	//车辆下拉框
	public String findvhic(String ba_id,String comp_id){
		String sql="select * from TB_VEHICLE where 1=1";
		if(ba_id!=null&&!ba_id.isEmpty()){
			sql += " and ba_id = '"+ba_id+"'";
		}
		if(comp_id!=null&&!comp_id.isEmpty()){
			sql += " and comp_id = '"+comp_id+"'";
		}
		List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
		List al = new ArrayList();
		Map map1 = new HashMap();
		map1.put("name", "所有车辆");
		map1.put("id", "0");
		al.add(map1);
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("vehi_no"));
			map.put("id", list.get(i).get("vehi_no"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}

	public String findvhicgs(String comp_name) {
		String sql="select * from TB_VEHICLE where 1=1";
		if(comp_name!=null&&!comp_name.isEmpty()){
			sql += " and comp_name = '"+comp_name+"'";
		}
		List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
		List al = new ArrayList();
		Map map1 = new HashMap();
		map1.put("name", "所有车辆");
		map1.put("id", "0");
		al.add(map1);
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("vehi_no"));
			map.put("id", list.get(i).get("vehi_no"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	public String findcomp_tj(String comp_name){
		String sql="select comp_id,comp_name from TB_COMPANY where 1=1";
		if(comp_name!=null&&!comp_name.isEmpty()){
			sql += " and comp_name like '%"+comp_name+"%'";
		}
		sql += " order by comp_name";
		List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
		List al = new ArrayList();
		Map map1 = new HashMap();
		map1.put("name", "所有公司");
		map1.put("id", "0");
		al.add(map1);
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("comp_name"));
			map.put("id", list.get(i).get("comp_id"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	public String findvhictj(String vhic){
		String sql= "select vehi_no,vehi_no from TB_VEHICLE where 1=1";
		if(vhic!=null&&!vhic.isEmpty()){
			sql += " and vehi_no like '%"+vhic+"%'";
		}
		List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
		List al = new ArrayList();
		Map map1 = new HashMap();
		map1.put("name", "所有车辆");
		map1.put("id", "0");
		al.add(map1);
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("vehi_no"));
			map.put("id", list.get(i).get("vehi_no"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	public String findwxry(HttpServletRequest request){
		String power_name = String.valueOf(request.getSession().getAttribute("power_name"));
		String user_name = String.valueOf(request.getSession().getAttribute("username"));
		String sql = "";
			if(!power_name.equals("一般员工")){
				sql = "select user_name,user_id,real_name from TB_USER_WX where user_name like 'wx%' order by user_name";
			}else {
				sql = "select user_name,user_id,real_name from TB_USER_WX where user_name like 'wx%' and user_name = '"+user_name+"' order by user_name";
			}			
		List<Map<String, Object>> list =jdbcTemplate2.queryForList(sql);
		List al = new ArrayList();
		Map map1 = new HashMap();
		map1.put("name", "所有人员");
		map1.put("id", "0");
		al.add(map1);
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("user_name"));
			map.put("real", list.get(i).get("real_name"));
			map.put("id", list.get(i).get("user_id"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	public String findzdlx(){
		String sql="select * from tb_mdt_type";
		
		List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
		List al = new ArrayList();
		Map map1 = new HashMap();
		map1.put("name", "所有终端");
		map1.put("id", "0");
		al.add(map1);
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("mt_name"));
			map.put("id", list.get(i).get("mt_name"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	public String findwxlx(){
		String sql="select * from TB_REPAIR_TYPE";
		List<Map<String, Object>> list =  jdbcTemplate2.queryForList(sql);
		List al = new ArrayList();
		Map map1 = new HashMap();
		map1.put("name", "所有维修类型");
		map1.put("id", "0");
		al.add(map1);
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("rt_type"));
			map.put("id", list.get(i).get("rt_type"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	public int findMaxId(){
		int id = 1;
		String sql = "select RR_ID from TB_REPAIR_RECORD  order by to_number(RR_ID) desc";
		System.out.println(sql);
		List<Map<String, Object>> list =jdbcTemplate2.queryForList(sql);
		if(list.size()>0){
			id = Integer.parseInt(list.get(0).get("RR_ID").toString())+1;
		}
		return id;
	}
	public String upload(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> filenames = new ArrayList<String>();
		int tp = 0;
		if (ServletFileUpload.isMultipartContent(request)) {
			String saveAsFileName = "";
			ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
			PrintWriter writer = null;
			response.setContentType("application/json");
			try {
				// writer = response.getWriter();
				List<FileItem> items = uploadHandler.parseRequest(request);
				int rr_id = findMaxId();				
				jdbcTemplate2.update("insert into TB_REPAIR_RECORD (RR_ID) values ('"+rr_id+"')");
				
				request.getSession().setAttribute("RR_ID", rr_id);
				for (FileItem item : items) {

					/**
					 * 提交的时候有图片的才上传保存至数据库
					 */
					if (item.getName() != null && item.getName().length() > 0) {
					
						if (!item.isFormField()) {
							String fp = "E:\\upload\\";
							File f = new File(fp);
							if (!f.exists()) {
								f.mkdirs();
							}
							if (item.getName().isEmpty() || item.getSize() == 0) {
								continue;
							}
							saveAsFileName = System.currentTimeMillis() + item.getFieldName()
									+ item.getName().substring(item.getName().lastIndexOf("."));

							File file = new File(fp + saveAsFileName + ".tmp");
							item.write(file);
							file.renameTo(new File(fp + saveAsFileName));
							try {
								String in = fp+ saveAsFileName;

								if (item.getFieldName().equals("RR_PICT")) {
									int a = jdbcTemplate2.update(
												"update TB_REPAIR_RECORD set RR_PICT =? where RR_ID = ?",
												new Object[] {in,rr_id});
									if (a == 0) {
										return "{\"code\":400,\"data\":\"图片提交失败\"}";
									} else {
										tp = 1;
									}
								} else if (item.getFieldName().equals("RR_END_PICT")) {
									int a =jdbcTemplate2.update(
												"update TB_REPAIR_RECORD set RR_END_PICT =? where RR_ID=?",
												new Object[] {in,rr_id});
									
									if (a == 0) {
										return "{\"code\":400,\"data\":\"图片提交失败\"}";
									} else {
										tp = 1;
									}
								} else if (item.getFieldName().equals("RR_WXD_PICT")) {
									int a  = jdbcTemplate2.update(
												"update TB_REPAIR_RECORD set RR_WXD_PICT =? where RR_ID=?",
												new Object[] {in,rr_id});
									if (a == 0) {
										return "{\"code\":400,\"data\":\"图片提交失败\"}";
									} else {
										tp = 1;
									}
								}
							} catch (Exception e) {
								return "{\"code\":400,\"data\":\"图片提交失败\"}";
							}
							filenames.add(saveAsFileName);
						}
					}
				}
			} catch (FileUploadException e) {
				return "{\"code\":400,\"data\":\"" + e.getMessage() + "\"}";
				
			} catch (Exception e) {
				return "{\"code\":400,\"data\":\"" + e.getMessage() + "\"}";
			} finally {
			}
		}
		if (tp == 0) {
			return "{\"code\":400,\"data\":\"图片提交未提交，请重新提交\"}";
		}
		map.put("data", "OK");
		return jacksonUtil.toJson(map);
	}
	public void query_pic(HttpServletRequest request, HttpServletResponse response,String type, String rr_id) {
		try {
			String sql = "select RR_PICT,RR_END_PICT,RR_WXD_PICT from TB_REPAIR_RECORD where rr_id = '"+ rr_id+"'";
			List<Map<String, Object>> list = jdbcTemplate2.queryForList(sql);
			String url = "";
			if(type.equals("0")){
				url = list.get(0).get("RR_PICT").toString();
			}else if(type.equals("1")){
				url = list.get(0).get("RR_END_PICT").toString();
			}else if(type.equals("2")){
				url = list.get(0).get("RR_WXD_PICT").toString();
			}
			System.out.println(url);
			File pf = new File(url);
			if (!pf.exists()) {
				return;
			}
			double rate = 0.5;
			int[] results = getImgWidth(pf);
			int widthdist = 0;
			int heightdist = 0;
			if (results == null || results[0] == 0 || results[1] == 0) {
				return;
			} else {
//				if (results[0]>126) {
//					rate = (double)126/(double)results[0];
//				}
				
				widthdist = (int) (results[0] * rate);
				heightdist = (int) (results[1] * rate);
			}
			Image src = javax.imageio.ImageIO.read(pf);
			BufferedImage tag = new BufferedImage((int) widthdist, (int) heightdist,
					BufferedImage.TYPE_INT_RGB);

			tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0,
					null);
			ServletOutputStream fout = response.getOutputStream();
			ImageIO.write(tag, "jpg", fout);
			fout.close();
		} catch (Exception e) {
		}
	}
	public static int[] getImgWidth(File file) {
		InputStream is = null;
		BufferedImage src = null;
		int result[] = { 0, 0 };
		try {
			is = new FileInputStream(file);
			src = javax.imageio.ImageIO.read(is);
			result[0] = src.getWidth(null); // 得到源图宽
			result[1] = src.getHeight(null); // 得到源图高
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
