package cr.ac.ucr.ecci.proyecto_arce_mall.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.EncryptPassword;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Product;

public class DbHelper extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "Store.db";

    // Users table name
    private static final String TABLE_USER = "User";

    // Products table name
    private static final String TABLE_PRODUCTS = "Products";

    // Product Table Columns names
    private static final String COLUMN_PRODUCT_ID = "ID";
    private static final String COLUMN_PRODUCT_TITLE = "Title";
    private static final String COLUMN_PRODUCT_DESCRIPTION = "Description";
    private static final String COLUMN_PRODUCT_PRICE = "Price";
    private static final String COLUMN_PRODUCT_DISCOUNT_PERCENTAGE = "DiscountPercentage";
    private static final String COLUMN_PRODUCT_STOCK = "Stock";
    private static final String COLUMN_PRODUCT_BRAND = "Brand";
    private static final String COLUMN_PRODUCT_CATEGORY = "Category";
    private static final String COLUMN_PRODUCT_THUMBNAIL = "Thumbnail";
    private static final String COLUMN_PRODUCT_IMAGE = "Images";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "ID";
    private static final String COLUMN_USER_NAME = "Name";
    private static final String COLUMN_USER_EMAIL = "Email";
    private static final String COLUMN_USER_PASSWORD = "Password";
    private static final String COLUMN_USER_PROVINCE = "Province";
    private static final String COLUMN_USER_BIRTHDAY = "Birthday"; //FORMAT YYY MM DD ISO 8601,
    private static final String COLUMN_USER_FIRST = "FirstTime";

    // Create table sql query
    private final String  CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " TEXT PRIMARY KEY ,"
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_EMAIL + " TEXT, "
            + COLUMN_USER_PROVINCE + " TEXT, "
            + COLUMN_USER_BIRTHDAY + " TEXT, "
            + COLUMN_USER_PASSWORD + " TEXT, "
            + COLUMN_USER_FIRST + " INTEGER " + ")";

    // Drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    // Create table sql query
    private final String  CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
            + COLUMN_PRODUCT_ID + " TEXT PRIMARY KEY ,"
            + COLUMN_PRODUCT_TITLE + " TEXT, "
            + COLUMN_PRODUCT_DESCRIPTION + " TEXT, "
            + COLUMN_PRODUCT_PRICE + " INTEGER, "
            + COLUMN_PRODUCT_DISCOUNT_PERCENTAGE + " INTEGER, "
            + COLUMN_PRODUCT_STOCK + " INTEGER, "
            + COLUMN_PRODUCT_BRAND + " TEXT, "
            + COLUMN_PRODUCT_CATEGORY + " TEXT, "
            + COLUMN_PRODUCT_THUMBNAIL + " TEXT, "
            + COLUMN_PRODUCT_IMAGE + " TEXT " + ")";

    // Drop table sql query
    private String DROP_PRODUCT_TABLE = "DROP TABLE IF EXISTS " + TABLE_PRODUCTS;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop User and Product Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_PRODUCT_TABLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * Creates a new user and adds it to the database.
     * @param user  The new user to add
     */
    public String addUser(User user) {
        String error = "Succes";
        Boolean isRegistered = false;
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

        // Inserting Row
        Log.i("DATA BASE ",db.insert(TABLE_USER, null, values) + "");
        BDTRY = db.isOpen();
        List<User> users = this.getAllUser();
        BDTRY = db.isOpen();
        db = getWritableDatabase();

        for (int i = 0; i<users.size(); ++i) {
            if (user.getIdentification().equals(users.get(i).getIdentification())) {
                isRegistered = true;
                error = " Esta identificación ya se encuentra en el sistema ";
            }

            if(user.getEmail().equals(users.get(i).getEmail())){
                isRegistered = true;
                error = " Este correo ya se encuentra en el sistema ";
            }
        };

        if (!isRegistered) {
            // Inserting Row
            Log.i("DATA BASE ", db.insert(TABLE_USER, null, values) + "");
        }

        db.close();
        return error;
    }

    /**
     * Creates a new product and adds it to the database.
     * @param product  The new product to add
     */
    public String addProduct(Product product) {
        String error = "Succes";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PRODUCT_ID, product.getId());
        values.put(COLUMN_PRODUCT_TITLE, product.getTitle());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(COLUMN_PRODUCT_DISCOUNT_PERCENTAGE, product.getDiscountPercentage());
        values.put(COLUMN_PRODUCT_STOCK, product.getStock());
        values.put(COLUMN_PRODUCT_BRAND,product.getBrand());
        values.put(COLUMN_PRODUCT_CATEGORY,product.getCategory());
        values.put(COLUMN_PRODUCT_THUMBNAIL,product.getThumbnail());
        values.put(COLUMN_PRODUCT_IMAGE,product.getImages().get(0));

        // Inserting Row
        Log.i("DATA BASE ",db.insert(TABLE_PRODUCTS, null, values) + "");

        db.close();
        return error;
    }


    /**
     * Updates an user in the database.
     * @param user The user to update
     */
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

        // Updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getIdentification())});
        db.close();
    }

    /**
     * Updates a product in the database.
     * @param product The product to update
     */
    public void updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PRODUCT_ID, product.getId());
        values.put(COLUMN_PRODUCT_TITLE, product.getTitle());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(COLUMN_PRODUCT_DISCOUNT_PERCENTAGE, product.getDiscountPercentage());
        values.put(COLUMN_PRODUCT_STOCK, product.getStock());
        values.put(COLUMN_PRODUCT_BRAND,product.getBrand());
        values.put(COLUMN_PRODUCT_CATEGORY,product.getCategory());
        values.put(COLUMN_PRODUCT_THUMBNAIL,product.getThumbnail());
        values.put(COLUMN_PRODUCT_IMAGE,product.getImages().get(0));

        // Updating row
        db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
        db.close();
    }

    /**
     * Deletes an user from the database.
     * @param user The user to delete
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete user record by id.
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                  new String[]{String.valueOf(user.getIdentification())});
        db.close();
    }

    /**
     * Returns a list of all registered users in the database.
     * @return list of all registered users
     */
    public List<User> getAllUser() {
        // Array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PROVINCE,
                COLUMN_USER_BIRTHDAY,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_FIRST
        };

        // Sorting orders
        String sortOrder = COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the user table
        Cursor cursor = db.query(TABLE_USER, // Table to query
                                 columns,    // Columns to return
                                 null,       // Columns for the WHERE clause
                                 null,       // The values for the WHERE clause
                                 null,       // Group the rows
                                 null,       // Filter by row groups
                                 sortOrder); // The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setIdentification(cursor.getString(
                                       cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
                user.setProvince(cursor.getString(
                                 cursor.getColumnIndexOrThrow(COLUMN_USER_PROVINCE)));
                user.setBirthday(cursor.getString(
                                 cursor.getColumnIndexOrThrow(COLUMN_USER_BIRTHDAY)));
                user.setPassword(cursor.getString(
                                 cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)));
                user.setFirstTime(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_FIRST)));

                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Return user list
        return userList;
    }

    /**
     * Returns a list of all registered products in the database.
     * @return list of all registered products
     */
    public List<Product> getAllProducts() {
        // Array of columns to fetch
        String[] columns = {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_TITLE,
                COLUMN_PRODUCT_DESCRIPTION,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_DISCOUNT_PERCENTAGE,
                COLUMN_PRODUCT_STOCK,
                COLUMN_PRODUCT_BRAND,
                COLUMN_PRODUCT_CATEGORY,
                COLUMN_PRODUCT_THUMBNAIL,
                COLUMN_PRODUCT_IMAGE
        };

        // Sorting orders
        String sortOrder = COLUMN_PRODUCT_ID + " ASC";
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the user table
        Cursor cursor = db.query(TABLE_PRODUCTS, // Table to query
                columns,    // Columns to return
                null,       // Columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,       // Group the rows
                null,       // Filter by row groups
                sortOrder); // The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                product.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_TITLE)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESCRIPTION)));
                product.setPrice(cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE)));
                product.setDiscountPercentage(cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DISCOUNT_PERCENTAGE)));
                product.setStock(cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_STOCK)));
                product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_BRAND)));
                product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_CATEGORY)));
                product.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_THUMBNAIL)));
                product.setImages(Collections.singletonList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE))));
                // Adding product record to list
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Return product list
        return productList;
    }

    /**
     * Checks if an user is logging in for the first time.
     * @param email The e-mail of the user that logs in
     * @return true if the user logs for the first time; false otherwise
     */
    public boolean isFirstTime(String email) {
        boolean firstTime = false;
        int value = -1;

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                COLUMN_USER_FIRST
        };
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USER,        // Table to query
                                 columns,           // Columns to return
                                 selection,         // Columns for the WHERE clause
                                 selectionArgs,     // The values for the WHERE clause
                                 null,              // Group the rows
                                 null,              // Filter by row groups
                                 null);             // The sort order

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

    /**
     * Checks if a given e-mail matches with a given password.
     * @param email     The e-mail of an user
     * @param password  The password of an user
     * @return true if the parameters match; false otherwise
     */
    public boolean checkUser(String email, String password) throws Exception {
        boolean success = false;
        EncryptPassword encryptPassword = new EncryptPassword();
        password = encryptPassword.encryptPassword(password);

        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = { email, password };
        Cursor cursor = db.query(TABLE_USER,        // Table to query
                                 columns,           // Columns to return
                                 selection,         // Columns for the WHERE clause
                                 selectionArgs,     // The values for the WHERE clause
                                 null,              // Group the rows
                                 null,              // Filter by row groups
                                 null);             // The sort order

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            success = true;
        }

        return success;
    }

}

