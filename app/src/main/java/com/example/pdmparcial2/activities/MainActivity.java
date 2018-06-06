package com.example.pdmparcial2.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.adapter.ViewPagerAdapter;
import com.example.pdmparcial2.database.NewViewModel;
import com.example.pdmparcial2.fragments.NewsListFragment;
import com.example.pdmparcial2.model.Category;
import com.example.pdmparcial2.model.New;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NewViewModel newViewModel;
    private NewsListFragment newsListFragment;
    private String selectedCategory = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        FragmentManager fragmentManager = getSupportFragmentManager();
        newsListFragment = new NewsListFragment();

        final TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.addFragment(newsListFragment, "News");
        tabLayout.setupWithViewPager(viewPager);

        /*FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentView, newsListFragment);
        fragmentTransaction.commit();*/

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        final Menu menu = navigationView.getMenu();
        final SubMenu subMenu = menu.findItem(R.id.drawerGames).getSubMenu();
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawerNews:
                        selectedCategory = "all";
                        break;
                }
                if (item.getGroupId() == R.id.drawerGameMenu) {
                    tabLayout.setVisibility(View.VISIBLE);
                    subMenu.setGroupCheckable(R.id.drawerGameMenu, true, true);
                    selectedCategory = item.getTitle().toString();
                } else {
                    tabLayout.setVisibility(View.GONE);
                }
                newsListFragment.setNewsList(new ArrayList<New>(), "all");
                newViewModel.refresh();
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });

        newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);
        newViewModel.getAllNews().observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> news) {
                if (newsListFragment != null) {
                    newsListFragment.setNewsList(news, selectedCategory);
                }
            }
        });
        newViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                subMenu.clear();
                int i = 0;
                for (Category c : categories) {
                    subMenu.add(R.id.drawerGameMenu, i, i, c.getName());
                    i++;
                }
            }
        });
        newViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                RelativeLayout loadingLayout = findViewById(R.id.loadingLayout);
                if (aBoolean) {
                    loadingLayout.setVisibility(View.VISIBLE);
                } else {
                    loadingLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.refreshButton:
                newViewModel.refresh();
                return true;
        }
        return false;
    }
}
