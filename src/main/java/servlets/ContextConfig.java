package servlets;

import model.Game;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

//@WebListener
public class ContextConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        Game game=Game.getInstance();
        servletContext.setAttribute("game",game);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
