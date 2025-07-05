package utils;

import testCases.BaseTest;

import java.sql.*;

public class OracleDatabaseConnections extends BaseTest {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Boolean flag;
    public Connection getConnection(){
        String url = getData("oracle_url");
        String username = getData("oracle_username");
        String password = getData("oracle_password");
        try{
            Class.forName(getData("oracle_driver"));
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to Oracle Database !!");
        } catch (Exception e) {
            System.err.println("Error While Connecting to Database : " + e.getMessage());
            connection = null;
        }
        return connection;
    }

    public boolean extractDataFromDatabase(){
        connection = getConnection();
        if(connection == null){
            System.out.println("Database connection was failed");
        }
        StringBuilder stringBuilder = new StringBuilder();
        String query = "Select * from tablename where name = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "Lakshman");
            int val = preparedStatement.executeUpdate();
            if(val > 0){
                flag = true;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return flag;
    }
}
