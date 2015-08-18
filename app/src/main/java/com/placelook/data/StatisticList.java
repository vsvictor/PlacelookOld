package com.placelook.data;

import com.placelook.utils.DateTimeOperator;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by victor on 13.08.15.
 */
public class StatisticList extends ArrayList<StatisticCard>{
    public StatisticList(BaseObjectList cards){
        StatCard prev = null;
        StatisticCard curr = null;
        for(int i = 0;i<cards.size();i++){
            StatCard st = (StatCard) cards.get(i);
            if(prev == null || prev.getDate()!=st.getDate()){
                curr = new StatisticCard(DateTimeOperator.dateToMMDDYYYY(st.getDate()));
                curr.add(st);
                this.add(curr);
            }
            else curr.add(st);
        }
    }
}
