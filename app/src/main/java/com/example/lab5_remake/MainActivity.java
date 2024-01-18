package com.example.lab5_remake;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CurrencyListManager currencyListManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currencyListManager = new CurrencyListManager(this);

        EditText searchEditText = findViewById(R.id.searchEditText);
        ListView currencyListView = findViewById(R.id.currencyListView);

        currencyListManager.initializeViews(currencyListView, searchEditText);

        currencyListManager.loadData();
    }

    public void performCurrencySearch(View view) {
        currencyListManager.performCurrencySearch();
    }
}
