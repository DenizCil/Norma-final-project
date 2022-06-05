package com.bootcamp.mybank.entity.account;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.Date;

@Entity
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "savingId")
public class SavingAccount extends Account{


    private int day;
    private Date createdDate;
    private Date endDate;
    private boolean isActive;
    private int remainingDay;
    private double starterBalance;

}
