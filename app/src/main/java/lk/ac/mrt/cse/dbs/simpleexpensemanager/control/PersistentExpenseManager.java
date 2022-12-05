package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.myimpl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.myimpl.PersistentStorageHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.myimpl.PersistentTransactionDAO;

public class PersistentExpenseManager extends  ExpenseManager{
    private Context context;

    public PersistentExpenseManager(Context context){
        super();
        this.context=context;
        setup();
    }

    @Override
    public void setup()
    {
        SQLiteOpenHelper persistentStorageHelper=new PersistentStorageHelper(this.context);

        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(persistentStorageHelper);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(persistentStorageHelper);
        setAccountsDAO(persistentAccountDAO);
    }




}
