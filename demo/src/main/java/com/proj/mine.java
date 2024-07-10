package com.proj;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.sql.*;


public class mine extends HttpServlet{

    public mine(){
        super();
    }

    //Mysql连接
    //定义JDBC相关变量
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/test?useSS=false&allowPublicKeyRetrieval=true&serverTimeZone=UTC";
    
    //定义用户名和密码
    static final String USER = "user1";
    static final String PASSWORD = "123456";
    
   
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         //获取数据库数据
         Connection conn = null;
         PreparedStatement stmt = null;
         String uid = req.getParameter("uid");
        
         try{
             //注册jdbc驱动
             Class.forName(JDBC_DRIVER);
 
             //连接数据库
             conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
 
             //执行sql
             String sql;
             sql = "select record.ticket_id as ticket_id,train_name,origin,dest,start_time,timestamp(start_time,spend) as end_time,spend,price1,price,type from ticket,record where user_id=? and ticket.ticket_id=record.ticket_id";
             
             stmt = conn.prepareStatement(sql);
             stmt.setString(1, uid);
             ResultSet rs = stmt.executeQuery();
             
             //输出数据
             JSONArray array = new JSONArray(); 
             ResultSetMetaData MeteData= rs.getMetaData();
             int ColumnCount = MeteData.getColumnCount();
             while(rs.next()){
                 JSONObject json = new JSONObject();
 
                 for(int i = 1;i <= ColumnCount;i++){
                     String value = null;
                     String ColumnName = MeteData.getColumnLabel(i);
                     if(rs.getString(ColumnName)!=null && !rs.getString(ColumnName).equals("")){
                         value = new String(rs.getBytes(ColumnName),"UTF-8");
                     }else{
                         value = "";
                     }
                     json.put(ColumnName, value);
                 }
                 array.add(json);
             }
             
             
             resp.setContentType("text/html");
             
             PrintWriter out = resp.getWriter();
 
             out.println(array);
             
         }catch(SQLException se){
             // 处理 JDBC 错误
             se.printStackTrace();
         }catch(Exception e){
             // 处理 Class.forName 错误
             e.printStackTrace();
         }finally{
             // 关闭资源
             try{
                 if(stmt!=null) stmt.close();
             }catch(SQLException se2){
             }// 什么都不做
             try{
                 if(conn!=null) conn.close();
             }catch(SQLException se){
                 se.printStackTrace();
             }
         }
    }
}
