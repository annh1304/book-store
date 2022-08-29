package com.example.myapplication.adapter;

import android.content.Context;

import android.content.DialogInterface;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.fragment.CartFragment;
import com.example.myapplication.model.MyCart;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    Context context;
    List<MyCart> myCartList;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    int totalQuantity;

    int totalPrice = 0;

    public MyCartAdapter(Context context, List<MyCart> myCartList) {
        this.context = context;
        this.myCartList = myCartList;

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        totalPrice = myCartList.get(position).getTOTALQUANTITY() * myCartList.get(position).getPRICE();
        holder.tvTenSanPhamGioHang.setText(myCartList.get(position).getTITLE());
        holder.tvTotalQuantity.setText(String.valueOf(myCartList.get(position).getTOTALQUANTITY()));
        holder.tvGiaSanPhamGioHang.setText(String.valueOf(totalPrice) + " VNĐ");
        Glide.with(context).load(myCartList.get(position).getIMAGE()).into(holder.ivBiaSanPham);

        //pass total mount to my cart fragment



        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
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
                        Fragment fragment = new CartFragment();
                        ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame,fragment).commit();
                        firestore.collection("ADDTOCART").document(auth.getCurrentUser().getUid())
                                .collection("CURRENTUSER")
                                .document(myCartList.get(position).getDOCUMENTID())
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            myCartList.remove(myCartList.get(position));
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

        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CartFragment();
                ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame,fragment).commit();
                if ( myCartList.get(position).getTOTALQUANTITY() < 10) {
                    totalQuantity = myCartList.get(position).getTOTALQUANTITY();
                    totalQuantity++;
                    totalPrice = totalQuantity * myCartList.get(position).getPRICE();
                    //set gia tri
                    myCartList.get(position).setTOTALQUANTITY(totalQuantity);
                    myCartList.get(position).setTOTALPRICE(totalPrice);
                    holder.tvTotalQuantity.setText(String.valueOf(totalQuantity));
                    holder.tvGiaSanPhamGioHang.setText(String.valueOf(totalPrice) + " VNĐ");

                    //UPDATE TONG SACH
                    firestore.collection("ADDTOCART").document(auth.getCurrentUser().getUid())
                            .collection("CURRENTUSER")
                            .document(myCartList.get(position).getDOCUMENTID()).update("TOTALQUANTITY",totalQuantity)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    notifyDataSetChanged();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Error" + e, Toast.LENGTH_SHORT).show();

                                }
                            });
                    // UPDATE TONG TIEN
                    firestore.collection("ADDTOCART").document(auth.getCurrentUser().getUid())
                            .collection("CURRENTUSER")
                            .document(myCartList.get(position).getDOCUMENTID()).update("TOTALPRICE",totalPrice)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Error" + e, Toast.LENGTH_SHORT).show();

                                }
                            });

                }

                Fragment fragment1 = new CartFragment();
                ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame,fragment1).commit();

            }

        });

        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CartFragment();
                ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame,fragment).commit();
                if (myCartList.get(position).getTOTALQUANTITY() > 0) {
                    totalQuantity = myCartList.get(position).getTOTALQUANTITY();
                    totalQuantity--;
                    totalPrice = totalQuantity * myCartList.get(position).getPRICE();
                    holder.tvTotalQuantity.setText(String.valueOf(totalQuantity));
                    holder.tvGiaSanPhamGioHang.setText(String.valueOf(totalPrice) + " VNĐ");
                    //set gia tri
                    myCartList.get(position).setTOTALQUANTITY(totalQuantity);
                    myCartList.get(position).setTOTALPRICE(totalPrice);
                    firestore.collection("ADDTOCART").document(auth.getCurrentUser().getUid())
                            .collection("CURRENTUSER")
                            .document(myCartList.get(position).getDOCUMENTID()).update("TOTALQUANTITY",totalQuantity)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    notifyDataSetChanged();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Error" + e, Toast.LENGTH_SHORT).show();

                                }
                            });
                    // UPDATE TONG TIEN
                    firestore.collection("ADDTOCART").document(auth.getCurrentUser().getUid())
                            .collection("CURRENTUSER")
                            .document(myCartList.get(position).getDOCUMENTID()).update("TOTALPRICE",totalPrice)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    notifyDataSetChanged();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Error" + e, Toast.LENGTH_SHORT).show();

                                }
                            });

                }

                Fragment fragment2 = new CartFragment();
                ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame,fragment2).commit();

            }
        });


    }

    @Override
    public int getItemCount() {
        return myCartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTenSanPhamGioHang, tvTotalQuantity, tvGiaSanPhamGioHang;

        ImageView ivBiaSanPham, imgDelete, imgMinus, imgAdd;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTenSanPhamGioHang = itemView.findViewById(R.id.tvTenSanPhamGioHang);
            tvTotalQuantity = itemView.findViewById(R.id.tvQuantity);
            tvGiaSanPhamGioHang = itemView.findViewById(R.id.tvGiaSanPhamGioHang);
            ivBiaSanPham = itemView.findViewById(R.id.ivBiaSanPham);

            imgMinus = itemView.findViewById(R.id.imgMinus);
            imgAdd = itemView.findViewById(R.id.imgAdd);
            imgDelete = itemView.findViewById(R.id.imgDelete);

        }
    }
}
