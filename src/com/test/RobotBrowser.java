package com.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;


public class RobotBrowser {
	
	/*public static void main(String[] args){
		System.out.println(getPostMethod("http://221.232.141.109/cx.php","xx=10520&sfzh=310227199012260024&sub=提交","utf-8"));
	}*/
	
	public String getPostMethod(String reqURL, String reqData, String encodeCharset){
		String reseContent = "";
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
		
		HttpPost httpPost = new HttpPost(reqURL);
		httpPost.addHeader("Accept", "*/*");
		httpPost.addHeader("Accept-Language", "zh-cn");
		httpPost.addHeader("Referer", "http://221.232.141.109/caxun1.php");
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		httpPost.addHeader("Cache-Control", "no-cache");
		httpPost.addHeader("Accept-Encoding", "gzip, deflate");
		httpPost.addHeader("User-Agent",
				"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
		httpPost.addHeader("Host", "221.232.141.109");
		
		try{
			if(null != reqData){
				if(reqData.contains("&")){
					List<NameValuePair> formParams = new ArrayList<NameValuePair>();
					for(String str : reqData.split("&")){
						formParams.add(new BasicNameValuePair(str.substring(0,str.indexOf("=")), str.substring(str.indexOf("=")+1)));
					}
					StringEntity entity = new StringEntity(URLEncodedUtils.format(formParams, encodeCharset));
					entity.setContentType("application/x-www-form-urlencoded");
					httpPost.setEntity(entity);
				}else{
					httpPost.setEntity(new StringEntity(reqData, encodeCharset));
				}
				
			}
			HttpResponse response = httpClient.execute(httpPost);
			
			//System.out.println(response.toString());
			HttpEntity entity = response.getEntity();

			if(null != entity){
				reseContent = EntityUtils.toString(entity, "utf-8");
			}
		}catch(ConnectTimeoutException e){
			System.out.println("1");
		}catch(SocketTimeoutException e){
			System.out.println("2");
		}catch(ClientProtocolException e){
			System.out.println("3");
		}catch(IOException e){
			System.out.println("IO4"+e);
		}
		
		
		return reseContent;
	}
	
	@SuppressWarnings("deprecation")
	public File download(String url,String dest) throws IOException{
		File storeFile = null;
		
		HttpClient client = new DefaultHttpClient();  
		HttpGet get = new HttpGet(url);  
		HttpResponse res = client.execute(get);
		if (HttpStatus.SC_OK == res.getStatusLine().getStatusCode()) {
	         //请求成功
	         HttpEntity entity = res.getEntity();

	         if (entity != null && entity.isStreaming()) {
	            // 　为目标文件创建目录
	            int lastSptAt = dest.lastIndexOf(File.separator);
	            File dir = new File(dest.substring(0, lastSptAt));
	            dir.mkdirs();
	            // 创建一个空的目标文件
	            storeFile = new File(dest);
	            storeFile.createNewFile();
	            FileOutputStream fos = new FileOutputStream(storeFile);

	            // 将取得的文件文件流写入目标文件
	            InputStream is = entity.getContent();
	            byte[] b = new byte[1024];
	            int j = 0;

	            while ((j = is.read(b)) != -1) {
	               fos.write(b, 0, j);
	            }

	            fos.flush();
	            fos.close();
	         } else {
	            System.out.println("[" + url + "] 未找到.");
	            return null;
	         }
	         if (entity != null) {
	            entity.consumeContent();
	         }

	      }
		return storeFile;
	}
	
	public String inputStream2String (InputStream in) throws IOException {
	    StringBuffer out = new StringBuffer();
	    byte[] b = new byte[4096];
	    for (int n; (n = in.read(b)) != -1;) {
	        out.append(new String(b, 0, n));
	    }
	    return out.toString();
	}
}
