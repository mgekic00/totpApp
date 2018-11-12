package com.example.marko.totpappv1;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.GeneralSecurityException;

import java.util.Arrays;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class ResultActivity extends AppCompatActivity {


    public static final int DEFAULT_TIME_STEP_SECONDS = 30;
    private static Button btnTOTP;
    public String secret;



    public static long generateCurrentNumber(String base32Secret) throws GeneralSecurityException {
        return generateNumber(base32Secret, System.currentTimeMillis(), DEFAULT_TIME_STEP_SECONDS);
    }


    public static long generateNumber(String base32Secret, long timeMillis, int timeStepSeconds)
            throws GeneralSecurityException {

        byte[] key = base32Secret.getBytes();

        byte[] data = new byte[8];
        long value = timeMillis / 1000 / timeStepSeconds;
        for (int i = 7; value > 0; i--) {
            data[i] = (byte) (value & 0xFF);
            value >>= 8;
        }

        // encrypt the data with the key and return the SHA1 of it in hex
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        // if this is expensive, could put in a thread-local
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        // take the 4 least significant bits from the encrypted string as an offset
        int offset = hash[hash.length - 1] & 0xF;

        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = offset; i < offset + 4; ++i) {
            truncatedHash <<= 8;
            // get the 4 bytes at the offset
            truncatedHash |= (hash[i] & 0xFF);
        }
        // cut off the top bit
        truncatedHash &= 0x7FFFFFFF;

        // the token is then the last 6 digits in the number
        truncatedHash %= 1000000;

        return truncatedHash;
    }








    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        btnTOTP = findViewById(R.id.btnTOTP);
        textView = findViewById(R.id.textViewResult2);

        Intent intent = getIntent();

        secret = intent.getStringExtra("STRING I NEED");
        textView.setText("" + secret);

        try {
            long result = generateCurrentNumber(secret);
            textView.setText("" + result);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }


        setResult(RESULT_OK,intent);
        btnTOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTOTPClicked();
            }
        });
    }

    private void onTOTPClicked()
    {


        try {
            long result = generateCurrentNumber(secret);
            textView.setText("" + result);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

    }

}
