package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.myapplication.R;
import com.example.myapplication.adapter.BillAdapter;
import com.example.myapplication.adapter.MyCartAdapter;
import com.example.myapplication.model.MyBill;
import com.example.myapplication.model.MyCart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class BillFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    RecyclerView rcvBill;
    List<MyBill> myBillList;
    BillAdapter billAdapter;


    public BillFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_bill, container, false);

        rcvBill = root.findViewById(R.id.rcvBill);
        rcvBill.setLayoutManager(new LinearLayoutManager(getActivity()));

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        myBillList = new ArrayList<>();
        billAdapter = new BillAdapter(getActivity(), myBillList);
        rcvBill.setAdapter(billAdapter);

        firestore.collection("CURRENTUSER").document(auth.getCurrentUser().getUid())
                .collection("MYORDER").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                        String documentId = documentSnapshot.getId();

                        MyBill bill = documentSnapshot.toObject(MyBill.class);
                        bill.setDOCUMENTID(documentId);


                        myBillList.add(bill);
                        billAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return root;
    }
}