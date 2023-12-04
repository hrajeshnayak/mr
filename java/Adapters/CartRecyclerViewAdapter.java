package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.prathigram.shopbot.EditCartItem;
import com.prathigram.shopbot.R;

import java.util.List;

import Model.Cart;



public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {
    @NonNull
    private Context context;
    private List<Cart> cartList;

    public CartRecyclerViewAdapter(@NonNull Context context, List<Cart> carts) {
        this.context = context;
        cartList = carts;
    }
    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_row, parent, false);

        return new ViewHolder(view, context);
    }
    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.ViewHolder holder, int position) {

        Cart cart = cartList.get(position);

        holder.cartName.setText(cart.getCartName());
        holder.cartQty.setText(cart.getCartQty());
        holder.cartPrice.setText(cart.getCartPrice());
        holder.cartAmnt.setText(cart.getCartAmnt());

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView cartName, cartQty, cartPrice, cartAmnt;
        public TextView buttonViewOption;


        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            cartName = (TextView) itemView.findViewById(R.id.cartName);
            cartQty = (TextView) itemView.findViewById(R.id.cartQty);
            cartPrice = (TextView) itemView.findViewById(R.id.cartPrice);
            cartAmnt = (TextView) itemView.findViewById(R.id.cartAmount);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cart cart = cartList.get(getAdapterPosition());
                    String listId = cart.getListId();


                    Intent intent = new Intent(context, EditCartItem.class);

                    intent.putExtra("listId", listId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);



                }
            });


        }

        @Override
        public void onClick(View v) {

        }


    }
}
