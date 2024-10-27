package DAO;
import Util.ConnectionUtil;
import java.util.List;
import Model.Account;
import java.sql.*;
import java.util.ArrayList;



public class AccountDAO {

    public Account addAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "insert into account (username, password) values (?,?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,account.getUsername());
            preparedStatement.setString(2,account.getPassword());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }
    

    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try{
            String sql = "SELECT * FROM account";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account newAccount = new Account(rs.getInt("account_id"),
                rs.getString("username"),rs.getString("password"));
                accounts.add(newAccount);
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return accounts;
    }



}