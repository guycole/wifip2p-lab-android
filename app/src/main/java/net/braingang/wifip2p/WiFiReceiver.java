package net.braingang.wifip2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Collection;
import java.util.Iterator;

public class WiFiReceiver extends BroadcastReceiver implements WifiP2pManager.ConnectionInfoListener, WifiP2pManager.PeerListListener {
    public static final String LOG_TAG = WiFiReceiver.class.getName();

    private final Context _context;

    private final WifiP2pManager _manager;
    private final WifiP2pManager.Channel _channel;

    public WiFiReceiver(Context context, WifiP2pManager manager, WifiP2pManager.Channel channel) {
        super();

        _context = context;

        _manager = manager;
        _channel = channel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(LOG_TAG, "onReceive:" + intent.getAction());

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Log.d(LOG_TAG, "wifi state enabled");
                Personality.wifiEnabled = true;
            } else {
                Log.d(LOG_TAG, "wifi state disabled");
                Personality.wifiEnabled = false;
            }
        } else if (WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED) {
                Log.d(LOG_TAG, "wifi discovery start");
                Personality.wifiDiscovery = true;
            } else {
                Log.d(LOG_TAG, "wifi discovery stop");
                Personality.wifiDiscovery = false;
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            _manager.requestPeers(_channel, this);
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            NetworkInfo netInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            WifiP2pInfo p2pInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
            WifiP2pGroup p2pGroup = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_GROUP);

            Log.d(LOG_TAG, "netInfo:" + netInfo);
            Log.d(LOG_TAG, "p2pInfo:" + p2pInfo);
            Log.d(LOG_TAG, "p2pGroup:" + p2pGroup);

            if (netInfo.isConnected()) {
                Log.d(LOG_TAG, "connect noted");
                _manager.requestConnectionInfo(_channel, this);
                WiFiService.startAction(_context, WiFiService.PORT, p2pInfo, p2pGroup);
            } else {
                Log.d(LOG_TAG, "disconnect noted");
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Log.d(LOG_TAG, "update local device");
            Personality.localDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
        }
    }

    // ConnectionInfoListener
    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
        Log.d(LOG_TAG, "connection noted:" + p2pInfo.toString());

        if (p2pInfo.isGroupOwner) {
            Toast.makeText(_context, "Group Owner True", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(_context, "Group Owner False", Toast.LENGTH_LONG).show();
        }
    }

    // PeerListListener
    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        Log.d(LOG_TAG, "peers available");

        DeviceList results = new DeviceList();

        Collection<WifiP2pDevice> deviceList = peerList.getDeviceList();
        Iterator<WifiP2pDevice> iterator = deviceList.iterator();
        while (iterator.hasNext()) {
            WifiP2pDevice device = iterator.next();
            Log.d(LOG_TAG, "fresh device:" + device.deviceName + ":" + device.deviceAddress);
            results.add(device);
        }

        Personality.deviceList = results;
    }
}
