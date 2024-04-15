package com.about.kby.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<List<PersonalPojo>> dates = new MutableLiveData<>();

    public void setDateList(List<PersonalPojo> dateList) {
        dates.setValue(dateList);
    }

    public LiveData<List<PersonalPojo>> getDateList() {
        return dates;
    }
}
