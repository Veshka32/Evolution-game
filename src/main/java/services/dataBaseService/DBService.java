package services.dataBaseService;
//

import entities.Users;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;

@ApplicationScoped
public class DBService {
    //@Resource(lookup = "jdbc/H2database") //using web.xml
    @Resource(lookup = "java:app/jdbc/h2test")
    DataSource dataSource;

    @PersistenceContext
    EntityManager em;
    //https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html

    public DBService() {
    }

    public void createTable() {
        Statement statement = null;
        Connection connection = null;
        String sql = "CREATE TABLE IF NOT EXISTS Users (id INTEGER not NULL AUTO_INCREMENT, login VARCHAR(255) NOT NULL, password BINARY(20) NOT NULL, salt BINARY(8) NOT NULL, PRIMARY KEY(login))";
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

    public boolean isUserValid(String login, String password) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        String sql = "SELECT password,salt FROM Users WHERE login=?";

        //use try-with resource https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()){
                    byte[] storedPassword=resultSet.getBytes("password");
                    byte[] salt=resultSet.getBytes("salt");
                    return PasswordEncryptionService.authenticate(password,storedPassword,salt);
                }
                return false;
            }
        }
    }



    public boolean addUser1(String login, String password) throws NoSuchAlgorithmException,InvalidKeySpecException{
        byte[] salt=PasswordEncryptionService.generateSalt();
        Users user=new Users(login,PasswordEncryptionService.getEncryptedPassword(password,salt),salt);
        em.persist(user);
        return true;
    }
    public boolean addUser (String login, String password) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {

        String checkUser="SELECT id FROM Users WHERE login=?";
        String add = "INSERT INTO Users (login,password,salt) VALUES(?,?,?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement p1 = connection.prepareStatement(checkUser);
             PreparedStatement p2=connection.prepareStatement(add);
             SQLCloseable finish = connection::rollback) { //This will always call rollback(), but after a successful completion of commit(), the rollback will become a no-op as it resets the state to that after the last successful completion of commit()
            connection.setAutoCommit(false);
            p1.setString(1, login);
            try (ResultSet resultSet = p1.executeQuery()) {
                if (resultSet.isBeforeFirst()) return false;//isBeforeFirst return false if no rows in resultSet

            byte[] salt=PasswordEncryptionService.generateSalt();
            p2.setString(1,login);
            p2.setBytes(2, PasswordEncryptionService.getEncryptedPassword(password,salt));
            p2.setBytes(3,salt);
            p2.executeUpdate();}
            connection.commit();
            return true;
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
