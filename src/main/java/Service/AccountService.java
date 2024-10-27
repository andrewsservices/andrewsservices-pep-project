package Service;

import java.util.List;
import DAO.AccountDAO;


import Model.Account;



public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }


    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }

    public int[] getAccountIds(){
        List<Account> accounts = accountDAO.getAllAccounts();
        int[] accountIds = new int[accounts.size()];
        
        for(int i = 0; i < accounts.size(); i++){
            accountIds[i] = accounts.get(i).getAccount_id();
        }
        return accountIds;
    }

    public Account addAccount(Account account){
        String userName = account.getUsername();
        String passWord = account.getPassword();


        if(userName.length() > 0 && passWord.length() >= 4){
            return accountDAO.addAccount(account);
        }
        return null;

    }

    public Account verifyAccount(Account account){
        
        String username = account.getUsername();
        String password = account.getPassword();

        List<Account> accounts = accountDAO.getAllAccounts();
        for(Account a: accounts){
            if(a.getUsername().equals(username) && a.getPassword().equals(password)){
               return a;
            }
        }
        return null;
    }

}

