//package services.dataBaseService;
//
//import java.sql.*;
//
//public class DBService {
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
