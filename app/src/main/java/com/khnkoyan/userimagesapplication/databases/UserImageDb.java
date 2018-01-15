package com.khnkoyan.userimagesapplication.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserImageDb extends SQLiteOpenHelper {
    // database version number
    public static final int DATABASE_VERSION = 1;

    // database name
    private static final String DATABASE_NAME = "userManager.db";

    // labels table name;
    public static final String TABLE_USER = "users";
    public static final String TABLE_USER_IMAGE = "images";

    // labels table columns
    public static final String USER_ID = "id";
    public static final String IMG_ID = "id";

    public static final String USER_IMAGE_ID = "user_id";

    public static final String USER_NAME = "name";
    public static final String USER_SURNAME = "surname";
    public static final String USER_AGE = "age";
    public static final String USER_GENDER = "gender";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_IS_LOGINED = "login_status";

    public static final String IMAGE = "image";


    public UserImageDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER +
            "("
            + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_NAME + " TEXT, "
            + USER_SURNAME + " TEXT, "
            + USER_AGE + " SMALLINT, "
            + USER_GENDER + " TEXT, "
            + USER_EMAIL + " TEXT UNIQUE, "
            + USER_PASSWORD + " TEXT, "
            + USER_IS_LOGINED + " BOOLEAN)";

    private String CREATE_TABLE_IMAGE = "CREATE TABLE " + TABLE_USER_IMAGE +
            "("
            + IMG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_IMAGE_ID + " INTEGER, "
            + IMAGE + " BLOB NOT NULL, "
            + "FOREIGN KEY (" + USER_IMAGE_ID + ")REFERENCES " + TABLE_USER + "(" + USER_ID + ")" +
            ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_IMAGE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_IMAGE);
        onCreate(db);
    }
}
