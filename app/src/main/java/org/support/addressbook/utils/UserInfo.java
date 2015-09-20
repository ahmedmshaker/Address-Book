package org.support.addressbook.utils;

/**
 * Created by shika on 9/9/2015.
 */
public class UserInfo {
    private String name;
    private String path;

    private int id;
    public UserInfo(int mId,String mName , String mPath){
        name=mName;
        path=mPath;
        id=mId;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public int getId() {
        return id;
    }
}
