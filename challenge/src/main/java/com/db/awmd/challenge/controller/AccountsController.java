package com.db.awmd.challenge.controller;



import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountTransactionException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.NotificationService;

@RestController
public class AccountsController {
    @Autowired
    private AccountsService accountsService;
    
    @Autowired
    NotificationService notificationService;
     
    @RequestMapping(value = "/v1/accounts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getAccount(@PathVariable("id") String id) 
    {
    	if(StringUtils.isEmpty(id)) {
    		return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
    	}
      Account account=accountsService.getAccount(id);   
      return new ResponseEntity<Account>(account, HttpStatus.OK);

    }
     
    @RequestMapping(value = "/v1/accounts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> addAccount(@RequestBody Account account) 
    {
    	if(!validateAccount(account)) {
    		return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    	accountsService.createAccount(account);      
       return new ResponseEntity<Account>(accountsService.getAccount(account.getAccountId()), HttpStatus.CREATED);
    } catch (DuplicateAccountIdException ex) {
    	return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
      }
    }
    
    @RequestMapping(value = "/v1/accounts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> sendMoney(@RequestBody String accountFromId,@RequestBody String accountToId,@RequestBody double amount) 
    {
    	
    	if(StringUtils.isEmpty(accountFromId)||StringUtils.isEmpty(accountToId)||amount<0) {
    		return new ResponseEntity<Double>(HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    	String transactionId=accountsService.sendMoney(accountFromId, accountToId, amount) ; 
    	
    	notificationService.notifyAboutTransfer(accountsService.getAccount(accountFromId),transactionId);
    	notificationService.notifyAboutTransfer(accountsService.getAccount(accountToId),transactionId);
       return new ResponseEntity<Double>(amount, HttpStatus.OK);
     } catch (AccountTransactionException ex) {
    	return new ResponseEntity<Double>(HttpStatus.BAD_REQUEST);
      }
    }
    
    
    private boolean validateAccount(Account account) {
    	if(account.getBalance()==null
    	||account.getBalance().compareTo(BigDecimal.ZERO) <0
    	||StringUtils.isEmpty(account.getAccountId())){
    	return false;
    	}
    	return true;
    }
    
    
    
    
}