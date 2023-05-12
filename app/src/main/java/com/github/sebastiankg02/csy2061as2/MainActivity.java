package com.github.sebastiankg02.csy2061as2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.github.sebastiankg02.csy2061as2.data.Category;
import com.github.sebastiankg02.csy2061as2.data.Order;
import com.github.sebastiankg02.csy2061as2.data.OrderProduct;
import com.github.sebastiankg02.csy2061as2.user.User;
import com.github.sebastiankg02.csy2061as2.user.UserAccessLevel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;

/**
 * The MainActivity class is the main entry point for the application. It sets up the navigation drawer,
 * navigation controller, and handles navigation events.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private AppBarConfiguration appBarConfig;
    private ActionBarDrawerToggle toggle;
    public static NavigationView navView;
    public static NavController globalNavigation;
    public static User currentLoggedInUser;

    public static boolean hasLoggedIn = false;

    /**
     * Called when the activity is starting. This method initializes the activity and sets the content view to the main layout.
     * It also initializes the navigation controller and the drawer layout. If the user is logged in, the navigation drawer menu
     * is set to the logged in state, otherwise it is set to the logged out state.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains
     *                             the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set toolbar up
        NavHostFragment navFrag = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        globalNavigation = navFrag.getNavController();
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        appBarConfig = new AppBarConfiguration.Builder(globalNavigation.getGraph()).setOpenableLayout(drawer).build();

        navView = findViewById(R.id.nav_view);

        //Set drawer navigation functionality up
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
        User.DBHelper userHelper = new User.DBHelper(getApplicationContext());
        Order.DBHelper orderHelper = new Order.DBHelper(getApplicationContext());
        OrderProduct.DBHelper opHelper = new OrderProduct.DBHelper(getApplicationContext());
        Category.DBHelper catHelper = new Category.DBHelper(getApplicationContext());

        User adminUserDefault = new User();
        adminUserDefault.level = UserAccessLevel.ADMIN;
        adminUserDefault.fullName = "Kwesi James";
        adminUserDefault.registeredDate = LocalDateTime.now();
        adminUserDefault.updatedDate = LocalDateTime.now();
        adminUserDefault.email = "admin@kwesijames.shop";
        adminUserDefault.password = "HelloWorld1";
        adminUserDefault.address = "N/A";
        adminUserDefault.hobbies = "";

        userHelper.addUser(adminUserDefault);

        SharedPreferences prefs = getSharedPreferences("kwesiData", Context.MODE_PRIVATE);
        hasLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if(hasLoggedIn){
            String lastEmail = prefs.getString("loggedUser", "");
            if(!lastEmail.isEmpty()){
                for(User u: userHelper.getUsers()){
                    if(u.email.equals(lastEmail)){
                        navView.inflateMenu(R.menu.drawer);
                        currentLoggedInUser = u;
                        globalNavigation.navigate(R.id.action_loginFragment_to_basketFragment);
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

    /**
     * Inflates the options menu when the activity is created.
     *
     * @param menu The menu to inflate
     * @return true if the menu was successfully inflated, false otherwise
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    /**
     * Selects an item from the menu and performs the corresponding action.
     *
     * @param item The item to be selected
     * @return True if the item was successfully selected and its action performed, false otherwise
     */
    public boolean selectItem(MenuItem item){
        return onOptionsItemSelected(item);
    }

    /**
     * Changes the drawer menu to the specified menu resource ID.
     *
     * @param newDrawerMenu The resource ID of the new drawer menu
     */
    public static void changeDrawer(int newDrawerMenu){
        navView.getMenu().clear();
        navView.inflateMenu(newDrawerMenu);
    }

    /**
     * Called when an options menu item is selected. Handles the selection of different menu items.
     *
     * @param item The selected menu item
     * @return True if the item was handled, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.bugReportOption:
                Snackbar.make(navView, "BUG REPORT OPTION TBC.", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.exitOption:
                //exit app
                this.finishAffinity();
                break;
            case R.id.getSupportOption:
                Snackbar.make(navView, "GET SUPPORT REPORT OPTION TBC.", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.logoutOption:
                /*
                 * Display a logout dialog if the user has logged in, otherwise display a Snackbar message.
                 * If the user confirms the logout, their login status is updated in SharedPreferences and
                 * the UI is updated accordingly. The navigation stack is cleared and the user is navigated
                 * to the login fragment.
                 */
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
                                    globalNavigation.popBackStack(R.id.basketFragment, true);
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