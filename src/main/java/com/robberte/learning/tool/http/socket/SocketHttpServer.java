package com.robberte.learning.tool.http.socket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketHttpServer {

    public static final Logger logger = LoggerFactory.getLogger(SocketHttpServer.class.getName());

    public static final String BODY = "<html><head><title>Example</title></head><body><p>Hello World!</p></body></html>";

    public static final String HEADER = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: ";
    public static final String END_OFF_HEADER = "\r\n\r\n";

    public int port = 8080;

    public String ip = "127.0.0.1";

    private ServerSocket serverSocket = null;

    public SocketHttpServer() {

    }

    public SocketHttpServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) {
        SocketHttpServer socketHttpServer = new SocketHttpServer();
        socketHttpServer.start();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName(ip));
            logger.info("start http server, ip={}, port={}", ip, port);
            boolean stop = false;

            while(!stop) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String line;
                    while((line = bf.readLine()) != null) {
                        logger.info(line);
                        if(line.isEmpty()) {
                            break;
                        }
                    }
                    sendResponse(socket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(Socket socket) {
        try(BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            bf.write(HEADER + BODY.getBytes().length + END_OFF_HEADER + BODY);
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
