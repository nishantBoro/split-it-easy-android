package com.nishantboro.splititeasy;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MemberViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private String gName;

    public MemberViewModelFactory(Application application,String gName) {
        this.application = application;
        this.gName = gName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MemberViewModel(application, gName);
    }
}
