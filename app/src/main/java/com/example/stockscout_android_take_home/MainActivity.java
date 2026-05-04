package com.example.stockscout_android_take_home;

import static com.example.stockscout_android_take_home.EnqueuePickKt.enqueuePick;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText input;
    TextView tvresult;
    Button scan;

    List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.input);
        tvresult = findViewById(R.id.result);
        scan = findViewById(R.id.scan);

        requestCameraPermission();
        loadItems();

        scan.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setPrompt("Scan barcode");
            integrator.setBeepEnabled(true);
            integrator.setOrientationLocked(true);
            integrator.initiateScan();
        });
    }

    // ---------------- CAMERA ----------------
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    100);
        }
    }

    // ---------------- LOAD DATA ----------------
    private void loadItems() {

        RetrofitClient.getApi().getItems().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {

                if (response.body() == null) return;

                List<Item> rawItems = response.body();
                List<Item> safeItems = new ArrayList<>();

                for (Item item : rawItems) {

                    // 🔥 NULL SAFETY CHECK
                    if (item.itemCode == null ||
                            item.name == null ||
                            item.unit == null) {
                        continue;
                    }

                    // convert aliases
                    if (item.aliases != null) {
                        item.aliasesJson = new Gson().toJson(item.aliases);
                    }

                    safeItems.add(item);
                }

                items = safeItems;

                new Thread(() -> {
                    AppDatabase.getInstance(MainActivity.this)
                            .itemDao()
                            .insertAll(safeItems);
                }).start();
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

                new Thread(() -> {
                    List<Item> dbItems = AppDatabase.getInstance(MainActivity.this)
                            .itemDao()
                            .getAll();

                    runOnUiThread(() -> items = dbItems);
                }).start();
            }
        });
    }

    // ---------------- SEARCH ----------------
    public void onSearch(View view) {

        if (items == null || items.isEmpty()) {
            tvresult.setText("Loading...");
            return;
        }

        String text = input.getText().toString();

        Item item = AliasResolver.resolve(text, items);

        if (item != null) {
            tvresult.setText(item.name + " Qty: " + item.quantity);
        } else {
            tvresult.setText("Item not found");
        }
    }

    // ---------------- SCANNER ----------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null && result.getContents() != null) {

            String scanned = result.getContents();

            if (items == null) {
                tvresult.setText("Data loading...");
                return;
            }

            Item item = AliasResolver.resolve(scanned, items);

            tvresult.setText(item != null ? item.name : "Not found");
        }
    }
}