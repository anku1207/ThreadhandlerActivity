package com.msandroid.threadhandleractivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String MSG_KEY = "yo";

    /**
     * perform the action in `handleMessage` when the thread calls
     * `mHandler.sendMessage(msg)`
     */
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString(MSG_KEY);
            final TextView myTextView = (TextView)findViewById(R.id.myTextView);
            myTextView.setText(string);
        }
    };



    private final Runnable mMessageSender = new Runnable() {
        public void run() {
            Message msg = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString(MSG_KEY, getCurrentTime());
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }
    };

    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.US);
        return dateFormat.format(new Date());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       /* mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setProgress(currentProgressCount);
            }
        });*/


        final Button button = (Button) findViewById(R.id.myButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleButtonClick(v);
            }
        });
    }

    public void handleButtonClick(View view) {
        new Thread(mMessageSender).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO not sure if this is needed for this use case
        mHandler.removeCallbacks(mMessageSender);
    }
}