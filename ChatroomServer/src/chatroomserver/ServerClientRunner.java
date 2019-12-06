package chatroomserver;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author Nicholas Ouellette #3556762
 */
public class ServerClientRunner implements Runnable {
   
    
    Socket ClientSocket;
    Scanner in;
    PrintWriter out;
    HashMap<String, String> UserDB;
    String temp, UserName;
    Set<PrintWriter> outputList;
    Set<String> UserList;

    public ServerClientRunner(Socket user, HashMap<String, String> UserDB, Set<PrintWriter> outputList,Set<String> UserList ) {
        this.ClientSocket = user;
        this.UserDB = UserDB;
        this.outputList = outputList;
        this.UserList = UserList;
    } 

    @Override
    public void run() {
        try {
            in = new Scanner(ClientSocket.getInputStream(),"UTF-8");
            out = new PrintWriter(new OutputStreamWriter( ClientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
            while (true) {

                temp = in.nextLine();

                String[] request = temp.split(":");
                //user is attempint to login
                if (temp.contains("LOGINREQUEST")) {

                    if (userLoginChecker(request[1], request[2]) && !UserList.contains(request[1])) {
                        out.println("LOGINREQUEST:ACCEPTED");
                        UserName = request[1];
                        UserList.add(UserName);
                        System.out.println(ClientSocket.getRemoteSocketAddress().toString().replaceFirst(":\\d*", "") + " LOGINREQUEST:ACCEPTED");

                        break;
                    } else {
                        
                        if(UserList.contains(request[1])){
                        
                         out.println("LOGINREQUEST:USERERROR");
                        System.out.println(ClientSocket.getRemoteSocketAddress().toString().replaceFirst(":\\d*", "") + " LOGINREQUEST:USERERROR");
                        }
                        out.println("LOGINREQUEST:REJECTED");
                        System.out.println(ClientSocket.getRemoteSocketAddress().toString().replaceFirst(":\\d*", "") + " LOGINREQUEST:REJECTED");
                    }

                } else if (temp.contains("REGISTERREQUEST")) {

                    if (userRegisterChecker(request[1], request[2])) {

                        out.println("REGISTERREQUEST:ACCEPTED");
                        System.out.println(ClientSocket.getRemoteSocketAddress().toString().replaceFirst(":\\d*", "") + " REGISTERREQUEST:ACCEPTED");

                    } else {
                        out.println("REGISTERREQUEST:REJECTED");
                        System.out.println(ClientSocket.getRemoteSocketAddress().toString().replaceFirst(":\\d*", "") + " REGISTERREQUEST:REJECTED");

                    }
                } else {

                    out.println("Error:Error");
                    System.out.println(ClientSocket.getRemoteSocketAddress().toString().replaceFirst(":\\d*", "") + " Error:Error");
                }

            }
            Date now = new java.util.Date();
            Timestamp current = new java.sql.Timestamp(now.getTime());
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(current);
            String CurrentUsers = UserList.toString();
            for (PrintWriter writer : outputList) {
                writer.println("CURRENTUSERS:"+UserList);
                writer.println("[" + timeStamp + "] " + UserName + " has joined!");
            }
            out.println("CURRENTUSERS:"+UserList);
            out.println("[" + timeStamp + "] " + UserName + " has joined!");
            outputList.add(out);
            while (true) {

                String input = in.nextLine();
                if (input.startsWith("CLIENTLOGOUT:")) {
                    System.out.println("[" + timeStamp + "] " + UserName + " has left!");
                    UserList.remove(UserName);
                    for (PrintWriter writer : outputList) {
                        writer.println("CURRENTUSERS:"+UserList);
                        writer.println("[" + timeStamp + "] " + UserName + " has left!");
                    }
                    return;

                }
                now = new java.util.Date();
                current = new java.sql.Timestamp(now.getTime());
                timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(current);

                System.out.println("[" + timeStamp + "]" + UserName + ClientSocket.getRemoteSocketAddress().toString().replaceFirst(":\\d*", "") + ": " + input);

                for (PrintWriter writer : outputList) {
                    writer.println("[" + timeStamp + "] " + UserName + ": " + input);
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public synchronized boolean userLoginChecker(String username, String password) {

        if (UserDB.containsKey(username)) {

            if (UserDB.get(username).contentEquals(password)) {

                return true;
            }

        }

        return false;
    }

    public synchronized boolean userRegisterChecker(String username, String password) throws IOException {

        String path = "ChatroomData/Users/";

        if (!UserDB.containsKey(username)) {

            try (FileWriter myWriter = new FileWriter(path + username + ".txt")) {
                myWriter.write(password);
                myWriter.close();
            }

            UserDB.put(username, password);

            return true;

        }

        return false;
    }

}
