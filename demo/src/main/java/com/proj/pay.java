package com.proj;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class pay extends HttpServlet{

    public pay(){
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
         PreparedStatement ptmt = null;

         String uid = req.getParameter("uid");
         String tid = req.getParameter("tid");
         String type = req.getParameter("type");
         Date date=new Date();
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String dtime = df.format(date);
         try{
             //注册jdbc驱动
             Class.forName(JDBC_DRIVER);
 
             //连接数据库
             conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
 
             String sql;
             if(type.equals("1")){
                sql="select rest1 from ticket where ticket_id = ?";
             }else{
                sql="select rest from ticket where ticket_id = ?";
             }
             ptmt = conn.prepareStatement(sql);
             ptmt.setString(1, tid);
             ResultSet res = ptmt.executeQuery();
             if(res.next()){
                Integer rest = res.getInt(1);
                if(rest<=0){
                    out.print("e1");
                    return ;
                }
             }else{
                out.print("e");
                return ;  
             }

             sql="insert into record (user_id,ticket_id,dealtime,type) value (?,?,?,?)"; 
             stmt = conn.prepareStatement(sql);
             stmt.setString(1, uid);
             stmt.setString(2, tid);
             stmt.setString(3, dtime);
             stmt.setString(4, type);
             
             int cnt = stmt.executeUpdate();
             if(cnt>0){
                out.print("s");
                PreparedStatement sptmt = null;

                if(type.equals("1")) sql="update ticket set rest1=rest1-1 where ticket_id = ?";
                else sql="update ticket set rest = rest-1 where ticket_id = ?";
                sptmt=conn.prepareStatement(sql);
                sptmt.setString(1, tid);
                int cc=sptmt.executeUpdate();
                sptmt.close();
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
                 if(ptmt!=null) ptmt.close();
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
