package com.robberte.learning.tool.http.socket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;

/**
 *
 */
public class SocketHttpClient {

    public static final Logger logger = LoggerFactory.getLogger(SocketHttpClient.class);

    public static final String REQUEST_HEADER = "GET / HTTP/1.1\r\n"
            + "Host: localhost:8080\r\n";

    public static final String END_OFF_HEADER = "\r\n\r\n";

    public int port = 8080;

    public String ip = "127.0.0.1";

    public static void main(String[] args) {
        SocketHttpClient client = new SocketHttpClient();
        client.start();
    }

    public void start() {
        try(
                Socket socket = new Socket(ip, port);
                OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
                BufferedWriter bw = new BufferedWriter(osw);

                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                )
        {
            bw.write(REQUEST_HEADER + END_OFF_HEADER);
            bw.flush();
            String responseLine;
            while((responseLine = br.readLine()) != null) {
                logger.info(responseLine);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
