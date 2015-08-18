package com.placelook.data;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by victor on 13.08.15.
 */
public class StatCard extends IntStringPair implements Comparator<StatCard>{
    private String type;
    private Date created;
    private int balance;

    public StatCard(int id){
        super(id,"Operation "+String.valueOf(id));
        type = "undefined";
        created = new Date();
        balance = 0;
    }
    public void setRole(String role){
        if(!role.equals("client") && !role.equals("operator")) return;
        this.type = role;
    }
    public String getRole(){
        return this.type;
    }
    public void setDate(long unixTime){
        this.created = new Date(unixTime);
    }
    public Date getDate(){
        return this.created;
    }
    public void setBalance(int balance){
        this.balance = balance;
    }
    public int getBalance(){
        return this.balance;
    }

    @Override
    public int compare(StatCard lhs, StatCard rhs) {
        if(lhs.created.getTime()>rhs.created.getTime()) return 1;
        else if (lhs.created.getTime()<rhs.created.getTime()) return -1;
        else return 0;
    }
}
