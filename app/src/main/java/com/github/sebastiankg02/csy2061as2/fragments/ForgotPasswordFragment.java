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

public class ForgotPasswordFragment extends Fragment {
    private View masterLayout;
    private EditText usernameEnter;
    private EditText nameEnter;
    private EditText hobbiesEnter;
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
        hobbiesEnter = (EditText) masterLayout.findViewById(R.id.forgotHobbiesEnter);
        newPasswordEnter = (EditText) masterLayout.findViewById(R.id.forgotNewPasswordEnter);
        confirmPasswordEnter = (EditText) masterLayout.findViewById(R.id.forgotConfirmPasswordEnter);

        resetButton = (Button) masterLayout.findViewById(R.id.forgotResetButton);
        clearButton = (Button) masterLayout.findViewById(R.id.forgotClearButton);
        cancelButton = (Button) masterLayout.findViewById(R.id.forgotCancelButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameEnter.setText("");
                nameEnter.setText("");
                hobbiesEnter.setText("");
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