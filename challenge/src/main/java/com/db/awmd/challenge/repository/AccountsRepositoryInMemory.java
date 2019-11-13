package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountTransactionException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

  @Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }

  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }
  
  //new code
  @Override
  // MANDATORY: Transaction must be created before.
//  @Transactional(propagation = Propagation.MANDATORY )
  public synchronized void addAmount(String accountId, double amount) throws AccountTransactionException {
      Account account = this.getAccount(accountId);
      if (account == null) {
          throw new AccountTransactionException("Account not found " + accountId);
      }
      BigDecimal decimalAmount=BigDecimal.valueOf(amount);
      BigDecimal newBalance = account.getBalance().add(decimalAmount);
      if (account.getBalance().add(decimalAmount).compareTo(BigDecimal.ZERO) <0) {
          throw new AccountTransactionException(
                  "The money in the account '" + accountId + "' is not enough (" + account.getBalance() + ")");
      }
      account.setBalance(newBalance);
  }
  
  // Do not catch AccountTransactionException in this method.
//  @Transactional(propagation = Propagation.REQUIRES_NEW, 
//                      rollbackFor = AccountTransactionException.class)
  public synchronized String  sendMoney(String accountFrom, String accountTo, double amount) throws AccountTransactionException {

      addAmount(accountTo, amount);
      addAmount(accountFrom,-amount);
      
      return String.valueOf(System.currentTimeMillis());
  }

  
  


}
