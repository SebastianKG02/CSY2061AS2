package com.github.sebastiankg02.csy2061as2.fragments;

import android.os.Bundle;
import android.util.Log;
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

public class ForgotPasswordFragment extends Fragment {
    private View masterLayout;
    private EditText usernameEnter;
    private EditText nameEnter;
    private EditText addressEnter;
    private EditText newPasswordEnter;
    private EditText confirmPasswordEnter;

    private Button resetButton;
    private Button clearButton;
    private Button cancelButton;

    public ForgotPasswordFragment() {
        super(R.layout.fragment_forgot_password);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        masterLayout = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        return masterLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameEnter = (EditText) masterLayout.findViewById(R.id.forgotUsernameEnter);
        nameEnter = (EditText) masterLayout.findViewById(R.id.forgotNameEnter);
        addressEnter = (EditText) masterLayout.findViewById(R.id.forgotAddressEnter);
        newPasswordEnter = (EditText) masterLayout.findViewById(R.id.forgotNewPasswordEnter);
        confirmPasswordEnter = (EditText) masterLayout.findViewById(R.id.forgotConfirmPasswordEnter);

        resetButton = (Button) masterLayout.findViewById(R.id.forgotResetButton);
        clearButton = (Button) masterLayout.findViewById(R.id.forgotClearButton);
        cancelButton = (Button) masterLayout.findViewById(R.id.forgotCancelButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEnter.getText().toString();
                String fullname = nameEnter.getText().toString();
                String address = addressEnter.getText().toString();
                String password = newPasswordEnter.getText().toString();
                String passwordConf = confirmPasswordEnter.getText().toString();

                User.DBHelper userHelper = new User.DBHelper(getActivity().getApplicationContext(), "User", null, 1);
                boolean userFound = false;
                User likelyUser = null;

                for(User u: userHelper.getUsers()){
                    Log.i("UAC", "Forgot PW: " + u.email);
                    if(u.email.equals(username)){
                        userFound = true;
                        likelyUser = u;
                    }
                }

                if(userFound){
                    if(likelyUser.fullName.equals(fullname) && likelyUser.address.equals(address)){
                        if(!password.isEmpty() && !passwordConf.isEmpty() && password.equals(passwordConf)){
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
                                    likelyUser.password = password;
                                    userHelper.updateUser(likelyUser, username);
                                    Snackbar.make(masterLayout, R.string.forgot_pw_success, Snackbar.LENGTH_SHORT).show();
                                    MainActivity.globalNavigation.navigate(R.id.loginFragment);
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
                        } else if (password.isEmpty()){
                            Snackbar.make(masterLayout, R.string.register_password_empty, Snackbar.LENGTH_SHORT).show();
                        } else if (passwordConf.isEmpty()){
                            Snackbar.make(masterLayout, R.string.register_password2_empty, Snackbar.LENGTH_SHORT).show();
                        } else if (!password.equals(passwordConf)){
                            Snackbar.make(masterLayout, R.string.register_password_no_match, Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(masterLayout, R.string.unknown_error, Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(masterLayout, R.string.forgot_pw_wrong_details, Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(masterLayout, R.string.forgot_pw_no_user, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameEnter.setText("");
                nameEnter.setText("");
                addressEnter.setText("");
                newPasswordEnter.setText("");
                confirmPasswordEnter.setText("");

                Snackbar.make(view, "All fields cleared", Snackbar.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
            }
        });
    }
}