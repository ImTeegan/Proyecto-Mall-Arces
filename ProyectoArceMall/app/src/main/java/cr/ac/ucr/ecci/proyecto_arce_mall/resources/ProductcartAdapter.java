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

public class ProductcartAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    List<Product> products;

    public ProductcartAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.productName.setText(products.get(position).getTitle());
        holder.productPrice.setText("Precio: $" + products.get(position).getPrice());
        Picasso.get().load(products.get(position).getImgid()).into(holder.productImage);
        holder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ID",products.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void filterList(ArrayList<Product> filterlist) {
        products = filterlist;
        notifyDataSetChanged();
    }
}
