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
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.models.GamerDetailModel;
import com.example.arcgbot.repository.GameRepository;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.utils.FirebaseLogs;
import com.example.arcgbot.utils.Utils;
import com.example.arcgbot.view.adapter.CustomerAdapter;
import com.example.arcgbot.view.adapter.CustomerVisitAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class CustomerViewModel extends ViewModel {
    private CustomerAdapter customerAdapter;
    private CustomerVisitAdapter customerVisitAdapter;
    private MutableLiveData<Customer> selectedCustomerItem = new MutableLiveData();
    private ObservableField<Boolean> isCustomerListSet = new ObservableField(false);
    private ObservableField<Boolean> isVisitDataAvailable = new ObservableField(false);
    public GameRepository gameRepository;
    FirebaseLogs firebaseLogs;
    private MutableLiveData<String> clickEventsLiveData = new MutableLiveData();
    private String phoneNo;
    private List<CustomerView> customerList;
    private List<CustomerVisit> customerVisitList = new ArrayList<>();
    private ObservableField<GamerDetailModel> gamerDetailModelObservableField = new ObservableField<>();
    private String customerPhone;

    @Inject
    public CustomerViewModel(GameRepository gameRepository) {
        customerAdapter = new CustomerAdapter(R.layout.customer_item, this);
        customerVisitAdapter = new CustomerVisitAdapter(R.layout.visit_item, this);
        this.gameRepository = gameRepository;
        firebaseLogs = new FirebaseLogs();
    }

    public ObservableField<Boolean> getIsCustomerListSet() {
        return isCustomerListSet;
    }

    public CustomerAdapter getCustomerAdapter() {
        return customerAdapter;
    }

    public String getDate(Date date) {
        String monthString = (String) DateFormat.format("MMM", date); // Jun
        String day = (String) DateFormat.format("dd", date);
        String year = (String) DateFormat.format("yyyy", date); // 2013
        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        return dayOfTheWeek + " " + day + " " + monthString + " " + year;
    }

    public void setCustomerList(List<CustomerView> customerList) {
        if (customerList != null) {
            isCustomerListSet.set(!customerList.isEmpty());
            Collections.sort(customerList, (customerView, t1) -> Integer.compare(t1.customerVisitList.size(), customerView.customerVisitList.size()));
        }
        this.customerList = customerList;
        customerAdapter.setCustomerList(customerList);
        customerAdapter.notifyDataSetChanged();

    }

    private void addGameDetail(List<CustomerVisit> customerVisitList) {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(DATE_FORMAT), Constants.DATE_FORMAT);
        String monthString = (String) DateFormat.format("MMM", todayDate); // Jun
        int currentWeek = Utils.getCurrentWeekCount(Utils.getTodayDate(DATE_FORMAT));

        GamerDetailModel gamerDetailModel = new GamerDetailModel();
        gamerDetailModel.titleName =  "Total Spend: "+monthString +" Week "+currentWeek;
            Customer customer = gameRepository.getCustomerById(customerPhone);
            int averageVisitPerWeek = customerVisitList.size();
            int totalGamesPlayed = 0;
            double totalAmountSpend  =0;
            for (CustomerVisit visit:customerVisitList){
                totalAmountSpend+=visit.getAmountPaidToShop();
                totalGamesPlayed+=visit.getTotalGamesPlayed();
            }
            if (totalGamesPlayed%2!=0){
                totalGamesPlayed-=1;
            }
            gamerDetailModel.payableAmount = "KSh. "+totalAmountSpend+"0";
            gamerDetailModel.averageWeekSpend = totalAmountSpend!=0?"Ksh. "+totalAmountSpend/averageVisitPerWeek+"0":"Ksh. 0.00";
            gamerDetailModel.averageVisitPerWeek = averageVisitPerWeek+(averageVisitPerWeek>1?" Visits":" Visit");
            gamerDetailModel.averageGamesPerDay = totalGamesPlayed!=0?(totalGamesPlayed/averageVisitPerWeek)+" Games" : "0 Games";
            gamerDetailModel.customerCategory = customer.getCustomerType()!=null?customer.getCustomerType():"NEW CUSTOMER";
            gamerDetailModelObservableField.set(gamerDetailModel);

    }

    public ObservableField<GamerDetailModel> getGamerDetailModelObservableField() {
        return gamerDetailModelObservableField;
    }

    public void onCallClick(String phoneNo) {
        this.phoneNo = phoneNo;
        clickEventsLiveData.setValue(Constants.Events.CALL_GAMER);

    }

    public void onBack() {
        clickEventsLiveData.setValue(Constants.Events.BACK_TO_SCREENS);
    }

    public void onBackToCustomers() {
        clickEventsLiveData.setValue(Constants.Events.BACK_TO_CUSTOMERS);
    }

    public void onCustomerClick(Customer customer) {
        this.phoneNo = customer.getCustomerPhone();
        clickEventsLiveData.setValue(Constants.Events.CUSTOMER_CLICK);
    }

    public void onSendMessage(String phoneNo) {
        this.phoneNo = phoneNo;
        clickEventsLiveData.setValue(SEND_MESSAGE);

    }

    public String getVisitCountThisWeek(List<CustomerVisit> customerVisitList) {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(DATE_FORMAT), Constants.DATE_FORMAT);
        String monthString = (String) DateFormat.format("MMM", todayDate); // Jun
        String year = (String) DateFormat.format("yyyy", todayDate); // 2013
        int currentWeek = Utils.getCurrentWeekCount(Utils.getTodayDate(DATE_FORMAT));
        int count = 0;
        for (CustomerVisit visit : customerVisitList) {
            if (visit.getWeek() == currentWeek && visit.getMonth().equals(monthString + "_" + year)) {
                count += 1;
            }
        }
        return count + "";
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
        return gameRepository.getCustomerVisitThisWeek(customerPhone, currentWeek, monthString + "_" + year);
    }

    public ObservableField<Boolean> getIsVisitDataAvailable() {
        return isVisitDataAvailable;
    }

    public void setCustomerVisitList(List<CustomerVisit> customerVisitList) {
        isVisitDataAvailable.set(customerVisitList.size()>0);
        this.customerVisitList.removeAll(customerVisitList);
        this.customerVisitList.addAll(customerVisitList);
        customerVisitAdapter.setCustomerVisitList(customerVisitList);
        addGameDetail(customerVisitList);
        customerVisitAdapter.notifyDataSetChanged();
    }

    public CustomerVisitAdapter getCustomerVisitAdapter() {
        return customerVisitAdapter;
    }

    public void setCustomer(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}
