package main;

import accounts.AccountService;
import accounts.UserProfile;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.resource.Resource;
import servlets.SessionsServlet;
import servlets.SignInServlet;
import servlets.SignUpServlet;
import servlets.UsersServlet;

import java.net.URI;
import java.net.URL;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class Main {

    public static void main(String[] args) throws Exception {

        AccountService accountService = new AccountService();

        accountService.addNewUser(new UserProfile("admin"));
        accountService.addNewUser(new UserProfile("test"));

        ClassLoader cl  = Main.class.getClassLoader();
        URL f = cl.getResource("public_html/");
        if (f == null) { throw new RuntimeException("Unable to find resource directory");}
        URI webRootUri = f.toURI();
//        URI webRootUri = f.toURI().resolve("./").normalize();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new UsersServlet(accountService)), "/api/v1/users");
        context.addServlet(new ServletHolder(new SessionsServlet(accountService)), "/api/v1/sessions");
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), "/signin");

        context.setContextPath("/");
        context.setBaseResource(Resource.newResource(webRootUri));
        ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
        holderPwd.setInitParameter("dirAllowed","true");
        context.addServlet(holderPwd,"/");

//        ResourceHandler resource_handler = new ResourceHandler();
//        resource_handler.setResourceBase("Authorization/public_html/");
//        resource_handler.setResourceBase("/D:/StepicWebServerStudy/Authorization/target/classes/public_html");

        Server server = new Server(8080);
//        HandlerList handlers = new HandlerList();
//        handlers.setHandlers(new Handler[]{resource_handler, context});
//        handlers.setHandlers(new Handler[]{context});
//        server.setHandler(handlers);
        server.setHandler(context);

        server.start();
        Log.getRootLogger().info("Server started");
        server.join();
    }
}
