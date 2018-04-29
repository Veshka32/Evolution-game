package services.dataBaseService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



public class JDBCService{

    private DataSource dataSource;

    public JDBCService() {
    }

//    public void init(String dataSourceName) {
//        try {
//            InitialContext initContext = new InitialContext();
//            dataSource = (DataSource) initContext.lookup(dataSourceName);
//        } catch (NamingException e) {
//        }
//    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if (dataSource == null) {
            throw new SQLException("DataSource is null.");
        }
        return dataSource.getConnection();
    }

}