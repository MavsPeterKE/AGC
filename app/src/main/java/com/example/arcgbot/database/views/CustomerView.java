package com.example.arcgbot.database.views;

import androidx.room.DatabaseView;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.arcgbot.database.entity.Customer;
import com.example.arcgbot.database.entity.CustomerVisit;
import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.entity.Screen;

import java.util.List;

@DatabaseView("SELECT ct.*,cvt.* FROM customer_table ct LEFT JOIN customer_visit_table cvt ON cvt.customer_id=ct.id")
public class CustomerView {
    @Embedded
    public Customer screen;

    @Relation(parentColumn = "id", entityColumn = "customer_id", entity = CustomerVisit.class)
    public List<CustomerVisit> customerVisitList;
}
