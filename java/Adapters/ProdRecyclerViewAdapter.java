package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prathigram.shopbot.ProductDetail;
import com.prathigram.shopbot.R;
import com.prathigram.shopbot.ShowProduct;
import com.squareup.picasso.Picasso;

import java.util.List;


import Model.Prod;



public class ProdRecyclerViewAdapter extends RecyclerView.Adapter<ProdRecyclerViewAdapter.ViewHolder> {
    @NonNull
    private Context context;
    private List<Prod> prodList;



    public ProdRecyclerViewAdapter(@NonNull Context context, List<Prod> prods) {
        this.context = context;
        prodList = prods;
    }





    @Override
    public ProdRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row, parent, false);



        return new ViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(@NonNull ProdRecyclerViewAdapter.ViewHolder holder, int position) {

        Prod prod = prodList.get(position);

        holder.prodName.setText(prod.getProdName());
        holder.prodCat.setText(prod.getCatName());
        holder.prodAvail.setText(prod.getProdAvail());
        holder.prodPrice.setText(prod.getProdPrice());

    }

    @Override
    public int getItemCount() {
        return prodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView prodName,prodCat,prodAvail,prodPrice ;


        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;
            prodCat = (TextView)itemView.findViewById(R.id.prodCat);
            prodName = (TextView)itemView.findViewById(R.id.prodName);
            prodAvail = (TextView)itemView.findViewById(R.id.prodAvail);
            prodPrice = (TextView)itemView.findViewById(R.id.prodPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Prod prod = prodList.get(getAdapterPosition());
                    String prodCode = prod.getProdCode();
                    String retCode = prod.getRetCode();

                    Intent intent = new Intent(context, ProductDetail.class);

                    intent.putExtra("prodCode", prodCode);
                    intent.putExtra("retCode",retCode);
                    ctx.startActivity(intent);

                }
            });




        }

        @Override
        public void onClick(View v) {

        }
    }
}

