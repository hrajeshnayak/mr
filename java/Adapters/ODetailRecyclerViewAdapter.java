package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prathigram.shopbot.R;

import java.util.List;

import Model.ODetail;


public class ODetailRecyclerViewAdapter extends RecyclerView.Adapter<ODetailRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<ODetail> oDetailList;

    public ODetailRecyclerViewAdapter(Context context, List<ODetail> oDetails) {
        this.context = context;
        oDetailList = oDetails;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.odetail_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ODetail oDetail = oDetailList.get(position);
        holder.orderProdName.setText(oDetail.getOrderProdName());
        holder.orderProdPrice.setText(oDetail.getOrderProdPrice());
        holder.orderProdQty.setText(oDetail.getOrderProdQty());
        holder.orderProdAmount.setText(oDetail.getOrderProdAmount());


    }

    @Override
    public int getItemCount() {
        return oDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderProdName, orderProdPrice, orderProdQty, orderProdAmount;

        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;
            orderProdName = itemView.findViewById(R.id.orderProdName);
            orderProdPrice = itemView.findViewById(R.id.orderProdPrice);
            orderProdQty = itemView.findViewById(R.id.orderProdQty);
            orderProdAmount = itemView.findViewById(R.id.orderProdAmount);

        }
    }
}
