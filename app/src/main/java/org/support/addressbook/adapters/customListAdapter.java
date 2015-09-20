package org.support.addressbook.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.support.addressbook.R;
import org.support.addressbook.utils.UserInfo;

import java.util.List;

/**
 * Created by shika on 9/9/2015.
 */
public class customListAdapter extends ArrayAdapter<UserInfo> {

    Context con;
    List<UserInfo> infoList;
    LayoutInflater layoutInflater;
    public customListAdapter(Context context, List<UserInfo> info) {
        super(context, -1, info);
        con=context;
        infoList=info;
        layoutInflater=LayoutInflater.from(context);
    }

    class ViewHolder{
        ImageView UserImage;
        TextView UserName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.contact_item , parent ,false);
            holder=new ViewHolder();
            holder.UserImage= (ImageView) convertView.findViewById(R.id.userImage);
            holder.UserName= (TextView) convertView.findViewById(R.id.contact_name);
            convertView.setTag(holder);
        }else{

            holder= (ViewHolder) convertView.getTag();

        }

        UserInfo userInfo=infoList.get(position);
        if (userInfo.getPath()!=null) {
            new ThumbilLoadingImage().execute(holder.UserImage, Uri.parse(userInfo.getPath()));
        }else{
            holder.UserImage.setImageResource(R.mipmap.avatar_empty);
        }
       // Bitmap bm=LoadThumbil(Uri.parse(userInfo.getPath()), con.getContentResolver() ,new BitmapFactory.Options());
        //holder.UserImage.setImageBitmap(bm);
        holder.UserName.setText(userInfo.getName());

        return convertView;
    }

    public class ThumbilLoadingImage extends AsyncTask<Object ,Object ,Bitmap> {
        ImageView imageView;

        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView) params[0];



            return customListAdapter.LoadThumbil((Uri)params[1] ,
                    con.getContentResolver() , new BitmapFactory.Options());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }








    public static Bitmap LoadThumbil(Uri uri , ContentResolver contentResolver , BitmapFactory.Options options){


        int id = Integer.parseInt(uri.getLastPathSegment());

        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MICRO_KIND
                    , options);

        return bitmap;
    }


}
