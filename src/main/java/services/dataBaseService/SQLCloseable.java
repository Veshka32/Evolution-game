package services.dataBaseService;

import java.sql.SQLException;

interface SQLCloseable extends AutoCloseable {
    @Override
    void close() throws SQLException;
}