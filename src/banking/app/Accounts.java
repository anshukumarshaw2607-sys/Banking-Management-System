package banking.app;

import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection connection;

    private Scanner sc;

    public Accounts(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc=sc;
    }


    public long open_account(String email){
        if(!account_exist(email)){
            String open_account_query = "INSERT INTO accounts(account_number, full_name, email, balance, security_pin) VALUES(?,?,?,?,?)";
            sc.nextLine();
            System.out.print("Enter you full name: ");
            String full_name=sc.nextLine();
            System.out.print("Enter Initial amount: ");
            double balance=sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter Security pin: ");
            String security_pin = sc.nextLine();
            try{
                long account_number=generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5,security_pin);
                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected > 0){
                    return account_number;
                } else {
                    throw new RuntimeException("Account Creation Failed ");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account Already exist ");
    }

    public long getAccountnumber(String email){
        String query = "SELECT account_number FROM accounts WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet  = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Doesnt Exist ");
    }

    private long generateAccountNumber(){
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet= statement.executeQuery("SELECT account_number from accounts ORDER BY account_number DESC LIMIT 1");
            if(resultSet.next()) {
                long last_account_number=resultSet.getLong("account_number");
                return last_account_number+1;
            }else{
                return 100001001;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 100001001;
    }

    public boolean account_exist(String email){
        String query = "SELECT account_number FROM accounts WHERE email = ?";
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
