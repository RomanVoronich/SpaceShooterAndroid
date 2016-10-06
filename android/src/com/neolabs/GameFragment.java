package com.neolabs;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neolabs.main.MainGame;
import com.neolabs.utils.Constants;
import com.neolabs.utils.IAndroidActivity;
import com.neolabs.utils.IConsumeFinished;
import com.neolabs.utils.IPurchaseFinished;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.biling.IabHelper;
import com.biling.IabResult;
import com.biling.Inventory;
import com.biling.Purchase;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.googleplus.GooglePlusSocialNetwork;
import com.github.gorbin.asne.vk.VkSocialNetwork;
import com.google.gson.Gson;
import com.vk.sdk.VKScope;

import java.util.List;

public class GameFragment extends AndroidFragmentApplication implements IAndroidActivity, SocialNetworkManager.OnInitializationCompleteListener, OnLoginCompleteListener {

    private IabHelper iabHelper;
    public static SocialNetworkManager mSocialNetworkManager;
    public Inventory inventory;

    public static GameFragment newInstance(String inventory) {
        Bundle args = new Bundle();
        args.putString("INVENTORY", inventory);
        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        initSocialNetwork();
        String inventoryString = getArguments().getString("INVENTORY");
        if (inventoryString != null) {
            Gson gson = new Gson();
            inventory = gson.fromJson(inventoryString, Inventory.class);
        }

        iabHelper = new IabHelper(getActivity(), Constants.BILING_KEY);
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {

            }
        });
        return initializeForView(new MainGame(this), config);
    }


    @Override
    public void purchase(final String sku, final IPurchaseFinished cb) {
        try {
            iabHelper.launchPurchaseFlow(getActivity(), sku, Constants.BILING_REQUEST, new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, Purchase info) {
                    cb.onIabPurchaseFinished(true, sku);
                }
            });
        } catch (Throwable error) {
            System.out.println("onIabPurchaseFinished error"+ error);
            cb.onIabPurchaseFinished(false, sku);
        }
    }

    @Override
    public void comsume(final String sku, final IConsumeFinished cb) {
        try {
            Purchase purchase = inventory.mPurchaseMap.get(sku);
            iabHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
                @Override
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    cb.onIabConsumeFinished(true, sku);
                }
            });
        } catch (Throwable error) {
            cb.onIabConsumeFinished(false, sku);
        }
    }

    @Override
    public String getAndroidId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Override
    public void loginSocial(int socialId) {
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(socialId);
        if (!socialNetwork.isConnected())
            socialNetwork.requestLogin();
    }

    private void initSocialNetwork() {
        mSocialNetworkManager = (SocialNetworkManager) getFragmentManager().findFragmentByTag("SOCIAL_NETWORK_TAG");

        if (mSocialNetworkManager == null) {
            mSocialNetworkManager = new SocialNetworkManager();

            VkSocialNetwork vkNetwork = new VkSocialNetwork(this, Constants.VK_KEY, new String[]{VKScope.NOHTTPS});
//            ArrayList<String> fbScope = new ArrayList<String>();
//            fbScope.addAll(Arrays.asList("public_profile, email, user_friends"));
//            FacebookS
// ocialNetwork fbNetwork = new FacebookSocialNetwork(this, fbScope);
            GooglePlusSocialNetwork gpNetwork = new GooglePlusSocialNetwork(this);

            mSocialNetworkManager.addSocialNetwork(vkNetwork);
//            mSocialNetworkManager.addSocialNetwork(fbNetwork);
            mSocialNetworkManager.addSocialNetwork(gpNetwork);


            getFragmentManager().beginTransaction().add(mSocialNetworkManager, "SOCIAL_NETWORK_TAG").commit();
            mSocialNetworkManager.setOnInitializationCompleteListener(this);
        } else {
            if (!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                for (SocialNetwork socialNetwork : socialNetworks) {
                    socialNetwork.setOnLoginCompleteListener(this);
                }
            }
        }
    }

    @Override
    public void onSocialNetworkManagerInitialized() {
        for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks())
            socialNetwork.setOnLoginCompleteListener(this);
    }

    @Override
    public void onLoginSuccess(int socialNetworkID) {
        //
        System.out.println("onLoginSuccess" + socialNetworkID);
    }

    @Override
    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {

    }
}