package cr.ac.ucr.ecci.proyecto_arce_mall.resources;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.ProductActivity;
import cr.ac.ucr.ecci.proyecto_arce_mall.R;
import cr.ac.ucr.ecci.proyecto_arce_mall.StoreActivity;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;

    public ProductAdapter(@NonNull Context context, List<Product> productList) {
        super(context, 0, productList);
        this.context =context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Product product = getItem(position);
        TextView productName = listItemView.findViewById(R.id.productName);
        TextView productPrice = listItemView.findViewById(R.id.productPrice);
        ImageView productImage = listItemView.findViewById(R.id.productImage);
        Button buyButton = listItemView.findViewById(R.id.buyButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("ID",product.getId());
                context.startActivity(intent);
            }
        });
        productName.setText(product.getTitle());
        productPrice.setText("Precio: $" + product.getPrice());
        Picasso.get().load(product.getImgid()).into(productImage);
        return listItemView;
    }

}
