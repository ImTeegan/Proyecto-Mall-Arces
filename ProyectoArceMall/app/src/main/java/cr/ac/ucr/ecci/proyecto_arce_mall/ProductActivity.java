package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Product;
import cr.ac.ucr.ecci.proyecto_arce_mall.utility.NetworkChangeListener;

public class ProductActivity extends AppCompatActivity {

    private final String storeAPI = "https://dummyjson.com/products/";
    private int productQuant = 1;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private TextView name;
    private TextView price;
    private TextView description;
    private Button addButton;
    private Button lessButton;
    private TextView quantity;
    private TextView category;
    private ImageCarousel carousel;
    private BottomNavigationView bottomNavigationView;
    private List<CarouselItem> carouselList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        instantiateComponents();

        buildProductView();

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.navigation_user:
                        startActivity(new Intent(getApplicationContext(),UserActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(),StoreActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_cart:
                        startActivity(new Intent(getApplicationContext(),CartActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void instantiateComponents() {
        name = findViewById(R.id.product_name);
        price = findViewById(R.id.product_price);
        description = findViewById(R.id.product_descrption);
        addButton = findViewById(R.id.add_button);
        lessButton = findViewById(R.id.less_button);
        quantity = findViewById(R.id.quantity_number);
        category = findViewById(R.id.product_category);
        quantity.setText(productQuant + "");
        carousel = findViewById(R.id.carousel);
        carousel.registerLifecycle(getLifecycle());
        carouselList = new ArrayList<>();
    }

    private void buildProductView(){
        Bundle ID = getIntent().getExtras();
        int id = ID.getInt("ID");
        StringRequest myRequest = new StringRequest(Request.Method.GET,
                storeAPI + id,
                response -> {
                    try {
                        JSONObject myJsonObject = new JSONObject(response.toString());
                        Gson gson = new Gson();
                        Product product = gson.fromJson(String.valueOf(myJsonObject), Product.class);
                        name.setText(product.getTitle());
                        price.setText("$"+product.getPrice());
                        description.setText(product.getDescription());
                        category.setText("Categoría: " + product.getCategory());
                        List<String> images = product.getImages();
                        for(String image : images){
                            carouselList.add(new CarouselItem(image));
                        }
                        carousel.addData(carouselList);
                        //TODO: Put this methods in a separate method
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addProduct();
                                quantity.setText(productQuant +"");
                            }
                        });
                        lessButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                lessProduct();
                                quantity.setText(productQuant + "");
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                },
                volleyError -> Toast.makeText(this,
                        volleyError.getMessage(), Toast.LENGTH_SHORT).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myRequest);
    }

    private void addProduct(){
        productQuant++;
    }

    private void lessProduct(){
        if (productQuant>0) {
            productQuant--;
        }
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(this.networkChangeListener,intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(this.networkChangeListener);
        super.onStop();
    }
}