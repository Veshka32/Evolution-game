package services.dataBaseService;
//

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.*;

@ApplicationScoped
public class DBService {
    @Resource(lookup = "jdbc/H2database")
    DataSource dataSource;
    //https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html

    public DBService() {
    }

    public void createTable() throws SQLException {
        Statement statement = null;
        //String sql = "DROP TABLE IF EXISTS Users";
        String sql = "CREATE TABLE IF NOT EXISTS Users (id INTEGER not NULL AUTO_INCREMENT, login VARCHAR(255) NOT NULL, PRIMARY KEY(login))";
        try (Connection connection = dataSource.getConnection()) {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) statement.close();
        }
    }

    public boolean isUserExist(String login) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        String sql = "SELECT * FROM Users WHERE login='" + login + "'";
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
            return resultSet.isBeforeFirst(); ////isBeforeFirst return false if no rows in resultSet
        }
    }

    public void addUser(String login) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        String sql = "INSERT INTO Users (login) VALUES('" + login + "')";
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }
}
//
//    public DBService(){}
//
//    private static final JDBCService jdbcService = DataAccessFactory.getJDBCUtils();
//
//    public static void getData() throws Exception {
//        //Map<Integer, String> data = new HashMap<Integer, String>();
//        Connection connection = null;
//        Statement statement = null;
//        ResultSet resultSet = null;
//        try {
//            //Class.forName("org.h2.Driver");
//            //connection=DriverManager.getConnection("jdbc:h2:C:/Users/stas/Documents/evo/h2base", "admin", "admin");
//            //connection=DriverManager.getConnection("jdbc:h2:./h2base", "admin", "admin");
//            connection = jdbcService.getConnection();
//            System.out.println(connection.isValid(10)); //check connection is open
//            statement=connection.createStatement();
//            String sql="DROP TABLE IF EXISTS Users";
//            statement.executeUpdate(sql);
//            sql="CREATE TABLE IF NOT EXISTS Users (id INTEGER not NULL, login VARCHAR(255), password VARCHAR(255), email VARCHAR(255), PRIMARY KEY(id))";
//            statement.executeUpdate(sql);
//            sql="INSERT INTO Users VALUES(1, 'test','test','email')";
//            statement.executeUpdate(sql);
//            sql="INSERT INTO Users VALUES(2, 'poppy','test','email')";
//            statement.executeUpdate(sql);
//            sql="SHOW TABLES";
//            resultSet=statement.executeQuery(sql);
//            sql="SHOW COLUMNS FROM Users";
//            resultSet=statement.executeQuery(sql);
//            sql="SELECT login FROM Users";
//            resultSet=statement.executeQuery(sql);
//            while (resultSet.next()){
//                System.out.println(resultSet.getString("login"));}
//
//        } finally {
//            if (resultSet != null) {
//                resultSet.close();
//                System.out.println("Is resultset clodsed"+resultSet.isClosed());
//            }
//
//            if (statement != null) {
//                statement.close();
//                System.out.println("Is statement clodsed"+statement.isClosed());
//            }
//
//            if (connection != null) {
//                connection.close();
//                System.out.println("Is conn clodsed"+connection.isClosed());
//            }
//        }
//    }
//    }
//
//
