package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.R;
import com.example.myapplication.activities.AdminDetailActivity;
import com.example.myapplication.activities.AdminTypeActivity;
import com.example.myapplication.activities.TypeActivity;
import com.example.myapplication.adapter.BookAdapter;
import com.example.myapplication.adapter.BookAllAdminAdapter;
import com.example.myapplication.adapter.SliderAdapter;
import com.example.myapplication.model.Book;
import com.example.myapplication.model.SliderItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeAdminFragment extends Fragment {
    CardView cvChildren, cvNovel, cvSchool, cvLiterature, cvTechnology,cvPolitics,cvHistory;

    ProgressBar progressBar;
    ScrollView scvHome;

    //List
    FirebaseFirestore firestore;
    RecyclerView recyclerNewBook , recyclerViewTopSell , recyclerViewSale , recyclerViewRecommend;
    List<Book> newlist;
    List<Book> selllist;
    List<Book> salelist;
    List<Book> recommendlist;
    BookAllAdminAdapter bookAdapter , sellAdapter , saleAdapter , recommendAdapter;

    //Slide
    ViewPager2 viewPager2;
    Handler sliderHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_home, container, false);
        firestore = FirebaseFirestore.getInstance();
        recyclerNewBook = root.findViewById(R.id.recyclerNewBook);
        recyclerViewTopSell = root.findViewById(R.id.recyclerViewTopSell);
        recyclerViewSale = root.findViewById(R.id.recyclerViewSale);
        recyclerViewRecommend = root.findViewById(R.id.recyclerViewRecommend);

        scvHome = root.findViewById(R.id.scvHome);
        progressBar = root.findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        scvHome.setVisibility(View.GONE);

        recyclerNewBook.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerViewTopSell.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerViewSale.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerViewRecommend.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        newlist = new ArrayList<>();
        selllist = new ArrayList<>();
        salelist = new ArrayList<>();
        recommendlist = new ArrayList<>();

        bookAdapter = new BookAllAdminAdapter(getActivity(), newlist);
        sellAdapter = new BookAllAdminAdapter(getActivity(), selllist);
        saleAdapter = new BookAllAdminAdapter(getActivity(), salelist);
        recommendAdapter = new BookAllAdminAdapter(getActivity(), recommendlist);

        recyclerNewBook.setAdapter(bookAdapter);
        recyclerViewTopSell.setAdapter(sellAdapter);
        recyclerViewSale.setAdapter(saleAdapter);
        recyclerViewRecommend.setAdapter(recommendAdapter);

        firestore.collection("BOOK").whereEqualTo("STATUS","NEWBOOK").orderBy("TITLE", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Book book = document.toObject(Book.class);
                                String id = document.getId();
                                book.setDOCUMENTID(id);
                                newlist.add(book);
                                bookAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                scvHome.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        firestore.collection("BOOK").whereEqualTo("STATUS","TOPSELL").orderBy("TITLE", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Book book = document.toObject(Book.class);
                                String id = document.getId();
                                book.setDOCUMENTID(id);
                                selllist.add(book);
                                sellAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                scvHome.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        firestore.collection("BOOK").whereEqualTo("STATUS","BOOKSALE").orderBy("TITLE", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Book book = document.toObject(Book.class);
                                String id = document.getId();
                                book.setDOCUMENTID(id);
                                salelist.add(book);
                                saleAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                scvHome.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        firestore.collection("BOOK").whereEqualTo("STATUS","RECOMMEND").orderBy("TITLE", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Book book = document.toObject(Book.class);
                                String id = document.getId();
                                book.setDOCUMENTID(id);
                                recommendlist.add(book);
                                recommendAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                scvHome.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cvChildren = view.findViewById(R.id.cvChildren);
        cvNovel = view.findViewById(R.id.cvNovel);
        cvSchool = view.findViewById(R.id.cvSchool);
        cvLiterature = view.findViewById(R.id.cvLiterature);
        cvTechnology = view.findViewById(R.id.cvTechnology);
        cvPolitics = view.findViewById(R.id.cvPolitics);
        cvHistory = view.findViewById(R.id.cvHistory);

        cvChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdminTypeActivity.class);
                intent.putExtra("type", "Trẻ Em");
                startActivity(intent);
            }
        });
        cvNovel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdminTypeActivity.class);
                intent.putExtra("type", "Tiểu Thuyết");
                startActivity(intent);
            }
        });
        cvSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdminTypeActivity.class);
                intent.putExtra("type", "Giáo Khoa");
                startActivity(intent);
            }
        });
        cvLiterature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdminTypeActivity.class);
                intent.putExtra("type", "Văn Học");
                startActivity(intent);
            }
        });
        cvTechnology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdminTypeActivity.class);
                intent.putExtra("type", "Khoa Học");
                startActivity(intent);
            }
        });
        cvPolitics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdminTypeActivity.class);
                intent.putExtra("type", "Chính Trị");
                startActivity(intent);
            }
        });
        cvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdminTypeActivity.class);
                intent.putExtra("type", "Lịch Sử");
                startActivity(intent);
            }
        });

        viewPager2 = view.findViewById(R.id.ViewPagerImageSlider);
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.a));
        sliderItems.add(new SliderItem(R.drawable.a1));
        sliderItems.add(new SliderItem(R.drawable.a2));
        sliderItems.add(new SliderItem(R.drawable.a3));
        sliderItems.add(new SliderItem(R.drawable.a4));
        sliderItems.add(new SliderItem(R.drawable.a5));

        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 4000);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 4000);
    }
}