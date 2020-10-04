package com.saptco.dispatcher.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.saptco.dispatcher.R;
import com.saptco.dispatcher.data.model.LoggedInUser;
import com.saptco.dispatcher.data.repositories.DispatcherRepository;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private Boolean isArabic = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button langButton = findViewById(R.id.langButton);
        final Button loginButton = findViewById(R.id.login);

        String lang = getIntent().getStringExtra("lang");
        if(lang == null || lang.equals(""))
            changeLocale("ar");
        else if(lang.equals("ar"))
            isArabic = true;
        else
            isArabic = false;

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                loginAction(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                return false;
            }
        });

        langButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLocale(isArabic ? "en" : "ar");
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAction(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
            }
        });
    }

    private void loginAction(String userName, String password){
        try {
            if(userName != null && !userName.isEmpty() && password != null && !password.isEmpty()) {
                LoggedInUser userInf = DispatcherRepository.getInstance().login(userName, password,
                        getString(R.string.wsNS),getString(R.string.wsURL),isArabic);
                if (userInf != null && userInf.getUserId() != null &&
                        userInf.getLoggedInSDP() != null && userInf.getSdpsList() != null &&
                        !userInf.getSdpsList().isEmpty()) {
                    userInf.setArabic(isArabic);
                    Intent intent = new Intent(LoginActivity.this, SelectSDPActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(getBaseContext(), getString(R.string.loginFailed), Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(getBaseContext(), getString(R.string.invalidLogin), Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getBaseContext(), getString(R.string.loginFailed), Toast.LENGTH_LONG).show();
        }
    }

    private void changeLocale(String lang){
        Locale locale = new Locale(lang);
        Resources resources = getResources();
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        Intent intent = getIntent();
        finish();
        intent.putExtra("lang",lang);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}