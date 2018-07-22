package com.robberte.learning.tool.http.socket;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class SocketHttpServer {

    public static int PORT = 8080;

    public static String IP = "127.0.0.1";

    private ServerSocket serverSocket = null;

    public SocketHttpServer(String ip, int port) {
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName(IP));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
