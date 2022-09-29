package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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
import cr.ac.ucr.ecci.proyecto_arce_mall.utility.NetworkChangeListener;

public class StoreActivity extends AppCompatActivity {

    private final String storeAPI = "https://dummyjson.com/products";

    private List<Product> productsArrayList;

    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        buildRecycleView();
        EditText editText = findViewById(R.id.search_bar);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

    }

    private void buildRecycleView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        productsArrayList = new ArrayList<Product>();
        StringRequest myRequest = new StringRequest(Request.Method.GET,
                storeAPI,
                response -> {
                    try{
                        JSONObject myJsonObject = new JSONObject(response.toString());
                        JSONArray arr = myJsonObject.getJSONArray("products");
                        Gson gson = new Gson();
                        Product[] userArray = gson.fromJson(String.valueOf(arr), Product[].class);
                        Collections.addAll(productsArrayList, userArray);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        adapter = new ProductAdapter(getApplicationContext(), productsArrayList);
                        recyclerView.setAdapter(adapter);
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

    private void filter(String text) {
        ArrayList<Product> filteredList = new ArrayList<Product>();

        for (Product item : productsArrayList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
}