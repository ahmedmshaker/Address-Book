package org.support.addressbook.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.support.addressbook.R;
import org.support.addressbook.adapters.customListAdapter;
import org.support.addressbook.db.AddressBookHelper;
import org.support.addressbook.db.DatabaseConnector;
import org.support.addressbook.utils.UserInfo;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private TextView empty;
    private DatabaseConnector mConnector;

    customListAdapter adapter;

    List<UserInfo> userInfos;


    public static final String row_id = "row_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mConnector = new DatabaseConnector(MainActivity.this);
        mListView = (ListView) findViewById(R.id.listview);


        empty = (TextView) findViewById(android.R.id.empty);


        mListView.setEmptyView(empty);
        mListView.setOnItemClickListener(this);

       // new LoadInformation().execute((Object[])null);

    }

    @Override
    protected void onResume() {
        super.onResume();


        //LoadDataFromDataBaseToListView();
        new LoadInformation().execute((Object[])null);

    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            startActivityForResult(new Intent(this, EditContactActivity.class), 5);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
    }

    class LoadInformation extends AsyncTask<Object, Object, Cursor> {
        DatabaseConnector databaseconcetor = new DatabaseConnector(
                MainActivity.this);

        @Override
        protected Cursor doInBackground(Object... arg0) {
            // TODO Auto-generated method stub
            databaseconcetor.open();
            return databaseconcetor.getAllContacts();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Cursor result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            userInfos=new ArrayList<>();
            String Name;
            String Path;
            int ID;

            if (result != null && result.getCount() > 0) {



                try {


                    if (result.moveToFirst()){

                        int i = 0;
                    int index_id = result.getColumnIndex(AddressBookHelper.COLUMN_ID);
                    int index_name = result.getColumnIndex(AddressBookHelper.COLUMN_NAME);
                    int index_path = result.getColumnIndex(AddressBookHelper.COLUMN_PATH);


                    do {
                        ID = result.getInt(index_id);
                        Name = result.getString(index_name);
                        Path = result.getString(index_path);


                        userInfos.add(new UserInfo(ID, Name, Path));

                        ++i;

                    } while (result.moveToNext());


                        databaseconcetor.close();
                }


                    adapter=new customListAdapter(MainActivity.this ,userInfos );
                    mListView.setAdapter(adapter);



                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(),
                            Toast.LENGTH_LONG).show();

                }

            } else {
                Toast.makeText(MainActivity.this, "is null", Toast.LENGTH_LONG).show();
               ArrayAdapter<String> madapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1);

                mListView.setAdapter(madapter);

            }





        }
    }

    /*private void LoadDataFromDataBaseToListView() {

        mConnector.open();
        result = mConnector.getAllContacts();


        if (result != null && result.getCount() > 0) {
            int i = 0;
            names = new String[result.getCount()];
            ids = new int[result.getCount()];

            try {

                if (result.moveToFirst()) {


                    int index_id = result.getColumnIndex(AddressBookHelper.COLUMN_ID);
                    int index_name = result.getColumnIndex(AddressBookHelper.COLUMN_NAME);


                    do {
                        ids[i] = result.getInt(index_id);
                        String ss = result.getString(index_name);


                        names[i] = ss;

                        ++i;

                    } while (result.moveToNext());

                    mConnector.close();

                    adapter = new ArrayAdapter<String>(
                            getApplicationContext(),
                            R.layout.contact_item,R.id.contact_name,
                            names);
                    mListView.setAdapter(adapter);


                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(),
                        Toast.LENGTH_LONG).show();

            }

        } else {
            Toast.makeText(this, "is null", Toast.LENGTH_LONG).show();
            adapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    android.R.layout.simple_list_item_1);

            mListView.setAdapter(adapter);

        }


    }*/


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ViewContact(position);

    }

    private void ViewContact(int position) {
        try {
            Intent intent = new Intent(MainActivity.this, ViewContactActivity.class);
            intent.putExtra(row_id, userInfos.get(position).getId());
            Toast.makeText(getApplicationContext(), userInfos.get(position).getId() + "",
                    Toast.LENGTH_LONG).show();
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
