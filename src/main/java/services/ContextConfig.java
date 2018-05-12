package services;

import model.Game;
import services.dataBaseService.DBService;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class ContextConfig implements ServletContextListener {
    @Inject
    private Game game;

//    @Inject
//    DBService dbService;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        servletContext.setAttribute("game", game);
        //dbService.createTable();

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
