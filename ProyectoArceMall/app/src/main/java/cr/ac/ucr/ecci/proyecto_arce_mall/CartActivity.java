package cr.ac.ucr.ecci.proyecto_arce_mall;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;
import cr.ac.ucr.ecci.proyecto_arce_mall.mail.JavaMailApi;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Product;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.ProductCartAdapter;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.PurchaseHistory;

public class CartActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageView userIcon;
    private TextView userName;
    private DbHelper dataBase;
    private User activeUser;
    private ProductCartAdapter Adapter;
    private List<Product> cartProducts;
    private TextView totalPrice;
    private Button payButton;
    private String Email;
    private Button cancelButton;
    private CollectionReference usersCollection;
    private StorageReference storageReference;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.dataBase = new DbHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initiateComponents();
        setComponentActions();
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_cart);

        buildRecycleView();
        getTotalPurchasePrice();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.navigation_user:
                        startActivity(new Intent(getApplicationContext(),UserActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_cart:
                        return true;
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(),StoreActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Initiate all components of the activity
     */
    private void initiateComponents(){
        activeUser = dataBase.getLoginUser();
        userName = findViewById(R.id.user_name);
        userIcon = findViewById(R.id.user_image_cart);
        userName.setText(activeUser.getName());
        payButton = findViewById(R.id.pay_button);
        cancelButton = findViewById(R.id.cancel_button);
        totalPrice = findViewById(R.id.total_price);
        Bitmap bitmap = BitmapFactory.decodeByteArray(activeUser.getImage(), 0, activeUser.getImage().length);
        userIcon.setImageBitmap(bitmap);
        Email = activeUser.getEmail();
    }

    /**
     * Sets the action for the button to pay for the products
     */
    private void setComponentActions() {
        this.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartProducts.size() > 0){
                    savePurchase();
                    sendOrderDetailEmail();
                    cleanStoreCart();
                }
            }
        });
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanStoreCart();
            }
        });
    }

    /**
     * Saves the purchase to the database
     */
    private void savePurchase() {
        PurchaseHistory purchaseHistory = new PurchaseHistory();
        purchaseHistory.setUserId(this.activeUser.getIdentification());
        purchaseHistory.setTotalPrice(getTotalPurchasePrice());
        purchaseHistory.setItemsId(this.cartProducts.toString());
        this.dataBase.addPurchase(purchaseHistory);
    }

    /**
     * Clean the products cart
     */
    private void cleanStoreCart(){
        if(this.cartProducts.size() > 0){
            this.dataBase.deleteAllProductCart(activeUser.getIdentification());
            this.dataBase.deleteAll();
            buildRecycleView();
            getTotalPurchasePrice();
        }
    }

    private void sendOrderDetailEmail(){
        final String subject = "Tienda Arce - Detalles de la orden";
        int sum =this.cartProducts.stream().map(Product::getTotalPriceValue).reduce(0, Integer::sum);

        String message = "<body style=\"margin:0;padding:0;\">\n" +
                "    <table role=\"presentation\" style=\"width:100%;border-collapse:collapse;border:0;border-spacing:0;background:#ffffff;\">\n" +
                "        <tr>\n" +
                "            <td align=\"center\" style=\"padding:0;\">\n" +
                "                <h1><font color=#90AACB> Hemos recibido tu pedido</font></h1>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td align=\"center\" style=\"padding:0;\">\n" +
                "                <h2><font color=#90AACB>Gracias por comprar con nosotros </font></h2>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td align=\"center\" style=\"padding:0;\">\n" +
                "                <h3> A continuación puede ver el detalle de la compra: </font></h3>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>";

        for (int index = 0; index<cartProducts.size();++index){
            message += "<body style=\"margin:0;padding:0;\">\n" +
                    "    <table role=\"presentation\" style=\"width:100%;border-collapse:collapse;border:0;border-spacing:0;background:#ffffff;\">\n" +
                    "        <tr>\n" +
                    "            <td align=\"center\" style=\"padding:0;\">\n" +
                    "                <img src=\"" +this.cartProducts.get(index).getImgid() + " \" width=\"120\" height=\"80\">\n" +
                    "            </td>\n" +
                    "            <td align=\"center\" style=\"padding:0;\">\n" +
                    //"               <h4> Nombre del producto: </h4>\n" +
                     this.cartProducts.get(index).getTitle() +
                    "            </td>\n" +
                    "            <td align=\"center\" style=\"padding:0;\">\n" +
                    //"               <h4> Precio del producto: </h4>\n" +
                    "            </td>\n" +"$"+ this.cartProducts.get(index).getPrice() +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "</body>";
        }

        message += "<body style=\"margin:0;padding:0;\">\n" +
                "    <table role=\"presentation\" style=\"width:100%;border-collapse:collapse;border:0;border-spacing:0;background:#ffffff;\">\n" +
                "        <tr>\n" +
                "            <td align=\"center\" style=\"padding:0;\">\n" +
                "                <h2><font color=#90AACB> Precio total: $" + sum +" </font></h2>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td align=\"center\" style=\"padding:0;\">\n" +
                "                <h3><font color=#90AACB> Esperamos su visita pronto. </font></h3>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>";

        JavaMailApi javaMailApi = new JavaMailApi(this, Email, message, subject);
        javaMailApi.execute();
    }

    /**
     * Creates the Recycler view with the products list
     */
    private void buildRecycleView(){
        //cartProducts = this.dataBase.getProductsCart();
        cartProducts = this.dataBase.getProductsCart();
        RecyclerView recycler = findViewById(R.id.recyclerCart);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(gridLayoutManager);
        this.dataBase = new DbHelper(this);
        Adapter = new ProductCartAdapter(getApplicationContext(), this.cartProducts, this.dataBase);
        recycler.setAdapter(Adapter);

      /*  cartProducts = new ArrayList<>();
        this.fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        CollectionReference cartCollection = dataBase.collection("Cart");
        Log.d("si ya esta el productos", "si entra al getproductscart");
        dataBase.collection("Cart")
                .whereEqualTo("UID", this.fAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Product product = new Product();
                                Log.d("si ya esta el productos", "desde recycle Si entra al for de getproductcart");
                                Log.d("si ya esta el productos", document.get("name").toString());
                                product.setId(Integer.parseInt(document.get("productID").toString()));
                                Log.d("si ya esta el productos", (document.get("productID").toString()));
                                product.setTitle(document.get("name").toString());
                                product.setPrice(Integer.parseInt(document.get("price").toString()));
                                Log.d("si ya esta el productos", document.get("price").toString());
                                product.setTotalPrice(Integer.parseInt(document.get("totalPrice").toString()));
                                Log.d("si ya esta el productos", document.get("totalPrice").toString());

                                product.setQuantity(Integer.parseInt(document.get("quantity").toString()));
                                Log.d("si ya esta el productos", document.get("quantity").toString());
                                product.setStock(Integer.parseInt(document.get("stock").toString()));
                                Log.d("si ya esta el productos", document.get("stock").toString());
                                cartProducts.add(product);
                            }
                        }
                    }
                });
        RecyclerView recycler = findViewById(R.id.recyclerCart);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(gridLayoutManager);
        this.dataBase = new DbHelper(this);
        Adapter = new ProductCartAdapter(getApplicationContext(), this.cartProducts, this.dataBase);
        recycler.setAdapter(Adapter);*/
    }

    /**
     * Calculates the total price of the order
     */
    private int getTotalPurchasePrice(){
        int Sum =this.cartProducts.stream().map(Product::getTotalPriceValue).reduce(0, Integer::sum);
        totalPrice.setText("Precio total: $" + Sum);
        return Sum;
    }
}