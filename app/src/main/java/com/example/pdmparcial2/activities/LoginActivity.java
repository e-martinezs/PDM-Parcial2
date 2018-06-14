package com.example.pdmparcial2.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.database.NewViewModel;

public class LoginActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.login_usernameEditText);
        final EditText passwordEditText = findViewById(R.id.login_passwordEditText);
        Button loginButton = findViewById(R.id.login_loginButton);

        final NewViewModel newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (username.matches("") || password.matches("")){

                }else{
                    newViewModel.login(username, password);
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
