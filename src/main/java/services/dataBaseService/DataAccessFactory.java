package services.dataBaseService;

public class DataAccessFactory {

    private static final DataAccessFactory instance = new DataAccessFactory();
    private JDBCService jdbcService;

    private DataAccessFactory() {
    }

    public static DataAccessFactory getInstance() {
        return instance;
    }

    private JDBCService prepareJDBCUtils() {
        if (jdbcService == null) {
            jdbcService = new JDBCService();
            jdbcService.init("jdbc/H2database"); //why not java:comp/env/jdbc/H2database?
        }

        return jdbcService;
    }

    public static synchronized JDBCService getJDBCUtils() {
        return getInstance().prepareJDBCUtils();
    }
}