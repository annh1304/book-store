package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.Book;
import com.example.myapplication.model.MyCart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    ImageView imageViewDetail , imgAdd , imgMinus , imgBack;
    TextView tvBookNameDetail, tvBookAuthorDetail , tvBookTypeDetail , tvBookPageDetail , tvBookIntroduction , tvBookPriceDetail , tvQuantity;
    Button btnAddCart;
    Book book = null;
    List<MyCart> list;

    String id;

    int totalQuantity = 1;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_book_detail_item);
        list = new ArrayList<>();

        final String idtype = getIntent().getStringExtra("type");
        if (idtype != null) {
            id = idtype;
        }
        final String idall = getIntent().getStringExtra("all");
        if (idall != null) {
            id = idall;
        }
        final String iddetail = getIntent().getStringExtra("detail");
        if (iddetail != null) {
            id = iddetail;
        }

        imageViewDetail = this.findViewById(R.id.imageViewDetail);
        imgAdd = this.findViewById(R.id.imgAdd);
        imgMinus = this.findViewById(R.id.imgMinus);
        imgBack = this.findViewById(R.id.imgBack);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        tvBookNameDetail = this.findViewById(R.id.tvBookNameDetail);
        tvBookAuthorDetail = this.findViewById(R.id.tvBookAuthorDetail);
        tvBookTypeDetail = this.findViewById(R.id.tvBookTypeDetail);
        tvBookPageDetail = this.findViewById(R.id.tvBookPageDetail);
        tvBookIntroduction = this.findViewById(R.id.tvBookIntroduction);
        tvBookPriceDetail = this.findViewById(R.id.tvBookPriceDetail);
        tvQuantity = this.findViewById(R.id.tvQuantity);

        DocumentReference documentReference = firestore.collection("BOOK").document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                book = documentSnapshot.toObject(Book.class);
                Glide.with(getApplicationContext()).load(book.getIMAGE()).into(imageViewDetail);
                tvBookNameDetail.setText(book.getTITLE());
                tvBookAuthorDetail.setText(book.getAUTHOR());
                tvBookTypeDetail.setText(book.getTYPENAME());
                tvBookPageDetail.setText(book.getPAGE().toString());
                tvBookPriceDetail.setText(book.getPRICE().toString() + " VNĐ");
                tvBookIntroduction.setText(book.getINTRODUCTION());
            }
        });


        btnAddCart = this.findViewById(R.id.btnAddCart);
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InvaledBook()==true) {
                    addedToCart();
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalQuantity < 10){
                    totalQuantity++;
                    tvQuantity.setText(String.valueOf(totalQuantity));
                }
            }
        });

        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(totalQuantity > 0){
                    totalQuantity--;
                    tvQuantity.setText(String.valueOf(totalQuantity));
                }
            }
        });

        firestore.collection("ADDTOCART").document(auth.getCurrentUser().getUid())
                .collection("CURRENTUSER").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        String documentId = documentSnapshot.getId();
                        MyCart cart = documentSnapshot.toObject(MyCart.class);
                        cart.setDOCUMENTID(documentId);
                        list.add(cart);
                    }
                }
            }
        });
    }

    private boolean InvaledBook(){
        boolean kq = true;

        String title = book.getTITLE();
        for (MyCart cart: list){
            if (cart.getTITLE().equals(title)){
                Toast.makeText(DetailActivity.this, "Bạn đã có sách này trong giỏ hàng", Toast.LENGTH_LONG).show();
                kq = false;
                break;
            }
        }
        return kq;
    }

    private void addedToCart() {
            Integer totalPrice = totalQuantity * book.getPRICE();

            String saveCurrentDate, saveCurrentTime;
            Calendar calForDate = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calForDate.getTime());

            final HashMap<String, Object> cartMap = new HashMap<>();

            cartMap.put("TITLE", book.getTITLE());
            cartMap.put("IMAGE", book.getIMAGE());
            cartMap.put("TOTALPRICE", totalPrice);
            cartMap.put("CURRENTDATE",saveCurrentDate );
            cartMap.put("CURRENTTIME", saveCurrentTime);
            cartMap.put("TOTALQUANTITY", totalQuantity);
            cartMap.put("PRICE",book.getPRICE());

            firestore.collection("ADDTOCART").document(auth.getCurrentUser().getUid())
                    .collection("CURRENTUSER").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    Toast.makeText(DetailActivity.this, "Added complete", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);

    }
}