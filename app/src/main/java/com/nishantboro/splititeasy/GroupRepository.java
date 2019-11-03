package com.nishantboro.splititeasy;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class GroupRepository {
    private GroupDao groupDao;
    private LiveData<List<GroupEntity>> allGroups;

    public GroupRepository(Application application) {
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

    public LiveData<List<GroupEntity>> getAllGroups() {
        return allGroups;
    }

    public List<GroupEntity> getAllGroupsNonLive() {
        GetAllGroupsNonLiveAsyncTask groups = new GetAllGroupsNonLiveAsyncTask(groupDao);
        try {
            return groups.execute().get();
        } catch (Exception err) {
            return new ArrayList<>(); // if there are no groups in database return a blank array list
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
