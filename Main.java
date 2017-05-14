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
        try {
            // TODO code application logic here
//        System.out.println(args[0]);
  
            if (args[0].length()>0){
                 new ChatClient(args[0]).show();
            }else{
                 new ChatClient("").show();
            }
        }
        catch(java.lang.ArrayIndexOutOfBoundsException e){
                 new ChatClient("").show();
        
        }finally {
        }
        
        
    }
}
