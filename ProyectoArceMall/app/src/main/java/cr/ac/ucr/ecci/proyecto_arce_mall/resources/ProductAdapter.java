package cr.ac.ucr.ecci.proyecto_arce_mall.resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.R;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(@NonNull Context context, List<Product> productList) {
        super(context, 0, productList);
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

        productName.setText(product.getTitle());
        productPrice.setText("Precio: $" + product.getPrice());
        Picasso.get().load(product.getImgid()).into(productImage);
        return listItemView;
    }
}
