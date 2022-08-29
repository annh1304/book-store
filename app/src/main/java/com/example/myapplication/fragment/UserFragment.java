package com.example.myapplication.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.User;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UserFragment extends Fragment {
    private ImageView profilePic;
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageRef, fileRefernce;
    private DatabaseReference databaseRef;
    private FirebaseStorage storage;
    private FirebaseFirestore database;
    FirebaseAuth auth;
    private Context mContext;

    String email, name, phone, address, birthday, imageEmail, fileTail = "jpg";
    Long Role;
    EditText edtUserName, edtDate, edtPhone, edtEmail, edtAddress;
    Button btnSave, btnCancel;
    String id;
    int imageChecked = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        edtUserName = view.findViewById(R.id.edtUserName);
        edtDate = view.findViewById(R.id.edtDate);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtAddress = view.findViewById(R.id.edtAddress);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancelUpdate);
        profilePic = view.findViewById(R.id.profilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePic();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
                if (imageChecked == 1) {
                    uploadFile();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUser();
                try {
                    getUserImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        id = auth.getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        imageEmail = email.replace(".com", "");
        getUser();
        Role = getUser();
        try {
            getUserImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Long getUser() {
        DocumentReference reference = database.collection("USER").document(id);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    name = task.getResult().getString("NAME");
                    email = user.getEmail();
                    phone = task.getResult().getString("PHONE");
                    address = task.getResult().getString("ADDRESS");
                    birthday = task.getResult().getString("BIRTHDAY");
                    Role = task.getResult().getLong("ROLE");
                    String userImageLink = "";
                    edtUserName.setText(name);
                    edtDate.setText(birthday);
                    edtPhone.setText(phone);
                    edtEmail.setText(email);
                    edtAddress.setText(address);
                } else {
                    Toast.makeText(getActivity(), "vui long dang nhap lai!:", Toast.LENGTH_LONG).show();
                }
            }
        });
        return Role;
    }

    public void updateUser() {
        name = edtUserName.getText().toString();
        email = edtEmail.getText().toString();
        birthday = edtDate.getText().toString();
        phone = edtPhone.getText().toString();
        address = edtAddress.getText().toString();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "Name is empty !", Toast.LENGTH_LONG).show();
            edtUserName.requestFocus();
            edtDate.setFocusable(false);
            edtPhone.setFocusable(false);
            edtEmail.setFocusable(false);
            edtAddress.setFocusable(false);
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Email is empty !", Toast.LENGTH_LONG).show();
            edtUserName.setFocusable(false);
            edtDate.setFocusable(false);
            edtPhone.setFocusable(false);
            edtEmail.requestFocus();
            edtAddress.setFocusable(false);
            return;
        }
        if (TextUtils.isEmpty(birthday)) {
            Toast.makeText(getActivity(), "Birthday is empty !", Toast.LENGTH_LONG).show();
            edtUserName.setFocusable(false);
            edtDate.requestFocus();
            edtPhone.setFocusable(false);
            edtEmail.setFocusable(false);
            edtAddress.setFocusable(false);
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getActivity(), "Phonenumber is empty !", Toast.LENGTH_LONG).show();
            edtUserName.setFocusable(false);
            edtDate.setFocusable(false);
            edtPhone.requestFocus();
            edtEmail.setFocusable(false);
            edtAddress.setFocusable(false);
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(getActivity(), "Address is empty !", Toast.LENGTH_LONG).show();
            edtUserName.setFocusable(false);
            edtDate.setFocusable(false);
            edtPhone.setFocusable(false);
            edtEmail.setFocusable(false);
            edtAddress.requestFocus();
            return;
        }
        DocumentReference documentReference = database.collection("USER").document(id);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("NAME", name);
                            user.put("ADDRESS", address);
                            user.put("BIRTHDAY", birthday);
                            user.put("PHONE", phone);
                            user.put("ROLE", Role);
                            documentReference.set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getActivity(), "Update success", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "Update failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

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
            Picasso.with(getActivity()).load(mImageUri).into(profilePic);
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

    private void uploadFile() {
        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        imageEmail = email.replace(".com", "");
        fileRefernce = storageRef.child(imageEmail + "." + getMimeType(getContext(), mImageUri));
        auth = FirebaseAuth.getInstance();
        if (mImageUri != null) {

            fileRefernce.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "image updated !", Toast.LENGTH_LONG).show();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "User image not updated", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_LONG).show();
        }

        try {
            getUserImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getUserImage() throws IOException {
        storageRef = FirebaseStorage.getInstance().getReference().child(imageEmail + "." + fileTail);

        File localFile = File.createTempFile(imageEmail, "jpg");

        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(getContext(), "image retrieved", Toast.LENGTH_LONG).show();
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                profilePic.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "you have no User's image!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}