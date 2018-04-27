package services;

import model.Game;
import servlets.GameChangeListener;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebListener
public class ContextConfig implements ServletContextListener {
@Inject
Game game;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        //game.addPropertyChangeListener(new GameChangeListener());
        ServletContext servletContext=event.getServletContext();
        servletContext.setAttribute("game",game);

        try {
            Connection connection=DriverManager.getConnection("jdbc:h2:C:/Users/stas/Documents/evo/h2", "admin", "admin");
        } catch (SQLException e) {e.printStackTrace();

        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
