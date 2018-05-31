package servlets;

import game.controller.Game;
import game.controller.GameHandler;
import services.dataBaseService.GameDAO;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.transaction.*;

@WebListener
public class ContextConfig implements ServletContextListener {
    @Inject
    private GameHandler gameHandler;

//    @Inject
//    DBService dbService;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        servletContext.setAttribute("gameHandler", gameHandler);
        //dbService.createTable();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
