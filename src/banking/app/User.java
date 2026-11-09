package banking.app;

import java.util.Scanner;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private Connection connection;

    private Scanner sc;

    public User(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc=sc;
    }

    public void register(){
        sc.nextLine();
        System.out.print("Full Name: ");
        String full_name=sc.nextLine();
        System.out.print("Email: ");
        String email=sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        if(user_exist(email)){
            System.out.println("User Already Exists for this Email Address!!");
            return;
        }
        String register_query = "INSERT INTO user(full_name, email, password) VALUES(?, ?, ?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3,password);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                System.out.println("Registration successfull!!");
            } else {
                System.out.println("Registration Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String login(){
        sc.nextLine();
        System.out.print("Email: ");
        String email=sc.nextLine();
        System.out.print("Password: ");
        String password=sc.nextLine();
        String login_query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return email;
            }else{
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;

    }

    public boolean user_exist(String email){
      String query = "SELECT * FROM user WHERE email = ?";
      try{
          PreparedStatement preparedStatement = connection.prepareStatement(query);
          preparedStatement.setString(1, email);
          ResultSet resultSet  = preparedStatement.executeQuery();
          if(resultSet.next()){
              return true;
          }else{
              return false;
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return false;
    }
}
