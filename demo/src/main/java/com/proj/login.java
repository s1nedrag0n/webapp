package com.proj;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.sql.*;
import java.util.Date;
import java.util.HashMap;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.proj.HashTools;



public class login extends HttpServlet{

    public login(){
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
         String uname = req.getParameter("uname");
         String pwd = req.getParameter("pwd");
        
         try{
             //注册jdbc驱动
             Class.forName(JDBC_DRIVER);
 
             //连接数据库
             conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
 
             //执行sql
             String sql;
             sql = "select password,salt,user_id from user where username = ?";
             stmt = conn.prepareStatement(sql);
             stmt.setString(1, uname);
             ResultSet rs = stmt.executeQuery();
             
             if(rs.next()){
                String uid = rs.getString("user_id");
                String salt = rs.getString("salt");
                String pwdin=rs.getString("password");
                try{
                    pwd=HashTools.digestBySHA1(pwd+salt);
                    if(pwd.equals(pwdin)){
                        //登陆成功，发放token
                        
                        try{
                            String jwt=JWT.create()
                                .withHeader(new HashMap<>())
                                .withClaim("uname",uname)
                                .withClaim("uid", uid)
                                .withExpiresAt(new Date(System.currentTimeMillis() + 600*1000))// 10min
                                .sign(Algorithm.HMAC256("lzx"));
                            
                            
                            out.println(jwt);
                        }catch(Throwable e){
                            System.out.println(e);
                        }
                    }else{
                        out.print("e2");
                    }
                }catch(NoSuchAlgorithmException e){
                    System.out.println(e);
                }
             }else{
                out.print("e1");
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
