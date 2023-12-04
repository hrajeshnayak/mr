package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prathigram.shopbot.EditCartItem;
import com.prathigram.shopbot.MyOrders;
import com.prathigram.shopbot.R;

import java.util.List;

import Model.Order;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Order> orderList;

    public OrderRecyclerViewAdapter(Context context, List<Order> orders) {
        this.context = context;
        orderList = orders;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderId.setText(order.getOrderId());
        holder.orderRet.setText(order.getOrderRet());
        holder.orderDat.setText((CharSequence) order.getOrderDate());
        holder.orderStatus.setText(order.getOrderStatus());



    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       TextView orderId, orderRet, orderDat, orderStatus;


        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;
            orderId = (TextView) itemView.findViewById(R.id.orderId);
            orderRet = (TextView) itemView.findViewById(R.id.orderRet);
            orderStatus = (TextView) itemView.findViewById(R.id.orderStatus);
            orderDat = (TextView) itemView.findViewById(R.id.orderDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Order order = orderList.get(getAdapterPosition());
                    String oId = order.getOrderId();
                    String oRet = order.getOrderRet();
                    String oStat = order.getOrderStatus();
                    String oDate = order.getOrderDate();
                    //Toast.makeText(context, oRet, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, MyOrders.class);

                    intent.putExtra("orderId", oId);
                    intent.putExtra("orderRet", oRet);
                    intent.putExtra("orderStat", oStat);
                    intent.putExtra("oDate", oDate);
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
