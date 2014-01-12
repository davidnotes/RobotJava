package com.test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class RobotJava {
	public static void main(String[] args){
		
		//new Reptile().updateMysql();
		try {
			//System.out.println(new Reptile().getMD5("1009050102"));
			new Reptile().migrate();
			
			
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
