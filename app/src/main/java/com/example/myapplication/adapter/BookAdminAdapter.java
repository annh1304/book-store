package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activities.AdminDetailActivity;
import com.example.myapplication.activities.DetailActivity;
import com.example.myapplication.model.Book;

import java.util.List;

public class BookAdminAdapter extends RecyclerView.Adapter<BookAdminAdapter.ViewHolder> {

    Context context;
    List<Book> lst;

    public BookAdminAdapter(Context context, List<Book> lst) {
        this.context = context;
        this.lst = lst;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book_items_main, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(lst.get(position).getIMAGE()).into(holder.imageView);
        holder.tvTitle.setText(lst.get(position).getTITLE());
        holder.tvAuthor.setText(lst.get(position).getAUTHOR());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminDetailActivity.class);
                intent.putExtra("detail", lst.get(position).getDOCUMENTID());
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTitle, tvAuthor;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageNewBook);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
        }
    }
}
