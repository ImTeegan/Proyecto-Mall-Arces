package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Product;
import cr.ac.ucr.ecci.proyecto_arce_mall.utility.NetworkChangeListener;

public class ProductActivity extends AppCompatActivity {

    private final String storeAPI = "https://dummyjson.com/products/";
    private int productQuant = 1;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        TextView name = findViewById(R.id.nameOFproduct);
        TextView price = findViewById(R.id.priceOfProduct);
        TextView Description = findViewById(R.id.productDescription);
        ImageView Image = findViewById(R.id.productIcon);
        Button More = findViewById(R.id.More);
        Button less = findViewById(R.id.Less);
        TextView Number = findViewById(R.id.QuantityProducts);
        Number.setText(productQuant + "");

        // Button buy = findViewById(R.id.buy_Button);

        Bundle ID = getIntent().getExtras();
        int id = ID.getInt("ID");
        StringRequest myRequest = new StringRequest(Request.Method.GET,
                storeAPI + id,
                response -> {
                    try {
                        JSONObject myJsonObject = new JSONObject(response.toString());
                        Gson gson = new Gson();
                        Product products = gson.fromJson(String.valueOf(myJsonObject), Product.class);
                        name.setText(products.getTitle());
                        price.setText("$"+products.getPrice());
                        Description.setText(products.getDescription());
                        String [] images = products.getImages();
                        URL url = new URL(images[0]);
                        Glide.with(this).load(url).into(Image);
                        More.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MoreProduct();
                                Number.setText(productQuant +"");
                            }
                        });
                        less.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LessProduct();
                                Number.setText(productQuant + "");
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> Toast.makeText(this,
                        volleyError.getMessage(), Toast.LENGTH_SHORT).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myRequest);
    }
    public void MoreProduct(){
        productQuant++;
    }
    public void LessProduct(){
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