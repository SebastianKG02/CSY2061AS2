package com.github.sebastiankg02.csy2061as2.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.github.sebastiankg02.csy2061as2.MainActivity;
import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.user.User;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;

/**
 * A fragment that displays a login screen with username and password fields, as well as buttons for logging in,
 * registering, and resetting a forgotten password.
 */
public class LoginFragment extends Fragment {
    private View masterLayout;
    private Button loginButton;
    private Button forgotButton;
    private Button registerButton;
    private EditText usernameInput;
    private EditText passwordInput;

    /**
     * Constructs a new LoginFragment object.
     * This constructor sets the layout resource of the fragment to R.layout.fragment_login.
     */
    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        masterLayout = inflater.inflate(R.layout.fragment_login, container, false);
        return masterLayout;
    }

    /**
     * Called when the view is created. Initializes the login, forgot password, and register buttons
     * and sets up their click listeners to navigate to the appropriate fragments.
     *
     * @param view The view that was created
     * @param savedInstanceState The saved instance state of the fragment
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton = (Button) masterLayout.findViewById(R.id.loginButton);
        forgotButton = (Button) masterLayout.findViewById(R.id.forgotButton);
        registerButton = (Button) masterLayout.findViewById(R.id.registerButton);

        usernameInput = (EditText) masterLayout.findViewById(R.id.loginUsernameEnter);
        passwordInput = (EditText) masterLayout.findViewById(R.id.loginPasswordEnter);

        /*
         * Set an OnClickListener on the login button. When clicked, retrieve the username and password
         * from the input fields and checks if they are valid against database records. 
         * If the username and password are valid, log the user in and save their information to SharedPreferences.
         * Only the email is kept, as well as a flag indicating the user has not logged out yet.
         * 
         * If the username or password is invalid, display an error message.
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if(!username.isEmpty() && !password.isEmpty()){
                    User.DBHelper userHelper = new User.DBHelper(getContext());
                    boolean userFound = false;

                    for(User u: userHelper.getUsers()){
                        if(u.email.equals(username)){
                            userFound = true;
                            if(u.password.equals(password)){
                                u.updatedDate = LocalDateTime.now();
                                MainActivity.currentLoggedInUser = u;
                                userHelper.updateUser(u);
                                SharedPreferences prefs = getActivity().getSharedPreferences("kwesiData", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = prefs.edit();
                                edit.putBoolean("isLoggedIn", true);
                                edit.putString("loggedUser", u.email);
                                edit.apply();
                                MainActivity.changeDrawer(R.menu.drawer);
                                MainActivity.hasLoggedIn = true;
                                ((TextView)MainActivity.navView.findViewById(R.id.userDrawerNameSection)).setText("Welcome, " + u.fullName);
                                MainActivity.globalNavigation.navigate(R.id.action_loginFragment_to_basketFragment);
                            } else {
                                Snackbar.make(masterLayout, R.string.password_wrong, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }

                    if(!userFound){
                        Snackbar.make(masterLayout, R.string.forgot_pw_no_user, Snackbar.LENGTH_SHORT).show();
                    }
                } else if (username.isEmpty()){
                    Snackbar.make(masterLayout, R.string.register_username_empty, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(masterLayout, R.string.register_password_empty, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //Go to register new user fragment
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        //Go to forgot password fragment
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });
    }
}