package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	 public static final String REDISSERVER = "127.0.0.1"; 
	    public static final String IMGSERVER = "http://127.0.0.1"; 
	    public static final String PRESSLOGOPATH = "/img/presslogo"; 
	    public static final boolean REDIS = true; 
	    Connection conn = null; 
	    public void getConnection(){ 
	  
	    try{ 
	        Class.forName("com.mysql.jdbc.Driver");   
	        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/znufe_2010_info","root",""); 
	        //conn = DBPool.getConnection(); 
	        //System.out.println("DB.java:链接成功"); 
	    }catch(ClassNotFoundException e){ 
	        System.out.println("数据库驱动未找到，详情为"+e); 
	    }catch(SQLException e){ 
	        System.out.println("连接数据库出错，详情为"+e); 
	    } 
	  
	    } 
	      
	    public ResultSet query(String sql){ 
	        ResultSet rs = null; 
	        this.getConnection(); 
	        try{ 
	            Statement stmt = conn.createStatement(); 
	            rs = stmt.executeQuery(sql); 
	        }catch(SQLException e){ 
	            System.err.println(e.toString() + sql); 
	        } 
	        this.close(); 
	        return rs; 
	    } 
	      
	    public int update(String sql){ 
	        int re = -1; 
	        this.getConnection(); 
	        try{ 
	            Statement stmt = conn.createStatement(); 
	            re = stmt.executeUpdate(sql); 
	        }catch(SQLException e){ 
	            System.err.println(e.toString() + sql); 
	        } 
	        this.close(); 
	        return re; 
	    } 
	      
	    public int updateInsert(String sql){ 
	        int id = -1; 
	        this.getConnection(); 
	        try{ 
	            Statement stmt = conn.createStatement(); 
	            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS); 
	            ResultSet rs = stmt.getGeneratedKeys();   
	            if (rs.next()) {   
	                id = rs.getInt(1);   
	                //System.out.println ("生成记录的key为 ：" + id);   
	            } 
	        }catch(SQLException e){ 
	            System.err.println(e.toString() + sql); 
	        } 
	        this.close(); 
	        return id; 
	    } 
	      
	      
	    public void close(){ 
	        conn = null; 
	    } 
}
