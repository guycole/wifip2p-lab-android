package net.braingang.wifip2p;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

/**
 *
 */
public class Personality {
    public static Boolean wifiDiscovery;
    public static Boolean wifiEnabled;

    public static DeviceList deviceList;

    public static WifiP2pManager wifiP2pManager;
    public static WifiP2pManager.Channel wifiP2pChannel;
    public static WifiP2pDevice localDevice;
}
