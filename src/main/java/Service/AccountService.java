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
}

