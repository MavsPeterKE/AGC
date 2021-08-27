package com.example.arcgbot.viewmodels;

import static com.example.arcgbot.utils.Constants.DATE_FORMAT;
import static com.example.arcgbot.utils.Constants.Events.SEND_MESSAGE;

import android.os.Build;
import android.text.format.DateFormat;

import androidx.annotation.RequiresApi;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.Customer;
import com.example.arcgbot.database.entity.CustomerVisit;
import com.example.arcgbot.database.views.CustomerView;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.utils.Utils;
import com.example.arcgbot.view.adapter.CustomerAdapter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class CustomerViewModel extends ViewModel {
    private CustomerAdapter customerAdapter;
    private MutableLiveData<Customer> selectedCustomerItem = new MutableLiveData();
    private ObservableField<Boolean> isCustomerListSet = new ObservableField(false);
    public GameRepository gameRepository;
    FirebaseLogs firebaseLogs;
    private MutableLiveData<String> clickEventsLiveData = new MutableLiveData();
    private String phoneNo;
    private List<CustomerView> customerList;

    @Inject
    public CustomerViewModel(GameRepository gameRepository) {
        customerAdapter = new CustomerAdapter(R.layout.customer_item, this);
        this.gameRepository = gameRepository;
        firebaseLogs = new FirebaseLogs();
    }

    public ObservableField<Boolean> getIsCustomerListSet() {
        return isCustomerListSet;
    }

    public CustomerAdapter getCustomerAdapter() {
        return customerAdapter;
    }

    public void setCustomerList(List<CustomerView> customerList) {
        if (customerList != null) {
            isCustomerListSet.set(!customerList.isEmpty());
        }
        this.customerList = customerList;
        customerAdapter.setCustomerList(customerList);
        customerAdapter.notifyDataSetChanged();

    }

    public void onCallClick(String phoneNo) {
        this.phoneNo = phoneNo;
        clickEventsLiveData.setValue(Constants.Events.CALL_GAMER);

    }

    public void onCustomerClick(String phoneNo) {
        this.phoneNo = phoneNo;
        clickEventsLiveData.setValue(Constants.Events.CUSTOMER_CLICK);
    }

    public void onSendMessage(String phoneNo) {
        this.phoneNo = phoneNo;
        clickEventsLiveData.setValue(SEND_MESSAGE);

    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public MutableLiveData<String> getClickEventsLiveData() {
        return clickEventsLiveData;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void searchGamer(String searchText) {
        List<CustomerView> searchList = customerList.stream().filter(customer ->
                customer.gamer.getCustomerName().toLowerCase().contains(searchText) ||
                        customer.gamer.getCustomerPhone().toLowerCase().contains(searchText)).collect(Collectors.toList());
        customerAdapter.setCustomerList(searchList);
        customerAdapter.notifyDataSetChanged();
    }

    public LiveData<List<CustomerVisit>> getThisWeekCustomerVisitsByPhone(String customerPhone) {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(DATE_FORMAT), Constants.DATE_FORMAT);
        String monthString = (String) DateFormat.format("MMM", todayDate); // Jun
        String year = (String) DateFormat.format("yyyy", todayDate); // 2013
        int currentWeek = Utils.getCurrentWeekCount(Utils.getTodayDate(DATE_FORMAT));
        return gameRepository.getCustomerVisitThisWeek(customerPhone,currentWeek,monthString+"_"+year);
    }
}
