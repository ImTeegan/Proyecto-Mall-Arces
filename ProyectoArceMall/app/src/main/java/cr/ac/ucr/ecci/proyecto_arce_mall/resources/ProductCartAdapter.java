package cr.ac.ucr.ecci.proyecto_arce_mall.resources;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.ProductActivity;
import cr.ac.ucr.ecci.proyecto_arce_mall.R;

public class ProductCartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private Context context;
    List<Product> products;

    public ProductCartAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.productTotal = Integer.parseInt(products.get(position).getPrice());
        holder.productName.setText(products.get(position).getTitle());
        holder.productPrice.setText("Precio: $" + holder.productTotal);
        Picasso.get().load(products.get(position).getImgid()).into(holder.productImage);

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.productQuant++;
                holder.productTotal += Integer.parseInt(products.get(position).getPrice());
                holder.productPrice.setText("Precio: $" + holder.productTotal);
                holder.quantity.setText(holder.productQuant +"");
            }
        });
        holder.lessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.productQuant>0) {
                    holder.productTotal -= Integer.parseInt(products.get(position).getPrice());
                    holder.productPrice.setText("Precio: $" + holder.productTotal);
                    holder.productQuant--;
                }
                holder.quantity.setText(holder.productQuant + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}
