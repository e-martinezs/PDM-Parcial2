package com.example.pdmparcial2.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.api.APIRequest;
import com.example.pdmparcial2.database.NewViewModel;

public class LoginActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final RelativeLayout loadingLayout = findViewById(R.id.login_loadingLayout);
        final EditText usernameEditText = findViewById(R.id.login_usernameEditText);
        final EditText passwordEditText = findViewById(R.id.login_passwordEditText);
        final Button loginButton = findViewById(R.id.login_loginButton);

        final NewViewModel newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);
        final APIRequest apiRequest = new APIRequest(this, newViewModel);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (username.matches("") || password.matches("")){
                    Toast.makeText(getApplicationContext(), "Fields must not be empty", Toast.LENGTH_SHORT).show();
                }else{
                    //newViewModel.login(username, password);
                    apiRequest.login(username, password);
                }
            }
        });

        /*newViewModel.getLogged().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        newViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (!s.isEmpty()){
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                }
            }
        });
        newViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loading) {
                if (loading){
                    loadingLayout.setVisibility(View.VISIBLE);
                }else{
                    loadingLayout.setVisibility(View.GONE);
                }
            }
        });*/
    }
}
