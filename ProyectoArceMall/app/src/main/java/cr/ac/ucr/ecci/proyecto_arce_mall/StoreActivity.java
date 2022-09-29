package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.resources.ProductAdapter;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Product;

public class StoreActivity extends AppCompatActivity {

    GridView productsGrid;
    private final String storeAPI = "https://dummyjson.com/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        productsGrid = findViewById(R.id.grid);

        List<Product> productsArrayList = new ArrayList<Product>();
        StringRequest myRequest = new StringRequest(Request.Method.GET,
                storeAPI,
                response -> {
                    try{
                        JSONObject myJsonObject = new JSONObject(response.toString());
                        JSONArray arr = myJsonObject.getJSONArray("products");
                        Gson gson = new Gson();
                        Product[] userArray = gson.fromJson(String.valueOf(arr), Product[].class);
                        Collections.addAll(productsArrayList, userArray);
                        ProductAdapter adapter = new ProductAdapter(this, productsArrayList);
                        productsGrid.setAdapter(adapter);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> Toast.makeText(this,
                        volleyError.getMessage(), Toast.LENGTH_SHORT).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myRequest);
    }


}