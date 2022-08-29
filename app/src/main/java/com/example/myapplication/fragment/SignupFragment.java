package com.example.myapplication.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupFragment extends Fragment {
    EditText etname, etemail, etpassword, etrepeatPassword;
    Button btnSubmit;
    FirebaseAuth auth;
    FirebaseFirestore database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup, container, false);

//        mobileNumber.setTranslationX(800);
//        email.setTranslationX(800);
//        password.setTranslationX(800);
//        repeatPassword.setTranslationX(800);
//
//        mobileNumber.setAlpha(0);
//        email.setAlpha(0);
//        password.setAlpha(0);
//        repeatPassword.setAlpha(0);
//
//        mobileNumber.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
//        email.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
//        password.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
//        repeatPassword.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etemail = view.findViewById(R.id.edtEmail);
        etpassword = view.findViewById(R.id.edtPassword_SignUp);
        etrepeatPassword = view.findViewById(R.id.edtRepeatPassword);
        etname = view.findViewById(R.id.edtName_SignUp);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
    }

    public void createUser() {
        String name = etname.getText().toString();
        String email = etemail.getText().toString();
        String password = etrepeatPassword.getText().toString();
        String rppassword = etrepeatPassword.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "Username is empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Email is empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Password is empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(rppassword)) {
            Toast.makeText(getActivity(), "Re-enter password", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.equals(rppassword) == false) {
            Toast.makeText(getActivity(), "Password incorrect", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getActivity(),"Password must be longer than 6 characters", Toast.LENGTH_LONG).show();
            return;
        }
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            User user = new User(email,password,name,",",",",0,2);
                            String id = auth.getCurrentUser().getUid();
                            DocumentReference documentReference = database.collection("USER").document(id);
                            Map<String, Object> user = new HashMap<>();
                            user.put("IMAGE", "");
                            user.put("EMAIL", email);
                            user.put("NAME", name);
                            user.put("ADDRESS", "");
                            user.put("BIRTHDAY", "");
                            user.put("PHONE", "");
                            user.put("ROLE", 2);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Sign Up Success", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Sign Up Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}