package com.example.qrscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private scannerinterface retrofitInterface;
    private String BASE_URL = "http://172.20.10.5:3000";
    private String userId;
    private String actualUserID;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(scannerinterface.class);

        Button scanButton = findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrScanner();
            }
        });
    }

    private void startQrScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a QR code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                userId = result.getContents();
                validateUserId(userId);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void validateUserId(String userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", userId);
        Call<User> call = retrofitInterface.getUser(map);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    Toast.makeText(MainActivity.this, "User exists", Toast.LENGTH_LONG).show();
                    actualUserID = response.body().getId();
                    name = response.body().getName();
                    Intent addReceiptIntent = new Intent(MainActivity.this, addreceiptActivity.class);
                    addReceiptIntent.putExtra("userId", actualUserID);
                    addReceiptIntent.putExtra("name", name);
                    startActivity(addReceiptIntent);
                } else {
                    Toast.makeText(MainActivity.this, "User doesn't exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to validate user", Toast.LENGTH_LONG).show();
            }
        });
    }
}



