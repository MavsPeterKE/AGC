package com.example.arcgbot.viewmodels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.Customer;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.view.adapter.CustomerAdapter;

import java.util.List;

import javax.inject.Inject;

import static com.example.arcgbot.utils.Constants.Events.SEND_MESSAGE;

public class CustomerViewModel extends ViewModel {
    private CustomerAdapter customerAdapter;
    private MutableLiveData<Customer> selectedCustomerItem = new MutableLiveData();
    private ObservableField<Boolean> isCustomerListSet = new ObservableField(false);
    public GameRepository gameRepository;
    FirebaseLogs firebaseLogs;
    private MutableLiveData<String> clickEventsLiveData = new MutableLiveData();
    private String phoneNo;

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

    public void setCustomerList(List<Customer> customerList) {
        if (customerList != null) {
            isCustomerListSet.set(!customerList.isEmpty());
        }
        customerAdapter.setCustomerList(customerList);
        customerAdapter.notifyDataSetChanged();

    }

    public void onCallClick(String phoneNo) {
        clickEventsLiveData.setValue(Constants.Events.CALL_GAMER);
        this.phoneNo = phoneNo;
    }

    public void onSendMessage(String phoneNo) {
        clickEventsLiveData.setValue(SEND_MESSAGE);
        this.phoneNo = phoneNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public MutableLiveData<String> getClickEventsLiveData() {
        return clickEventsLiveData;
    }
}
