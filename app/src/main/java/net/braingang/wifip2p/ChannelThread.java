package net.braingang.wifip2p;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 *
 */
public class ChannelThread extends Thread {
    public final String LOG_TAG = getClass().getName();

    private Handler _handler1;
    private Handler _handler2;

    public Looper getLooper() {
        return _handler2.getLooper();
    }

    @Override
    public void run() {
        try {
            Looper.prepare();

            _handler2 = new Handler() {
                public void handleMessage(Message message) {
                    try {
                        Log.i(LOG_TAG, "worker thread has fresh message");
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            };

            Looper.loop();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    /*
       Looper.prepare(); //init current thread as a looper

      _handler2 = new Handler() {
        public void handleMessage(Message message) {
          LogFacade.info(LOG_TAG, "xxx worker thread has fresh message:" + (String) message.obj);

          try {
            sleep(100);

            Time time = new Time();
            time.setToNow();

            Message arg = _handler1.obtainMessage();
            arg.obj = "back at ya:" + time.toMillis(true);

            _handler1.sendMessage(arg);
          } catch (Exception exception) {
            LogFacade.error(LOG_TAG, exception.getMessage());
          }
        }
      };

      Looper.loop(); //run message queue

     */
}
