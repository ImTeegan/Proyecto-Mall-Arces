package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Product;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.ProductCartAdapter;

public class CartActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageView userIcon;
    private TextView userName;
    private DbHelper dataBase;
    private User activeUser;
    private ProductCartAdapter adapter;
    private List<Product> cartProducts;
    private TextView totalPrice;
    private Button payButton;

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
        totalPrice = findViewById(R.id.total_price);
        Bitmap bitmap = BitmapFactory.decodeByteArray(activeUser.getImage(), 0, activeUser.getImage().length);
        userIcon.setImageBitmap(bitmap);
    }

    /**
     * Sets the action for the button to pay for the products
     */
    private void setComponentActions() {
        this.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanStoreCart();
            }
        });
    }

    /**
     * Clean the products cart
     */
    private void cleanStoreCart(){
        if(this.cartProducts.size() > 0){
            this.dataBase.deleteAllProductCart(activeUser.getIdentification());
            buildRecycleView();
            getTotalPurchasePrice();
        }
    }

    /**
     * Creates the Recycler view with the products list
     */
    private void buildRecycleView(){
        cartProducts = this.dataBase.getProductsCart(this.activeUser.getIdentification());
        RecyclerView recycler = findViewById(R.id.recyclerCart);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(gridLayoutManager);
        this.dataBase = new DbHelper(this);
        adapter = new ProductCartAdapter(getApplicationContext(), this.cartProducts, this.dataBase);
        recycler.setAdapter(adapter);
    }

    /**
     * Calculates the total price of the order
     */
    private void getTotalPurchasePrice(){
        int sum =this.cartProducts.stream().map(Product::getTotalPriceValue).reduce(0, Integer::sum);
        totalPrice.setText("Precio total: $" + sum);
    }
}