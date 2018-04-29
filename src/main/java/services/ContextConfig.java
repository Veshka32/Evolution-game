package services;

import model.Game;
import services.dataBaseService.JDBCService;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class ContextConfig implements ServletContextListener {
@Inject
Game game;

    @Resource(lookup="jdbc/H2database")
    DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        //game.addPropertyChangeListener(new GameChangeListener());
        ServletContext servletContext=event.getServletContext();
        servletContext.setAttribute("game",game);


        try (Connection connection = dataSource.getConnection()) {
            System.out.println(connection.isValid(10));
        } catch (SQLException e) {
            e.printStackTrace();
    }}

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
