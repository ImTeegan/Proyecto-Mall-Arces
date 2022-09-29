package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.resources.ProductAdapter;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Product;

public class StoreActivity extends AppCompatActivity {

    GridView productsGrid;
    private final String storeAPI = "https://dummyjson.com/products";

    private ArrayAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        productsGrid = findViewById(R.id.grid);

        TextInputEditText filter = (TextInputEditText) findViewById(R.id.identification_field);
        //EditText filter = (EditText) findViewById(R.id.searchField);

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

        List<Product> searchList = new ArrayList<Product>(productsArrayList);

        adapter1 = new ArrayAdapter(this,R.layout.list_item, productsArrayList);
        productsGrid.setAdapter(adapter1);


       filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (StoreActivity.this).adapter1.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


}