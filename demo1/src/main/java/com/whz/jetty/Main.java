package com.whz.jetty;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 参考自：zkui工程
 *
 */
public class Main {

    // 表示web资源目录
    private static final String WEB_FOLDER = "webapp";

    /**
     * localhost:8080/whz-context/login
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 创建Jetty server实例，这里使用守护线程启动Jetty服务器，如果是作为单独的服务启动
        Server httpServer = new Server(buildThreadPool(true));

        // 绑定8080端口
        ServerConnector connector = new ServerConnector(httpServer);
        connector.setPort(8080);
        httpServer.addConnector(connector);

        // 扫描容器和web app jars查找@WebServlet、@WebFilter、@WebListener等
        supportJettyAnnotation(httpServer);

        // 设置请求处理处理器
        httpServer.setHandler(buildHandler());

        // 启动http服务
        httpServer.start();
        // 阻塞主线程，直到Jetty线程池停止
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

    /**
     * 创建web请求处理器
     *
     * @return
     */
    private static Handler buildHandler() {
        // 创建Web应用上下文
        Handler servletContextHandler = createWebAppContext(WEB_FOLDER);

        // 添加静态资源处理器
        Handler staticResourceHandler = createResourceHandler(WEB_FOLDER);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{staticResourceHandler, servletContextHandler});

        return handlers;
    }

    /**
     * 扫描容器和web app jars查找@WebServlet、@WebFilter、@WebListener等
     *
     * @param httpServer
     */
    private static void supportJettyAnnotation(Server httpServer) {
        Configuration.ClassList clist = Configuration.ClassList.setServerDefault(httpServer);
        clist.addBefore(JettyWebXmlConfiguration.class.getName(), AnnotationConfiguration.class.getName());
    }

    /**
     * 创建web请求处理器
     *
     * @param webFolder
     * @return
     */
    private static WebAppContext createWebAppContext(String webFolder) {
        WebAppContext servletContextHandler = new WebAppContext();
        // 设置上下文路径：localhost:8080/whz-context/*
        servletContextHandler.setContextPath("/whz-context");
        servletContextHandler.setResourceBase("src/main/resources/" + webFolder);
        servletContextHandler.setParentLoaderPriority(true);
        servletContextHandler.setInitParameter("useFileMappedBuffer", "false");
        servletContextHandler.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*(/target/classes/|.*.jar)");
        return servletContextHandler;
    }

    /**
     * 添加静态资源处理器
     *
     * @param webFolder
     * @return
     */
    private static ResourceHandler createResourceHandler(String webFolder) {
        ResourceHandler staticResourceHandler = new ResourceHandler();
        staticResourceHandler.setDirectoriesListed(false);
        staticResourceHandler.setBaseResource(Resource.newClassPathResource(webFolder));
        staticResourceHandler.setWelcomeFiles(new String[]{"html/index.html"});
        return staticResourceHandler;
    }

}