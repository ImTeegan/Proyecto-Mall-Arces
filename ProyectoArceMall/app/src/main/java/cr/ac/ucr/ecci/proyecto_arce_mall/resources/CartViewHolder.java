package cr.ac.ucr.ecci.proyecto_arce_mall.resources;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cr.ac.ucr.ecci.proyecto_arce_mall.R;

public class CartViewHolder extends RecyclerView.ViewHolder {
    TextView productName;
    TextView productPrice;
    ImageView productImage;
    Button addButton;
    Button lessButton;
    int productQuant;
    TextView quantity;
    int productTotal;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        productName = itemView.findViewById(R.id.cartproductName);
        productPrice = itemView.findViewById(R.id.cartproductPrice);
        productImage = itemView.findViewById(R.id.productImage);
        addButton = itemView.findViewById(R.id.add_button);
        lessButton = itemView.findViewById(R.id.less_button);
        quantity = itemView.findViewById(R.id.quantity_number);
    }
}
