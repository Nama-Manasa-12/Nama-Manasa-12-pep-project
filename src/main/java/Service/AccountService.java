package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /*
     * public Account addAccount(Account account){
     * return accountDAO.insertAccount(account);
     * }
     */
    public Account updateAccount(int account_id, Account account) {
        Account existingAccount = accountDAO.getaccountById(account_id);
        if (existingAccount == null) {
            return null;
        } else {
            accountDAO.updateAccount(account_id, account);
            existingAccount = accountDAO.getaccountById(account_id);
            return existingAccount;
        }
    }

    public List<Account> getAllAccounts() {
        return AccountDAO.getAllAccounts();

    }

    public Account getAccountByUsername(String str) {
        return accountDAO.getAccountByUsername(str);

    }

    public Account getAccountByAccountid(int id) {
        return accountDAO.getaccountById(id);

    }

    public Account registerUser(Account account) {
        Account savedAccount = accountDAO.insertAccount(account);
        return savedAccount;
    }

    public Account loginUser(Account account) {
        return null;
    }

}
