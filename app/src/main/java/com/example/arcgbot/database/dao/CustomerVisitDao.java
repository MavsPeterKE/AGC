package com.example.arcgbot.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.arcgbot.database.entity.CustomerVisit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Dao
public abstract class CustomerVisitDao extends BaseDao<CustomerVisit> {

    @Query("SELECT * FROM customer_visit_table WHERE customer_id=:phoneNo ")
    public abstract LiveData<List<CustomerVisit>> getVisitsByCustomerPhone(String phoneNo);

    @Query("SELECT * FROM customer_visit_table WHERE customer_id=:phone AND date=:date")
    abstract CustomerVisit getCustomerByPhone(String phone, Date date);

    public void insertGamerVisit(List<CustomerVisit> customerVisits) {
        for (CustomerVisit visit : customerVisits) {
            CustomerVisit savedCustomerVisit = getCustomerByPhone(visit.getCustomer_phone(), visit.getDate());
            if (savedCustomerVisit != null) {
                visit.setTotalGamesPlayed((visit.getTotalGamesPlayed() + savedCustomerVisit.getTotalGamesPlayed()));
                visit.setAmountPaidToShop((visit.getAmountPaidToShop() + savedCustomerVisit.getAmountPaidToShop()));
                update(visit);

            } else {
                insert(visit);
            }
        }

    }

    public  void updateCustomerVisit(String player1Phone, String player2Phone, int gamesCount , double amountPayable){
        List<String>customers = new ArrayList<>();
        customers.add(player1Phone);
        customers.add(player2Phone);
        updateCustomerVisitDetail(customers,gamesCount,amountPayable);
    }

    private void updateCustomerVisitDetail(List<String> customers, int gamesCount, double amountPayable){
       List<CustomerVisit> customersToUpdate = getCustomersByPhone(customers);
       for (CustomerVisit customerVisit:customersToUpdate){
           customerVisit.setTotalGamesPlayed((customerVisit.getTotalGamesPlayed()+gamesCount));
           customerVisit.setAmountPaidToShop((customerVisit.getAmountPaidToShop()+amountPayable));
           update(customerVisit);
       }
    }

    @Query("SELECT * FROM customer_visit_table WHERE customer_id IN(:customers)")
    abstract List<CustomerVisit> getCustomersByPhone(List<String> customers);
}
