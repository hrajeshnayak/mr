package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prathigram.shopbot.R;

import com.prathigram.shopbot.ShowProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Cat;


public class CatRecyclerViewAdapter extends RecyclerView.Adapter<CatRecyclerViewAdapter.ViewHolder> {
    @NonNull
    private Context context;
    private List<Cat> catList;



    public CatRecyclerViewAdapter(@NonNull Context context, List<Cat> cats) {
        this.context = context;
        catList = cats;
    }





    @Override
    public CatRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cat_row, parent, false);



        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CatRecyclerViewAdapter.ViewHolder holder, int position) {
        final String url = "https://www.prathigram.com/images/";
        Cat cat = catList.get(position);
        String imageLink = cat.getCatImage();
        holder.catName.setText(cat.getCatName());

        Picasso.get()
                .load(url+imageLink)
                .placeholder(android.R.drawable.ic_menu_camera)
                .into(holder.poster);
        holder.catDesc.setText(cat.getCatDesc());

    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView catName,catDesc;
        ImageView poster;

        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;
            catName = (TextView)itemView.findViewById(R.id.catName);
            catDesc = (TextView)itemView.findViewById(R.id.catDesc);
            poster = (ImageView)itemView.findViewById(R.id.catImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cat cat = catList.get(getAdapterPosition());
                    String catCode = cat.getCatCode();
                    String catName = cat.getCatName();

                    Intent intent = new Intent(context, ShowProduct.class);

                    intent.putExtra("catCode", catCode);
                    intent.putExtra("catName",catName);
                    ctx.startActivity(intent);

                }
            });




        }

        @Override
        public void onClick(View v) {

        }
    }
}
