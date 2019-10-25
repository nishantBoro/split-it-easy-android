package com.nishantboro.splititeasy;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MemberViewModel extends AndroidViewModel {
    private MemberRepository repository;
    private LiveData<List<MemberEntity>> allMembers;

    public MemberViewModel(@NonNull Application application, String gName) {
        super(application);
        repository = new MemberRepository(application,gName);
        allMembers = repository.getAllMembers();
    }

    public void insert(MemberEntity member) {
        repository.insert(member);
    }

    public void update(MemberEntity member) {
        repository.update(member);
    }

    public void delete(MemberEntity member) {
        repository.delete(member);
    }

    public void deleteAll(String gName) {
        repository.deleteAll(gName);
    }

    public LiveData<List<MemberEntity>> getAllMembers() {
        return allMembers;
    }
}
