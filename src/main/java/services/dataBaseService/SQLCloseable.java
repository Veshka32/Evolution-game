package services.dataBaseService;

import java.sql.SQLException;

interface SQLCloseable extends AutoCloseable {
    @Override public void close() throws SQLException;
}