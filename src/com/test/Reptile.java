package com.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



import redis.clients.jedis.Jedis;

public class Reptile {
	public void getUrl(){
		long start = System.currentTimeMillis();
		ArrayList<Info> infoList = list();
		Jedis jedis = new Jedis("192.168.56.100");
		int n = 0;
		for(Info i : infoList){
			if(n > 1970){
				String id = i.getId();
				System.out.println(n+":"+id);
				System.out.println(jedis.get(id));
				jedis.set(id, getImgUrl(id));
			}
			//if(n > 1970){
				//服务器当机了。明天继续。
				//String id = i.getId();
				//jedis.set(id, getImgUrl(id));1378345654688 1378345656082
			//}		
			n++;
		}
		//System.out.println(getImgUrl("310227199012260024"));
		long end = System.currentTimeMillis();
		
		System.out.println("时长：" + (end-start)/1000 );
		//爬1971-5095数据，共用时2126秒。
	}
	
	public void getUrlMend() throws IOException{
		long start = System.currentTimeMillis();
		
		BufferedReader br=new BufferedReader(new FileReader("C:\\temp.txt"));
		String line="";
		StringBuffer  buffer = new StringBuffer();
		while((line=br.readLine())!=null){
		buffer.append(line);
		}
		String fileContent = buffer.toString();
		br.close();
		System.out.println("debug1:"+fileContent);
		ArrayList<String> idList = new ArrayList<String>(); 
		char charArr[] = fileContent.toCharArray();
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < charArr.length; i++){	
			if(i == 0 || i % 18 != 0 ){
				sb.append(charArr[i]);
			}else{
				idList.add(sb.toString());
				sb.delete(0,sb.length());
				//System.out.println("debug"+sb.toString()+"debug");
				sb.append(charArr[i]);
			}	
		}

