package com.example.arcgbot.database.dao;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.arcgbot.database.entity.Customer;
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
}
