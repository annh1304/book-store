package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activities.AdminDetailActivity;
import com.example.myapplication.activities.DetailActivity;
import com.example.myapplication.model.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BookAllAdminAdapter extends RecyclerView.Adapter<BookAllAdminAdapter.ViewHolder> {

    Context context;
    List<Book> list;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public BookAllAdminAdapter(Context context, List<Book> list) {
        this.context = context;
        this.list = list;

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_all_book_items_admin, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getIMAGE()).into(holder.imageView);
        holder.tvTitle.setText(list.get(position).getTITLE());
        holder.tvPrice.setText(list.get(position).getPRICE().toString() + " VNĐ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminDetailActivity.class);
                intent.putExtra("all", list.get(position).getDOCUMENTID());
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        holder.ivDeleteBookAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure ?");
                builder.setMessage("Please confirm !");
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firestore.collection("BOOK")
                                .document(list.get(position).getDOCUMENTID())
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            list.remove(list.get(position));
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
                builder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, ivDeleteBookAdmin;
        TextView tvTitle, tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageNewBooktest);
            tvTitle = itemView.findViewById(R.id.tvTitletest);
            tvPrice = itemView.findViewById(R.id.tvPricetest);
            ivDeleteBookAdmin = itemView.findViewById(R.id.ivDeleteBookAdmin);
        }
    }
}
