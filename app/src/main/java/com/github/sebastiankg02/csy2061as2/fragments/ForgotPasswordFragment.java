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

/**
 * A fragment that allows users to reset their password if they have forgotten it.
 * Contains fields for entering username, name, address, new password, and confirm password.
 * Also contains buttons for resetting the password, and clearing the fields, as well as a cancel button
 * that navigates back to the login fragment.
 */
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

    /**
     * Constructs a new instance of the ForgotPasswordFragment.
     * It inflates the fragment_forgot_password layout.
     */
    public ForgotPasswordFragment() {
        super(R.layout.fragment_forgot_password);
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
        masterLayout = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        return masterLayout;
    }

    /**
     * Called when the view is created. Initializes the EditText fields for username, name, address, new password, and confirm password.
     * Also sets up the onClickListeners for the reset, clear and cancel buttons.
     *
     * @param view The view that was created
     * @param savedInstanceState The saved instance state of the fragment
     */
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

        /*
         * Set an OnClickListener on the resetButton. When clicked, retrieve the user's entered
         * username, full name, address, new password, and confirmed password. Then check if the user exists
         * in the database and if the entered full name and address match the user's details. If so, check if
         * the new password meets the password requirements and if the confirmed password matches the new password.
         * If all conditions are met, the user's password is updated in the database and a success message is displayed.
         * The fragment will then navigate back to the login screen.
         * If any of the conditions are not met, an appropriate error message is displayed.
         */
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Load data from fields
                String username = usernameEnter.getText().toString();
                String fullname = nameEnter.getText().toString();
                String address = addressEnter.getText().toString();
                String password = newPasswordEnter.getText().toString();
                String passwordConf = confirmPasswordEnter.getText().toString();

                User.DBHelper userHelper = new User.DBHelper(getContext());
                boolean userFound = false;
                User likelyUser = null;

                //Check if username likely exists in database
                for(User u: userHelper.getUsers()){
                    if(u.email.equals(username)){
                        userFound = true;
                        likelyUser = u;
                    }
                }

                //Check for password requirements, reset password if met, if not, display error messages
                if(userFound){
                    if(likelyUser.fullName.equals(fullname) && likelyUser.address.equals(address)){
                        if(!password.isEmpty() && !passwordConf.isEmpty() && password.equals(passwordConf)){
                            boolean hasUppercase = false, hasLowercase = false, hasNumber = false;

                            if(password.length() >= User.passwordMinChars) {
                                //Check password for matching password rules
                                for (char c : User.passwordChars) {
                                    if (password.contains("" + c)) {
                                        hasLowercase = true;
                                    }
                                    if (password.contains("" + String.valueOf(c).toUpperCase())) {
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

                                //If all requirements ment, reset password & go to login fragment.
                                if (hasUppercase && hasLowercase && hasNumber) {
                                    likelyUser.password = password;
                                    userHelper.updateUser(likelyUser);
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

        //Clear all fields
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

        //Go back to login fragment
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
            }
        });
    }
}