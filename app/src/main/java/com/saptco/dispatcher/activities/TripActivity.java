package com.saptco.dispatcher.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.saptco.dispatcher.R;
import com.saptco.dispatcher.data.model.LoggedInUser;
import com.saptco.dispatcher.data.model.SDPType;
import com.saptco.dispatcher.data.model.TripInfo;
import com.saptco.dispatcher.data.repositories.DispatcherRepository;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TripActivity extends AppCompatActivity {

    private LoggedInUser userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Button searchTripButton = findViewById(R.id.searchTripButton);
        final TextView userNameText = findViewById(R.id.userNameText);
        final TextView sdpText = findViewById(R.id.sdpText);
        final EditText tripCodeText = findViewById(R.id.tripCodeText);
        final DatePicker tripDateText = findViewById(R.id.tripDateText);

        userInfo = LoggedInUser.getInstance();

        if(userInfo.getUserId() == null){
            Intent intent = new Intent(TripActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        userNameText.setText(getString(R.string.welcomeUser) + " " + userInfo.getDisplayName());
        sdpText.setText(getString(R.string.sdp) + " " + userInfo.getSelectedSDP());

        tripCodeText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String tripDate = tripDateText.getYear() + "-" +
                        StringUtils.leftPad(String.valueOf(tripDateText.getMonth() + 1),2,"0") +
                        "-" + StringUtils.leftPad(String.valueOf(tripDateText.getDayOfMonth()),2,"0");
                checkTripAction(tripCodeText.getText().toString(),tripDate);
                return false;
            }
        });

        searchTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tripDate = tripDateText.getYear() + "-" +
                        StringUtils.leftPad(String.valueOf(tripDateText.getMonth() + 1),2,"0") +
                        "-" + StringUtils.leftPad(String.valueOf(tripDateText.getDayOfMonth()),2,"0");
                checkTripAction(tripCodeText.getText().toString(),tripDate);
            }
        });
    }

    public void checkTripAction(String tripCode, String tripDate){
        try{
            if(tripCode != null && !tripCode.isEmpty() && tripDate != null && !tripDate.isEmpty()) {
                TripInfo trip = TripInfo.getInstance();
                trip.setStationID(userInfo.getStationID());
                trip.setTripCode(tripCode);
                trip.setTripDate(tripDate);
                trip.setTicketNumber(null);
                trip.setCamScanned(false);
                trip.setDispatch(true);
                userInfo.setDispatchChaneLang(false);
                trip = DispatcherRepository.getInstance().checkTrip(trip,userInfo,
                        getString(R.string.wsNS),getString(R.string.wsURL));
                if(trip != null && trip.getValidCall() != null && trip.getValidCall()) {
                    Intent intent = new Intent(TripActivity.this, DispatchActivity.class);
                    startActivity(intent);
                }else
                    Toast.makeText(getBaseContext(), trip.getErrorMessage(), Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(getBaseContext(), getString(R.string.invalidData), Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getBaseContext(), getString(R.string.invalidTrip), Toast.LENGTH_LONG).show();
        }
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