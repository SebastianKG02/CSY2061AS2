package com.github.sebastiankg02.csy2061as2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.github.sebastiankg02.csy2061as2.MainActivity;
import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.user.User;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;

public class RegisterFragment extends Fragment {
    private View masterLayout;
    private Button registerButton;
    private Button clearButton;
    private Button cancelButton;
    private EditText usernameEnter;
    private EditText fullNameEnter;
    private EditText passwordEnter;
    private EditText passwordConfirm;
    private EditText addressEnter;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerButton = (Button) masterLayout.findViewById(R.id.registerUserButton);
        clearButton = (Button) masterLayout.findViewById(R.id.registerClearButton);
        cancelButton = (Button) masterLayout.findViewById(R.id.registerCancelButton);

        usernameEnter = (EditText) masterLayout.findViewById(R.id.registerUsernameEnter);
        fullNameEnter = (EditText) masterLayout.findViewById(R.id.registerFullNameEnter);
        passwordEnter = (EditText) masterLayout.findViewById(R.id.registerPasswordEnter);
        passwordConfirm = (EditText) masterLayout.findViewById(R.id.registerPasswordConfirm);
        addressEnter = (EditText) masterLayout.findViewById(R.id.registerAddressEnter);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.DBHelper userHelper = new User.DBHelper(getContext());
                String username = usernameEnter.getText().toString();
                String fullName = fullNameEnter.getText().toString();
                String password = passwordEnter.getText().toString();
                String passwordConf = passwordConfirm.getText().toString();
                String address = addressEnter.getText().toString();

                if(!username.isEmpty() && !fullName.isEmpty() && !password.isEmpty() && password.equals(passwordConf) && !address.isEmpty()){
                    boolean hasUppercase = false, hasLowercase = false, hasNumber = false;

                    if(password.length() > User.passwordMinChars) {
                        for (char c : User.passwordChars) {
                            if (password.contains("" + c)) {
                                hasLowercase = true;
                            }
                            if (password.toUpperCase().contains("" + String.valueOf(c).toUpperCase())) {
                                hasUppercase = true;
                            }

                            if (hasLowercase && hasUppercase) {
                                break;
                            }
                        }

                        for (char c : User.passwordNumbers) {
                            if (password.contains("" + c)) {
                                hasNumber = true;
                                break;
                            }
                        }

                        if (hasUppercase && hasLowercase && hasNumber) {
                            User newUser = new User(userHelper.getUsers().size(), fullName, username, LocalDateTime.now(), LocalDateTime.now(), password, address);
                            if (userHelper.addUser(newUser)){
                                Snackbar.make(masterLayout, R.string.register_user_success, Snackbar.LENGTH_SHORT).show();
                                MainActivity.globalNavigation.navigate(R.id.loginFragment);
                            } else {
                                Snackbar.make(masterLayout, R.string.register_user_exists, Snackbar.LENGTH_SHORT).show();
                            }
                        } else if (!hasLowercase) {
                            Snackbar.make(masterLayout, R.string.register_password_lowercase, Snackbar.LENGTH_SHORT).show();
                        } else if (!hasNumber){
                            Snackbar.make(masterLayout, R.string.register_password_no_number, Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(masterLayout, R.string.register_password_uppercase, Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(masterLayout, R.string.register_password_min_chars, Snackbar.LENGTH_SHORT).show();
                    }

                } else if (username.isEmpty()){
                    Snackbar.make(masterLayout, R.string.register_username_empty, Snackbar.LENGTH_SHORT).show();
                } else if (fullName.isEmpty()){
                    Snackbar.make(masterLayout, R.string.register_fullname_empty, Snackbar.LENGTH_SHORT).show();
                } else if (password.isEmpty()){
                    Snackbar.make(masterLayout, R.string.register_password_empty, Snackbar.LENGTH_SHORT).show();
                } else if (passwordConf.isEmpty()){
                    Snackbar.make(masterLayout, R.string.register_password2_empty, Snackbar.LENGTH_SHORT).show();
                } else if (!password.equals(passwordConf)){
                    Snackbar.make(masterLayout, R.string.register_password_no_match, Snackbar.LENGTH_SHORT).show();
                } else if (address.isEmpty()){
                    Snackbar.make(masterLayout, R.string.register_address_empty, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(masterLayout, R.string.unknown_error, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameEnter.setText("");
                fullNameEnter.setText("");
                passwordEnter.setText("");
                passwordConfirm.setText("");
                addressEnter.setText("");

                Snackbar.make(view, "All fields cleared", Snackbar.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        masterLayout = inflater.inflate(R.layout.fragment_register, container, false);
        return masterLayout;
    }
}