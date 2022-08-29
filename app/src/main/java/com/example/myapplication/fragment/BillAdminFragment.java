package com.example.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.BillAdapter;
import com.example.myapplication.adapter.BillAdminAdapter;
import com.example.myapplication.model.BillAdmin;
import com.example.myapplication.model.MyBill;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class BillAdminFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    RecyclerView rcvBillAdmin;
    List<BillAdmin> billAdminList;
    BillAdminAdapter billAdminAdapter;


    public BillAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_admin_bill, container, false);

        rcvBillAdmin = root.findViewById(R.id.rcvBillAdmin);
        rcvBillAdmin.setLayoutManager(new LinearLayoutManager(getActivity()));

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        billAdminList = new ArrayList<>();
        billAdminAdapter = new BillAdminAdapter(getActivity(), billAdminList);
        rcvBillAdmin.setAdapter(billAdminAdapter);

        firestore.collection("ADMINBILL").orderBy("STATUS", Query.Direction.ASCENDING).orderBy("CURRENTDATE", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                        String documentId = documentSnapshot.getId();

                        BillAdmin bill = documentSnapshot.toObject(BillAdmin.class);
                        bill.setDOCUMENTID(documentId);

                        billAdminList.add(bill);
                        billAdminAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return root;
    }
}