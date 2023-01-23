package com.example.qrscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class addreceiptActivity extends AppCompatActivity {
    private String actualUserID;
    private Retrofit retrofit;
    private scannerinterface retrofitInterface;
    private String BASE_URL = "http://172.20.10.5:3000";
    private String userId;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreceipt);
        actualUserID = getIntent().getStringExtra("userId");
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(scannerinterface.class);

        Button sendReceiptButton = findViewById(R.id.send_receipt_button);
        TextView textView= findViewById(R.id.usertv);
        TextView textVieww= findViewById(R.id.useritems);
        textView.setText(actualUserID);
        textVieww.setText(name);


        sendReceiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", "Ikea");
                map.put("userID", actualUserID);
                map.put("date", "2023-1-20");
                map.put("time", "12:12:12");
                map.put("total", "1000.00");
                map.put("category", "Groceries");
                map.put("bonus", "30");

                Call<Void> call = retrofitInterface.addReceipt(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                        if (response.code() == 200) {
                            Toast.makeText(addreceiptActivity.this, "Receipt added successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(addreceiptActivity.this, "Didn't add receipt", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(addreceiptActivity.this, "Failed to add receipt", Toast.LENGTH_LONG).show();
                    }
                });

                HashMap<String, String> mapitems = new HashMap<>();
                mapitems.put("receiptID", "3");
                mapitems.put("item_name", "Sofa");
                mapitems.put("item_price", "1000.00");
                mapitems.put("quantity", "1");
                mapitems.put("user", actualUserID);

                Call<Void> callitem = retrofitInterface.newitem(mapitems);
                callitem.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                        if (response.code() == 200) {
                            Toast.makeText(addreceiptActivity.this, "Items added successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(addreceiptActivity.this, "Didn't add items", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(addreceiptActivity.this, "Failed to add items", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}