package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.MyBill;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder>{
    Context context;
    List<MyBill> myBillList;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    int totalPrice = 0;

    public BillAdapter(Context context, List<MyBill> myBillList) {
        this.context = context;
        this.myBillList = myBillList;

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bill_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapter.ViewHolder holder, int position) {
        totalPrice = myBillList.get(position).getTOTALQUANTITY() * myBillList.get(position).getPRICE();
        holder.tvTenSanPhamGioHang.setText(myBillList.get(position).getTITLE());
        holder.tvGiaSanPhamGioHang.setText(String.valueOf(totalPrice) + " VNĐ");
        holder.tvNgayBill.setText(myBillList.get(position).getCURRENTDATE());
        holder.tvSoLuongBill.setText(String.valueOf("Số lượng: " + myBillList.get(position).getTOTALQUANTITY()));
        Glide.with(context).load(myBillList.get(position).getIMAGE()).into(holder.ivBiaSanPham);
        holder.tvTrangThaiBill.setText(myBillList.get(position).getSTATUS());
        String status = myBillList.get(position).getSTATUS();
        if (status.equalsIgnoreCase("Đã xác nhận")){
            holder.tvTrangThaiBill.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return myBillList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTenSanPhamGioHang, tvGiaSanPhamGioHang, tvNgayBill, tvSoLuongBill, tvTrangThaiBill;

        ImageView ivBiaSanPham;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTenSanPhamGioHang = itemView.findViewById(R.id.tvTenSanPhamGioHangBill);
            tvGiaSanPhamGioHang = itemView.findViewById(R.id.tvGiaSanPhamGioHangBill);
            tvNgayBill = itemView.findViewById(R.id.tvNgayBill);
            tvSoLuongBill = itemView.findViewById(R.id.tvSoLuongBill);
            tvTrangThaiBill = itemView.findViewById(R.id.tvTrangThaiBill);
            ivBiaSanPham = itemView.findViewById(R.id.ivBiaSanPhamBill);


        }
    }
}