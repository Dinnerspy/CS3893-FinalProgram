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
    javax.swing.JTextArea jTextArea2;

    public clientInputRunner(Scanner in, JTextArea jTextArea1, JTextArea jTextArea2) {
        this.in = in;
        this.jTextArea1 = jTextArea1;
        this.jTextArea2 = jTextArea2;
    }

    @Override
    public void run() {
        try {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if(line.startsWith("CURRENTUSERS:")){
                    jTextArea2.setText("");
                    
                    line = line.replaceAll("\\[", "").replaceAll("\\]","");;
                    line = line.substring(13);
                    String holder[] =  line.split(",");
                    
                    for(int i =0; i<holder.length ; i++){
                    
                    jTextArea2.append(holder[i]+"\n");

                    }
                    
                    
                
                
                }else{

                jTextArea1.append(line+"\n");
                
                }
            }
        } finally {
            
            System.out.println("Thread closed");
        }
    }

}
