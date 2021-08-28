package com.example.arcgbot.database.dao;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.arcgbot.database.entity.Customer;
import com.example.arcgbot.database.entity.CustomerVisit;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.views.CustomerView;

import java.util.List;

@Dao
public abstract class CustomerDao extends BaseDao<Customer> {

    @Query("SELECT * FROM customerview")
    public abstract LiveData<List<CustomerView>> getAllCustomer();

    @Query("SELECT * FROM customer_table WHERE id in (:gamers)")
    public abstract List<Customer> getGamers(List<String> gamers);

    @Query("SELECT COUNT(*) FROM customer_table WHERE id=:customerPhone")
    public abstract int getCustomerCount(String customerPhone);

    public long insertCustomer(Customer customer){
        int count = getCustomerCount(customer.getCustomerPhone());
        if (count>0){
            Log.e("insertCustomer:" ,"__exists" );
            return 0;
        }else {
            return  insert(customer);
        }
    }


    @Query("SELECT * FROM customerview")
    public abstract List<CustomerView> getSavedCustomers();

    @Query("UPDATE customer_table SET loyalty_bonus=1,customer_type=:customerType,loyalty_bonus_week=:currentWeek WHERE id IN (:customerPhoneList)")
    public abstract void updateLoyaltyBonusAwarded(List<String> customerPhoneList,String customerType,int currentWeek);

    @Query("SELECT count(*) FROM customer_table WHERE id IN (:customerVisitList) AND loyalty_bonus=1 AND loyalty_bonus_week=:loyaltyBonusWeek")
    public abstract int getLoyaltyBonusCount(List<String> customerVisitList,int loyaltyBonusWeek);

    @Query("SELECT * FROM customer_table WHERE id=:customerPhone")
    public abstract Customer getCustomerLiveDataById(String customerPhone);
}
