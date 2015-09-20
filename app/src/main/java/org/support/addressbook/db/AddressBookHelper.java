package org.support.addressbook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by donsaad on 8/27/2015.
 */
public class AddressBookHelper extends SQLiteOpenHelper {

    public static final String TABLE_CONTACTS = "CONTACTS";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_PHONE = "PHONE";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_STREET = "STREET";
    public static final String COLUMN_CITY = "CITY";
    public static final String COLUMN_PATH = "PATH";

    private static final String DB_NAME = "contacts.db";
    private static final int DB_VERSION = 2;
    private static final String DB_CREATE =
            "CREATE TABLE " + TABLE_CONTACTS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_STREET + " TEXT, " +
                    COLUMN_CITY + " TEXT, " +
                    COLUMN_PATH +" TEXT);";

    public AddressBookHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);



// Re Create on method  onCreate

        onCreate(db);


    }
}