		Jedis jedis = new Jedis("192.168.56.100");
		int i = 0;
		if(idList.size() > 0)
			for(String id : idList){
				System.out.println(i+":"+id);
				jedis.set(id, getImgUrl(id));
				System.out.println(jedis.get(id));
				i++;
			}
		long end = System.currentTimeMillis();
		System.out.println("时长1：" + (end-start)/1000 );
		downloadPicMend(idList);
		
		
		end = System.currentTimeMillis();
		System.out.println("时长2：" + (end-start)/1000 );
	}
	
	public void updateMysql(){
		ArrayList<Info> infoList = list();
		Jedis jedis = new Jedis("192.168.56.100");		
		//DB db = new DB();
		int n = 0;
		long start = System.currentTimeMillis();
		int count = 0;
		System.out.println("start");
		for(Info i : infoList){
			if(n>0){
				
				String id = i.getId();
				String img = jedis.get(id);
				if(img.equals("img1/cw.jpg")){
					count++;
					System.out.println(id);
				}
					
				//System.out.println(n+":"+id);
				//System.out.println(img);
				//String sql = "UPDATE `stu_info` SET  `imgNet` = '" + img + "' WHERE `id` =  '" + id + "'";
				//db.update(sql);			
			}		
			n++;
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("时长：" + (end-start)/1000 +"\n错讹：" + count);	
	}
	
	public void downloadPicMend(ArrayList<String> idList){
		RobotBrowser rb = new RobotBrowser();
		StringBuffer sb = new StringBuffer();
		long start = System.currentTimeMillis();
		Jedis jedis = new Jedis("192.168.56.100");
		int n = 0;
		for(String id : idList){
			if(n > -1){
				String img = jedis.get(id);
				String url = "http://221.232.141.109/"+img;
				System.out.println(n+":"+id);
				//System.out.println(img);
				if(img.indexOf("/") != -1){
					String dest = "C:\\2010\\"+img.split("/")[1];
					//System.out.println(url+":"+dest);
					
					try {
						rb.download(url, dest);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					sb.append(id+"\n");
				}
				
			}
	
			n++;
		}
		long end = System.currentTimeMillis();
		
		System.out.println("时长：" + (end-start)/1000 );
		System.out.println(sb.toString());
		
		//61张图38秒
	}
	
	public void downloadPic(){
		RobotBrowser rb = new RobotBrowser();
		StringBuffer sb = new StringBuffer();
		long start = System.currentTimeMillis();
		ArrayList<Info> infoList = list();
		Jedis jedis = new Jedis("192.168.56.100");
		int n = 0;
		for(Info i : infoList){
			if(n > -1){
				String id = i.getId();
				String img = jedis.get(id);
				String url = "http://221.232.141.109/"+img;
				System.out.println(n+":"+id);
				//System.out.println(img);
				if(img.indexOf("/") != -1){
					String dest = "C:\\2010\\"+img.split("/")[1];
					//System.out.println(url+":"+dest);
					
					try {
						rb.download(url, dest);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					sb.append(id+"\n");
				}
				
			}
	
			n++;
		}
		long end = System.currentTimeMillis();
		
		System.out.println("时长：" + (end-start)/1000 );
		System.out.println(sb.toString());
	}
	
	public static ArrayList<Info> list(){
		ArrayList<Info> infoList = new ArrayList<Info>();
		DB db = new DB();
        String sql = "SELECT stu_num, id FROM stu_info";
        ResultSet rs = db.query(sql);
        try {
            while(rs.next()){
            	Info i = new Info();
            	i.setStu_num(rs.getString(1));
            	i.setId(rs.getString(2));
            	infoList.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return infoList;
	} 
	
	public static String getImgUrl(String sfzh){
		String reqData = "xx=10520&sfzh="+sfzh+"&sub=提交";
		//System.out.println("debug1:"+reqData);
		String img = "";
		RobotBrowser rb = new RobotBrowser();
		String resp = rb.getPostMethod("http://221.232.141.109/cx.php",reqData,"utf-8");
		//System.out.println(resp);
		if(resp.indexOf("src=") != -1){
			String temp[] = resp.split("src="); 
			img = temp[1].split(">")[0];
			img = img.replaceAll("'", "");
		}
		return img;
	}
	
	public void migrate() throws UnsupportedEncodingException, NoSuchAlgorithmException{
		DB db = new DB();
		String sql = "SELECT stu_num, imgNet, sex FROM stu_info";
		ResultSet rs = db.query(sql);
		ArrayList<Info> infoList = new ArrayList<Info>();
		try {
            while(rs.next()){
            	Info i = new Info();
            	i.setStu_num(rs.getString(1));
            	i.setImgNet(rs.getString(2));
            	i.setImgLocal(rs.getString(3));
            	infoList.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		for(Info i : infoList){
			sql = "INSERT INTO `znufe_2010_info`.`student` (`id`, `origin`,`sex`, `img`, `score`, `expect`, `result`) VALUES ('" + getMD5(i.getStu_num()) + "','" + i.getStu_num() + "','"+i.getImgLocal()+"','" + i.getImgNet() + "','0', '0.5', '0')";
			
			int re = db.updateInsert(sql);
			System.out.println(re + ":" + sql);
			long start = System.currentTimeMillis();
			for(int c = 0; c < 300000; c++);//缓冲，本来可以写个线程什么的，懒得查API，趁着这个时间，就出去喝杯茶好了。
			long end = System.currentTimeMillis();
			System.out.println("时长：" + (end-start)/1000 );
		}
		
	}
	public String getMD5(String pwd) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		pwd = pwd + "david";
		MessageDigest md = MessageDigest.getInstance("MD5");//SHA 或者 MD5
		byte[] pwdByte = pwd.getBytes("UTF-8");
		md.reset();
		md.update(pwdByte);
		byte[] digest = md.digest();
		BigInteger bigInt = new BigInteger(1,digest);
		String hashtext = bigInt.toString(16);
		while(hashtext.length() < 32 ){
			hashtext = "0"+hashtext;
		}
		return hashtext;
	}
}
