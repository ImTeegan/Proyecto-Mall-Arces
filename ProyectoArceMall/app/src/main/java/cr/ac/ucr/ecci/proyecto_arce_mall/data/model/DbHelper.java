package cr.ac.ucr.ecci.proyecto_arce_mall.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Product;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.PurchaseHistory;

public class DbHelper extends SQLiteOpenHelper {

    //Variables to use FireBase as Database provider
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;



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
    private static final String COLUMN_CART_STOCK = "Stock";
    private static final String COLUMN_CART_PRICE_TOTAL = "TotalPrice";
    private static final String COLUMN_CART_QUANTITY = "Quantity";
    private static final String COLUMN_CART_IMAGE = "Image";
    private static final String COLUMN_CART_ID_USER = "UserId";

    // Cart table name
    private static final String TABLE_PURCHASE_HISTORY = "PurchaseHistory";

    // History purchase table columns names
    private static final String COLUMN_PURCHASE_ID = "ID";
    private static final String COLUMN_PURCHASE_ITEMS_ID = "ItemsId";
    private static final String COLUMN_PURCHASE_TOTAL_PRICE = "TotalPrice";
    private static final String COLUMN_PURCHASE_ID_USER = "UserId";

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
            + COLUMN_CART_STOCK + " INT, "
            + COLUMN_CART_IMAGE + ")";

    private final String  CREATE_TABLE_PURCHASE_HISTORY = "CREATE TABLE " + TABLE_PURCHASE_HISTORY + "("
            + COLUMN_PURCHASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PURCHASE_ID_USER + " TEXT, "
            + COLUMN_PURCHASE_ITEMS_ID + " TEXT, "
            + COLUMN_PURCHASE_TOTAL_PRICE + " INT " + ")";

    // Drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    // Drop table sql query
    private String DROP_CART_TABLE = "DROP TABLE IF EXISTS " + TABLE_CART;

    // Drop table sql query
    private String DROP_TABLE_PURCHASE_HISTORY = "DROP TABLE IF EXISTS " + TABLE_PURCHASE_HISTORY;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_TABLE_PURCHASE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        //Drop Cart Table if exist
        db.execSQL(DROP_CART_TABLE);
        //Drop Cart Table if exist
        db.execSQL(DROP_TABLE_PURCHASE_HISTORY);
        // Create tables again
        onCreate(db);
    }

    /**
     * Upload an image to firestore bucket of the app
     * @param image uri where image is store
     */
    public void uploadImage(Uri image){
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        StorageReference ref = storageReference.child("Users/" + fAuth.getCurrentUser().getUid());
        UploadTask uploadTask = ref.putFile(image);
    }

    /**
    * Store the new user on FireBase
    * @param : The New User
    * */
    public void addUserFb(User user,Uri image){
        //Firebase can´t serialize bitmap attribute of Use
        fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        CollectionReference userCollection = dataBase.collection("Users");
        fAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserDataHolder userFB = new UserDataHolder(user, FirebaseAuth.getInstance().getUid());
                            userCollection.document(userFB.getUid()).set(userFB);
                            uploadImage(image);
                        }else{
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }


    /**
     * Creates a product to  and adds it to the database for shopping cart.
     * @param product  The new product to add
     */
    /*public String addProduct(Product product , int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_NAME,product.getTitle());
        values.put(COLUMN_CART_ID_USER, getLoginUser().getIdentification());
        values.put(COLUMN_CART_ID, product.getId());
        values.put(COLUMN_CART_PRICE,product.getPrice());
        values.put(COLUMN_CART_PRICE_TOTAL,product.getTotalPrice());
        values.put(COLUMN_CART_QUANTITY, quantity);
        values.put(COLUMN_CART_STOCK, product.getStock());
        values.put(COLUMN_CART_IMAGE,product.getImages().get(0));
        // Inserting Row
        db = this.getWritableDatabase();
        Log.i("DATA BASE ",db.insert(TABLE_CART, null, values) + "");
        db = getWritableDatabase();

        db.close();
        return "TRUE";
    }*/

    public String addProduct (Product product, int quantity) {
        fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        CollectionReference cartCollection = dataBase.collection("Cart");


        return "TRUE";
    }

    /**
     * Creates a purchase history and adds it to the database for the purchase history.
     * @param purchaseHistory  The new product to add
     */
    public String addPurchase(PurchaseHistory purchaseHistory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PURCHASE_ID_USER, purchaseHistory.getUserId());
        values.put(COLUMN_PURCHASE_TOTAL_PRICE, purchaseHistory.getTotalPrice());
        values.put(COLUMN_PURCHASE_ITEMS_ID,purchaseHistory.getItemsId());
        // Inserting Row
        Log.i("DATA BASE ",db.insert(TABLE_PURCHASE_HISTORY, null, values) + "");
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

        String userId = getLoginUser().getIdentification();

        // Updating row
        db.update(TABLE_CART, values, COLUMN_CART_ID + " = ? AND " + COLUMN_CART_ID_USER + " = ?",
                new String[]{String.valueOf(product.getId()), userId});
        db.close();
    }

    /*public void updateProductCart(Product product, int totalPrice){
        fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        CollectionReference cartCollection = dataBase.collection("Cart");
        int quantity = product.getQuantity();
        int idP = product.getId();
        String UID = fAuth.getUid();
        dataBase.collection("Cart")
                .whereEqualTo("UID", UID)
                .whereEqualTo("productID", idP)
                .whereEqualTo("quantity", quantity)


    }*/

    /**
     * Deletes an product from the database.
     * @param product The product to delete
     */
    public void deleteProductCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete product record by id.
        String userId = getLoginUser().getIdentification();
        db.delete(TABLE_CART, COLUMN_CART_ID + " = ? AND " + COLUMN_CART_ID_USER + " = ?",
                new String[]{String.valueOf(product.getId()), userId});
        db.close();
    }

    /**
     * Deletes all the products in the cart from the database.
     */
    public void deleteAllProductCart(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete product record by id.
        db.delete(TABLE_CART, COLUMN_CART_ID_USER + " = ?",
                new String[]{id});
        db.close();
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
                COLUMN_CART_STOCK,
                COLUMN_CART_IMAGE,

        };
        String userId = getLoginUser().getIdentification();
        // Sorting orders
        String sortOrder = COLUMN_CART_NAME + " ASC";
        List<Product> productList = new ArrayList<Product>();
        String selection = COLUMN_CART_ID_USER + " = ?";
        String[] selectionArgs = { userId };
        SQLiteDatabase db = this.getReadableDatabase();
        // Query the product table
        Cursor cursor = db.query(TABLE_CART, // Table to query
                columns,    // Columns to return
                selection,       // Columns for the WHERE clause
                selectionArgs,       // The values for the WHERE clause
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
                product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_STOCK)));
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

        // Return user list
        return user;
    }

}

