package  DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    // Insert a new account into the database
    private  Connection connection;
    public AccountDAO(){
        this.connection=ConnectionUtil.getConnection();
    }

    public Account insertAccount(Account account) {
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        //String sql1 ="SELECT * FROM accounts WHERE username = ?";
        Connection conn = ConnectionUtil.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, account.getUsername());
                ps.setString(2, account.getPassword());
                ps.executeUpdate();
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return new Account(generatedKeys.getInt("account_id"),account.getUsername(),account.getPassword());
                    } 
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                
            }
            return null;
            
        }
    // Update an existing account's password in the database
    public  int updateAccountPassword(int account_id ,Account account) {
        String sql = "UPDATE accounts SET password = ? WHERE account_id = ?";
        Connection conn=ConnectionUtil.getConnection();
        try(PreparedStatement ps =conn.prepareStatement(sql)){
            ps.setString(1, account.getPassword());
            ps.setInt(2, account.getAccount_id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ExceptionDAO();
        } finally {
            System.out.println("Account is updated");
        }
        return account_id;
        
    }


    // Delete an account from the database
    public String deleteAccount(Account account) {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        Connection conn=ConnectionUtil.getConnection();
        try(PreparedStatement ps=conn.prepareStatement(sql)){
            
            ps.setInt(1,account.getAccount_id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ExceptionDAO();
        } finally {
            System.out.println("Account is deleted");
        }
    return "account";
    
}
    public Account getaccountById(int account_id) {
        String query = "SELECT * FROM Account WHERE account_id= ?";
        Account account = null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, account_id);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                int accountId = resultSet.getInt("account_id");
                String password = resultSet.getString("password");
                String username=resultSet.getString("username");
                return new Account(accountId,username,password);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your application's error handling strategy
        }

        return null;
 }
    public void updateAccount(int account_id, Account account) {
    }
    public static List<Account> getAllAccounts() {
        return null;
    }
    public Account getAccountByUsername(String username) {
        String query = "SELECT * FROM Account WHERE username = ?";
        Account account = null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                int accountId = resultSet.getInt("account_id");
                String password = resultSet.getString("password");

                return new Account(accountId, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your application's error handling strategy
        }

        return null;
    }
}

