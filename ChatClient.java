/*
 * ChatClient.java
 *
 * Created on Pondelok, 2007, apr�l 16, 14:44
 */

package chat;
import java.net.*;
import java.io.*;
/**
 *
 * @author  sk1u06w4
 */
public class ChatClient extends javax.swing.JFrame {
    //public static final int CHAT_PORT=12345;
    public static String HOST="localhost";
    public static final int CHAT_PORT=12345;
    //public static String HOST="sk16614c.siemens-pse.sk";
    
    //Pomocou tohto socketu komunikujem so serverom
    Socket socket;

    //Streamy na komunikaciu
    BufferedReader in;
    OutputStreamWriter out;
    
    /** Creates new form ChatClient */
    public ChatClient() {}
    
    public ChatClient(String login) {
        if (login.length()>0){skMyLogin=login;}else{
        skMyLogin=System.getProperty("user.name","someone");}
        try{
            //Vytvorenie spojenia so serverom
            socket = new Socket(HOST, CHAT_PORT);

            //Streamy na komunikaciu
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new OutputStreamWriter(socket.getOutputStream());
            //out.write(System.getProperty("user.name","someone")+" appeared\n");
            out.write("6\n1\n");
            out.write(skMyLogin+"\n");//enter
            out.flush();
        }catch (Exception e){
            System.out.println("Chyba "+e.getMessage());
            System.exit(1);
        }

        initComponents();
        new ReceiveThread().start();
        //userList();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        skTextArea = new javax.swing.JTextArea();
        skMsgField = new javax.swing.JTextField();
        skSendButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(skMyLogin);
        setResizable(false);
        jTabbedPane1.setVerifyInputWhenFocusTarget(false);
        jSplitPane1.setDividerLocation(500);
        jSplitPane2.setDividerLocation(200);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        skListModel=new javax.swing.DefaultListModel();
        jList1.setModel(skListModel
        );
        jScrollPane2.setViewportView(jList1);

        jSplitPane2.setTopComponent(jScrollPane2);

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList2);

        jSplitPane2.setRightComponent(jScrollPane3);

        jSplitPane1.setRightComponent(jSplitPane2);

        skTextArea.setColumns(20);
        skTextArea.setEditable(false);
        skTextArea.setRows(5);
        skTextArea.setText("just click on listed chat or user to start\n");
        jScrollPane1.setViewportView(skTextArea);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jTabbedPane1.addTab("info", jSplitPane1);

        skMsgField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skMsgFieldActionPerformed(evt);
            }
        });

        skSendButton.setText("Send");
        skSendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skSendButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(skMsgField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(skSendButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 386, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(skSendButton)
                    .add(skMsgField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void skMsgFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skMsgFieldActionPerformed
        // TODO add your handling code here:
        enterMsg();
    }//GEN-LAST:event_skMsgFieldActionPerformed

    private void skSendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skSendButtonActionPerformed
        enterMsg();
    }//GEN-LAST:event_skSendButtonActionPerformed
    
    private void userList(){
        
        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "user 1", "user 2","user3" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
    }
    
     private void enterMsg(){
        try{
            //Odoslanie spravy
            out.write("0\n1\n"+skMyLogin+"> "+skMsgField.getText()+"\n");
            out.flush();
            //Vymazanie obsahu pola skMsgField
            
            skMsgField.setText("");
        }catch (Exception e){
            System.out.println("Chyba "+e.getMessage());
            System.exit(1);
        }
    } 
        //Sluzi na prijem sprav zo servera a vypis do okna
    class ReceiveThread extends Thread {
        //Metoda vykonavaneho vlakna
        public void run(){
            try{
                String line;
                //Precitam riadok zo standardneho vstupu
                
                    //Vypisem riadok
                while ((line = in.readLine()) != null) {
                    //line=in.readLine();
                int msgCode=new Integer(line).intValue();
                switch (msgCode){
                    case 1:
         //               skTextArea.append(line.substring(1));
                        skListModel.removeAllElements();
                        int users=new Integer(in.readLine()).intValue();
                        for (int i=0; i<users;i++) {
                            line=in.readLine();                                       
                            skListModel.addElement(line+"\n");                        
                        }
                            //                System.out.println(line);
                        break;
                        
                    case 0:
                        int lines=new Integer(in.readLine()).intValue();
                        for (int i=0; i<lines;i++) {
                            line=in.readLine();                                       
                            skTextArea.append(line+"\n");
                        }
                        break;
                    default:
//                        while ((line = in.readLine()) != null) 
                            skTextArea.append("default  "+line+"\n");
//                        skTextArea.append(line+"\n");break;
                }
                }
                //Uzavriem streamy
                in.close();
                out.close();
            }catch (Exception e){
                System.out.println("Chyba "+e.getMessage());
            }
            System.exit(0);
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatClient().setVisible(true);
            }
        });
    }

    private String skMyLogin;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField skMsgField;
    private javax.swing.JButton skSendButton;
    private javax.swing.JTextArea skTextArea;
    // End of variables declaration//GEN-END:variables
    private javax.swing.DefaultListModel skListModel;

}
