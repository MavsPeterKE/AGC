package com.example.arcgbot.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arcgbot.R;
import com.example.arcgbot.models.GameModel;
import com.example.arcgbot.models.ScreenItem;
import com.example.arcgbot.utils.Constants;
import com.example.arcgbot.view.adapter.ScreenAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.example.arcgbot.utils.Constants.Events.GAME_ITEM_CLICK;

public class EODViewModel extends ViewModel {
    private MutableLiveData<String> gameItemClickLiveData = new MutableLiveData();

    @Inject
    public EODViewModel() {
    }

    private void addTestData() {
        List<ScreenItem> gameCounts = new ArrayList<>();
        ScreenItem model = new ScreenItem();
        model.screenLable = "Screen 1 TCL";
        model.GameCount =  "4";
        model.startTime = "2.30 pm";
        model.endTime = "4.30pm";
        model.payableAmount = String.valueOf(Integer.parseInt(model.GameCount)*30);
        gameCounts.add(model);

        ScreenItem model2 = new ScreenItem();
        model2.screenLable = "Screen 2 Hsc";
        model2.GameCount =  "5";
        model2.startTime = "11.30 pm";
        model2.endTime = "2.30pm";
        model2.payableAmount = String.valueOf(Integer.parseInt(model.GameCount)*50);
        gameCounts.add(model2);

        ScreenItem model3 = new ScreenItem();
        model3.screenLable = "Screen 3 TCL";
        model3.GameCount =  "10";
        model3.startTime = "09.30 pm";
        model3.endTime = "10.30am";
        model3.payableAmount = String.valueOf(Integer.parseInt(model.GameCount)*50);
        gameCounts.add(model3);


        ScreenItem model4 = new ScreenItem();
        model4.screenLable = "Screen 4 TCL";
        model4.GameCount =  "8";
        model4.startTime = "08.30 pm";
        model4.endTime = "9.30am";
        model4.payableAmount = String.valueOf(Integer.parseInt(model.GameCount)*30);
        gameCounts.add(model4);
    }

    public void onGameItemClick(GameModel gameModel){
        gameItemClickLiveData.setValue(GAME_ITEM_CLICK);
    }

    public MutableLiveData<String> getGameItemClickLiveData() {
        return gameItemClickLiveData;
    }
}
