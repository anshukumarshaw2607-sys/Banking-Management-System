package banking.app;

import java.util.Scanner;
import java.sql.*;

import static java.lang.Class.forName;

public class banking_system {

    private static final String url = "jdbc:mysql://localhost:3306/banking_system";

    private static final String username = "root";

    private static final String password = "AnshuKS260505@";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);
            User user = new User(connection, sc);
            Accounts accounts = new Accounts(connection, sc);
            AccountManager accountManager = new AccountManager(connection, sc);

            String email;
            long account_number;

            while(true){
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice1 = sc.nextInt();
                switch(choice1){
                    case 1:
                        user.register();
                        System.out.println("\033[H\033[2J");
                        System.out.flush();
                        break;
                    case 2:
                        email=user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("User Logged In!");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if(sc.nextInt() == 1){
                                    account_number=accounts.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your account Number is: " + account_number);
                                }else{
                                    break;
                                }
                            }
                            account_number=accounts.getAccountnumber(email);
                            int choice2=0;
                            while(choice2 != 5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log out");
                                System.out.println("Enter your choice: ");
                                choice2 = sc.nextInt();
                                switch (choice2){
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice");
                                        break;
                                }
                            }
                        }
                       else{
                            System.out.println("Incorrect Email or Password!");
                        }
                    case 3:
                        System.out.println("THANKYOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Existing System!");
                        return;
                    default :
                        System.out.println("Enter valid choice");
                        break;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
