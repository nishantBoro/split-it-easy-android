package com.nishantboro.splititeasy;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

/* Use AsyncTask to perform operations insert,delete,update,deleteAll,getAllGroupsNonLive. Doing these operations on the main thread could
 * lock our user interface */
public class GroupRepository {
    private GroupDao groupDao;
    private LiveData<List<GroupEntity>> allGroups;

    GroupRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        groupDao = database.groupDao();
        allGroups = groupDao.getAll();
    }

    public void insert(GroupEntity group) {
        new InsertMemberAsyncTask(groupDao).execute(group);
    }

    public void delete(GroupEntity group) {
        new DeleteMemberAsyncTask(groupDao).execute(group);
    }

    public void update(GroupEntity group) {
        new UpdateMemberAsyncTask(groupDao).execute(group);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(groupDao).execute();
    }

    LiveData<List<GroupEntity>> getAllGroups() {
        return allGroups;
    }

    List<GroupEntity> getAllGroupsNonLive() {
        GetAllGroupsNonLiveAsyncTask groups = new GetAllGroupsNonLiveAsyncTask(groupDao);
        try {
            return groups.execute().get();
        } catch (Exception err) {
            return new ArrayList<>(); // if there are no groups in database return a blank array list
        }
    }

    String getGroupCurrencyNonLive(String gName) {
        GetGroupCurrencyNonLive group = new GetGroupCurrencyNonLive(groupDao);
        try {
            return group.execute(gName).get();
        } catch (Exception err) {
            return "";
        }
    }

    LiveData<String> getGroupCurrency(String gName) {
        GetGroupCurrency group = new GetGroupCurrency(groupDao);
        try {
            return group.execute(gName).get();
        } catch (Exception err) {
            return new LiveData<String>() {
                @Override
                public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super String> observer) {
                    super.observe(owner, observer);
                }
            };
        }
    }

    private static class GetGroupCurrencyNonLive extends AsyncTask<String,Void,String> {
        private GroupDao groupDao;

        private GetGroupCurrencyNonLive(GroupDao groupDao) {
            this.groupDao = groupDao;
        }

        @Override
        protected String doInBackground(String... strings) {
            return groupDao.getGroupCurrencyNonLive(strings[0]);
        }
    }

    private static class GetGroupCurrency extends AsyncTask<String,Void,LiveData<String>> {
        private GroupDao groupDao;

        private GetGroupCurrency(GroupDao groupDao) {
            this.groupDao = groupDao;
        }

        @Override
        protected LiveData<String> doInBackground(String... strings) {
            return groupDao.getGroupCurrency(strings[0]);
        }
    }

    private static class GetAllGroupsNonLiveAsyncTask extends AsyncTask<Void,Void,List<GroupEntity>> {
        private GroupDao groupDao;

        private GetAllGroupsNonLiveAsyncTask(GroupDao groupDao) {
            this.groupDao = groupDao;
        }

        @Override
        protected List<GroupEntity> doInBackground(Void... voids) {
            return groupDao.getAllNonLive();
        }
    }

    private static class InsertMemberAsyncTask extends AsyncTask<GroupEntity,Void,Void> {
        private GroupDao groupDao;

        private InsertMemberAsyncTask(GroupDao groupDao) {
            this.groupDao = groupDao;
        }

        @Override
        protected Void doInBackground(GroupEntity... groupEntities) {
            groupDao.insert(groupEntities[0]);
            return null;
        }
    }

    private static class DeleteMemberAsyncTask extends AsyncTask<GroupEntity,Void,Void> {
        private GroupDao groupDao;

        private DeleteMemberAsyncTask(GroupDao groupDao) {
            this.groupDao = groupDao;
        }

        @Override
        protected Void doInBackground(GroupEntity... groupEntities) {
            groupDao.delete(groupEntities[0]);
            return null;
        }
    }

    private static class UpdateMemberAsyncTask extends AsyncTask<GroupEntity,Void,Void> {
        private GroupDao groupDao;

        private UpdateMemberAsyncTask(GroupDao groupDao) {
            this.groupDao = groupDao;
        }

        @Override
        protected Void doInBackground(GroupEntity... groupEntities) {
            groupDao.update(groupEntities[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void> {
        private GroupDao groupDao;

        private DeleteAllAsyncTask(GroupDao groupDao) {
            this.groupDao = groupDao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            groupDao.deleteAll();
            return null;
        }
    }
}
