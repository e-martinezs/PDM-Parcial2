package com.example.pdmparcial2.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.example.pdmparcial2.api.APIRequest;
import com.example.pdmparcial2.database.NewViewModel;
import com.example.pdmparcial2.fragments.NewsListFragment;
import com.example.pdmparcial2.fragments.PlayerListFragment;
import com.example.pdmparcial2.model.Category;
import com.example.pdmparcial2.model.New;
import com.example.pdmparcial2.model.Player;
import com.example.pdmparcial2.utils.ActivityManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NewViewModel newViewModel;
    private APIRequest apiRequest;
    private FragmentManager fragmentManager;
    private NewsListFragment newsListFragment;
    private PlayerListFragment playerListFragment;
    private String selectedCategory = "all";
    private List<New> allNews;
    private List<Player> allPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View loadingLayout = findViewById(R.id.loadingLayout);

        newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);
        apiRequest = new APIRequest(this, loadingLayout, newViewModel);
        login();

        setupToolbar();

        fragmentManager = getSupportFragmentManager();
        newsListFragment = new NewsListFragment();
        playerListFragment = new PlayerListFragment();

        setupDrawer();

        /*newsListFragment.setViewModel(newViewModel);
        newViewModel.getNews().observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> news) {
                newsListFragment.setNewsList(news, selectedCategory);
                allNews = news;
            }
        });
        newViewModel.getCategories().observe(this, new Observer<List<Category>>() {
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
        newViewModel.getPlayers().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(@Nullable List<Player> players) {
                playerListFragment.setPlayerList(players, selectedCategory);
                allPlayers = players;
            }
        });
        newViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loading) {
                RelativeLayout loadingLayout = findViewById(R.id.loadingLayout);
                if (loading) {
                    loadingLayout.setVisibility(View.VISIBLE);
                } else {
                    loadingLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (!newViewModel.getLogged().getValue()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }*/
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

    private void setupToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void setupDrawer(){
        final TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.addFragment(newsListFragment, "News");
        viewPagerAdapter.addFragment(playerListFragment, "Top Players");
        viewPagerAdapter.setCount(1);
        tabLayout.setupWithViewPager(viewPager);

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
                        tabLayout.getTabAt(0).select();
                        selectedCategory = "all";
                        viewPagerAdapter.setCount(1);
                        newsListFragment.setNewsList(allNews, selectedCategory);
                        break;
                    case R.id.drawerFavorites:
                        tabLayout.getTabAt(0).select();
                        selectedCategory = "favorites";
                        viewPagerAdapter.setCount(1);
                        newsListFragment.setNewsList(allNews, selectedCategory);
                        break;
                    case R.id.drawerLogout:
                        logout();
                        break;
                }
                if (item.getGroupId() == R.id.drawerGameMenu) {
                    /*tabLayout.setVisibility(View.VISIBLE);
                    subMenu.setGroupCheckable(R.id.drawerGameMenu, true, true);
                    selectedCategory = item.getTitle().toString();
                    viewPagerAdapter.setCount(2);
                    newsListFragment.setNewsList(allNews, selectedCategory);
                    playerListFragment.setPlayerList(allPlayers, selectedCategory);*/
                } else {
                    tabLayout.setVisibility(View.GONE);
                }
                //newViewModel.refresh();
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        newViewModel.refresh();
    }*/

    private void login(){
        if (!apiRequest.isLogged()){
            ActivityManager.openLoginActivity(this);
            finish();
        }
    }

    private void logout(){
        apiRequest.logout();
        ActivityManager.openMainActivity(this);
        finish();
    }
}
