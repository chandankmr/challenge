package com.db.awmd.challenge.domain;

//import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
//import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class PaymentDetails {

  @NotNull
  @NotEmpty
  private final String accountFromId;
  
  @NotNull
  @NotEmpty
  private final String accountToId;

  @NotNull
  @Min(value = 0, message = "Transferrable amount must be positive.")
  private double amount;

  public PaymentDetails(@JsonProperty("accountFromId") String accountFromId,
		  @JsonProperty("accountToId") String accountToId,@JsonProperty("amount") double amount) {
    this.accountFromId = accountFromId;
    this.accountToId = accountToId;
    this.amount = amount;
  }



public double getAmount() {
	return amount;
}



public void setAmount(double amount) {
	this.amount = amount;
}



public String getAccountFromId() {
	return accountFromId;
}

public String getAccountToId() {
	return accountToId;
}

//  @JsonCreator
//  public PaymentDetails(@JsonProperty("accountId") String accountId,
//    @JsonProperty("balance") BigDecimal balance) {
//    this.accountId = accountId;
//    this.balance = balance;
//  }


}

