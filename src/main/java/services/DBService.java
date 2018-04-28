package services;

import java.sql.*;

public class DBService {

    public DBService(){}

    public void startDBService(){
        try{
            Class.forName("org.h2.Driver");
            Connection connection=DriverManager.getConnection("jdbc:h2:C:/Users/stas/Documents/evo/h2", "admin", "admin");
            System.out.println(connection.isValid(10)); //check connection is open
            Statement statement=connection.createStatement();
            String sql="DROP TABLE IF EXISTS Users";
            statement.executeUpdate(sql);
            sql="CREATE TABLE IF NOT EXISTS Users (id INTEGER not NULL, login VARCHAR(255), password VARCHAR(255), email VARCHAR(255), PRIMARY KEY(id))";
            statement.executeUpdate(sql);
            sql="INSERT INTO Users VALUES(1, 'test','test','email')";
            statement.executeUpdate(sql);
            sql="INSERT INTO Users VALUES(2, 'poppy','test','email')";
            statement.executeUpdate(sql);
            sql="SHOW TABLES";
            ResultSet resultSet=statement.executeQuery(sql);
            sql="SHOW COLUMNS FROM Users";
            resultSet=statement.executeQuery(sql);
            sql="SELECT login FROM Users";
            resultSet=statement.executeQuery(sql);
            while (resultSet.next()){
            System.out.println(resultSet.getString("login"));}
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {e.printStackTrace();
            System.out.println("db error");

        }}
    }


