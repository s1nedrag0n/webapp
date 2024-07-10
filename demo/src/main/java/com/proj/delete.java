package com.proj;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.sql.*;


public class delete extends HttpServlet{

    public delete(){
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
         resp.setContentType("text/html");
       
         PrintWriter out = resp.getWriter();
         //获取数据库数据
         Connection conn = null;
         PreparedStatement stmt = null;

         String tid = req.getParameter("tid");
         String uid = req.getParameter("uid");
        //  Date date=new Date();
        //  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //  String dtime = df.format(date);
         try{
             //注册jdbc驱动
             Class.forName(JDBC_DRIVER);
 
             //连接数据库
             conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
 
             String sql;
             sql="delete from record where ticket_id = ? and user_id = ?"; 
             stmt=conn.prepareStatement(sql);
             stmt.setString(1, tid);
             stmt.setString(2, uid);

             int cc = stmt.executeUpdate();
             if(cc>0){
                out.print("s");
             }else{
                out.print("e");
             }

         }catch(SQLException se){
             // 处理 JDBC 错误
             se.printStackTrace();
             out.print("e");
         }catch(Exception e){
             // 处理 Class.forName 错误
             e.printStackTrace();
             out.print("e");
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
