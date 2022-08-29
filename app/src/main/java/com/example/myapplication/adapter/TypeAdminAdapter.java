package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activities.AdminDetailActivity;
import com.example.myapplication.model.Book;

import java.util.List;

public class TypeAdminAdapter extends RecyclerView.Adapter<TypeAdminAdapter.ViewHolder> {

    Context context;
    List<Book> list;

    public TypeAdminAdapter(Context context, List<Book> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getIMAGE()).into(holder.imageView4);
        holder.tvTenSach.setText(list.get(position).getTITLE());
        holder.tvAuthor.setText(list.get(position).getAUTHOR());
        holder.tvPrice.setText(list.get(position).getPRICE().toString()+" VNĐ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminDetailActivity.class);
                intent.putExtra("type", list.get(position).getDOCUMENTID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView4;
        TextView tvTenSach,tvAuthor, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView4 = itemView.findViewById(R.id.imageView4);
            tvTenSach = itemView.findViewById(R.id.tvTenSach);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}