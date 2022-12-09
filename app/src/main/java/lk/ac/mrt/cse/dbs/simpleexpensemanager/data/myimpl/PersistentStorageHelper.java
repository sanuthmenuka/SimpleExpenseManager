package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.myimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PersistentStorageHelper extends SQLiteOpenHelper {

    private static final  String DB_NAME="ExpenseManager";
    private static final int DB_VERSION=1;

    //refer to constructor of SQLiteOpenHelper
    public PersistentStorageHelper(Context context){

        super(context,DB_NAME,null,DB_VERSION);
    }

    //GETS CALLED WHEN DB FIRST GETS CREATED
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE ACCOUNTS(AccountNo TEXT NOT NULL PRIMARY KEY,"
                            +"BankName TEXT,"
                            +"AccountHolder TEXT,"
                            +"Balance REAL);");
        sqLiteDatabase.execSQL("CREATE TABLE TRANSACTIONS(Date TEXT," +
                "AccountNo TEXT," +
                "ExpenseType TEXT," +
                "Amount REAL," +
                "FOREIGN KEY (AccountNo) REFERENCES ACCOUNTS(AccountNo));");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
