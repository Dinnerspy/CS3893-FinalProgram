/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomserver;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Set;
import java.util.HashSet;
/**
 *
 * @author Nicholas Ouellette #3556762
 */
public class ChatroomServer {

    static HashMap<String, String> UserDB = new HashMap<String, String>();
    static Set<PrintWriter> outputList = new HashSet<>();
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {

        final File folder = new File("ChatroomData/Users");
        //file reading for filling user data base
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {

            } else {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(folder + "/" + fileEntry.getName()), "UTF8"));
                String str;

                str = in.readLine();
                     
                
                 UserDB.put(fileEntry.getName().substring(0, fileEntry.getName().length() -4), str);
            }
        }

        //starts up communcation
        try {
            ServerSocket server = new ServerSocket(1998);
            System.out.println("Server started on port " + server.getLocalPort());
            ExecutorService ClientPool = Executors.newFixedThreadPool(20);
            Socket client;
            while (true) {
                client = server.accept();
                System.out.println("Client connected from ip address:" + client.getRemoteSocketAddress().toString().replaceFirst(":\\d*", "") + "port: 1998");
                ClientPool.execute(new ServerClientRunner(client,UserDB,outputList ));
            }
        } catch (IOException i) {
            System.out.println(i);
        }

    }

}
