package com.shagii.brandsnmobile;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import android.widget.Toolbar;            
//the above android toolbar shows error to import method setSupportActionBar()
//needed androidx.appcompat.widget.Toolbar

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    PostAdapter adapter;
    //we will Use this boolean in scrolling view to start loading data
    Boolean isScrolling = false;
    //for fetching items we need the 3 int variables to manage its as
    int currentItems, totalItems, scrollOutItems;
    //now we here initialize Token with empty string as below for next page view after 10 post
    String token = "";
    //Here we created our object using Spinkitview to use it in Progress Bar
    SpinKitView progress;

    List<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.postList);
        manager = new LinearLayoutManager(this);
        adapter = new PostAdapter(this, items);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        //here initiate the id of progress bar
        progress = findViewById(R.id.spin_kit);

        setUpToolbar();
        navigationView = findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this, "Clicked Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_android:
                        Toast.makeText(MainActivity.this, "Clicked Android", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_ios:
                        Toast.makeText(MainActivity.this, "Clicked IOS", Toast.LENGTH_SHORT).show();
                        break;

                }
                return false;
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {   //When Scroll Started this method will be in call
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //When Scroll completed this method will be in call
                super.onScrolled(recyclerView, dx, dy);

                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    //data fetch
                    isScrolling = false;
                    getData();
                }
            }
        });
        getData();
    }

    private void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    //This Method Call BloggerAPI
    private void getData() {
        //creating Url using Blogger API
        String url = BloggerAPI.url + "?key=" + BloggerAPI.key;
        //for not going auto next token we implement if method
        if (token != "") {
            url = url + "&pageToken=" + token;
        }
        if (token == null){
            return;
        }
        //here we initiate visiblity of progress bar
        progress.setVisibility(View.VISIBLE);
        /*this way we implemented before creating the token method to initiate next page
        Call<PostList> postList = BloggerAPI.getService().getPostList();*/

        Call<PostList> postList = BloggerAPI.getService().getPostList(url);
        //for calling its API
        postList.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                PostList list = response.body();
                //here we initiate that nextpage token Here we are implementing our infinite data scrolling event
                token = list.getNextPageToken();
                items.addAll(list.getItems());
                adapter.notifyDataSetChanged();
                /*the below method is used previous for fetching items from list
                in website
              recyclerView.setAdapter(new PostAdapter(MainActivity.this, list.getItems()));*/
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
               //Here if the data will stop then visibility of progress bar gone
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
            }
        });

    }

}