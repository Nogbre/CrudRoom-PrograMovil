package com.example.crudroom;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudroom.R;
import com.example.crudroom.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private List<Product> products = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product currentProduct = products.get(position);
        holder.textViewName.setText(currentProduct.getName());
        holder.textViewPrice.setText("Price: $" + currentProduct.getPrice());
        holder.textViewQuantity.setText("Quantity: " + currentProduct.getQuantity());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewPrice;
        private TextView textViewQuantity;
        private Button buttonUpdate;
        private Button buttonDelete;

        public ProductHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            buttonUpdate = itemView.findViewById(R.id.button_update);
            buttonDelete = itemView.findViewById(R.id.button_delete);

            // Configurar el botón de actualización
            buttonUpdate.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onUpdateClick(products.get(position));
                }
            });

            // Configurar el botón de eliminación
            buttonDelete.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(products.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onUpdateClick(Product product);
        void onDeleteClick(Product product);
    }
}
