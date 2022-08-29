package com.example.myapplication.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ChangePasswordFragment extends Fragment {

    private EditText edtOldPassword, edtPassword_Change, edtRepeatPassword_Change;
    private Button btnOk_Change, btnCancel;
    FirebaseAuth auth;
    FirebaseFirestore database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_change_password, container, false);
        return root;
    }


    public void OnClickChangePass() {
        String oldPassword = edtOldPassword.getText().toString().trim();
        String password_Change = edtPassword_Change.getText().toString();
        String rppassword_Change = edtRepeatPassword_Change.getText().toString();
        LoginFragment Loginfrmnt = new LoginFragment();
        if (!oldPassword.equals(Loginfrmnt.getPass())) {
            Toast.makeText(getActivity(), "Enter wrong old password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password_Change)) {
            Toast.makeText(getActivity(), "Enter new password", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(rppassword_Change)) {
            Toast.makeText(getActivity(), "Password is empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (!rppassword_Change.equals(password_Change)) {
            Toast.makeText(getActivity(), "Incorrect password!", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(password_Change)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "User password updated", Toast.LENGTH_LONG).show();
                            auth.signOut();
                            getActivity().finishAffinity();
                            Intent intent = new Intent(getContext(), SignUpActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Error , Please check again !", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtOldPassword = view.findViewById(R.id.edtOldPassword);
        edtPassword_Change = view.findViewById(R.id.edtPassword_Change);
        edtRepeatPassword_Change = view.findViewById(R.id.edtRepeatPassword_Change);
        btnOk_Change = view.findViewById(R.id.btnOk_Change);
        btnCancel = view.findViewById(R.id.btnCancel);
        auth = FirebaseAuth.getInstance();
        auth.getCurrentUser();
        database = FirebaseFirestore.getInstance();
        btnOk_Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClickChangePass();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtOldPassword.setText("");
                edtPassword_Change.setText("");
                edtRepeatPassword_Change.setText("");
            }
        });
    }
}