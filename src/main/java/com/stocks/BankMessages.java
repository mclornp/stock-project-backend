package com.stocks;

import com.stocks.BankActor.Bank;
import com.stocks.BankActor.Account;

import java.io.Serializable;
import java.util.*;  
import com.fasterxml.jackson.databind.ObjectMapper;

public interface BankMessages {

    class ActionPerformed implements Serializable {
        private final String description;

        public ActionPerformed(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    class GetBalance implements Serializable {
    }

    class CreateAccount implements Serializable{
        private final Account account;

        public CreateAccount(Account account){
            this.account = account;
        }

        public Account getAccount(){
            return account;
        }
    }

}