package com.nishantboro.splititeasy;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.ArrayList;
import java.util.List;
/* Use AsyncTask to perform operations insert,delete,update,deleteAll,getAllMembersNonLive. Doing these operations on the main thread could
* lock our user interface */

public class MemberRepository {
    private MemberDao memberDao;
    private LiveData<List<MemberEntity>> allMembers;

    MemberRepository(Application application, String gName) {
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

    LiveData<List<MemberEntity>> getAllMembers() {
        return allMembers;
    }

    List<MemberEntity> getAllMembersNonLive(String gName) {
        GetAllMembersNonLiveAsyncTask members = new GetAllMembersNonLiveAsyncTask(memberDao);
        try {
            return members.execute(gName).get();
        } catch (Exception err) {
            return new ArrayList<>(); // if there are no members in database return a blank array list
        }
    }

    private static class GetAllMembersNonLiveAsyncTask extends AsyncTask<String,Void,List<MemberEntity>> {
        private MemberDao memberDao;

        private GetAllMembersNonLiveAsyncTask(MemberDao memberDao) {
            this.memberDao = memberDao;
        }

        @Override
        protected List<MemberEntity> doInBackground(String... strings) {
            return memberDao.getAllNonLive(strings[0]);
        }
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
