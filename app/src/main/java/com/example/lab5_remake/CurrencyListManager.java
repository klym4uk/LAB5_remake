package com.example.lab5_remake;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CurrencyListManager {

    private static final int DEBOUNCE_DELAY = 400;

    private final AppCompatActivity activity;
    private final ArrayAdapter<String> currencyAdapter;
    private final Handler debounceHandler;
    private final DataLoader dataRetriever;
    private final EditText searchEditText;

    private ArrayList<String> originalCurrencyData;

    public CurrencyListManager(AppCompatActivity activity) {
        this.activity = activity;
        this.currencyAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1);
        this.debounceHandler = new Handler(Looper.getMainLooper());
        this.dataRetriever = new DataLoader();
        this.searchEditText = activity.findViewById(R.id.searchEditText);
    }

    public void initializeViews(ListView currencyListView, EditText searchEditText) {
        currencyListView.setAdapter(currencyAdapter);

        searchEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performCurrencySearch();
                return true;
            }
            return false;
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                debounceHandler.removeCallbacksAndMessages(null);
                debounceHandler.postDelayed(() -> performCurrencySearch(), DEBOUNCE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void loadData() {
        dataRetriever.retrieveData("https://api.coinbase.com/v2/currencies", this::onDataRetrieved);
    }

    private void onDataRetrieved(String result) {
        if (result != null) {
            System.out.println("JSON Data: " + result);
            Parser.parseJSONData(result, this::onJSONParsed);
        }
    }

    private void onJSONParsed(ArrayList<String> result) {
        originalCurrencyData = result;
        activity.runOnUiThread(() -> {
            currencyAdapter.clear();
            currencyAdapter.addAll(originalCurrencyData);
            currencyAdapter.notifyDataSetChanged();
        });
    }

    public void performCurrencySearch() {
        String searchText = searchEditText.getText().toString().toUpperCase();

        ArrayList<String> filteredCurrencyList = new ArrayList<>();

        if (originalCurrencyData != null) {
            for (String currency : originalCurrencyData) {
                if (currency.toUpperCase().contains(searchText)) {
                    filteredCurrencyList.add(currency);
                }
            }
        }

        activity.runOnUiThread(() -> {
            currencyAdapter.clear();
            currencyAdapter.addAll(filteredCurrencyList);
            currencyAdapter.notifyDataSetChanged();
        });
    }
}
