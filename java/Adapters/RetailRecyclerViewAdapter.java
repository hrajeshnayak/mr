package Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prathigram.shopbot.R;
import com.prathigram.shopbot.ShowCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Retail;

public class RetailRecyclerViewAdapter extends RecyclerView.Adapter<RetailRecyclerViewAdapter.ViewHolder> {
    @NonNull
    private Context context;
    private List<Retail> retailList;



    public RetailRecyclerViewAdapter(@NonNull Context context, List<Retail> retails) {
        this.context = context;
        retailList = retails;
    }





    @Override
    public RetailRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.retail_row, parent, false);



        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RetailRecyclerViewAdapter.ViewHolder holder, int position) {
        final String url = "https://www.prathigram.com/images/";
        Retail retail = retailList.get(position);
        String imageLink = retail.getRetImage();
        holder.retName.setText(retail.getRetName());
        holder.retAddress.setText(retail.getRetAdd());
        holder.retCity.setText(retail.getRetCity());
        holder.retPhone.setText(retail.getRetPhone());
        Picasso.get()
                .load(url+imageLink)
                .placeholder(android.R.drawable.ic_menu_camera)
                .into(holder.poster);
        holder.retDesc.setText(retail.getRetDesc());

    }

    @Override
    public int getItemCount() {
        return retailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView retName,retAddress,retCity,retPhone,retDesc;
        ImageView poster;

        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;
            retName = (TextView)itemView.findViewById(R.id.retName);
            retAddress = (TextView)itemView.findViewById(R.id.retAdd);
            retCity = (TextView)itemView.findViewById(R.id.retCity);
            retPhone = (TextView)itemView.findViewById(R.id.retPhone);
            retDesc = (TextView)itemView.findViewById(R.id.retDesc);
            poster = (ImageView)itemView.findViewById(R.id.retImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Retail retail = retailList.get(getAdapterPosition());
                    String retCode = retail.getRetCode();

                    Intent intent = new Intent(context, ShowCategory.class);

                    intent.putExtra("retCode", retCode);




                    ctx.startActivity(intent);

                }
            });




        }

        @Override
        public void onClick(View v) {

        }
    }
}
