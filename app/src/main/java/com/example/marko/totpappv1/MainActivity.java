package com.example.marko.totpappv1;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static Button btnStart;
    private static Button btnResult;
    private static final int startQR = 1;
    private TextView mTextViewResult;
    public String result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = findViewById(R.id.btnStart);
        btnResult = findViewById(R.id.btnResult);
        mTextViewResult = findViewById(R.id.textViewResult);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartClicked();
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResultClicked();
            }
        });

    }

    private void onStartClicked()
    {


        Intent intent = new Intent("android.intent.action.QRScan");
        startActivityForResult(intent,startQR);

    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult (requestCode, resultCode, data);

        if(requestCode == 1)
            if(resultCode == RESULT_OK){
                result = data.getStringExtra("QRCode");
                mTextViewResult.setText("" + result);
            }
    }

    private void onResultClicked()
    {

        Intent intent = new Intent("android.intent.action.ResultActivity");

        intent.putExtra("STRING I NEED", result);

        startActivity(intent);


    }



}
