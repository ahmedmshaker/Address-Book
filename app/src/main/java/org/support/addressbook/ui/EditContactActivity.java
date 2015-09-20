package org.support.addressbook.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.support.addressbook.R;
import org.support.addressbook.adapters.customListAdapter;
import org.support.addressbook.db.DatabaseConnector;
import org.support.addressbook.utils.UserInfo;

import java.net.URL;


public class EditContactActivity extends ActionBarActivity {


    EditText[] edtingcontact = new EditText[5];
    private int rowid;
    Bundle busket;
    ImageView UserImageView;

    String Path;

    final String[] option={"take from camera","select from gallary"};
    private ArrayAdapter<String> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        edtingcontact[0] = (EditText) findViewById(R.id.et_name);
        edtingcontact[1] = (EditText) findViewById(R.id.et_phone);
        edtingcontact[2] = (EditText) findViewById(R.id.et_email);
        edtingcontact[3] = (EditText) findViewById(R.id.et_street);
        edtingcontact[4] = (EditText) findViewById(R.id.et_city);

        UserImageView= (ImageView) findViewById(R.id.userImage);
        UserImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opencameraorgallry();
            }
        });

        busket = getIntent().getExtras();
        if (busket != null) {
            rowid = busket.getInt("row_id");
            edtingcontact[0].setText(busket.getString("name"));
            edtingcontact[1].setText(busket.getString("phone"));
            edtingcontact[2].setText(busket.getString("email"));
            edtingcontact[3].setText(busket.getString("street"));
            edtingcontact[4].setText(busket.getString("city"));
            Path=busket.getString("path");
            new ThumbilLoadingImage().execute(UserImageView , Uri.parse(Path));
        }
        Button btnSave = (Button) findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveOrUpdateContact();
            }
        });


        arr=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.select_dialog_singlechoice,option);

    }

    protected void SaveOrUpdateContact() {
        DatabaseConnector dataBaseObject = new DatabaseConnector(
                EditContactActivity.this);
        String name = edtingcontact[0].getText().toString();
        String phone = edtingcontact[1].getText().toString();
        String email = edtingcontact[2].getText().toString();
        String street =  edtingcontact[3].getText().toString();
        String city = edtingcontact[4].getText().toString();

        if (name.length() > 0 && phone.length() > 0 && email.length() >0
                && street.length() > 0 && city.length() > 0) {
            if (getIntent().getExtras() == null) {
                // come from ListView activity

                dataBaseObject.open();
                dataBaseObject.insertContact(name, phone, email, street, city, Path);
            } else {
                // come From Show Data Activity

                // saad  update here the data
                dataBaseObject.open();
                dataBaseObject.updateContact(rowid, name, phone, email, street, city , Path);


            }

            dataBaseObject.close();
            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(EditContactActivity.this);
            builder.setTitle("Oops");
            builder.setMessage("Please fill all fields");
            builder.setPositiveButton("yes", null);
            AlertDialog dialog= builder.create();
            dialog.show();
        }

    }



    private void opencameraorgallry(){
        try{

            AlertDialog.Builder bil=new AlertDialog.Builder(EditContactActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            bil.setTitle("select option");

            bil.setAdapter(arr, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    switch (which) {
                        case 0:
                            Intent intg=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intg, 0);
                            break;
                        case 1:
                            Intent intent=new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "select contact image"), 1);
                            break;
                        default:
                            break;
                    }

                }

            });
            final AlertDialog ar=bil.create();

            ar.show();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Uri uri=data.getData();
            new ThumbilLoadingImage().execute(UserImageView , uri);
            Path=uri.toString();
            Toast.makeText(EditContactActivity.this , Path , Toast.LENGTH_LONG).show();
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
