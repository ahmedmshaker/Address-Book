package org.support.addressbook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseConnector {

    private SQLiteDatabase mDatabase;
    private AddressBookHelper mBookHelper;

    public DatabaseConnector(Context context) {
        mBookHelper = new AddressBookHelper(context);
    }

    public void open() {
        mDatabase = mBookHelper.getWritableDatabase();
    }

    public void close() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public Cursor getAllContacts() {
        Cursor cursor = mDatabase.query(
                AddressBookHelper.TABLE_CONTACTS, // table name
                new String[] { AddressBookHelper.COLUMN_ID, AddressBookHelper.COLUMN_NAME ,AddressBookHelper.COLUMN_PATH }, // column names
                null, // where clause
                null, // where params
                null, // groupby
                null, // having
                AddressBookHelper.COLUMN_NAME // orderby
        );

        return cursor;
    }

    public Cursor getContactById(int id) {
        String[] columns = { AddressBookHelper.COLUMN_NAME, AddressBookHelper.COLUMN_PHONE,
                AddressBookHelper.COLUMN_EMAIL, AddressBookHelper.COLUMN_STREET, AddressBookHelper.COLUMN_CITY , AddressBookHelper.COLUMN_PATH};

        return mDatabase.query(
                AddressBookHelper.TABLE_CONTACTS, // table name
                columns, // column names
                AddressBookHelper.COLUMN_ID + " = " + id, // where clause // id param. could be here or appended as it is ^
                null, // where params
                null, // groupby
                null, // having
                null // orderby
        );
    }

    public void deleteContactById(int id) {
        open();
        mDatabase.delete(
                AddressBookHelper.TABLE_CONTACTS, // table name
                AddressBookHelper.COLUMN_ID +"="+ id, // where clause
                null // where params
        );
        close();
    }

    public void insertContact(String name, String phone, String email,
                              String street, String city ,String Path) {
        mDatabase.beginTransaction();
        try {
            ContentValues newContact = new ContentValues();
            newContact.put(AddressBookHelper.COLUMN_NAME, name);
            newContact.put(AddressBookHelper.COLUMN_PHONE, phone);
            newContact.put(AddressBookHelper.COLUMN_EMAIL, email);
            newContact.put(AddressBookHelper.COLUMN_STREET, street);
            newContact.put(AddressBookHelper.COLUMN_CITY, city);
            newContact.put(AddressBookHelper.COLUMN_PATH, Path);
            mDatabase.insert(AddressBookHelper.TABLE_CONTACTS, null, newContact);
            mDatabase.setTransactionSuccessful();
        }
        finally {
            mDatabase.endTransaction();
        }
    }

    public void updateContact(int rowId, String name, String phone,
                              String email, String street, String city ,String Path) {
        ContentValues editContact = new ContentValues();
        editContact.put(AddressBookHelper.COLUMN_NAME, name);
        editContact.put(AddressBookHelper.COLUMN_PHONE, phone);
        editContact.put(AddressBookHelper.COLUMN_EMAIL, email);
        editContact.put(AddressBookHelper.COLUMN_STREET, street);
        editContact.put(AddressBookHelper.COLUMN_CITY, city);
        editContact.put(AddressBookHelper.COLUMN_PATH, Path);

        mDatabase.update(
                AddressBookHelper.TABLE_CONTACTS, // table name
                editContact, // values
                AddressBookHelper.COLUMN_ID + " = " + rowId, // where clause
                null // where params
        );

    }

}
