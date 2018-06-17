package com.example.pdmparcial2.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
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
import android.widget.TextView;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.adapter.ViewPagerAdapter;
import com.example.pdmparcial2.api.APIRequest;
import com.example.pdmparcial2.database.viewmodels.CategoryViewModel;
import com.example.pdmparcial2.database.viewmodels.NewViewModel;
import com.example.pdmparcial2.database.viewmodels.PlayerViewModel;
import com.example.pdmparcial2.fragments.NewsListFragment;
import com.example.pdmparcial2.fragments.PlayerListFragment;
import com.example.pdmparcial2.model.Category;
import com.example.pdmparcial2.utils.ActivityManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String ALL = "all";
    public static String FAVORITES = "favorites";
    private DrawerLayout drawerLayout;
    private NewViewModel newViewModel;
    private PlayerViewModel playerViewModel;
    private CategoryViewModel categoryViewModel;
    private APIRequest apiRequest;
    private FragmentManager fragmentManager;
    private NewsListFragment newsListFragment;
    private PlayerListFragment playerListFragment;
    private String selectedCategory = ALL;
    private SubMenu subMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View loadingLayout = findViewById(R.id.loadingLayout);

        newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        apiRequest = new APIRequest(this, loadingLayout, newViewModel, playerViewModel, categoryViewModel);

        login();
        setupToolbar();
        fragmentManager = getSupportFragmentManager();
        newsListFragment = new NewsListFragment();
        newsListFragment.setApiRequest(apiRequest);
        playerListFragment = new PlayerListFragment();
        setupDrawer();

        categoryViewModel.getCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {

                //Llena el submenu de categorias
                subMenu.clear();
                int i = 0;
                for (Category c : categories) {
                    subMenu.add(R.id.drawerGameMenu, i, i, c.getName().toUpperCase()).setCheckable(true);
                    if (c.getName().matches(selectedCategory)) {
                        subMenu.getItem(i).setChecked(true);
                    }
                    i++;
                }
            }
        });
        refresh();
    }

    //Agrega los botones de Search y Refresh al ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    //Verifica que boton del ActionBar se selecciono
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.refreshButton:
                refresh();
                return true;
        }
        return false;
    }

    //Configura el ActionBar y le pone el boton para abrir el Drawer
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    //Configura el Drawer
    private void setupDrawer() {

        //Configura el ViewPager con sus fragmentos
        final TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.addFragment(newsListFragment, getString(R.string.tab_news));
        viewPagerAdapter.addFragment(playerListFragment, getString(R.string.tab_players));
        viewPagerAdapter.setCount(1);
        tabLayout.setupWithViewPager(viewPager);

        drawerLayout = findViewById(R.id.drawerLayout);
        final NavigationView navigationView = findViewById(R.id.navigationView);
        final Menu menu = navigationView.getMenu();
        subMenu = menu.findItem(R.id.drawerGames).getSubMenu();
        navigationView.getMenu().getItem(0).setChecked(true);

        //Realiza una accion dependiendo de que boton del drawer se selecciona
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.drawerNews:
                        //Abre el fragmento de noticias general
                        tabLayout.getTabAt(0).select();
                        selectedCategory = ALL;
                        viewPagerAdapter.setCount(1);
                        newViewModel.setCategory(selectedCategory);
                        playerViewModel.setCategory(selectedCategory);
                        break;

                    case R.id.drawerFavorites:
                        //Abre el fragmento de favoritos
                        tabLayout.getTabAt(0).select();
                        selectedCategory = FAVORITES;
                        viewPagerAdapter.setCount(1);
                        newViewModel.setCategory(selectedCategory);
                        playerViewModel.setCategory(selectedCategory);
                        break;

                    case R.id.drawerSettings:
                        //Abre el fragmento de noticias general
                        navigationView.getMenu().getItem(0).setChecked(true);
                        tabLayout.getTabAt(0).select();
                        selectedCategory = ALL;
                        viewPagerAdapter.setCount(1);
                        newViewModel.setCategory(selectedCategory);
                        playerViewModel.setCategory(selectedCategory);

                        openSettings();
                        break;

                    case R.id.drawerLogout:
                        logout();
                        break;
                }

                //Verifica cual de los botones de categorias se seleccciono y abre el fragmento por categoria
                if (item.getGroupId() == R.id.drawerGameMenu) {
                    tabLayout.setVisibility(View.VISIBLE);
                    subMenu.setGroupCheckable(R.id.drawerGameMenu, true, true);
                    selectedCategory = item.getTitle().toString().toLowerCase();
                    viewPagerAdapter.setCount(2);
                    newViewModel.setCategory(selectedCategory);
                    playerViewModel.setCategory(selectedCategory);
                } else {
                    tabLayout.setVisibility(View.GONE);
                }

                refresh();
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    //Verifica si el usuario esta loguado, si no abre la actividad de login
    private void login() {
        if (!apiRequest.isLogged()) {
            ActivityManager.openLoginActivity(this);
            finish();
        }
    }

    //Manda un request de logout al API y abre la actividad de login
    private void logout() {
        apiRequest.logout();
        ActivityManager.openLoginActivity(this);
        finish();
    }

    //Abre la actividad de settings
    private void openSettings() {
        ActivityManager.openSettingsActivity(this);
    }

    //Obtiene los datos del API y pone el nombre de usuario en el header del Drawer
    private void refresh() {
        apiRequest.refresh();

        final NavigationView navigationView = findViewById(R.id.navigationView);
        View header = navigationView.getHeaderView(0);
        TextView usernameTextView = header.findViewById(R.id.headerUsernameTextView);
        usernameTextView.setText(apiRequest.getUsername());
    }
}
