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

/**
 * A fragment that displays a registration form for a user to fill out.
 * Contains fields for username, full name, password, password confirmation, and address.
 * Also contains buttons for registering, clearing the form, and cancelling the registration process.
 */
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

    /**
     * Constructs a new RegisterFragment object.
     * This constructor initializes the fragment with the layout resource ID of R.layout.fragment_register.
     */
    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    /**
     * Called when the view is created. Initializes the buttons and text fields and sets up their click listeners.
     * Handle registration logic (password validation, adding new user to database).
     * 
     * @param view The view that was created
     * @param savedInstanceState The saved instance state of the fragment
     */
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

        /*
         * Set an OnClickListener on the registerButton. When clicked, retrieve the user's input from the
         * EditText fields and validate the input. If the input is valid, create a new User object and add it
         * to the database. If the input is invalid, display an appropriate error message using a Snackbar.
         */
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.DBHelper userHelper = new User.DBHelper(getContext());
                String username = usernameEnter.getText().toString();
                String fullName = fullNameEnter.getText().toString();
                String password = passwordEnter.getText().toString();
                String passwordConf = passwordConfirm.getText().toString();
                String address = addressEnter.getText().toString();

                //Check if fields are empty
                if(!username.isEmpty() && !fullName.isEmpty() && !password.isEmpty() && password.equals(passwordConf) && !address.isEmpty()){
                    //Password validation
                    boolean hasUppercase = false, hasLowercase = false, hasNumber = false;

                    if(password.length() >= User.passwordMinChars) {
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
                        
                        //If password is valid, register user, add them to the database and navigate to the login fragment.
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

        //Clear all fields
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

        //Go back to login fragment
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
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
        masterLayout = inflater.inflate(R.layout.fragment_register, container, false);
        return masterLayout;
    }
}