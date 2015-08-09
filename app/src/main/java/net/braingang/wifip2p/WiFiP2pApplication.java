package net.braingang.wifip2p;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.util.Log;

/**
 *
 */
public class WiFiP2pApplication extends Application {
    public final String LOG_TAG = getClass().getName();

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "xoxoxoxoxoxoxoxoxoxoxoxxoxoxoxoxoxo");
        Log.i(LOG_TAG, "xo start start start start start xo");
        Log.i(LOG_TAG, "xoxoxoxoxoxoxoxoxoxoxoxxoxoxoxoxoxo");

        super.onCreate();
        Log.d(LOG_TAG, "onCreate");

        setupWiFiReceiver();
    }

    private void setupWiFiReceiver() {
        final WifiP2pManager manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        final WifiP2pManager.Channel channel = manager.initialize(this, Looper.getMainLooper(), null);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);

        WiFiReceiver wiFiReceiver = new WiFiReceiver(this, manager, channel);
        registerReceiver(wiFiReceiver, intentFilter);

        Personality.wifiP2pManager = manager;
        Personality.wifiP2pChannel = channel;
    }
}