package banking.app;

import java.sql.*;
import java.util.Scanner;

public class AccountManager {

    private Connection connection;

    private Scanner sc;

    public AccountManager(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc=sc;
    }

    public void credit_money(long account_number) throws SQLException {
        sc.nextLine();
        System.out.print("enter Amount: ");
        double amount=sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security pin: ");
        String security_pin=sc.nextLine();
        try{
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? and security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet= preparedStatement.executeQuery();

                if(resultSet.next()){
                    //double current_balance= resultSet.getDouble("balance");
                   // if(amount<=current_balance){
                        String credit_query="UPDATE accounts SET balance=balance+? WHERE account_number=?";
                        PreparedStatement preparedStatement1= connection.prepareStatement(credit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2,account_number);
                        int rowsAffected= preparedStatement1.executeUpdate();
                        if(rowsAffected>0){
                            System.out.println("Rs. " + amount+ "credited successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Invalid security pin");
                    }
                }

        } catch(SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void debit_money(long account_number) throws SQLException {
        sc.nextLine();
        System.out.print("enter Amount: ");
        double amount=sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security pin: ");
        String security_pin=sc.nextLine();
        try{
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? and security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet= preparedStatement.executeQuery();

                if(resultSet.next()){
                    double current_balance= resultSet.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query="UPDATE accounts SET balance=balance-? WHERE account_number=?";
                        PreparedStatement preparedStatement1= connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2,account_number);
                        int rowsAffected= preparedStatement1.executeUpdate();
                        if(rowsAffected>0){
                            System.out.println("Rs. " + amount+ "debited successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance");
                    }
                }else{
                    System.out.println("Invalid pin ");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }


    public void transfer_money(long sender_account_number) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Receiver account number :");
        long receiver_account_number = sc.nextLong();
        System.out.print("enter amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("enter security pin : ");
        String security_pin = sc.nextLine();
        try {
            connection.setAutoCommit(false);
            if (sender_account_number != 0 && receiver_account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE accounts_number =? AND security_pin=?");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "UPDATE accounts SET balance=balance-? WHERE account_number=?";
                        String credit_query = "UPDATE accounts SET balance=balance+? WHERE account_number=?";
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);
                        creditPreparedStatement.setDouble(1, amount);
                        creditPreparedStatement.setLong(2, receiver_account_number);
                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, sender_account_number);
                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("transaction successfull");
                            System.out.println("Rs." + amount + "Transferred successfull");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else{
                        System.out.println("Insufficient balance");
                    }
                }else {
                    System.out.println("Invalid security pin ");
                }
            } else{
                System.out.println("Invalid account number");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getBalance(long account_number){
        sc.nextLine();
        System.out.println("Enter Security pin: ");
        String security_pin=sc.nextLine();
        try{
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT balance FROM accounts WHERE accpunt_number = ? AND security_pin=?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: " + balance);
            }else{
                System.out.println("Invalid pin ");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
