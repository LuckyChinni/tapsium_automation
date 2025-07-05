package utils;

import testCases.BaseTest;

import java.sql.*;

public class PostgresDatabaseConnections extends BaseTest {
    Connection connection;
    public Connection getConnection(){
        String url = getData("url");
        String username = getData("username");
        String password = getData("password");
        try{
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to PostgresSQL Database !!");
        } catch (SQLException e) {
            System.err.println("Error While Connecting to Database : " + e.getMessage());
            connection = null;
        }
        return connection;
    }

    public void extractDataFromDatabase(){
        connection = getConnection();
        if(connection == null){
            System.out.println("Database connection was failed");
        }
        StringBuilder stringBuilder = new StringBuilder();
        String query = "Select * from tablename where name = ?";
        try {
            CallableStatement statement = connection.prepareCall(query);
            statement.setString(1, "Lakshman");
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                stringBuilder.append(resultSet.getString("response")).append("\n");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
