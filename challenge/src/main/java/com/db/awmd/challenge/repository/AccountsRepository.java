package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountTransactionException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

public interface AccountsRepository {

  void createAccount(Account account) throws DuplicateAccountIdException;

  Account getAccount(String accountId);

  void clearAccounts();
  
  public void addAmount(String accountId, double amount) throws AccountTransactionException ;
  
  public boolean sendMoney(String accountFrom, String accountTo, double amount) throws AccountTransactionException;
}
