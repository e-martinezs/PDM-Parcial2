package com.example.pdmparcial2.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.pdmparcial2.activities.LoginActivity;
import com.example.pdmparcial2.activities.MainActivity;
import com.example.pdmparcial2.activities.SettingsActivity;

public class ActivityManager {

    public static void openMainActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void openLoginActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void openSettingsActivity(Context context){
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void closeActivity(Context context){
        ((AppCompatActivity)context).finish();
    }
}
