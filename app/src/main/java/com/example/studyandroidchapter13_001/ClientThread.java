package com.example.studyandroidchapter13_001;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;


public class ClientThread implements Runnable {
    // 定义向UI线程发送消息的handler对象
    private Handler handler;
    // 该线程所处理的Socket所对应的输入流
    private BufferedReader br;
    private OutputStream os;
    // 定义接收ui线程的消息的handler对象
    Handler revHandler;

    ClientThread(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run(){
        try {
            Socket s = new Socket("localhost", 30000);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = s.getOutputStream();
            // 启动一条子线程来读取服务器响应的数据
            new Thread(){
                @Override
                public void run(){
                    String content;
                    // 不断读取Socket输入流中的内容
                    try{
                        while ((content = br.readLine()) != null){
                            // 每当读到来自服务器的数据之后， 发送消息通知
                            // 程序界面显示该数据
                            Message msg = new Message();
                            msg.what = 0x123;
                            msg.obj = content;
                            handler.sendMessage(msg);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }.start();
            // 为当前线程初始化Looper
            Looper.prepare();
            // 创建revHandler对象
            revHandler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    // 接收到UI线程中用户输入的数据
                    if (msg.what == 0x345){
                        // 将用户在文本框内的内容写入网络
                        try {
                            os.write((msg.obj.toString() + "\r\n").getBytes("utf-8"));
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();
        }catch (SocketTimeoutException e){
            Log.e("liujianDebug", "网络连接超时");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
