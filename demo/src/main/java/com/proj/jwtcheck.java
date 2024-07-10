package com.proj;

import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;


public class jwtcheck extends HttpServlet{

    public jwtcheck(){
        super();
    }
    // jwt令牌校验
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         resp.setContentType("text/html");
             
         PrintWriter out = resp.getWriter(); 
        
         String token = req.getParameter("token");
         if(token==null){
            out.print("e");
         }else{
            try{
                JWTVerifier jwtVerifiers = JWT.require(Algorithm.HMAC256("lzx")).build();
                DecodedJWT decodedJWT = jwtVerifiers.verify(token);
                Claim uname = decodedJWT.getClaim("uname");
                Claim uid = decodedJWT.getClaim("uid");
                JSONArray array = new JSONArray();
                JSONObject json = new JSONObject();
                json.put("uname",uname.asString());
                json.put("uid", uid.asString());
                array.add(json);
                // System.out.println(array);
                out.println(array);
            }catch(Throwable e){
                System.out.println(e);
                out.print("e");
            }
         }
    }
}
