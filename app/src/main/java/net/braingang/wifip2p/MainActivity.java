package net.braingang.wifip2p;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    public static final String LOG_TAG = MainActivity.class.getName();

    private final WiFiHelper _helper = new WiFiHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button3 = (Button) findViewById(R.id.button_disconnect);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _helper.disconnect();
            }
        });

        final Button button2 = (Button) findViewById(R.id.button_connect);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _helper.connect();
            }
        });

        final Button button1 = (Button) findViewById(R.id.button_discovery);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _helper.discovery();
            }
        });
    }
}
