package com.example.pdmparcial2.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.pdmparcial2.activities.LoginActivity;
import com.example.pdmparcial2.activities.MainActivity;

public class ActivityManager {

    public static void openMainActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void openLoginActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
