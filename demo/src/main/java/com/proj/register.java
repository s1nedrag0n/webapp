package com.proj;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.proj.HashTools;

import java.sql.*;
import java.util.Date;

public class register extends HttpServlet{

    public register(){
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
         String id = req.getParameter("id");
         String name = req.getParameter("name");
         String birth = req.getParameter("birth");
         String sex = req.getParameter("sex");
         String phonenum = req.getParameter("phonenum");
         String uname = req.getParameter("uname");
         String pwd = req.getParameter("pwd");
         
         Date date=new Date();
         String salt= date.toString();
         
         try{
            pwd = HashTools.digestBySHA1(pwd+salt);
         }catch(NoSuchAlgorithmException e){
            System.out.println(e);
         }
         try{
             //注册jdbc驱动
             Class.forName(JDBC_DRIVER);
 
             //连接数据库
             conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
 
             String sql;
             // 检查用户名是否重复
             sql="select * from user where username = ?";  
             ptmt=conn.prepareStatement(sql);
             ptmt.setString(1, uname);
             ResultSet rs=ptmt.executeQuery();
             if(rs.next()){
                out.println("username exists!");
                return ;
             }
             //执行sql
             sql = "insert into user value(?,?,?,?,?,?,'guest',?,?)";
             stmt = conn.prepareStatement(sql);
             stmt.setString(1, id);
             stmt.setString(2, name);
             stmt.setString(3, sex);
             stmt.setString(4, phonenum);
             stmt.setString(5, uname);
             stmt.setString(6, pwd);
             stmt.setString(7, birth);
             stmt.setString(8, salt);
             int cnt = stmt.executeUpdate();

             if(cnt==0){
                out.println("register.error!");
             }else{
                out.println("register.success!");
             }
             
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
