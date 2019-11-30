/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomserver;

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Nicholas Ouellette #3556762
 */
public class ChatroomServer {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        try {
            ServerSocket server = new ServerSocket(1998);
            System.out.println("Server started on port " + server.getLocalPort());
            ExecutorService ClientPool = Executors.newFixedThreadPool(20);
            Socket client;
            while (true) {
                client = server.accept();
                System.out.println("Client connected from ip address:"+ client.getRemoteSocketAddress().toString().replaceFirst(":\\d*", "") +"port: 1998");
                ClientPool.execute(new ServerClientRunner(client));
            }
        } catch (IOException i) {
            System.out.println(i);
        }

    }

}
