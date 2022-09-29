package cr.ac.ucr.ecci.proyecto_arce_mall.resources;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cr.ac.ucr.ecci.proyecto_arce_mall.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView productName;
    TextView productPrice;
    ImageView productImage;
    Button buyButton;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        productName = itemView.findViewById(R.id.productName);
        productPrice = itemView.findViewById(R.id.productPrice);
        productImage = itemView.findViewById(R.id.productImage);
        buyButton = itemView.findViewById(R.id.buyButton);
    }
}
