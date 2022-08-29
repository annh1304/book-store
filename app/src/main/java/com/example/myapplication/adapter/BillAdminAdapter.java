package com.example.myapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import com.example.myapplication.fragment.BillAdminFragment;
import com.example.myapplication.model.BillAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BillAdminAdapter extends RecyclerView.Adapter<BillAdminAdapter.ViewHolder>{

    Context context;
    List<BillAdmin> billAdminList;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public BillAdminAdapter(Context context, List<BillAdmin> billAdminList) {
        this.context = context;
        this.billAdminList = billAdminList;

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillAdminAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bill_admin_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTenBillAdmin.setText(billAdminList.get(position).getTITLE());
        holder.DateBillAdmin.setText(billAdminList.get(position).getCURRENTDATE());
        holder.tvTimeBillAdmin.setText(billAdminList.get(position).getCURRENTTIME());
        holder.tvNameBillAdmin.setText(billAdminList.get(position).getNAME());
        holder.tvPhoneBillAdmin.setText(billAdminList.get(position).getPHONE());
        holder.tvAddressBillAdmin.setText(billAdminList.get(position).getADDRESS());
        holder.tvSoLuongBillAdmin.setText(String.valueOf(billAdminList.get(position).getTOTALQUANTITY()));
        holder.tvPriceBillAdmin.setText(String.valueOf(billAdminList.get(position).getTOTALPRICE()) + " VNĐ");
        holder.tvStatusBillAdmin.setText(billAdminList.get(position).getSTATUS());
        Glide.with(context).load(billAdminList.get(position).getIMAGE()).into(holder.ivBiaBillAdmin);

        String status = billAdminList.get(position).getSTATUS();
        if (status.equalsIgnoreCase("Đã xác nhận")){
            holder.tvStatusBillAdmin.setTextColor(Color.RED);
        }
        if (status.equalsIgnoreCase("Chờ xác nhận")){
            holder.tvStatusBillAdmin.setTextColor(Color.BLUE);
            holder.tvStatusBillAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Are you sure");
                    builder.setMessage("Please confirm");
                    builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            firestore.collection("ADMINBILL").
                                    document(billAdminList.get(position).getDOCUMENTID())
                                    .update("STATUS","Đã xác nhận").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context,"Update status successfull",Toast.LENGTH_LONG).show();;
                                    notifyDataSetChanged();
                                }
                            });
                            firestore.collection("CURRENTUSER").document(billAdminList.get(position).getUSERID())
                                    .collection("MYORDER")
                                    .document(billAdminList.get(position).getDOCUMENTID())
                                    .update("STATUS","Đã xác nhận").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context,"Update status successfull",Toast.LENGTH_LONG).show();;
                                    Fragment fragment1 = new BillAdminFragment();
                                    ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.frame,fragment1).commit();
                                }
                            });
                        }
                    });
                    builder.show();

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return billAdminList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTenBillAdmin, DateBillAdmin, tvTimeBillAdmin, tvNameBillAdmin, tvPhoneBillAdmin, tvAddressBillAdmin, tvSoLuongBillAdmin, tvPriceBillAdmin, tvStatusBillAdmin;
        ImageView ivBiaBillAdmin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTenBillAdmin = itemView.findViewById(R.id.tvTenBillAdmin);
            DateBillAdmin = itemView.findViewById(R.id.DateBillAdmin);
            tvTimeBillAdmin = itemView.findViewById(R.id.tvTimeBillAdmin);
            tvNameBillAdmin = itemView.findViewById(R.id.tvNameBillAdmin);
            tvPhoneBillAdmin = itemView.findViewById(R.id.tvPhoneBillAdmin);
            tvAddressBillAdmin = itemView.findViewById(R.id.tvAddressBillAdmin);
            tvSoLuongBillAdmin = itemView.findViewById(R.id.tvSoLuongBillAdmin);
            tvPriceBillAdmin = itemView.findViewById(R.id.tvPriceBillAdmin);
            tvStatusBillAdmin = itemView.findViewById(R.id.tvStatusBillAdmin);
            ivBiaBillAdmin = itemView.findViewById(R.id.ivBiaBillAdmin);
        }
    }
}
