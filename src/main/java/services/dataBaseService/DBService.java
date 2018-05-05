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
        Connection connection = null;
        //String sql = "DROP TABLE IF EXISTS Users";
        String sql = "CREATE TABLE IF NOT EXISTS Users (id INTEGER not NULL AUTO_INCREMENT, login VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, PRIMARY KEY(login))";
        //classic way to close resource
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public boolean isUserExist(String login) throws SQLException {
        String sql = "SELECT * FROM Users WHERE login=?";

        //use try-with resource https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.isBeforeFirst(); //isBeforeFirst return false if no rows in resultSet
            }
        }
    }

    public boolean isUserValid(String login, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE login=?";

        //use try-with resource https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String passwordStored = resultSet.getString("password");
                    return (password.equals(passwordStored));
                }
                return false;
            }
        }
    }

    public void addUser(String login, String password) throws SQLException {

        String sql = "INSERT INTO Users (login,password) VALUES(?,?)";

        try (Connection connection = dataSource.getConnection(); PreparedStatement prStatement = connection.prepareStatement(sql)) {
            prStatement.setString(1, login);
            prStatement.setString(2, password);
            prStatement.executeUpdate();
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
