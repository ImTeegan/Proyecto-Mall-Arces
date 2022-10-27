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
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.User;

public class ProductAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context Context;
    private  List<Product> Products;
    private DbHelper dataBase;
    private User userActive;
    public ProductAdapter(Context context, List<Product> products) {
        this.Context = context;
        this.Products = products;
        dataBase = new DbHelper(Context);
        userActive = dataBase.getLoginUser();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(Context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.productName.setText(Products.get(position).getTitle());
        holder.productPrice.setText("Precio: $" + Products.get(position).getPrice());
        Picasso.get().load(Products.get(position).getImgid()).into(holder.productImage);
        holder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Context, ProductActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ID",Products.get(position).getId());
                Context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Products.size();
    }

    public void filterList(ArrayList<Product> filterlist) {
        Products = filterlist;
        notifyDataSetChanged();
    }
}
