/*
 * Main.java
 *
 * Created on Pondelok, 2007, apríl 16, 14:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package chat;

/**
 *
 * @author sk1u06w4
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  throws Exception {
        // TODO code application logic here
        ChatClient client = new ChatClient();
        client.show();
        
    }
}
