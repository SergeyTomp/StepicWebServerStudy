package main;

import chat.WebSocketChatServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.resource.Resource;

import java.net.URI;
import java.net.URL;

/**
 * @author v.chibrikov
 *         <p/>
 *         Пример кода для курса на https://stepic.org/
 *         <p/>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        ClassLoader cl  = Main.class.getClassLoader();
        URL f = cl.getResource("public_html/");
        if (f == null) { throw new RuntimeException("Unable to find resource directory");}
        URI webRootUri = f.toURI();

        context.addServlet(new ServletHolder(new WebSocketChatServlet()), "/chat");

        context.setContextPath("/");
        context.setBaseResource(Resource.newResource(webRootUri));
        ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
        holderPwd.setInitParameter("dirAllowed","true");
        context.addServlet(holderPwd,"/");

//        ResourceHandler resource_handler = new ResourceHandler();
//        resource_handler.setDirectoriesListed(true);
//        resource_handler.setResourceBase("./WebSockets/resources/public_html");
//
//        HandlerList handlers = new HandlerList();
//        handlers.setHandlers(new Handler[]{resource_handler, context});
//        server.setHandler(handlers);
        server.setHandler(context);


        server.start();
        Log.getRootLogger().info("Server started");
        server.join();
    }
}
