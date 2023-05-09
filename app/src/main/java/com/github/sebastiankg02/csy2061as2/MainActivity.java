package com.github.sebastiankg02.csy2061as2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.github.sebastiankg02.csy2061as2.user.User;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private AppBarConfiguration appBarConfig;
    private ActionBarDrawerToggle toggle;
    public static NavigationView navView;
    public static NavController globalNavigation;
    public static User currentLoggedInUser;

    public static boolean hasLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set toolbar up
        NavHostFragment navFrag = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        globalNavigation = navFrag.getNavController();
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        //drawer.setFocusable(false);
        appBarConfig = new AppBarConfiguration.Builder(globalNavigation.getGraph()).setOpenableLayout(drawer).build();

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
        NavigationUI.setupActionBarWithNavController(this, globalNavigation, appBarConfig);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Control program flow - if a previous login has been detected, skip to landing page & not login page
        User.DBHelper userHelper = new User.DBHelper(getApplicationContext(), "User", null, 1);
        userHelper.addUser(new User());

        SharedPreferences prefs = getSharedPreferences("kwesiData", Context.MODE_PRIVATE);
        hasLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if(hasLoggedIn){
            String lastEmail = prefs.getString("loggedUser", "");
            if(!lastEmail.isEmpty()){
                for(User u: userHelper.getUsers()){
                    if(u.email.equals(lastEmail)){
                        navView.inflateMenu(R.menu.drawer);
                        currentLoggedInUser = u;
                        globalNavigation.navigate(R.id.basketFragment);
                        TextView header = (TextView)navView.getHeaderView(0).findViewById(R.id.userDrawerNameSection);
                        header.setText("Welcome, " + u.fullName);
                    }
                }
            } else {
                SharedPreferences.Editor prefEditor = prefs.edit();
                prefEditor.putBoolean("isLoggedIn", false);
                prefEditor.apply();
                navView.inflateMenu(R.menu.drawer_login);
            }
        } else {
            navView.inflateMenu(R.menu.drawer_login);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    public boolean selectItem(MenuItem item){
        return onOptionsItemSelected(item);
    }

    public static void changeDrawer(int newDrawerMenu){
        navView.getMenu().clear();
        navView.inflateMenu(newDrawerMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.bugReportOption:
                Snackbar.make(navView, "BUG REPORT OPTION TBC.", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.exitOption:
                this.finishAffinity();
                break;
            case R.id.getSupportOption:
                Snackbar.make(navView, "GET SUPPORT REPORT OPTION TBC.", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.logoutOption:
                if(hasLoggedIn) {
                    AlertDialog logoutDialog = new AlertDialog.Builder(this, R.style.PauseDialog)
                            .setTitle(R.string.logout)
                            .setMessage(R.string.logout_message)
                            .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    SharedPreferences prefs = getSharedPreferences("kwesiData", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("isLoggedIn", false);
                                    editor.putString("loggedUser", "");
                                    editor.apply();
                                    hasLoggedIn = false;
                                    ((TextView)navView.findViewById(R.id.userDrawerNameSection)).setText(R.string.app_title);
                                    changeDrawer(R.menu.drawer_login);
                                    globalNavigation.navigate(R.id.loginFragment);
                                }
                            })
                            .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.cancel();
                                }
                            }).show();

                    logoutDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white));
                    logoutDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.white));
                } else {
                    Snackbar.make(navView, R.string.logout_no_user, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.forgotPasswordFragment:
                Snackbar.make(navView, "SHOULD GO TO FORGOT PASSWORD & CLOSE", Snackbar.LENGTH_SHORT).show();
                break;
        }

        //Close drawer if open
        if(drawer.isDrawerOpen(navView)){
            drawer.closeDrawer(navView);
        }

        //Navigate to appropriate fragment or process item further
        return NavigationUI.onNavDestinationSelected(item, globalNavigation)
                || super.onOptionsItemSelected(item);
    }

}