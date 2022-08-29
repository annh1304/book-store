package com.example.myapplication.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.BillFragment;
import com.example.myapplication.fragment.BookFragment;
import com.example.myapplication.fragment.CartFragment;
import com.example.myapplication.fragment.ChangePasswordFragment;
import com.example.myapplication.fragment.ContactFragment;
import com.example.myapplication.fragment.ExitFragment;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.InsertBookFragment;
import com.example.myapplication.fragment.RevenueFragment;
import com.example.myapplication.fragment.SearchFragment;
import com.example.myapplication.fragment.TopFragment;
import com.example.myapplication.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    int startingPosition;
    BottomNavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TextView title;
    ImageView ivsearch;
    FragmentManager manager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new HomeFragment(), 1);
        drawerLayout = this.findViewById(R.id.drawerLayout_main);
        toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.bottom_nav);
        title = findViewById(R.id.title);
        ivsearch = findViewById(R.id.ivsearch);
        navigationView.setOnNavigationItemSelectedListener(navlistener);
        NavigationView navigationView_main = findViewById(R.id.navView_main);
        navigationView_main.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = new Fragment();
                switch (id) {
                    case R.id.nav_trangchu:
                        fragment = new HomeFragment();
                        title.setText("BOOK STORE");
                        break;
                    case R.id.nav_sach:
                        fragment = new BookFragment();
                        title.setText("ALL OF BOOK");
                        break;
                    case R.id.nav_lienhe:
                        fragment = new ContactFragment();
                        title.setText("CONTACT US");
                        break;
                    case R.id.nav_doimatkhau:
                        fragment = new ChangePasswordFragment();
                        title.setText("CHANGE PASSWORD");
                        break;
                }
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null).setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.frame, fragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            HomeFragment fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame,fragment).commit();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();

        ivsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Fragment();
                fragment = new SearchFragment();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.push_up_in,R.anim.push_down_out)
                        .replace(R.id.frame,fragment).commit();

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment selectfrg = null;
            int newPosition = 0;
            switch (item.getItemId()) {
                case R.id.home:
                    selectfrg = new HomeFragment();
                    title.setText("HOME");
                    newPosition = 1 ;
                    break;
                case R.id.cart:
                    selectfrg = new CartFragment();
                    title.setText("CART");
                    newPosition = 2 ;
                    break;
                case R.id.list_bottom:
                    selectfrg = new BillFragment();
                    title.setText("BILL");
                    newPosition = 3 ;
                    break;
                case R.id.user:
                    selectfrg = new UserFragment();
                    title.setText("USER");
                    newPosition = 4 ;
                    break;
                case R.id.exit:
                    selectfrg = new ExitFragment();
                    title.setText("EXIT");
                    newPosition = 5 ;
                    break;
            }
//            getSupportFragmentManager().beginTransaction().setCustomAnimations(
//                    .addToBackStack(null).replace(R.id.frame, selectfrg).commit();
            return loadFragment(selectfrg, newPosition);
        }
    };
    private boolean loadFragment(Fragment fragment, int newPosition) {
        if(fragment != null) {
            if(startingPosition > newPosition) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right );
                transaction.addToBackStack(null);
                transaction.replace(R.id.frame, fragment);
                transaction.commit();
            }
            if(startingPosition < newPosition) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.addToBackStack(null);
                transaction.replace(R.id.frame, fragment);
                transaction.commit();
            }
            startingPosition = newPosition;
            return true;
        }

        return false;
    }

}