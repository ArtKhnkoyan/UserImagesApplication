package com.khnkoyan.userimagesapplication.dbManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.khnkoyan.userimagesapplication.databases.UserImageDb;
import com.khnkoyan.userimagesapplication.models.Gender;
import com.khnkoyan.userimagesapplication.models.Image;
import com.khnkoyan.userimagesapplication.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserImageDbManager {

    private Context context;
    private UserImageDb userMessengerDb;
    private SQLiteDatabase db;
    private static UserImageDbManager imageDbManager = null;

    private UserImageDbManager(Context context) {
        this.context = context;
        this.userMessengerDb = new UserImageDb(context);
    }

    public static UserImageDbManager getInstance(Context context) {
        if (imageDbManager == null) {
            synchronized (UserImageDbManager.class) {
                if (imageDbManager == null) {
                    imageDbManager = new UserImageDbManager(context);
                }
            }
        }
        return imageDbManager;
    }


    public void closeDb() {
        userMessengerDb.close();
    }


    public void saveUserData(User user) {
        db = userMessengerDb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserImageDb.USER_NAME, user.getName());
        contentValues.put(UserImageDb.USER_SURNAME, user.getSurName());
        contentValues.put(UserImageDb.USER_AGE, user.getAge());
        contentValues.put(UserImageDb.USER_GENDER, String.valueOf(user.getGender()));
        contentValues.put(UserImageDb.USER_EMAIL, user.getEmail());
        contentValues.put(UserImageDb.USER_PASSWORD, user.getPassword());
        contentValues.put(UserImageDb.USER_IS_LOGINED, user.isUserLoggedIn());
        // inserting rows
        db.insert(UserImageDb.TABLE_USER, null, contentValues);
    }


    private int selectUserId(String userLogin) {
        int id = 0;
        db = userMessengerDb.getReadableDatabase();
        String select = "select " + UserImageDb.USER_ID + " from " + UserImageDb.TABLE_USER
                + " where " + UserImageDb.USER_EMAIL + " =?";
        String[] selectionArgs = {userLogin};
        Cursor c = db.rawQuery(select, selectionArgs);
        if (c.moveToFirst()) {
            id = c.getInt(c.getColumnIndex(UserImageDb.USER_ID));
        }
        return id;
    }

    public void saveImageDb(byte[] blob, String userLogin) {
        // Open the database for writing
        db = userMessengerDb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(UserImageDb.IMAGE, blob);
        contentValues.put(UserImageDb.USER_IMAGE_ID, selectUserId(userLogin));
        // Insert Row
        db.insert(UserImageDb.TABLE_USER_IMAGE, null, contentValues);
    }

    public void updateUserData(User user, String userEmail) {
        db = userMessengerDb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserImageDb.USER_NAME, user.getName());
        contentValues.put(UserImageDb.USER_SURNAME, user.getSurName());
        contentValues.put(UserImageDb.USER_AGE, user.getAge());
        contentValues.put(UserImageDb.USER_GENDER, String.valueOf(user.getGender()));
        // update rows
        db.update(UserImageDb.TABLE_USER, contentValues, UserImageDb.USER_EMAIL + " =?",
                new String[]{userEmail});
    }

    /**
     * this method is to delete record
     *
     * @param email
     */
    public void deleteUserData(String email) {
        db = userMessengerDb.getReadableDatabase();

        // delete user record by id
        db.delete(UserImageDb.TABLE_USER, UserImageDb.USER_EMAIL + " =?",
                new String[]{email});
    }

    /**
     * this method check user exists or not
     *
     * @return
     */

    public boolean checkUser(String email) {
        db = userMessengerDb.getReadableDatabase();

        String[] columns = {UserImageDb.USER_EMAIL};
        String selection = UserImageDb.USER_EMAIL + " =?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(UserImageDb.TABLE_USER,
                columns, selection, selectionArgs, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }


    /**
     * this method check user exists or not
     *
     * @return false or true
     */

    public boolean checkUser(String email, String password) {
        db = userMessengerDb.getReadableDatabase();

        String[] columns = {UserImageDb.USER_EMAIL, UserImageDb.USER_PASSWORD};
        String selection = UserImageDb.USER_EMAIL + " =?" + " and " + UserImageDb.USER_PASSWORD + " =?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(UserImageDb.TABLE_USER,
                columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    /**
     * Getting user data
     *
     * @param
     * @return user
     */
    public User getUserData(String userEmail) {
        db = userMessengerDb.getReadableDatabase();

        String selectColumns = "SELECT " + UserImageDb.USER_NAME + ", " + UserImageDb.USER_SURNAME + ", " +
                UserImageDb.USER_EMAIL + ", " + UserImageDb.USER_PASSWORD + ", " +
                UserImageDb.USER_AGE + ", " + UserImageDb.USER_GENDER + ", " +
                UserImageDb.IMAGE + " FROM " + UserImageDb.TABLE_USER +
                " LEFT JOIN " + UserImageDb.TABLE_USER_IMAGE + " ON " +
                UserImageDb.TABLE_USER + "." + UserImageDb.USER_ID + "=" +
                UserImageDb.TABLE_USER_IMAGE + "." + UserImageDb.USER_IMAGE_ID +
                " WHERE " + UserImageDb.USER_EMAIL + " =?";

        String[] selectionArgs = {userEmail};

        Cursor cursor = db.rawQuery(selectColumns, selectionArgs);

        User userData = new User();
        if (cursor.moveToFirst()) {
            do {
                userData.setName(cursor.getString(cursor.getColumnIndex(UserImageDb.USER_NAME)));
                userData.setSurName(cursor.getString(cursor.getColumnIndex(UserImageDb.USER_SURNAME)));
                userData.setEmail(cursor.getString(cursor.getColumnIndex(UserImageDb.USER_EMAIL)));
                userData.setPassword(cursor.getString(cursor.getColumnIndex(UserImageDb.USER_PASSWORD)));
                userData.setAge(cursor.getInt(cursor.getColumnIndex(UserImageDb.USER_AGE)));
                userData.setGender(Gender.valueOf(cursor.getString(cursor.getColumnIndex(UserImageDb.USER_GENDER))));
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(UserImageDb.IMAGE));
                if (blob != null) {

                    List<Image> list = selectBlobOrIdByUserId(userEmail);
                    userData.setImageList(list);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return userData;
    }

    private List<Image> selectBlobOrIdByUserId(String userLogin) {
        List<Image> imageList = new ArrayList<>();
        db = userMessengerDb.getReadableDatabase();
        int user_id = selectUserId(userLogin);
        String select = "select " + UserImageDb.IMAGE + ", " + UserImageDb.IMG_ID + " from " + UserImageDb.TABLE_USER_IMAGE
                + " where " + UserImageDb.USER_IMAGE_ID + " =?";
        String[] selArgs = {String.valueOf(user_id)};
        Cursor cursor = db.rawQuery(select, selArgs);
        if (cursor.moveToFirst()) {
            do {
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(UserImageDb.IMAGE));
                int img_id = cursor.getInt(cursor.getColumnIndex(UserImageDb.IMG_ID));
                Image image = new Image();
                image.setBlob(blob);
                image.setId(img_id);
                imageList.add(image);
            } while (cursor.moveToNext());
        }
        return imageList;
    }


    public void setLogin(boolean isLoggedIn, String userName) {
        db = userMessengerDb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String selection = UserImageDb.USER_EMAIL + " =?";
        String[] selectionArgs = {userName};

        contentValues.put(UserImageDb.USER_IS_LOGINED, isLoggedIn);
        db.update(UserImageDb.TABLE_USER, contentValues, selection, selectionArgs);
    }


    public boolean isLoggedIn() {
        db = userMessengerDb.getReadableDatabase();

        String select = "select * from " + UserImageDb.TABLE_USER;
        Cursor cursor = db.rawQuery(select, null);

        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(UserImageDb.USER_IS_LOGINED)) > 0) {
                cursor.close();
                db.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public String selectLoginedUser() {
        db = userMessengerDb.getReadableDatabase();
        String selectLoginedUser = null;

        String select = "select " + UserImageDb.USER_EMAIL + " from " + UserImageDb.TABLE_USER
                + " where " + UserImageDb.USER_IS_LOGINED + " =?";
        String[] selArgs = {"1"};

        Cursor cursor = db.rawQuery(select, selArgs);
        if (cursor.moveToFirst()) {
            selectLoginedUser = cursor.getString(cursor.getColumnIndex(UserImageDb.USER_EMAIL));
        }
        cursor.close();
        return selectLoginedUser;
    }

    public void deleteItemImage(int imgId) {
        db = userMessengerDb.getReadableDatabase();
        db.delete(UserImageDb.TABLE_USER_IMAGE, UserImageDb.IMG_ID + " =?",
                new String[]{String.valueOf(imgId)});
    }

    public void deleteUserAllImages(String userEmail) {
        db = userMessengerDb.getReadableDatabase();
        int img_user_id = selectUserId(userEmail);
        db.delete(UserImageDb.TABLE_USER_IMAGE, UserImageDb.USER_IMAGE_ID + " =?",
                new String[]{String.valueOf(img_user_id)});
    }
}
