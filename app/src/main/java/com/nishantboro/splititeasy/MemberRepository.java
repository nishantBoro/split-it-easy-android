package com.nishantboro.splititeasy;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MemberRepository {
    private MemberDao memberDao;
    private LiveData<List<MemberEntity>> allMembers;

    public MemberRepository(Application application, String gName) {
        AppDatabase database = AppDatabase.getInstance(application);
        memberDao = database.memberDao();
        allMembers = memberDao.getAll(gName);
    }

    public void insert(MemberEntity member) {
        new InsertMemberAsyncTask(memberDao).execute(member);
    }

    public void delete(MemberEntity member) {
        new DeleteMemberAsyncTask(memberDao).execute(member);
    }

    public void update(MemberEntity member) {
        new UpdateMemberAsyncTask(memberDao).execute(member);
    }

    public void deleteAll(String gName) {
        new DeleteAllAsyncTask(memberDao).execute(gName);
    }

    public LiveData<List<MemberEntity>> getAllMembers() {
        return allMembers;
    }

    private static class InsertMemberAsyncTask extends AsyncTask<MemberEntity,Void,Void> {
        private MemberDao memberDao;

        private InsertMemberAsyncTask(MemberDao memberDao) {
            this.memberDao = memberDao;
        }

        @Override
        protected Void doInBackground(MemberEntity... memberEntities) {
            memberDao.insert(memberEntities[0]);
            return null;
        }
    }

    private static class DeleteMemberAsyncTask extends AsyncTask<MemberEntity,Void,Void> {
        private MemberDao memberDao;

        private DeleteMemberAsyncTask(MemberDao memberDao) {
            this.memberDao = memberDao;
        }

        @Override
        protected Void doInBackground(MemberEntity... memberEntities) {
            memberDao.delete(memberEntities[0]);
            return null;
        }
    }

    private static class UpdateMemberAsyncTask extends AsyncTask<MemberEntity,Void,Void> {
        private MemberDao memberDao;

        private UpdateMemberAsyncTask(MemberDao memberDao) {
            this.memberDao = memberDao;
        }

        @Override
        protected Void doInBackground(MemberEntity... memberEntities) {
            memberDao.update(memberEntities[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<String,Void,Void> {
        private MemberDao memberDao;

        private DeleteAllAsyncTask(MemberDao memberDao) {
            this.memberDao = memberDao;
        }


        @Override
        protected Void doInBackground(String... strings) {
            memberDao.deleteAll(strings[0]);
            return null;
        }
    }
}
