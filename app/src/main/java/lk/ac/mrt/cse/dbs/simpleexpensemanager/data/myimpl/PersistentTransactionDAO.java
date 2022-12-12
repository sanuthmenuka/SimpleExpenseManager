package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.myimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private SQLiteOpenHelper persistentStorageHelper;

   public PersistentTransactionDAO(SQLiteOpenHelper persistentStorageHelper){
       this.persistentStorageHelper=persistentStorageHelper;
   }



    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db=this.persistentStorageHelper.getWritableDatabase();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-mm-yyyy");

        ContentValues contentValues=new ContentValues();
        contentValues.put("Date",simpleDateFormat.format(date));
        contentValues.put("AccountNo",accountNo);
        contentValues.put("ExpenseType", String.valueOf(expenseType));
        contentValues.put("Amount",amount);

        db.insert("TRANSACTIONS",null,contentValues);

        db.close();

    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
       SQLiteDatabase db=this.persistentStorageHelper.getWritableDatabase();
        Cursor cursor=db.query("TRANSACTIONS",new String[]{"Date","AccountNo","ExpenseType","Amount"},null,
                null,null,null,null);
       List<Transaction> transactionList=new ArrayList<Transaction>();

       if(cursor.moveToFirst()){
           do{
               String date=cursor.getString(0);
               String accountNo=cursor.getString(1);
               String expenseType= cursor.getString(2);
               double amount=cursor.getDouble(3);

               Date dateType=new SimpleDateFormat("dd-mm-yyyy").parse(date);
               ExpenseType expenseType1=ExpenseType.valueOf(expenseType);

               Transaction transaction=new Transaction(dateType,accountNo,expenseType1,amount);
               transactionList.add(transaction);

           }while(cursor.moveToNext());

       }


       cursor.close();
       db.close();
       return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException{
        SQLiteDatabase db=persistentStorageHelper.getWritableDatabase();
        Cursor cursor=db.query("TRANSACTIONS",new String[]{"Date","AccountNo","ExpenseType","Amount"},null,
                null,null,null,null);

        List<Transaction> transactionList=new ArrayList<Transaction>();
        int size=0;

        if(cursor.moveToFirst()){
            do{
                String date=cursor.getString(0);
                String accountNo=cursor.getString(1);
                String expenseType= cursor.getString(2);
                double amount=cursor.getDouble(3);

                Date dateType=new SimpleDateFormat("dd-mm-yyyy").parse(date);
                ExpenseType expenseType1=ExpenseType.valueOf(expenseType);

                Transaction transaction=new Transaction(dateType,accountNo,expenseType1,amount);
                transactionList.add(transaction);

                size++;

            }while(cursor.moveToNext() && size<=limit);
        }

        cursor.close();
        db.close();

       return transactionList;
    }
}
