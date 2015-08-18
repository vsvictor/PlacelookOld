package com.placelook.data;

import com.placelook.utils.DateTimeOperator;

/**
 * Created by victor on 13.08.15.
 */
public class StatisticCard {
    private String key;
    private BaseObjectList list;

    public StatisticCard(String key){
        this.key = key;
        list = new BaseObjectList();
    }
    public void setKey(String key){
        this.key = key;
    }
    public String getKey(){
        return this.key;
    }
    public void setData(BaseObjectList data){
        this.list = (BaseObjectList) data.clone();
    }
    public void add(StatCard card){
        SubCard sub = new SubCard();
        sub.time = DateTimeOperator.dateToHHMM(card.getDate());
        sub.role = card.getRole();
        sub.balance = card.getBalance();
    }
    private class SubCard{
        private String time;
        private String role;
        private int balance;
    }
}
