package com.whz.jetty.demo2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于处理模块的HTTP请求
 *
 * @author luanjia@taobao.com
 */
public class ModuleHttpServlet extends HttpServlet {

    public ModuleHttpServlet() {

    }

    /**
     * 处理http的get请求，例如：http://localhost:8820/sandbox/default/module/http/sandbox-module-mgr/list
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("接收到get请求，uri:" + req.getRequestURI());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("接收到post请求，uri:" + req.getRequestURI());
    }

}
