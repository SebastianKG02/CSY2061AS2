package com.github.sebastiankg02.csy2061as2.fragments;

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

import com.github.sebastiankg02.csy2061as2.R;
import com.google.android.material.snackbar.Snackbar;

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