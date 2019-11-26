package com.example.studyandroidchapter13_001;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) throws IOException {
        // 创建一个ServerSocket，用于监听客户端Socket的链接请求
        ServerSocket ss = new ServerSocket(30004);
        // 采用循环不断接受来自客户端的请求
        while(true){
            // 每当接收到客户端Socket的请求，服务器端也对应产生一个Socket
            Socket s = ss.accept();
            Log.e("liujianDebug", "enter");
            OutputStream os = s.getOutputStream();
            os.write("您好，您收到了来自服务器的新年祝福！\n".getBytes("utf-8"));
            // 关闭输出流， 关闭Socket
            os.close();
            s.close();
        }
    }
}
