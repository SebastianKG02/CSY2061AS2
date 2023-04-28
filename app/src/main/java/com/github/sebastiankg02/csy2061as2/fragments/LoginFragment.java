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