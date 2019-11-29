package com.nishantboro.splititeasy;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class GroupViewModel extends AndroidViewModel {
    private GroupRepository repository;
    private LiveData<List<GroupEntity>> allGroups;

    public GroupViewModel(@NonNull Application application) {
        super(application);
        repository = new GroupRepository(application);
        allGroups = repository.getAllGroups();
    }

    public void insert(GroupEntity group) {
        repository.insert(group);
    }

    public void update(GroupEntity group) {
        repository.update(group);
    }

    public void delete(GroupEntity group) {
        repository.delete(group);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    LiveData<List<GroupEntity>> getAllGroups() {
        return allGroups;
    }

    List<GroupEntity> getAllGroupsNonLiveData() {
        return repository.getAllGroupsNonLive();
    }

    LiveData<String> getGroupCurrency(String gName) {
        return repository.getGroupCurrency(gName);
    }

    String getGroupCurrencyNonLive(String gName) {
        return repository.getGroupCurrencyNonLive(gName);
    }


}
