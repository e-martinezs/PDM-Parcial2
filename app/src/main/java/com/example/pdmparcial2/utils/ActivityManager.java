package com.example.pdmparcial2.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.pdmparcial2.activities.LoginActivity;
import com.example.pdmparcial2.activities.MainActivity;
import com.example.pdmparcial2.activities.SettingsActivity;

public class ActivityManager {

    //Abre la actividad main
    public static void openMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    //Abre la actividad login
    public static void openLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    //Abre la actividad settings
    public static void openSettingsActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    //Muestra un mensaje en Toast
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    //Cierra la actividad de context
    public static void closeActivity(Context context) {
        ((AppCompatActivity) context).finish();
    }
}
