package com.example.myapplication.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.BookAllAdapter;
import com.example.myapplication.model.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    EditText search_box;
    ImageView ivSearch;

    private List<Book> viewAllBookList;
    private RecyclerView recyclerViewSearch;
    private BookAllAdapter bookAllAdapter;
    //List
    FirebaseFirestore firestore;


    //Slide

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        firestore = FirebaseFirestore.getInstance();
        recyclerViewSearch = root.findViewById(R.id.search_rec);
        search_box = root.findViewById(R.id.edtSearchBox);
        ivSearch = root.findViewById(R.id.exitSearch);
        viewAllBookList = new ArrayList<>();
        bookAllAdapter = new BookAllAdapter(getContext(), viewAllBookList);
        recyclerViewSearch.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewSearch.setAdapter(bookAllAdapter);
        recyclerViewSearch.setHasFixedSize(true);
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewAllBookList.clear();
                bookAllAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String editable = search_box.getText().toString();
                if (editable.toString().isEmpty()) {
                    viewAllBookList.clear();
                    bookAllAdapter.notifyDataSetChanged();

                } else {
                    viewAllBookList.clear();
                    bookAllAdapter.notifyDataSetChanged();
                    searchProduct(editable.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Fragment();
                fragment = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.push_up_in,R.anim.push_down_out)
                        .replace(R.id.frame,fragment).commit();
            }
        });
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        viewAllBookList.clear();
        bookAllAdapter.notifyDataSetChanged();

    }
    private void searchProduct(String book) {
        if (!book.isEmpty()) {
            firestore.collection("BOOK").orderBy("TITLE").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                String title = document.getString("TITLE");
                                if (title.contains(book)) {
                                    Book bookfound = document.toObject(Book.class);
                                    String id  = document.getId();
                                    bookfound.setDOCUMENTID(id);
                                    viewAllBookList.add(bookfound);
                                    bookAllAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
    }
}