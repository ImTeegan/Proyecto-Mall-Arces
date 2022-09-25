package cr.ac.ucr.ecci.proyecto_arce_mall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

import cr.ac.ucr.ecci.proyecto_arce_mall.resources.ProductAdapter;
import cr.ac.ucr.ecci.proyecto_arce_mall.resources.Product;

public class StoreActivity extends AppCompatActivity {

    GridView coursesGV;

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
}