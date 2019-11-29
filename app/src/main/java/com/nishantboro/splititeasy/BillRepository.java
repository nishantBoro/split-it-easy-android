package com.nishantboro.splititeasy;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class BillRepository {
    private BillDao billDao;
    private LiveData<List<BillEntity>> allBills;

    BillRepository(Application application, String gName) {
        AppDatabase database = AppDatabase.getInstance(application);
        billDao = database.billDao();
        allBills = billDao.getAll(gName);
    }

    public void insert(BillEntity bill) {
        new InsertBillAsyncTask(billDao).execute(bill);
    }

    public void delete(BillEntity bill) {
        new DeleteBillAsyncTask(billDao).execute(bill);
    }

    public void update(BillEntity bill) {
        new UpdateBillAsyncTask(billDao).execute(bill);
    }

    public void deleteAll(String gName) {
        new DeleteAllAsyncTask(billDao).execute(gName);
    }

    LiveData<List<BillEntity>> getAllBills() {
        return allBills;
    }

    List<BillEntity> getAllBillsForMember(String gName, int mid) {
        GetAllBillsForMemberAsyncParams params = new GetAllBillsForMemberAsyncParams(gName,mid);
        GetAllBillsForMember bills = new GetAllBillsForMember(billDao);
        try {
            return bills.execute(params).get();
        } catch (Exception anyError) {
            return new ArrayList<>();
        }
    }



    private static class GetAllBillsForMember extends AsyncTask<GetAllBillsForMemberAsyncParams,Void,List<BillEntity>> {
        private BillDao billDao;

        private GetAllBillsForMember(BillDao billDao) {
            this.billDao = billDao;
        }


        @Override
        protected List<BillEntity> doInBackground(GetAllBillsForMemberAsyncParams... getAllBillsForMemberAsyncParams) {
            return billDao.getAllMemberBills(getAllBillsForMemberAsyncParams[0].gName,getAllBillsForMemberAsyncParams[0].mid);
        }
    }

    private static class InsertBillAsyncTask extends AsyncTask<BillEntity,Void,Void> {
        private BillDao billDao;

        private InsertBillAsyncTask(BillDao billDao) {
            this.billDao = billDao;
        }

        @Override
        protected Void doInBackground(BillEntity... billEntities) {
            billDao.insert(billEntities[0]);
            return null;
        }
    }

    private static class DeleteBillAsyncTask extends AsyncTask<BillEntity,Void,Void> {
        private BillDao billDao;

        private DeleteBillAsyncTask(BillDao billDao) {
            this.billDao = billDao;
        }

        @Override
        protected Void doInBackground(BillEntity... billEntities) {
            billDao.delete(billEntities[0]);
            return null;
        }
    }

    private static class UpdateBillAsyncTask extends AsyncTask<BillEntity,Void,Void> {
        private BillDao billDao;

        private UpdateBillAsyncTask(BillDao billDao) {
            this.billDao = billDao;
        }

        @Override
        protected Void doInBackground(BillEntity... billEntities) {
            billDao.update(billEntities[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<String,Void,Void> {
        private BillDao billDao;

        private DeleteAllAsyncTask(BillDao billDao) {
            this.billDao = billDao;
        }


        @Override
        protected Void doInBackground(String... strings) {
            billDao.deleteAll(strings[0]);
            return null;
        }
    }
}
