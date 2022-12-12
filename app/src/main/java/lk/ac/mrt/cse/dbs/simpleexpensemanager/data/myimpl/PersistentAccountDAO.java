package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.myimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private SQLiteOpenHelper persistentStorageHelper;


    public PersistentAccountDAO(SQLiteOpenHelper persistentStorageHelper)
    {
        this.persistentStorageHelper=persistentStorageHelper;

    }

    //calling getWritable() multiple times

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db=this.persistentStorageHelper.getWritableDatabase();

        Cursor cursor=db.query("ACCOUNTS",new String[]{"AccountNo"},
                null,null,null,null,null);

        List<String> accountList=new ArrayList<String>() ;
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            accountList.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return accountList;

    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db=persistentStorageHelper.getWritableDatabase();

        Cursor cursor=db.query("ACCOUNTS",new String[]{"AccountNo","BankName","AccountHolder","Balance"},
                null,null,null,null,null);

        List<Account> accountList=new ArrayList<Account>();
        if(cursor.moveToFirst()){
           do{
               Account temp=new Account(cursor.getString(0),cursor.getString(1),
                       cursor.getString(2),cursor.getDouble(3) );
                accountList.add(temp);
           }
           while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db=persistentStorageHelper.getWritableDatabase();
        Cursor cursor=db.query("ACCOUNTS",new String[]{"AccountNo","BankName","AccountHolder","Balance"},
                "AccountNo=?",new String[]{accountNo},null,null,null);

        Account account;
        if(cursor.moveToFirst()){
            account=new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3));

        }
        else {
            throw new InvalidAccountException("No account found");
        }


        cursor.close();
        db.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db=this.persistentStorageHelper.getWritableDatabase();

        ContentValues accountValues=new ContentValues();
        accountValues.put("AccountNo",account.getAccountNo());
        accountValues.put("BankName",account.getBankName());
        accountValues.put("AccountHolder",account.getAccountHolderName());
        accountValues.put("Balance",account.getBalance());

        db.insert("ACCOUNTS",null,accountValues);
        db.close();

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db=this.persistentStorageHelper.getWritableDatabase();
        int accountFound=db.delete("ACCOUNTS","AccountNo=?",new String[]{accountNo});
        db.close();

        if(accountFound==0){
            throw new InvalidAccountException("No such Account");
        }

    }

    @Override
    //need to check balance?
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db=this.persistentStorageHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        Cursor cursor=db.query("ACCOUNTS",new String[]{"AccountNo","Balance"},
                "AccountNo=?",new String[]{accountNo},null,null,null);

        double balance=0;
        if(cursor.moveToFirst()){
            balance=cursor.getDouble(1);
        }

        else{
            throw new InvalidAccountException("No such Account");
        }

        if(expenseType==ExpenseType.INCOME){
            balance+=amount;
        }
        else{
            balance=balance-amount;
        }

        contentValues.put("AccountNo",accountNo);
        contentValues.put("Balance",balance);

        db.update("ACCOUNTS",contentValues,"accountNo=?",new String[]{accountNo});

        cursor.close();
        db.close();



    }
}
