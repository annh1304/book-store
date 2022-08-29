package com.example.myapplication.fragment;

import static java.lang.Integer.parseInt;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.R;
//import com.example.myapplication.activities.TestActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class InsertBookFragment extends Fragment {
    private ImageView imgBook;
    private String bookAuthorDetail, bookTypeDetail, bookIntroduction, bookPriceDetail, bookNameDetail, bookStatus ,bookPageDetail;
    EditText edtAuthor, edtIntroduction, edtPage, edtPrice, edtTitle;
    Spinner spnType, spnStatus;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Button btnTest;
    private Uri mImageUri;
    private DatabaseReference databaseRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    static String bookName, imageUrl = "imageUrl";
    private StorageReference storageRef, fileRefernce;
    int imageChecked = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_insert_book, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        edtAuthor = view.findViewById(R.id.edtAuthor);
        imgBook = view.findViewById(R.id.imgBook);
        edtIntroduction = view.findViewById(R.id.edtIntroduction);
        edtPage = view.findViewById(R.id.edtPage);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtTitle = view.findViewById(R.id.edtTitle);
//        edtTypename = view.findViewById(R.id.edtTypeName);
        spnType = view.findViewById(R.id.spnTypeName);
        spnStatus = view.findViewById(R.id.spnStatus);

        firestore = FirebaseFirestore.getInstance();

        btnTest = view.findViewById(R.id.test);
        //Spinner thẻ loại
        String[] typelist = {"Tiểu Thuyết","Giáo Khoa","Văn Học","Khoa Học","Chính Trị","Lịch Sử"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,typelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnType.setAdapter(adapter);
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bookTypeDetail = typelist[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bookTypeDetail = "";
            }
        });

        //Spinner trang thai{sach moi, sach ban chay, ...}
        String[] statuslist = {"","RECOMMEND","BOOKSALE","TOPSELL","NEWBOOK"};
        ArrayAdapter<String> adapterstatus = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,statuslist);
        adapterstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(adapterstatus);
        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bookStatus = statuslist[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bookStatus = "";
            }
        });
        imgBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePic();
            }
        });
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    public void InsertData() {
        int priceInt = parseInt(edtPrice.getText().toString().trim());
        int pageInt = parseInt(edtPage.getText().toString().trim());

        Map<String, Object> items = new HashMap<>();
        items.put("AUTHOR", edtAuthor.getText().toString().trim());
        items.put("INTRODUCTION", edtIntroduction.getText().toString().trim());
        items.put("PAGE", pageInt);
        items.put("PRICE", priceInt);
        items.put("TITLE", edtTitle.getText().toString().trim());
        bookName = edtTitle.getText().toString();
        items.put("TYPENAME", bookTypeDetail);
        items.put("STATUS",bookStatus);
        String bookImageName = bookName.replaceAll(" ", "");
        storageRef = FirebaseStorage.getInstance().getReference();
        boolean imageVal =false;
        try {
            fileRefernce = storageRef.child(bookImageName + "." + getMimeType(getContext(), mImageUri));
            if (mImageUri != null) {
                fileRefernce.putFile(mImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(getContext(),"image updated !", Toast.LENGTH_LONG).show();
                                fileRefernce
                                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl = uri.toString();
                                        items.put("IMAGE", imageUrl);
                                        firestore.collection("BOOK").add(items)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        edtAuthor.setText("");
                                                        edtIntroduction.setText("");
                                                        edtPage.setText("");
                                                        edtPrice.setText("");
                                                        edtTitle.setText("");

                                                        Toast.makeText(getActivity(), "Add Book Success!", Toast.LENGTH_SHORT).show();
                                                        // Return
                                                        Fragment fragment1 = new BookAllAdminFragment();

                                                        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.frame,fragment1).commit();
                                                    }
                                                });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
            imageVal =true;
        }catch (Exception e){
            e.printStackTrace();
        }
        if(imageVal == false){
            Toast.makeText(getContext(),"Please insert your book image!!",Toast.LENGTH_LONG).show();
        }



    }

    public void validate() {
        bookNameDetail = edtTitle.getText().toString();
        bookAuthorDetail = edtAuthor.getText().toString();

        bookPageDetail = edtPage.getText().toString();
        bookIntroduction = edtIntroduction.getText().toString();
        bookPriceDetail = edtPrice.getText().toString();

        if (TextUtils.isEmpty(bookNameDetail)) {
            Toast.makeText(getContext(), "Title is empty !", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(bookAuthorDetail)) {
            Toast.makeText(getContext(), "Author is empty !", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(bookTypeDetail)) {
            Toast.makeText(getContext(), "Type is empty !", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(bookPageDetail) || isNumeric(bookPageDetail) == false) {
            Toast.makeText(getContext(), "Please check your book's page !", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(bookIntroduction)) {
            Toast.makeText(getContext(), "Introduction is empty !", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(bookPriceDetail) || isNumeric(bookPriceDetail) == false) {
            Toast.makeText(getContext(), "Please check your book's price !", Toast.LENGTH_LONG).show();
            return;
        }
        InsertData();
    }

    public void choosePic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imageChecked = 1;
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == -1 && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(getActivity()).load(mImageUri).into(imgBook);
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
            extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(Uri.fromFile(new File(uri.getPath().toString()))));
        }
        return extension;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}