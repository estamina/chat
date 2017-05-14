/*
 * ChatClient.java
 *
 * Created on Pondelok, 2007, apr�l 16, 14:44
 */

package chat;
import java.net.*;
import java.util.*;
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

    class tab{
            javax.swing.JScrollPane scroll1, scroll2, scroll3;
            javax.swing.JTextArea text;
            javax.swing.JSplitPane split1, split2, split3;
            javax.swing.JTextField field;
            javax.swing.JList userlist, chatlist;
            javax.swing.DefaultListModel userlistmodel, chatlistmodel;
     }

    //Streamy na komunikaciu
    BufferedReader in;
    OutputStreamWriter out;

    /** Creates new form ChatClient */
    public ChatClient() {}

    public ChatClient(String login) {
        if (login.length()>0){skMyNick=skMyLogin=login;}else{
        skMyNick=skMyLogin=System.getProperty("user.name","someone");}
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
        tabList = new LinkedList();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        skTextArea = new javax.swing.JTextArea();
        jSplitPane3 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        userlist = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        chatlist = new javax.swing.JList();
        skMsgField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(skMyNick);
        setResizable(false);
        jTabbedPane1.setVerifyInputWhenFocusTarget(false);
        jSplitPane1.setDividerLocation(375);
        jSplitPane1.setDividerSize(15);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setDividerLocation(500);
        skTextArea.setColumns(20);
        skTextArea.setEditable(false);
        skTextArea.setRows(5);
        skTextArea.setText("just click on listed chat or user to start\n");
        jScrollPane1.setViewportView(skTextArea);

        jSplitPane2.setLeftComponent(jScrollPane1);

        jSplitPane3.setDividerLocation(200);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        skList1Model=new javax.swing.DefaultListModel();
        userlist.setModel(skList1Model
        );
        jScrollPane2.setViewportView(userlist);

        jSplitPane3.setTopComponent(jScrollPane2);

        skList2Model=new javax.swing.DefaultListModel();
        chatlist.setModel(skList2Model);
        jScrollPane3.setViewportView(chatlist);

        jSplitPane3.setRightComponent(jScrollPane3);

        jSplitPane2.setRightComponent(jSplitPane3);

        jSplitPane1.setLeftComponent(jSplitPane2);

        skMsgField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skMsgFieldActionPerformed(evt);
            }
        });

        jSplitPane1.setRightComponent(skMsgField);

        jTabbedPane1.addTab("info", jSplitPane1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void skMsgFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skMsgFieldActionPerformed
        // TODO add your handling code here:
        enterMsg();
    }//GEN-LAST:event_skMsgFieldActionPerformed

    private void userList(){

        userlist.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "user 1", "user 2","user3" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
    }

     private void enterMsg(){
        try{
            //Odoslanie spravy
            out.write("0\n1\n"+skMyNick+"> "+skMsgField.getText()+"\n");
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
                        skList1Model.removeAllElements();
                        int users=new Integer(in.readLine()).intValue();
                        for (int i=0; i<users;i++) {
                            line=in.readLine();
                            if (line.compareTo(skMyLogin)==0){skMyNick=line=in.readLine();setTitle(skMyNick);}
                            else line=in.readLine();
                            skList1Model.addElement(line+"\n");
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
    private String skMyNick;
    private static LinkedList tabList;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList chatlist;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField skMsgField;
    private javax.swing.JTextArea skTextArea;
    private javax.swing.JList userlist;
    // End of variables declaration//GEN-END:variables
    private javax.swing.DefaultListModel skList1Model;
    private javax.swing.DefaultListModel skList2Model;

    public void addTab() {
 

            tab tb=new tab();
            tb.split1 = new javax.swing.JSplitPane();
            tb.split2 = new javax.swing.JSplitPane();
            tb.scroll1=new javax.swing.JScrollPane();
            tb.text=new javax.swing.JTextArea();
            tb.split3 = new javax.swing.JSplitPane();
            tb.scroll2=new javax.swing.JScrollPane();
            tb.userlist = new javax.swing.JList();
            tb.scroll3=new javax.swing.JScrollPane();
            tb.chatlist = new javax.swing.JList();
            tb.field= new javax.swing.JTextField();
            tb.userlistmodel=new javax.swing.DefaultListModel();
            tb.chatlistmodel=new javax.swing.DefaultListModel();
            //tb.text.setText("daco");
            
            tb.split1.setDividerLocation(375);
            tb.split1.setDividerSize(15);
            tb.split1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
            tb.split2.setDividerLocation(500);
            tb.text.setColumns(20);
            tb.text.setRows(5);
            tb.scroll1.setViewportView(tb.text);

            tb.split2.setLeftComponent(tb.scroll1);
           
            tb.split3.setDividerLocation(180);
            tb.split3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
            tb.userlist.setModel(tb.userlistmodel);
            tb.scroll2.setViewportView(tb.userlist);

            tb.split3.setLeftComponent(tb.scroll2);

            tb.chatlist.setModel(tb.chatlistmodel);
            tb.scroll3.setViewportView(tb.chatlist);

            tb.split3.setRightComponent(tb.scroll3);
            tb.split2.setRightComponent(tb.split3);
            tb.split1.setLeftComponent(tb.split2);

            tb.field.setText("tbfield");
            tb.split1.setRightComponent(tb.field);
            
            jTabbedPane1.addTab("tb",tb.split1);
            tabList.add(tb);
        
    }

}
