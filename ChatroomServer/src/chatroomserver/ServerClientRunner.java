/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomserver;

import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;

/**
 *
 * @author Nicho
 */
public class ServerClientRunner implements Runnable {

    Socket ClientSocket;
    Scanner in;
    PrintWriter out;

    public ServerClientRunner(Socket user) {
        this.ClientSocket = user;
    }

    @Override
    public void run() {
        try {
            in = new Scanner(ClientSocket.getInputStream());
            out = new PrintWriter(ClientSocket.getOutputStream(), true);
            while (true) {
              
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
