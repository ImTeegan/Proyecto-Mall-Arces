package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

import cr.ac.ucr.ecci.proyecto_arce_mall.resources.ProductAdapter;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Product;
import cr.ac.ucr.ecci.proyecto_arce_mall.utility.NetworkChangeListener;

public class StoreActivity extends AppCompatActivity {

    GridView coursesGV;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        coursesGV = findViewById(R.id.grid);
        ArrayList<Product> courseModelArrayList = new ArrayList<Product>();

        courseModelArrayList.add(new Product("DSA", "500", R.drawable.test));
        courseModelArrayList.add(new Product("JAVA", "500", R.drawable.test));
        courseModelArrayList.add(new Product("C++", "500", R.drawable.test));
        courseModelArrayList.add(new Product("Python", "500", R.drawable.test));
        courseModelArrayList.add(new Product("Javascript", "500", R.drawable.test));
        courseModelArrayList.add(new Product("DSA", "500", R.drawable.test));

        ProductAdapter adapter = new ProductAdapter(this, courseModelArrayList);
        coursesGV.setAdapter(adapter);
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