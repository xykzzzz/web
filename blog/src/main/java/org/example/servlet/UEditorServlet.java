package org.example.servlet;

import org.example.util.MyActionEnter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;


/**
 * ueditor 富文本编辑器图片上传
 * 修改idea中tomca配置的应用上下文路径
 * 实现后端接口
 */
@WebServlet("/ueditor")
public class UEditorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        URL url = UEditorServlet.class.getClassLoader().getResource("config.json");
        //URL获取到时，都是便编码后的字符串，使用需要先解码在使用
        String path= URLDecoder.decode(url.getPath(),"utf-8");
        //框架提供的富文本编辑器上传功能
        MyActionEnter enter=new MyActionEnter(req,path);
        String exec = enter.exec();//执行
        PrintWriter pw=resp.getWriter();
        pw.println(exec);
        pw.flush();
        pw.close();

    }
}
