package com.saptco.dispatcher.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.saptco.dispatcher.R;
import com.saptco.dispatcher.data.model.LoggedInUser;
import com.saptco.dispatcher.data.model.SDPType;

public class SelectSDPActivity extends AppCompatActivity {

    private LoggedInUser userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_s_d_p);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Spinner sdpSpinner = findViewById(R.id.sdpSpinner);
        final Button selectSDPButton = findViewById(R.id.selectSDPButton);
        final TextView userNameText = findViewById(R.id.userNameText);

//        userInfo = (LoggedInUser) getIntent().getSerializableExtra("userInfo");
        userInfo = LoggedInUser.getInstance();

        if(userInfo.getUserId() == null){
            Intent intent = new Intent(SelectSDPActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        userNameText.setText(getString(R.string.welcomeUser) + " " + userInfo.getDisplayName());
        ArrayAdapter<SDPType> sdpAdapter = new ArrayAdapter<SDPType>(this, android.R.layout.simple_spinner_dropdown_item, userInfo.getSdpsList());
        sdpSpinner.setAdapter(sdpAdapter);

        selectSDPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    selectSDPAction((SDPType) sdpSpinner.getSelectedItem());
           }
        });
    }

    public void selectSDPAction(SDPType selectedSDP){
        userInfo.setLoggedInSDP(selectedSDP.getSdpId());
        userInfo.setSelectedSDP(selectedSDP.getSdpName());
        userInfo.setStationID(selectedSDP.getStationID());
        Intent intent = new Intent(SelectSDPActivity.this, TripActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
    }
}