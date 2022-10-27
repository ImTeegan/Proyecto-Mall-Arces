package cr.ac.ucr.ecci.proyecto_arce_mall.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.EncryptPassword;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Product;

public class DbHelper extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "Users.db";

    // Users table name
    private static final String TABLE_USER = "User";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "ID";
    private static final String COLUMN_USER_NAME = "Name";
    private static final String COLUMN_USER_EMAIL = "Email";
    private static final String COLUMN_USER_PASSWORD = "Password";
    private static final String COLUMN_USER_PROVINCE = "Province";
    private static final String COLUMN_USER_BIRTHDAY = "Birthday"; //FORMAT YYY MM DD ISO 8601,
    private static final String COLUMN_USER_FIRST = "FirstTime";
    private static final String COLUMN_USER_LOGIN = "Login";
    private static final String COLUMN_USER_IMAGE = "Image";

    // Cart table name
    private static final String TABLE_CART = "Cart";

    // Cart Table Columns names
    private static final String COLUMN_CART_ID = "ID";
    private static final String COLUMN_CART_NAME = "Name";
    private static final String COLUMN_CART_PRICE = "Price";
    private static final String COLUMN_CART_PRICE_TOTAL = "TotalPrice";
    private static final String COLUMN_CART_QUANTITY = "Quantity";
    private static final String COLUMN_CART_IMAGE = "Image";
    private static final String COLUMN_CART_ID_USER = "UserId";

    // Create table sql query
    private final String  CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " TEXT PRIMARY KEY ,"
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_EMAIL + " TEXT, "
            + COLUMN_USER_PROVINCE + " TEXT, "
            + COLUMN_USER_BIRTHDAY + " TEXT, "
            + COLUMN_USER_PASSWORD + " TEXT, "
            + COLUMN_USER_FIRST + " INTEGER, "
            + COLUMN_USER_LOGIN + " INTEGER, "
            + COLUMN_USER_IMAGE + " BLOB "
            + ")";

    private final String  CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
            + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CART_ID_USER + " TEXT, "
            + COLUMN_CART_NAME + " TEXT , "
            + COLUMN_CART_PRICE + " INT, "
            + COLUMN_CART_PRICE_TOTAL + " INT, "
            + COLUMN_CART_QUANTITY + " INT, "
            + COLUMN_CART_IMAGE + ")";

    // Drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    // Drop table sql query
    private String DROP_CART_TABLE = "DROP TABLE IF EXISTS " + TABLE_CART;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        //Drop Cart Table if exist
        db.execSQL(DROP_CART_TABLE);
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
     * Creates a product to  and adds it to the database for shopping cart.
     * @param product  The new product to add
     */
    public String addProduct(Product product , int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_NAME,product.getTitle());
        values.put(COLUMN_CART_ID_USER, getLoginUser().getIdentification());
        values.put(COLUMN_CART_ID, product.getId());
        values.put(COLUMN_CART_PRICE,product.getPrice());
        values.put(COLUMN_CART_PRICE_TOTAL,product.getTotalPrice());
        values.put(COLUMN_CART_QUANTITY, quantity);
        values.put(COLUMN_CART_IMAGE,product.getImages().get(0));
        // Inserting Row
        db = this.getWritableDatabase();
        Log.i("DATA BASE ",db.insert(TABLE_CART, null, values) + "");
        db = getWritableDatabase();

        db.close();
        return "TRUE";
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
        values.put(COLUMN_USER_LOGIN, user.getLogin());
        values.put(COLUMN_USER_IMAGE, user.getImage());

        // Updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getIdentification())});
        db.close();
    }

    /**
     * Updates a product in the database.
     * @param product The product to update
     */
    public void updateProductCart(Product product, int totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_CART_PRICE_TOTAL, totalPrice);
        values.put(COLUMN_CART_QUANTITY, product.getQuantity());

        // Updating row
        db.update(TABLE_CART, values, COLUMN_CART_ID + " = ?",
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
     * Update the user logged from the database.
     */
    public void deleteUserLogged() {
        SQLiteDatabase db = this.getWritableDatabase();
        User user = this.getLoginUser();
        user.setLogin(0);
        updateUser(user);
        db.close();
    }

    /**
     * Deletes an product from the database.
     * @param product The product to delete
     */
    public void deleteProductCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete product record by id.
        db.delete(TABLE_CART, COLUMN_CART_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
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
                COLUMN_USER_FIRST,
                COLUMN_USER_LOGIN,
                COLUMN_USER_IMAGE,

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
                user.setLogin(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_LOGIN)));

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
     * Returns a list of the products in the cart in the database.
     * @return list of the products in the cart
     */
    public List<Product> getProductsCart() {
        // Array of columns to fetch
        String[] columns = {
                COLUMN_CART_ID,
                COLUMN_CART_NAME,
                COLUMN_CART_PRICE,
                COLUMN_CART_PRICE_TOTAL,
                COLUMN_CART_QUANTITY,
                COLUMN_CART_IMAGE,

        };

        // Sorting orders
        String sortOrder = COLUMN_CART_NAME + " ASC";
        List<Product> productList = new ArrayList<Product>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Query the product table
        Cursor cursor = db.query(TABLE_CART, // Table to query
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
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID)));
                product.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_NAME)));
                product.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_PRICE)));
                product.setTotalPrice(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_PRICE_TOTAL)));
                product.setQuantity(cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY)));
                product.setImgid(cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_CART_IMAGE)));

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

    /**
     * Checks if a user is registered by a given e-mail.
     * @param email The e-mail of an user
     * @return true if the user is registered; false otherwise
     */
    public boolean userIsRegistered(String email) {
        boolean registered = false;

        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };
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
            registered = true;
        }

        return registered;
    }

    /**
     * Gets the user by the id in the parameter
     * @param userId The id of the user
     * @return The user that has the id sent in the parameter
     */
    public User getUser(int userId) {
        // Array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PROVINCE,
                COLUMN_USER_BIRTHDAY,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_FIRST,
                COLUMN_USER_LOGIN,
                COLUMN_USER_IMAGE
        };

        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();
        // Query the user table
        Cursor cursor = db.query(TABLE_USER, // Table to query
                columns,    // Columns to return
                selection,       // Columns for the WHERE clause
                selectionArgs,       // The values for the WHERE clause
                null,       // Group the rows
                null,       // Filter by row groups
                null); // The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
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
                user.setLogin(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_LOGIN)));
                user.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_USER_IMAGE)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Return user list
        return user;
    }

    /**
     * Gets the user by a given e-mail
     * @param email The e-mail of the user
     * @return The user information
     */
    public User getUserByEmail(String email) {
        // Array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PROVINCE,
                COLUMN_USER_BIRTHDAY,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_FIRST,
                COLUMN_USER_LOGIN,
                COLUMN_USER_IMAGE
        };

        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };
        SQLiteDatabase db = this.getReadableDatabase();

        User user = new User();
        // Query the user table
        Cursor cursor = db.query(TABLE_USER,    // Table to query
                                 columns,       // Columns to return
                                 selection,     // Columns for the WHERE clause
                                 selectionArgs, // The values for the WHERE clause
                                 null,          // Group the rows
                                 null,          // Filter by row groups
                                 null);         // The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
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
                user.setLogin(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_LOGIN)));
                user.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_USER_IMAGE)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Return user list
        return user;
    }

    /**
     * Gets the user that is currently login into the app
     * @return the user information
     */
    public User getLoginUser() {
        // Array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PROVINCE,
                COLUMN_USER_BIRTHDAY,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_FIRST,
                COLUMN_USER_LOGIN,
                COLUMN_USER_IMAGE
        };

        String selection = COLUMN_USER_LOGIN + " = ?";
        String[] selectionArgs = {String.valueOf(1)};
        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();
        // Query the user table
        Cursor cursor = db.query(TABLE_USER, // Table to query
                columns,    // Columns to return
                selection,       // Columns for the WHERE clause
                selectionArgs,       // The values for the WHERE clause
                null,       // Group the rows
                null,       // Filter by row groups
                null); // The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
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
                user.setLogin(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_LOGIN)));
                user.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_USER_IMAGE)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Return user list
        return user;
    }

}

