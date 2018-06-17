package com.example.pdmparcial2.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.api.APIRequest;
import com.example.pdmparcial2.database.viewmodels.NewViewModel;
import com.example.pdmparcial2.utils.ActivityManager;

public class SettingsActivity extends AppCompatActivity{

    private APIRequest apiRequest;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final View loadingLayout = findViewById(R.id.settings_loadingLayout);
        final EditText oldPasswordEditText = findViewById(R.id.settings_oldPasswordEditText);
        final EditText newPasswordEditText = findViewById(R.id.settings_newPasswordEditText);
        final EditText repeatPasswordEditText = findViewById(R.id.settings_repeatPasswordEditText);
        final Button saveButton = findViewById(R.id.settings_saveButton);

        final NewViewModel newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);
        apiRequest = new APIRequest(this, loadingLayout, newViewModel, null, null);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = oldPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String repeatPassword = repeatPasswordEditText.getText().toString();
                if (oldPassword.matches("") || newPassword.matches("") || repeatPassword.matches("")){
                    ActivityManager.showToast(getApplicationContext(), getString(R.string.error_empty_fields));
                }else if(!newPassword.matches(repeatPassword)){
                    ActivityManager.showToast(getApplicationContext(), getString(R.string.error_password_notmatch));
                }else{
                    apiRequest.changePassword(oldPassword, newPassword);
                }
            }
        });
    }
}
