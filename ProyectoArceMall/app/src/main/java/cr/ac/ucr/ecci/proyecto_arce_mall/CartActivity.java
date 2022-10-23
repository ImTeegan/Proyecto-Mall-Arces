package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Product;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.ProductAdapter;

public class CartActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView recycler;
    private ImageView userIcon;
    private TextView userName;
    private ArrayList<Product> productList;
    private DbHelper database;
    private User activeUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = new DbHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initiateComponents();
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_cart);

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
        activeUser = database.getLoginUser();
        userName = findViewById(R.id.userName);
        userIcon = findViewById(R.id.userImageC);
        userName.setText(activeUser.getName());
        Bitmap bitmap = BitmapFactory.decodeByteArray(activeUser.getImage(), 0, activeUser.getImage().length);
        userIcon.setImageBitmap(bitmap);
    }

    /**
     * Calls the API with for the products information and creates
     * the Recycler view
     */
    private void buildRecycleView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerCart);
        productList = new ArrayList<Product>();


    }


}