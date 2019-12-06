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
    static Set<String> UserList = new HashSet<>();
    static int port = 1998;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        //Allows ports to be passed at command line
        if(args.length == 0) {
            System.out.println("Defulting to port 1998");

        } else {
            String temp = args[0];
            if(temp.matches("[0-9]+") && Integer.valueOf(temp) <= 65535 && Integer.valueOf(temp) > -1){
            port = Integer.valueOf(temp);
            }
        }
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

                UserDB.put(fileEntry.getName().substring(0, fileEntry.getName().length() - 4), str);
            }
        }

        //starts up communcation
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server started on port " + server.getLocalPort());
            ExecutorService ClientPool = Executors.newFixedThreadPool(20);
            Socket client;
            while (true) {
                client = server.accept();
                System.out.println("Client connected from ip address:" + client.getRemoteSocketAddress().toString().replaceFirst(":\\d*", "") + "port: 1998");
                ClientPool.execute(new ServerClientRunner(client, UserDB, outputList, UserList));
            }
        } catch (IOException i) {
            System.out.println(i);
        }

    }

}
