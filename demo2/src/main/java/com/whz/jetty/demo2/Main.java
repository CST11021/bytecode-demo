package com.whz.jetty.demo2;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

/**
 * 参考自：jvm-sandbox工程的com.alibaba.jvm.sandbox.core.server.jetty.JettyCoreServer类
 */
public class Main {

    /**
     * localhost:8080/whz-context/test1
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 绑定8080端口，创建Jetty server实例
        Server httpServer = new Server(8080);

        // 设置Jetty线程池
        httpServer.setThreadPool(buildThreadPool(true));

        final ServletContextHandler handler = new ServletContextHandler(NO_SESSIONS);
        // 设置上下文路径：localhost:8080/whz-context/*
        handler.setContextPath("/whz-context");
        // 添加请求处理器
        handler.addServlet(new ServletHolder(new ModuleHttpServlet()), "/test1/*");
        // 添加请求处理器
        handler.addServlet(new ServletHolder(new ModuleHttpServlet()), "/test2/*");
        httpServer.setHandler(handler);

        // 启动http服务
        httpServer.start();
        // Blocks until the thread pool is stopped.
        httpServer.join();
    }

    /**
     * 设置Jetty线程池
     *
     * @param daemon jetty线程设置为daemon，防止应用启动失败进程无法正常退出
     * @return
     */
    private static ThreadPool buildThreadPool(boolean daemon) {
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setDaemon(daemon);
        threadPool.setName("jetty-thread-pool-" + threadPool.hashCode());

        return threadPool;
    }

}