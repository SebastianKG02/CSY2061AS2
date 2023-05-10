package com.github.sebastiankg02.csy2061as2.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.sebastiankg02.csy2061as2.MainActivity;
import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.user.User;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;

public class LoginFragment extends Fragment {
    private View masterLayout;
    private Button loginButton;
    private Button forgotButton;
    private Button registerButton;
    private EditText usernameInput;
    private EditText passwordInput;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        masterLayout = inflater.inflate(R.layout.fragment_login, container, false);
        return masterLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton = (Button) masterLayout.findViewById(R.id.loginButton);
        forgotButton = (Button) masterLayout.findViewById(R.id.forgotButton);
        registerButton = (Button) masterLayout.findViewById(R.id.registerButton);

        usernameInput = (EditText) masterLayout.findViewById(R.id.loginUsernameEnter);
        passwordInput = (EditText) masterLayout.findViewById(R.id.loginPasswordEnter);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if(!username.isEmpty() && !password.isEmpty()){
                    User.DBHelper userHelper = new User.DBHelper(getActivity().getApplicationContext(), "User", null, 1);
                    boolean userFound = false;

                    for(User u: userHelper.getUsers()){
                        if(u.email.equals(username)){
                            userFound = true;
                            if(u.password.equals(password)){
                                u.updatedDate = LocalDateTime.now();
                                MainActivity.currentLoggedInUser = u;
                                userHelper.updateUser(u, u.email);
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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });
    }
}