package com.games.hoops.hoops;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.games.hoops.hoops.util.IabBroadcastReceiver;
import com.games.hoops.hoops.util.IabHelper;
import com.games.hoops.hoops.util.IabResult;
import com.games.hoops.hoops.util.Inventory;
import com.games.hoops.hoops.util.Purchase;

/**
 * Created by domin on 15 Sep 2016.
 */
public class Purchases implements IabBroadcastReceiver.IabBroadcastListener{
    private final Context context;
    private static String TAG= "Game";
    // Does the user have the premium upgrade?
    boolean mIsPremium = false;

    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
    static final String SKU_PREMIUM = "premium";
    static final String SKU_STAR1 = "stars";
    static final String SKU_STAR5 = "fivestars";
    static final String SKU_STAR10 = "tenstars";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    private IabHelper mHelper;
    private IabBroadcastReceiver mBroadcastReceiver;

    public Purchases (final Context context){
        this.context = context;
        /* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
         * (that you got from the Google Play developer console). This is not your
         * developer public key, it's the *app-specific* public key.
         *
         * Instead of just storing the entire literal string here embedded in the
         * program,  construct the key at runtime from pieces or
         * use bit manipulation (for example, XOR with some other string) to hide
         * the actual key.  The key itself is not secret information, but we don't
         * want to make it easy for an attacker to replace the public key with one
         * of their own and then fake messages from the server.
         */
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkgV5IhvGlyp4Ccgy0S5N3P45FTt0DZrpeQHMIAkWzYOXIlMInqgPmJFFrGmz/ETyqxNBKTcL7fdfkococCZ1MkIUg8t5bchMXpHy/FKd3wMTiNPBrInMq5oFpKXpSjPXHWACjfFr/7CZ7Ys98ry0rTOj6/gkurz2DC438GWUvbUj1Dqo66Hvhq/TSXWvm9SqEitUitrKOX42fzUkbYCqZJxp7OjFS+JjG/z+491yZVEItxeiF+h2jMgay9mwoNd0Tw/PdF05KVM9vBZgNetOvs0nhXcTWUMFunDo3Qxwb1z6fr0pnhV9aOl8jt00X611KtvgNmLJB2Y4wSe8Lpt3uwIDAQAB";

// Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(context, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(Purchases.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                context.registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            MainActivity.sGame.getSettings().setPremium(mIsPremium);
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase starPurchase = inventory.getPurchase(SKU_STAR1);
            if (starPurchase != null && verifyDeveloperPayload(starPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                try {
                    mHelper.consumeAsync(inventory.getPurchase(SKU_STAR1), mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming gas. Another async operation in progress.");
                }
                return;
            }

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            starPurchase = inventory.getPurchase(SKU_STAR5);
            if (starPurchase != null && verifyDeveloperPayload(starPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                try {
                    mHelper.consumeAsync(inventory.getPurchase(SKU_STAR5), mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming gas. Another async operation in progress.");
                }
                return;
            }

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            starPurchase = inventory.getPurchase(SKU_STAR10);
            if (starPurchase != null && verifyDeveloperPayload(starPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                try {
                    mHelper.consumeAsync(inventory.getPurchase(SKU_STAR10), mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming gas. Another async operation in progress.");
                }
                return;
            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_STAR1) || purchase.getSku().equals(SKU_STAR5) || purchase.getSku().equals(SKU_STAR10)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming gas. Another async operation in progress.");
//                    setWaitScreen(false);
                    return;
                }
            }
            else if (purchase.getSku().equals(SKU_PREMIUM)) {
                // bought the premium upgrade!
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                alert("Thank you for upgrading to premium!");
                mIsPremium = true;
                MainActivity.sGame.getSettings().setPremium(mIsPremium);
//                updateUi();
//                setWaitScreen(false);
            }
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");

                int totalStars = MainActivity.sGame.getSettings().getTotalStars();
                String starNumber = "no stars";
                if (purchase.getSku().equals(SKU_STAR1)){
                    totalStars = totalStars + 1;
                    starNumber = "" + 1 + " star";
                    MainActivity.sGame.getSettings().setTotalStars(totalStars);
                }

                if (purchase.getSku().equals(SKU_STAR5)){
                    totalStars = totalStars + 5;
                    starNumber = "" + 5 + " stars";
                    MainActivity.sGame.getSettings().setTotalStars(totalStars);
                }

                if (purchase.getSku().equals(SKU_STAR10)){
                    totalStars = totalStars + 10;
                    starNumber = "" + 10 + " stars";
                    MainActivity.sGame.getSettings().setTotalStars(totalStars);
                }
                if (purchase.getSku().equals(SKU_PREMIUM)){
                    totalStars = totalStars + 3;
                    MainActivity.sGame.getSettings().setTotalStars(totalStars);
                    MainActivity.sGame.getSettings().setPremium(true);
                    starNumber = "Premium and 3 Stars";
                }
                alert("You obtained " + starNumber + ". You now have " + String.valueOf(totalStars) + " stars");
            }
            else {
                complain("Error while consuming: " + result);
            }

            Log.d(TAG, "End consumption flow.");
        }
    };


    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    public boolean isResultHandled ( int requestCode, int resultCode, Intent data){
        return mHelper.handleActivityResult(requestCode, resultCode, data);
    }
    public boolean helperIsNull(){
        return (mHelper == null);
    }

    public void onDestroy(){
        // very important:
        if (mBroadcastReceiver != null) {
            context.unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(context);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    public void onStar1Pressed (){
        Log.d(TAG, "Buy gas button clicked.");

        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        try {
            mHelper.launchPurchaseFlow((Activity) context, SKU_STAR1, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
        }
    }

    public void onStar5Pressed (){
        Log.d(TAG, "Buy gas button clicked.");

        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        try {
            mHelper.launchPurchaseFlow((Activity) context, SKU_STAR5, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
        }
    }

    public void onStar10Pressed (){
        Log.d(TAG, "Buy gas button clicked.");

        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        try {
            mHelper.launchPurchaseFlow((Activity) context, SKU_STAR10, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
        }
    }

    public void onGoPremiumPressed (){
        String payload = "";
        try {
            mHelper.launchPurchaseFlow((Activity) context, SKU_PREMIUM, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
        }

    }
}
