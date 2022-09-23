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
import java.util.ArrayList;

import cr.ac.ucr.ecci.proyecto_arce_mall.R;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(@NonNull Context context, ArrayList<Product> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Product courseModel = getItem(position);
        TextView productName = listitemView.findViewById(R.id.productName);
        TextView productPrice = listitemView.findViewById(R.id.productPrice);
        ImageView productImage = listitemView.findViewById(R.id.productImage);

        productName.setText(courseModel.getproductName());
        productPrice.setText(courseModel.getproductPrice());
        productImage.setImageResource(courseModel.getImgid());
        return listitemView;
    }
}
