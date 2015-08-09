package net.braingang.wifip2p;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class WiFiService extends IntentService {
    public static final String LOG_TAG = WiFiService.class.getName();

    public static final int PORT = 8765;
    public static final int SOCKET_TIMEOUT = 5000;

    private static final String ACTION_START = "net.braingang.service.action.start";

    private static final String EXTRA_P2P_GROUP = "net.braingang.service.extra.group";
    private static final String EXTRA_P2P_INFO = "net.braingang.service.extra.info";
    private static final String EXTRA_PORT = "net.braingang.service.extra.port";

    public static void startAction(Context context, int port, WifiP2pInfo p2pInfo, WifiP2pGroup p2pGroup) {
        Intent intent = new Intent(context, WiFiService.class);
        intent.setAction(ACTION_START);
        intent.putExtra(EXTRA_PORT, port);
        intent.putExtra(EXTRA_P2P_GROUP, p2pGroup);
        intent.putExtra(EXTRA_P2P_INFO, p2pInfo);
        context.startService(intent);
    }

    public WiFiService() {
        super("WiFiService");
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                final Integer port = intent.getIntExtra(EXTRA_PORT, -1);
                final WifiP2pInfo p2pInfo = intent.getParcelableExtra(EXTRA_P2P_INFO);
                final WifiP2pGroup p2pGroup = intent.getParcelableExtra(EXTRA_P2P_GROUP);

                Log.d(LOG_TAG, "port:" + port);
                Log.d(LOG_TAG, "p2pInfo:" + p2pInfo);
                Log.d(LOG_TAG, "p2pGroup:" + p2pGroup);

                if (p2pGroup.isGroupOwner()) {
                    setupServer(port, p2pInfo, p2pGroup);
                } else {
                    setupClient(port, p2pInfo, p2pGroup);
                }
            }
        }
    }

    private String getDeviceName() {
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
 //       manager.get
        return "bogus";
    }

    private void setupClient(int port, WifiP2pInfo p2pInfo, WifiP2pGroup p2pGroup) {
        Log.d(LOG_TAG, "setup client");

        WiFiContainer container = new WiFiContainer();
        container.put(WiFiContainer.TIME_STAMP, new Date());
        container.put(WiFiContainer.ORIGIN_NAME, Personality.localDevice.deviceName);

        String host = p2pInfo.groupOwnerAddress.getHostAddress();

        Socket socket = new Socket();

        try {
            socket.bind(null);
            socket.connect((new InetSocketAddress(host, port)), SOCKET_TIMEOUT);
            ObjectOutputStream oss = new ObjectOutputStream(socket.getOutputStream());
            oss.writeObject(container);
            Log.d(LOG_TAG, "write:" + container.get(WiFiContainer.ORIGIN_NAME));

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            WiFiContainer wfc = (WiFiContainer) ois.readObject();
            Log.d(LOG_TAG, "read:" + wfc.get(WiFiContainer.ORIGIN_NAME));

            /*
            Personality.wifiP2pManager.cancelConnect(Personality.wifiP2pChannel, null);
            WiFiReceiver.restartReceiver(getBaseContext());
            */
        } catch(Exception exception) {
            exception.printStackTrace();
        } finally {
            if (socket != null) {
                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }
    }

    private void setupServer(int port, WifiP2pInfo p2pInfo, WifiP2pGroup p2pGroup) {
        Log.d(LOG_TAG, "server start");

        WiFiContainer container = new WiFiContainer();
        container.put(WiFiContainer.TIME_STAMP, new Date());
        container.put(WiFiContainer.ORIGIN_NAME, Personality.localDevice.deviceName);

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket client = serverSocket.accept();
            Log.d(LOG_TAG, "client socket:" + client.getInetAddress());

            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            WiFiContainer wfc = (WiFiContainer) ois.readObject();
            Log.d(LOG_TAG, "server read:" + wfc.get(WiFiContainer.ORIGIN_NAME));

            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(container);
            Log.d(LOG_TAG, "server write:" + container.get(WiFiContainer.ORIGIN_NAME));

            serverSocket.close();
            Log.d(LOG_TAG, "server close");

            /*
            Personality.wifiP2pManager.removeGroup(Personality.wifiP2pChannel, null);
            WiFiReceiver.restartReceiver(getBaseContext());
            */
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}