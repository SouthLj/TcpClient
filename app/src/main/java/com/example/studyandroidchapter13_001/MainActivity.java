package com.example.studyandroidchapter13_001;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private TextView show;
    // 定义与服务器通信的线程
    private ClientThread clientThread;
    static class MyHandler extends Handler{
        private WeakReference<MainActivity> mainActivity;
        MyHandler(WeakReference<MainActivity> mainActivity){
            this.mainActivity = mainActivity;
        }
        @Override
        public void handleMessage(Message msg){
            // 如果消息来自子线程
            if(msg.what == 0x123){
                // 将读取的内容追加显示在文本框中
                mainActivity.get().show.append("\n" + msg.obj.toString());
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 定义界面上的两个文本框
        final EditText input = findViewById(R.id.input);
        show = findViewById(R.id.show);
        // 定义界面上的一个按钮
        Button send = findViewById(R.id.send);
        MyHandler handler = new MyHandler(new WeakReference<>(this));
        clientThread = new ClientThread(handler);
        //客户端启动ClientThread线程创建网络连接，读取来自服务器的数据
        new Thread(clientThread).start();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 0x345;
                msg.obj = input.getText().toString();
                clientThread.revHandler.sendMessage(msg);
                input.setText("");
            }
        });

       /* show = (TextView) findViewById(R.id.show);
        checkPermission();
        new Thread(){
            @Override
            public void run(){
                try{
                    Log.e("liujianDebug", "enter1");
                    //  建立连接到远程服务器的Socket
                    Socket socket = new Socket("localhost", 30004);
                    Log.e("liujianDebug", "enter2");
                    // 将socket对应的输入流包装成BufferedReader
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    // 进行普通io
                    String line = br.readLine();
                    show.setText("来自服务器的数据：" + line);
                    br.close();
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }.start();*/

    }
    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_PHONE = 102;
            String[] permissions = {
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE
            };

            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(permissions, REQUEST_PHONE);
                    return;
                }
            }
        }
    }
}
