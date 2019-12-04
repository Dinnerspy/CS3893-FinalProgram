/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomclient;

import java.util.Scanner;
import javax.swing.JTextArea;

/**
 *
 * @author Nicho
 */
public class clientInputRunner implements Runnable {

    Scanner in;
    javax.swing.JTextArea jTextArea1;

    public clientInputRunner(Scanner in, JTextArea jTextArea1) {
        this.in = in;
        this.jTextArea1 = jTextArea1;
    }

    @Override
    public void run() {
        try {
            while (in.hasNextLine()) {
                String line = in.nextLine();

                jTextArea1.append(line+"\n");
            }
        } finally {
            
            System.out.println("Thread closed");
        }
    }

}
