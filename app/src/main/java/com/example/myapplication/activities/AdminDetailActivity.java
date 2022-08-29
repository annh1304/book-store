package com.example.myapplication.activities;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.fragment.CartFragment;
import com.example.myapplication.model.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDetailActivity extends AppCompatActivity {
    ImageView imageViewDetail, imgBack;
    EditText tvBookNameDetail, tvBookAuthorDetail, tvBookTypeDetail, tvBookPageDetail, tvBookIntroduction, tvBookPriceDetail;
    private String bookAuthorDetail, bookTypeDetail, bookPageDetail, bookIntroduction, bookPriceDetail, id;
    private StorageReference storageRef, fileRefernce;
    boolean success =false;
    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseFirestore firestore;
    Spinner spnBook;
    private Uri mImageUri;
    static String imageUrl="imageUrl",bookNameDetail;
    TextView btnSaveAdmin;
    Book book=null;
    ArrayAdapter spineradapter;

    List<Book> list;
    int position;


//    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail);
        firestore = FirebaseFirestore.getInstance();
        spnBook = this.findViewById(R.id.spnTypeBook);
//        auth = FirebaseAuth.getInstance();



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
//        id = getIntent().getStringExtra("type");
//
//        id = getIntent().getStringExtra("detail");

//        final Object objectType = getIntent().getSerializableExtra("type");
//        if (objectType instanceof Book) {
//            book = (Book) objectType;
//        }

        imageViewDetail = this.findViewById(R.id.imageViewDetail);
        imgBack = this.findViewById(R.id.imgBack);

        tvBookNameDetail = this.findViewById(R.id.tvBookNameDetailAD);
        tvBookAuthorDetail = this.findViewById(R.id.tvBookAuthorDetailAD);
        tvBookPageDetail = this.findViewById(R.id.tvBookPageDetailAD);
        tvBookIntroduction = this.findViewById(R.id.tvBookIntroductionAD);
        tvBookPriceDetail = this.findViewById(R.id.tvBookPriceDetailAD);
        btnSaveAdmin = this.findViewById(R.id.btnSaveAdmin);


        getBookDetail();

        imageViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePic();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CartFragment();

                finish();
            }
        });

        btnSaveAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    uploadFile();
                }catch (Exception e){
                    e.printStackTrace();
                }
                updateBook();
                getBookDetail();
                if(success == true){
                    finish();

                }

            }
        });

        //Spinner thẻ loại
        String[] typelist = {"Tiểu Thuyết","Giáo Khoa","Văn Học","Khoa Học","Chính Trị","Lịch Sử"};
        spineradapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,typelist);
        spineradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBook.setAdapter(spineradapter);
        spnBook.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bookTypeDetail = typelist[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bookTypeDetail = "";
            }
        });

    }

    public void updateBook(){
        bookNameDetail = tvBookNameDetail.getText().toString();
        bookAuthorDetail = tvBookAuthorDetail.getText().toString();
//        bookTypeDetail = tvBookTypeDetail.getText().toString();
        bookPageDetail = tvBookPageDetail.getText().toString();
        bookIntroduction = tvBookIntroduction.getText().toString();
        bookPriceDetail = tvBookPriceDetail.getText().toString();
        String price = bookPriceDetail.replace(" VNĐ","");
        int priceInt = parseInt(price);
        int pageInt = parseInt(bookPageDetail);

        if (TextUtils.isEmpty(bookNameDetail)) {
            Toast.makeText(this, "Title is empty  !", Toast.LENGTH_LONG).show();
            tvBookNameDetail.requestFocus();
            tvBookAuthorDetail.setFocusable(false);
//            tvBookTypeDetail.setFocusable(false);
            tvBookPageDetail.setFocusable(false);
            tvBookIntroduction.setFocusable(false);
            tvBookPriceDetail.setFocusable(false);
            return;
        }
        if (TextUtils.isEmpty(bookAuthorDetail)) {
            Toast.makeText(this, "Author is empty !", Toast.LENGTH_LONG).show();
            tvBookNameDetail.setFocusable(false);
            tvBookAuthorDetail.requestFocus();
//            tvBookTypeDetail.setFocusable(false);
            tvBookPageDetail.setFocusable(false);
            tvBookIntroduction.setFocusable(false);
            tvBookPriceDetail.setFocusable(false);
            return;
        }
        if (TextUtils.isEmpty(bookTypeDetail)) {
            Toast.makeText(this, "Type is empty !", Toast.LENGTH_LONG).show();
            tvBookNameDetail.setFocusable(false);
            tvBookAuthorDetail.setFocusable(false);
//            tvBookTypeDetail.requestFocus();
            tvBookPageDetail.setFocusable(false);
            tvBookIntroduction.setFocusable(false);
            tvBookPriceDetail.setFocusable(false);
            return;
        }
        if (TextUtils.isEmpty(bookPageDetail) || isNumeric(bookPageDetail) == false) {
            Toast.makeText(this, "Page is empty !", Toast.LENGTH_LONG).show();
            tvBookNameDetail.setFocusable(false);
            tvBookAuthorDetail.setFocusable(false);
//            tvBookTypeDetail.setFocusable(false);
            tvBookPageDetail.requestFocus();
            tvBookIntroduction.setFocusable(false);
            tvBookPriceDetail.setFocusable(false);
            return;
        }
        if (TextUtils.isEmpty(bookIntroduction)) {
            Toast.makeText(this, "Introduction is empty !", Toast.LENGTH_LONG).show();
            tvBookNameDetail.setFocusable(false);
            tvBookAuthorDetail.setFocusable(false);
//            tvBookTypeDetail.setFocusable(false);
            tvBookPageDetail.setFocusable(false);
            tvBookIntroduction.requestFocus();
            tvBookPriceDetail.setFocusable(false);
            return;
        }
        if (TextUtils.isEmpty(price) || isNumeric(price) == false) {
            Toast.makeText(this, "Price is empty !", Toast.LENGTH_LONG).show();
            tvBookNameDetail.setFocusable(false);
            tvBookAuthorDetail.setFocusable(false);
//            tvBookTypeDetail.setFocusable(false);
            tvBookPageDetail.setFocusable(false);
            tvBookIntroduction.setFocusable(false);
            tvBookPriceDetail.requestFocus();
            return;
        }


        price = bookPriceDetail.replace(" VNĐ","");
        priceInt = parseInt(price);
        pageInt = parseInt(bookPageDetail);
        DocumentReference documentReference = firestore.collection("BOOK").document(id);


        Map<String, Object> book = new HashMap<>();
//            user.put("EMAIL",email);
        book.put("TITLE", bookNameDetail);
        book.put("AUTHOR", bookAuthorDetail);
        book.put("TYPENAME", bookTypeDetail); //uploadFile());
        book.put("PAGE", pageInt);
        book.put("INTRODUCTION", bookIntroduction);
        book.put("PRICE", priceInt);


        documentReference.update(book).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AdminDetailActivity.this, "Update success", Toast.LENGTH_LONG).show();
            }
        });
        success = true;
    }

    public void getBookDetail(){
        DocumentReference documentReference = firestore.collection("BOOK").document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Book book = documentSnapshot.toObject(Book.class);

                Glide.with(getApplicationContext()).load(book.getIMAGE()).into(imageViewDetail);
//        Toast.makeText(this, "123"+book.getDOCUMENTID(),Toast.LENGTH_LONG).show();
                tvBookNameDetail.setText(book.getTITLE());
                tvBookAuthorDetail.setText(book.getAUTHOR());
                tvBookPageDetail.setText(book.getPAGE().toString());
                tvBookPriceDetail.setText(book.getPRICE().toString() + " VNĐ");
                tvBookIntroduction.setText(book.getINTRODUCTION());
                spnBook.setSelection(spineradapter.getPosition(book.getTYPENAME()));
                imageUrl = book.getIMAGE();
                bookNameDetail = book.getTITLE();
            }
        });
    }

    public void choosePic(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == -1 &&  data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(imageViewDetail);
        }
    }


    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(uri.getPath().toString()))));
        }
        return extension;
    }

    private void uploadFile(){

        String bookImageName = bookNameDetail.replaceAll(" ","");
        storageRef = FirebaseStorage.getInstance().getReference();
        fileRefernce = storageRef.child(bookImageName+"."+getMimeType(this,mImageUri));
        if(mImageUri !=  null){
            fileRefernce.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AdminDetailActivity.this,"image updated !", Toast.LENGTH_LONG).show();
                            fileRefernce
                                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                    DocumentReference documentReference = firestore.collection("BOOK").document(id);
                                    Map<String, Object> book = new HashMap<>();
                                    book.put("IMAGE", imageUrl);
                                    documentReference.update(book).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                        }
                                    });
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminDetailActivity.this, "Book image not updated", Toast.LENGTH_LONG).show();
                }
            });
        }else{Toast.makeText(AdminDetailActivity.this,"No file selected", Toast.LENGTH_LONG).show();}

    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);

    }
}