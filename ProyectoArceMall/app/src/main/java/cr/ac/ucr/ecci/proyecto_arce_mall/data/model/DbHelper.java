package cr.ac.ucr.ecci.proyecto_arce_mall.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "Users.db";

    //Users table name
    private static final String TABLE_USER = "User";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "ID";
    private static final String COLUMN_USER_NAME = "Name";
    private static final String COLUMN_USER_EMAIL = "Email";
    private static final String COLUMN_USER_PASSWORD = "Password";
    private static final String COLUMN_USER_PROVINCE = "Province";
    private static final String COLUMN_USER_BIRTHDAY = "Birthday"; //FORMAT YYY MM DD ISO 8601,
    private static final String COLUMN_USER_FIRST = "FirstTime";
    // create table sql query
    private final String  CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " TEXT PRIMARY KEY ," + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_EMAIL + " TEXT, " + COLUMN_USER_PROVINCE + " TEXT, "
            + COLUMN_USER_BIRTHDAY + " TEXT, " + COLUMN_USER_PASSWORD + " TEXT, "
            + COLUMN_USER_FIRST + " INTEGER " + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        // Create tables again
        onCreate(db);
    }

    /*
     create a new user
     */

    public String  addUser(User user) {
        String Error = "Succes";
        Boolean IsRegistered = false;
        Boolean BDTRY = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, user.getIdentification());
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PROVINCE, user.getProvince());
        values.put(COLUMN_USER_BIRTHDAY, user.getBirthday());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_FIRST,user.getFirstTime());
        BDTRY = db.isOpen();
        List<User> users = this.getAllUser();
        BDTRY = db.isOpen();
        db = getWritableDatabase();
        for(int i = 0;i<users.size();++i){
            if(user.getIdentification().equals(users.get(i).getIdentification())){
                IsRegistered=true;
                Error = " Esta identificación ya se encuentra en el sistema ";
            }
            if(user.getEmail().equals(users.get(i).getEmail())){
                IsRegistered=true;
                Error = " Este correo ya se encuentra en el sistema ";
            }
        };
        if (!IsRegistered){
            // Inserting Row
            Log.i("DATA BASE ",db.insert(TABLE_USER, null, values) + "");
        }
        db.close();
        return Error;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, user.getIdentification());
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PROVINCE, user.getProvince());
        values.put(COLUMN_USER_BIRTHDAY, user.getBirthday());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_FIRST,user.getFirstTime());
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getIdentification())});
        db.close();
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getIdentification())});
        db.close();
    }

    /*
     This Return a list of all registered Users
    */

    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PROVINCE,
                COLUMN_USER_BIRTHDAY,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_FIRST
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setIdentification(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
                user.setProvince(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PROVINCE)));
                user.setBirthday(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_BIRTHDAY)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)));
                user.setFirstTime(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_FIRST)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return userList;
    }

    public boolean isFirstTime(String email) {
        boolean firstTime = false;
        int value = -1;

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_USER_FIRST
        };

        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);

        if (cursor.moveToFirst()) {
            value = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_FIRST));
        }

        cursor.close();
        db.close();

        if (value == 1) {
            firstTime = true;
        }

        return firstTime;
    }

    public boolean checkUser(String email, String password) {
        boolean Success=false;
        String[] Columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_USER, //Table to query
                Columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            Success = true;
        }
        return Success;
    }
}

