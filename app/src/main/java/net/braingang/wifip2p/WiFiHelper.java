package net.braingang.wifip2p;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;

/**
 *
 */
public class WiFiHelper {
    public static final String LOG_TAG = WiFiHelper.class.getName();

    public void connect() {
        Log.d(LOG_TAG, "connect start");

        if ((Personality.deviceList == null) || (Personality.deviceList.size() < 1)) {
            Log.d(LOG_TAG, "no devices");
        } else {
            WifiP2pDevice target = Personality.deviceList.get(0);
            Log.d(LOG_TAG, "connect:" + target.deviceName + ":" + target.deviceAddress);

            final WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = target.deviceAddress;
            config.groupOwnerIntent = 0;

            Personality.wifiP2pManager.connect(Personality.wifiP2pChannel, config, null);
        }
    }

    public void disconnect() {
        Log.d(LOG_TAG, "disconnect start");
        Personality.wifiP2pManager.removeGroup(Personality.wifiP2pChannel, null);
        Personality.wifiP2pManager.cancelConnect(Personality.wifiP2pChannel, null);
    }

    public void discovery() {
        Log.d(LOG_TAG, "discovery start");
        Personality.wifiP2pManager.discoverPeers(Personality.wifiP2pChannel, null);
    }
}
