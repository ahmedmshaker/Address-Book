package org.support.addressbook.ui;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.support.addressbook.R;
import org.support.addressbook.adapters.customListAdapter;
import org.support.addressbook.db.AddressBookHelper;
import org.support.addressbook.db.DatabaseConnector;


/**
 * Created by user on 8/25/2015.
 */
public class ViewContactActivity extends ActionBarActivity {

    DatabaseConnector con;
    Cursor cur;
    int id;


    TextView name;
    TextView phone;
    TextView email;
    TextView street;
    TextView city;
    ImageView UserImage;

    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
         id = getIntent().getExtras().getInt("row_id");



        con=new DatabaseConnector(this);

        name = (TextView) findViewById(R.id.tv_name_show);
        phone = (TextView) findViewById(R.id.tv_phone_show);
        email = (TextView) findViewById(R.id.tv_email_show);
        street = (TextView) findViewById(R.id.tv_street_show);
        city = (TextView) findViewById(R.id.tv_city_show);

        UserImage = (ImageView) findViewById(R.id.userImage);

    }

    @Override
    protected void onResume() {
        super.onResume();
       // GetOneContact();
        new loadcontacttask().execute(id);
    }

    private void GetOneContact() {
        con.open();
        cur=con.getContactById(id);
        if (cur !=null&&cur.moveToFirst()) {


            String temp;
            TextView[] array = {name, phone, email, street, city};
            for (int i = 0; i < 5; ++i) {
                temp = cur.getString(i);
                array[i].setText(temp);
            }


        }
        con.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_showactivity,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.update: // TODO: 8/27/2015 to be checked - what is that activity here for
                UpdateContact();
                break;
            case R.id.remove:
                DeleteContact();

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void DeleteContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewContactActivity.this);
        builder.setTitle("are you sure?");
        builder.setMessage("are you want delete this contact");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
               con.deleteContactById(id);
                finish();
            }
        });
        builder.setNegativeButton("cancel", null);
        AlertDialog dialog= builder.create();
        dialog.show();
    }


    private void UpdateContact() {
        Intent intent = new Intent(ViewContactActivity.this, EditContactActivity.class);
        intent.putExtra("row_id", id);
        intent.putExtra("name",name.getText().toString());
        intent.putExtra("phone",phone.getText().toString());
        intent.putExtra("email",email.getText().toString());
        intent.putExtra("street",street.getText().toString());
        intent.putExtra("city",city.getText().toString());
        intent.putExtra("path" , path);
        startActivity(intent);
    }


    public class loadcontacttask extends AsyncTask<Integer, Object, Cursor> {
        DatabaseConnector databases = new DatabaseConnector(
                ViewContactActivity.this);

        @Override
        protected Cursor doInBackground(Integer... arg) {
            // TODO Auto-generated method stub
            databases.open();
            return databases.getContactById(arg[0]);
        }

        @Override
        protected void onPostExecute(Cursor result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            result.moveToFirst();
            int index_name = result.getColumnIndex(AddressBookHelper.COLUMN_NAME);

            int index_phone = result.getColumnIndex(AddressBookHelper.COLUMN_PHONE);
            int index_email = result.getColumnIndex(AddressBookHelper.COLUMN_EMAIL);
            int index_street = result.getColumnIndex(AddressBookHelper.COLUMN_STREET);
            int index_city = result.getColumnIndex(AddressBookHelper.COLUMN_CITY);
            int index_path = result.getColumnIndex(AddressBookHelper.COLUMN_PATH);

            name.setText(result.getString(index_name));
            phone.setText(result.getString(index_phone));
            email.setText(result.getString(index_email));
            street.setText(result.getString(index_street));
            city.setText(result.getString(index_city));
            path=result.getString(index_path);

            new ThumbilLoadingImage().execute(UserImage , Uri.parse(path));
            databases.close();
        }

    }



    public class ThumbilLoadingImage extends AsyncTask<Object ,Object ,Bitmap> {
        ImageView imageView;

        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView) params[0];



            return customListAdapter.LoadThumbil((Uri) params[1],
                    getContentResolver(), new BitmapFactory.Options());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }












}
