package com.saptco.dispatcher.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.saptco.dispatcher.R;
import com.saptco.dispatcher.data.model.LoggedInUser;
import com.saptco.dispatcher.data.model.TripInfo;
import com.saptco.dispatcher.data.repositories.DispatcherRepository;

import java.util.Locale;

public class DispatchActivity extends AppCompatActivity {

    private LoggedInUser userInfo;
    private TripInfo tripInfo;
    TextView icon;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Button dispatchButton = findViewById(R.id.dispatchButton);
        final TextView userNameText = findViewById(R.id.userNameText);
        final TextView sdpText = findViewById(R.id.sdpText);
        final TextView tripCodeText = findViewById(R.id.tripCodeText);
        final TextView tripDateText = findViewById(R.id.tripDateText);
        final EditText ticketNumText = findViewById(R.id.ticketNumText);
        final RadioButton radioDispatch = findViewById(R.id.radioDispatch);
        final RadioButton radioUnDispatch = findViewById(R.id.radioUnDispatch);
        icon = findViewById(R.id.icon);
        result = findViewById(R.id.result);

        userInfo = LoggedInUser.getInstance();
        tripInfo = TripInfo.getInstance();

        if(userInfo.getUserId() == null){
            Intent intent = new Intent(DispatchActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        if (!tripInfo.getDispatch()) {
            radioDispatch.setChecked(false);
            radioUnDispatch.setChecked(true);
        } else {
            radioDispatch.setChecked(true);
            radioUnDispatch.setChecked(false);
        }

        userNameText.setText(getString(R.string.welcomeUser) + " " + userInfo.getDisplayName());
        sdpText.setText(getString(R.string.sdp) + " " + userInfo.getSelectedSDP());
        tripCodeText.setText(getString(R.string.tripCode) + " " + tripInfo.getTripCode());
        tripDateText.setText(getString(R.string.tripDate) + " " + tripInfo.getTripDate());
        icon.setBackground(null);
        result.setText("");

        if (tripInfo.getCamScanned()) {
            if(userInfo.getDispatchChaneLang())
                changeLocale(userInfo.getArabic() ? "ar" : "en");
            else{
                dispatchTicket();
                ticketNumText.setText(null);
            }
        }

        ticketNumText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (radioDispatch.isChecked() || radioUnDispatch.isChecked()) {
                    tripInfo.setDispatch(radioDispatch.isChecked());
                    Intent intent = new Intent(DispatchActivity.this, ScannerActivity.class);
                    startActivity(intent);
                } else{
                    icon.setBackground(ContextCompat.getDrawable(getBaseContext(), R.mipmap.error));
                    result.setText(getString(R.string.invalidData));
                }
                return false;
            }
        });

        ticketNumText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String ticketNumString = ticketNumText.getText().toString();
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == 0  &&
                        ticketNumString != null && !ticketNumString.equals("")){
                    if (radioDispatch.isChecked() || radioUnDispatch.isChecked()) {
                        tripInfo.setDispatch(radioDispatch.isChecked());
                        tripInfo.setTicketNumber(ticketNumText.getText().toString());
                        dispatchTicket();
                        ticketNumText.setText(null);
                    } else{
                        icon.setBackground(ContextCompat.getDrawable(getBaseContext(), R.mipmap.error));
                        result.setText(getString(R.string.invalidData));
                    }
                }
                return false;
            }
        });

        ticketNumText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((radioDispatch.isChecked() || radioUnDispatch.isChecked()) &&
                        ticketNumText.getText().toString() != null &&
                        !ticketNumText.getText().toString().equals("")) {
                    tripInfo.setDispatch(radioDispatch.isChecked());
                    tripInfo.setTicketNumber(ticketNumText.getText().toString());
                    dispatchTicket();
                    ticketNumText.setText(null);
                }
                return false;
            }
        });

        dispatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((radioDispatch.isChecked() || radioUnDispatch.isChecked()) &&
                        tripInfo.getTicketNumber() != null && !tripInfo.getTicketNumber().isEmpty()) {
                    tripInfo.setDispatch(radioDispatch.isChecked());
                    tripInfo.setTicketNumber(ticketNumText.getText().toString());
                    dispatchTicket();
                    ticketNumText.setText(null);
                }else{
                    icon.setBackground(ContextCompat.getDrawable(getBaseContext(), R.mipmap.error));
                    result.setText(getString(R.string.invalidData));
                }
            }
        });
    }

    private void dispatchTicket() {
        try {
            if (tripInfo.getTicketNumber() != null && !tripInfo.getTicketNumber().isEmpty()) {
                tripInfo = DispatcherRepository.getInstance().dispatchTicket(tripInfo,userInfo,
                        getString(R.string.wsNS),getString(R.string.wsURL));
                if (tripInfo.getValidCall()) {
                    if(tripInfo.getDispatch()){
                        icon.setBackground(ContextCompat.getDrawable(getBaseContext(), R.mipmap.done));
                        result.setText(getString(R.string.validDispatch));
                    }else{
                        icon.setBackground(ContextCompat.getDrawable(getBaseContext(), R.mipmap.done));
                        result.setText(getString(R.string.validUnDispatch));
                    }
                }else{
                    icon.setBackground(ContextCompat.getDrawable(getBaseContext(), R.mipmap.error));
                    result.setText(tripInfo.getErrorMessage());
                }
            } else{
                icon.setBackground(ContextCompat.getDrawable(getBaseContext(), R.mipmap.error));
                result.setText(getString(R.string.invalidData));
            }
        } catch (Exception e) {
            icon.setBackground(ContextCompat.getDrawable(getBaseContext(), R.mipmap.error));
            result.setText(getString(R.string.invalidData));
        }
        tripInfo.setTicketNumber(null);
        tripInfo.setCamScanned(false);
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

    private void changeLocale(String lang){
        Locale locale = new Locale(lang);
        Resources resources = getResources();
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        userInfo.setDispatchChaneLang(false);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }

}