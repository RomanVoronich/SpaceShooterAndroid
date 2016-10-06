package com.neolabs;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.neolabs.utils.Constants;
import com.biling.IabHelper;
import com.biling.IabResult;
import com.biling.Inventory;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class SplashActivity extends FragmentActivity {
    private IabHelper mHelper;
    // TODO добавь sku
    private final List<String> skus = Arrays.asList("skin1", "skin2", "skin3");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHelper = new IabHelper(this, Constants.BILING_KEY);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                Loader loader = new Loader(result.isSuccess(), skus);
                loader.execute();
            }
        });

    }

    private class Loader extends AsyncTask<Void, Void, String> {
        private boolean iabResult;
        private List<String> skus;

        public Loader(boolean iabResult, List<String> skus) {
            this.iabResult = iabResult;
            this.skus = skus;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                if (iabResult) {
                    Gson gson = new Gson();
                    Inventory inventory = mHelper.queryInventory(true, skus);
                    return gson.toJson(inventory);
                }
            } catch (Throwable ignored) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String inventory) {
            super.onPostExecute(inventory);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("INVENTORY", inventory);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}