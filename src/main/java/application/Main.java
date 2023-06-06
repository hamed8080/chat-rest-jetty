package application;

import config.LoggerContextConfiguration;
import config.MyAppProperties;
import servlets.ConnectionStatusServlet;
import servlets.MessageServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.ThreadServlet;

/**
 * Created by hamed hosseini on 4/29/2023.
 */

public class Main {
    public static ChatManager chatManager;
    public static void main(String[] args) throws Exception {
        MyAppProperties config = MyAppProperties.loadExternalConfig() != null ? MyAppProperties.loadExternalConfig() : MyAppProperties.loadResourceConfig();
        chatManager = new ChatManager(config);
        System.out.println("**********************************");
        LoggerContextConfiguration.config(config.getIsLoggable());
        assert config != null;
        Server server = new Server(config.getServerPort());
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        ServletHolder messageHolder = context.addServlet(MessageServlet.class,"/message");
        ServletHolder threadHolder = context.addServlet(ThreadServlet.class,"/thread");
        ServletHolder connectionHolder = context.addServlet(ConnectionStatusServlet.class,"/connection");
        messageHolder.setAsyncSupported(true);
        threadHolder.setAsyncSupported(true);
        connectionHolder.setAsyncSupported(true);
        server.setHandler(context);
        server.start();
        server.join();
    }
}
