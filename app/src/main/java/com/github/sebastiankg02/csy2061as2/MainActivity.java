package com.github.sebastiankg02.csy2061as2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private AppBarConfiguration appBarConfig;
    private ActionBarDrawerToggle toggle;
    private NavigationView navView;
    private LinearLayout navLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set toolbar up
        NavHostFragment navFrag = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        NavController nav = navFrag.getNavController();
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setFocusable(false);
        appBarConfig = new AppBarConfiguration.Builder(nav.getGraph()).setOpenableLayout(drawer).build();

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return selectItem(item);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationUI.setupActionBarWithNavController(this, nav, appBarConfig);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Control program flow - if a previous login has been detected, skip to landing page & not login page
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    public boolean selectItem(MenuItem item){
        return onOptionsItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host);
        switch(item.getItemId()){
            case R.id.bugReportOption:
                Snackbar.make(navView, "BUG REPORT OPTION TBC.", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.exitOption:
                Snackbar.make(navView, "EXIT REPORT OPTION TBC.", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.getSupportOption:
                Snackbar.make(navView, "GET SUPPORT REPORT OPTION TBC.", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.logoutOption:
                Snackbar.make(navView, "LOGOUT REPORT OPTION TBC.", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.forgotPasswordFragment:
                Snackbar.make(navView, "SHOULD GO TO FORGOT PASSWORD & CLOSE", Snackbar.LENGTH_SHORT).show();
                break;
        }
        //Handle general Drawer Logic here
        if(drawer.isDrawerOpen(navView)){
            drawer.closeDrawer(navView);
        }
        Log.i("", "Close");
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

}