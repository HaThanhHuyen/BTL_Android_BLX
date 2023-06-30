package com.dhtl.btl_ptud.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dhtl.btl_ptud.model.Items;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "database.sqlite";
    public static final String DBLOCATION = "data/data/com.dhtl.btl_ptud/databases/";

    private Context mcontext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }

    private void copyDataBase() throws IOException {
        try {
            InputStream myInput = mcontext.getAssets().open(DBNAME); //Mở tệp cơ sở dữ liệu trong thư mục assets của ứng dụng bằng cách sử dụng một đối tượng InputStream.
            String outFileName = DBLOCATION + DBNAME; //Xác định vị trí đích của tệp cơ sở dữ liệu trong hệ thống tệp của thiết bị
            OutputStream myOutput = new FileOutputStream(outFileName); //Tạo một đối tượng OutputStream để ghi dữ liệu từ InputStream đã mở vào tệp đích
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void openDatabase() {
        try {
            String dbPath = mcontext.getDatabasePath(DBNAME).getAbsolutePath(); //lay ra duong dan cua DB
            if (mDatabase != null && mDatabase.isOpen()) {
                return;
            }
            mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void closeDatabase() {
        if (mDatabase != null)
            mDatabase.close();
    }

    public ArrayList<Items> getListItems() {
        Items items = null;
        ArrayList<Items> itemsList = new ArrayList<>();
        openDatabase();
        try {
            Cursor cursor = mDatabase.rawQuery("SELECT * FROM ITEMS", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                items = new Items(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                        , cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7));
                itemsList.add(items);
                cursor.moveToNext();
            }
            cursor.close();
            closeDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemsList;
    }

    public ArrayList<Items> getList20Items(int a) {
        Items items = null;
        ArrayList<Items> itemsList = new ArrayList<>();
        try {
            openDatabase();
            Cursor cursor = mDatabase.rawQuery("SELECT * FROM ITEMS WHERE _ID like '%" + String.valueOf(a) + "%'", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast() && itemsList.size() < 20) {
                items = new Items(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                        , cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7));
                itemsList.add(items);
                cursor.moveToNext();
            }
            cursor.close();
            closeDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemsList;
    }
}