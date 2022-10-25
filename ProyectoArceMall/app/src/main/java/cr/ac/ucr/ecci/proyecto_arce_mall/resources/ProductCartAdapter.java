package cr.ac.ucr.ecci.proyecto_arce_mall.resources;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cr.ac.ucr.ecci.proyecto_arce_mall.R;
import cr.ac.ucr.ecci.proyecto_arce_mall.data.model.DbHelper;

public class ProductCartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private Context context;
    List<Product> products;
    private DbHelper database;

    public ProductCartAdapter(Context context, List<Product> products,  DbHelper database) {
        this.context = context;
        this.products = products;
        this.database = database;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.productTotal = Integer.parseInt(products.get(position).getTotalPrice());
        holder.productName.setText(products.get(position).getTitle());
        holder.productPrice.setText("Precio: $" + holder.productTotal);
        Picasso.get().load(products.get(position).getImgid()).into(holder.productImage);
        holder.productQuant = products.get(position).getQuantity();
        holder.quantity.setText(holder.productQuant +"");
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.productQuant++;
                holder.productTotal += Integer.parseInt(products.get(position).getPrice());
                holder.productPrice.setText("Precio: $" + holder.productTotal);
                holder.quantity.setText(holder.productQuant +"");
                updateProductCart(products.get(position),holder.productTotal, holder.productQuant);
            }
        });
        holder.lessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.productQuant == 1){
                    deleteProductCart(products.get(position), position);
                }
                if (holder.productQuant>1) {
                    holder.productTotal -= Integer.parseInt(products.get(position).getPrice());
                    holder.productPrice.setText("Precio: $" + holder.productTotal);
                    holder.productQuant--;
                    holder.quantity.setText(holder.productQuant + "");
                    updateProductCart(products.get(position),holder.productTotal, holder.productQuant);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    /**
     * Updates the new quantity and price of the product in the database
     * @param product The product to be updated
     * @param price The new price of the product
     * @param quantity The new quantity of the product
     */
    private void updateProductCart(Product product, int price, int quantity){
        Product updatedProduct = product;
        updatedProduct.setQuantity(quantity);
        database.updateProductCart(updatedProduct, price);
    }

    /**
     * Deletes the product of the cart and database
     * @param product The product to be deleted
     * @param position The position of the product in the array
     */
    private void deleteProductCart(Product product, int position){
        database.deleteProductCart(product);
        products.remove(position);
        notifyItemRemoved(position);
    }
}
