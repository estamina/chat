/*
 * ChatClient.java
 *
 * Created on Pondelok, 2007, apríl 16, 14:44
 */

package chat;
import java.net.*;
import java.awt.*;
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

        //private LinkedList globalusers=new LinkedList();

        private String chatname="";

        private int chatid=-1;

        private String chattobe;

        public void fieldActionPerformed(java.awt.event.ActionEvent evt) {
            try{
                //Odoslanie spravy
                out.write(msgIntro+"\n0\n");
                out.write(new Integer(chatid).toString());
                out.write("\n");
                if (chatid==-1){
                    out.write("1\n");
                    out.write(chattobe+"\n");
                }else{
                    if (selectedusers!=null){
                        out.write(selectedusers.size()+"\n");
                        for (int i=0;i<selectedusers.size();i++){
                            out.write(selectedusers.get(i).toString()+"\n");
                        }
                        selectedusers=null;
                    } else out.write("0\n");
                }
                out.write(chatname+"\n");

                out.write("1\n"+skMyNick+"> "+field.getText()+"\n");
                out.flush();
                //Vymazanie obsahu pola skMsgField

                field.setText("");
            }catch (Exception e){
                System.out.println("Chyba1 "+e.getMessage());
                System.exit(1);
            }
        }

        public void enterMsg() {
            try{
                //Odoslanie spravy
                out.write(msgIntro+"\n0\n1\n"+skMyNick+"> "+field.getText()+"\n");
                out.flush();
                //Vymazanie obsahu pola skMsgField

                field.setText("");
            }catch (Exception e){
                System.out.println("Chyba2 "+e.getMessage());
                System.exit(1);
            }
        }

        public void initComponents() {
            split1 = new javax.swing.JSplitPane();
            split2 = new javax.swing.JSplitPane();
            scroll1=new javax.swing.JScrollPane();
            text=new javax.swing.JTextArea();
            split3 = new javax.swing.JSplitPane();
            scroll2=new javax.swing.JScrollPane();
            userlist = new javax.swing.JList();
            scroll3=new javax.swing.JScrollPane();
            chatlist = new javax.swing.JList();
            field= new javax.swing.JTextField();
            userlistmodel=new javax.swing.DefaultListModel();
            userscellrenderer=new UsersCellRenderer();
            chatlistmodel=new javax.swing.DefaultListModel();
            //tb.text.setText("daco");

            split1.setDividerLocation(375);
            split1.setDividerSize(15);
            split1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
            split2.setDividerLocation(500);
            text.setColumns(20);
            text.setRows(5);
            scroll1.setViewportView(text);

            split2.setLeftComponent(scroll1);

            split3.setDividerLocation(180);
            split3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
            userlist.setModel(skUserListModel);

            userlist.setCellRenderer(userscellrenderer);
            userlist.setFixedCellHeight(10);
            userlist.setFixedCellWidth(10);

            userlist.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    userlistMouseClicked(evt);
                }
            });

            scroll2.setViewportView(userlist);

            split3.setLeftComponent(scroll2);

            chatlist.setModel(chatlistmodel);
            scroll3.setViewportView(chatlist);

            split3.setRightComponent(scroll3);
            split2.setRightComponent(split3);
            split1.setLeftComponent(split2);

            //tb.field.setText("tbfield");
            field.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    fieldActionPerformed(evt);
                }
            });

            split1.setRightComponent(field);

        }

        public void userlistMouseClicked(java.awt.event.MouseEvent evt) {

            
                 if (evt.getClickCount() == 2) {
             int index = userlist.locationToIndex(evt.getPoint());
             String anick=skUserListModel.getElementAt(index).toString();
             if (anick.compareTo(skMyNick+"\n")!=0)
                if (findTab(getUser(anick))==null)
                    addTab(anick,-1);}//chatid is -1 while it is not assigned from server yet

             selectedusers=new ArrayList();
            if(evt.getButton()==3){
                java.lang.Object[] indicies=userlist.getSelectedValues();
                for (int i=0;i<indicies.length;i++)
                    //System.out.println("selected " + indicies[i].toString());
                    selectedusers.add(getUser(indicies[i].toString()));
            }
        }

        private ArrayList selectedusers=null;

        private UsersCellRenderer userscellrenderer;
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
            out.write(msgIntro+"\n6\n1\n");
            out.write(skMyLogin+"\n");//enter
            out.flush();
        }catch (Exception e){
            System.out.println("Chyba3 "+e.getMessage());
            System.exit(1);
        }
/*
        java.awt.Font f=skRenderer.getFont();
        f.getName();
        skRenderer.setFont(f);
 */
        skRenderer.setBackground(java.awt.Color.CYAN);
        initComponents();
        tabList = new LinkedList();
        new ReceiveThread().start();
        //updateUserList();
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
        skUserList = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        skChatList = new javax.swing.JList();
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
        skUserListModel=new javax.swing.DefaultListModel();
        skUserList.setModel(skUserListModel
        );
        skUserList.setCellRenderer(skRenderer);
        skUserList.setFixedCellHeight(10);
        skUserList.setFixedCellWidth(10);
        skUserList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                skUserListMouseClicked(evt);
            }
        });

        jScrollPane2.setViewportView(skUserList);

        jSplitPane3.setTopComponent(jScrollPane2);

        skChatListModel=new javax.swing.DefaultListModel();
        skChatList.setModel(skChatListModel);
        jScrollPane3.setViewportView(skChatList);

        jSplitPane3.setRightComponent(jScrollPane3);

        jSplitPane2.setRightComponent(jSplitPane3);

        jSplitPane1.setLeftComponent(jSplitPane2);

        skMsgField.setEnabled(false);
        skMsgField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skMsgFieldActionPerformed(evt);
            }
        });

        jSplitPane1.setRightComponent(skMsgField);

        jTabbedPane1.addTab("!", jSplitPane1);

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

    private void skUserListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_skUserListMouseClicked
// TODO add your handling code here:
                 if (evt.getClickCount() == 2) {
             int index = skUserList.locationToIndex(evt.getPoint());
             String anick=skUserListModel.getElementAt(index).toString();
             if (anick.compareTo(skMyNick+"\n")!=0)
                if (findTab(getUser(anick))==null)
                    addTab(anick,-1);//chatid is -1 while it is not assigned from server yet
          }
    }//GEN-LAST:event_skUserListMouseClicked

    private void skMsgFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skMsgFieldActionPerformed
        // TODO add your handling code here:
        enterMsg();
    }//GEN-LAST:event_skMsgFieldActionPerformed

    private void updateUserList(){
//        skUserListModel.removeAllElements();
        skUserListModel.clear();
           // System.gc();
        skTextArea.setText("");
        for (Iterator i=skGlobalUsers.iterator();i.hasNext();){
            String nick=((skUser)i.next()).nick;
          //  skTextArea.append(nick+"\n");
            skUserListModel.addElement(nick+"\n");
            skUserList.ensureIndexIsVisible(skUserListModel.size());
  /*
            String cell=null;
            boolean t1=false,t2=false;
            javax.swing.JLabel l1=(javax.swing.JLabel)(skUserList.getCellRenderer()).getListCellRendererComponent(skUserList,cell,skUserListModel.size(),t1,t2);
            java.awt.Rectangle g1=l1.getBounds();
            skTextArea.append("xy "+g1.x+" "+g1.y+" "+g1.width+" "+g1.height+"\n");
    */
            java.awt.Rectangle g1=skRenderer.getBounds();
            skTextArea.append(skRenderer.getFont().getFontName()+" xy "+g1.x+" "+g1.y+" "+g1.width+" "+g1.height+"\n");
      //   skUserList.updateUI();
        }

        for (Enumeration e = skUserListModel.elements() ; e.hasMoreElements() ;) {
            skTextArea.append(e.nextElement()+"\n");
        }
    }

     private void enterMsg(){
        try{
            //Odoslanie spravy
            out.write(msgIntro+"\n0\n1\n"+skMyNick+"> "+skMsgField.getText()+"\n");
            out.flush();
            //Vymazanie obsahu pola skMsgField

            skMsgField.setText("");
        }catch (Exception e){
            System.out.println("Chyba4 "+e.getMessage());
            System.exit(1);
        }
    }
        //Sluzi na prijem sprav zo servera a vypis do okna
    class ReceiveThread extends Thread {
        //Metoda vykonavaneho vlakna
        public void run(){
            int id;
            tab atb;

            try{
                String line;
                //Precitam riadok zo standardneho vstupu

                    //Vypisem riadok
                while ((line = in.readLine()) != null) {
                    while (line.compareTo(msgIntro)!=0){line = in.readLine();}
                    line = in.readLine();
                    System.out.println("msgcode:"+line);
                    //line=in.readLine();
                int msgCode=new Integer(line).intValue();
                switch (msgCode){
                    case 1:
         //               skTextArea.append(line.substring(1));
                        skGlobalUsers.clear();

                        int users=new Integer(in.readLine()).intValue();
                        for (int i=0; i<users;i++) {
                            skUser user=new skUser();
                            line=in.readLine();
                            user.user=line;
                            if (line.compareTo(skMyLogin)==0){skMyNick=line=in.readLine();setTitle(skMyNick);}
                            else line=in.readLine();
                            user.nick=line;
                            System.out.println(user.nick+" "+user.user);
                            skGlobalUsers.add(user);
                        }
                        updateUserList();
                            //                System.out.println(line);
                        break;
                    case 2:
                        id=new Integer(in.readLine()).intValue();
                        int iusers=new Integer(in.readLine()).intValue();
                        String otheruser=null,othernick=null;
                        ArrayList chatusers=new ArrayList();
                        for (int i=0;i<iusers;i++){
                            line=in.readLine();
                            if(line.compareTo(skMyLogin)!=0)otheruser=line;
                            line=in.readLine();
                            if(line.compareTo(skMyNick)!=0)othernick=line;
                            chatusers.add(line);
                        }
                        iusers--;
                        atb=findTab(id);
                        if (atb==null){
                            atb=findTab(otheruser);
                            if (atb==null) atb=addTab(othernick,id);
                            else atb.chatid=id;
                        }else{
/*                            
                                            atb.userlistmodel.removeAllElements();
            for (Iterator i=skGlobalUsers.iterator();i.hasNext();){
                atb.userlistmodel.addElement(((skUser)i.next()).nick);
            }
 */
                        }
                        atb.userscellrenderer.chatusers=chatusers;
                        break;
                    case 4:
                        id=new Integer(in.readLine()).intValue();
                        int lines=new Integer(in.readLine()).intValue();
                        atb=findTab(id);
                        for (int i=0; i<lines;i++) {
                            line=in.readLine();
                            atb.text.append(line+"\n");
                        }
                        break;
                    case 3:
                        //num of chats
                        line=in.readLine();
                        id=new Integer(in.readLine()).intValue();
                        atb=findTab(id);
                        atb.chatname=in.readLine();
                        atb.chattobe="";

                        int ind=jTabbedPane1.indexOfComponent(atb.split1);
                        jTabbedPane1.setTitleAt(ind,atb.chatname);
                        //atb.split1.setTitleAt(ind,atb.chatname);


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
                System.out.println("Chyba5 "+e.getMessage());
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
        String msgIntro="\6";
    private String skMyLogin;
    private String skMyNick;
    private static LinkedList tabList;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList skChatList;
    private javax.swing.JTextField skMsgField;
    private javax.swing.JTextArea skTextArea;
    private javax.swing.JList skUserList;
    // End of variables declaration//GEN-END:variables
    private javax.swing.DefaultListModel skUserListModel;
    private javax.swing.DefaultListModel skChatListModel;

    public tab addTab(String name, int lid) {


            tab tb=new tab();

            tb.initComponents();
            //tb.chatname=name;
            tb.chattobe=getUser(name);
            tb.chatid=lid;
            System.out.println(tb.chattobe);

            //        skGlobalUsers
            jTabbedPane1.addTab(name,tb.split1);


/*
            tb.userlistmodel.removeAllElements();
            for (Iterator i=skGlobalUsers.iterator();i.hasNext();){
                tb.userlistmodel.addElement(((skUser)i.next()).nick);
            }
 **/


            tabList.add(tb);
            return tb;

    }

    private LinkedList skGlobalUsers=new LinkedList();

    public final class skUser {
        private String nick;

        private String user;
    }

    public String getUser(String nick) {
        String user=null;
        for (Iterator i=skGlobalUsers.iterator();i.hasNext();){
            skUser needle=(skUser)i.next();
                System.out.println(needle.user+" "+needle.nick+" "+nick.trim());
            if ((needle).nick.compareTo(nick.trim())==0) {
                user=needle.user;
                                System.out.println(needle.user+" found "+needle.nick);

                break;
            }
        }
        return user;
    }

    public tab findTab(int lid) {
        tab ltab=null;
        for (Iterator i=tabList.iterator();i.hasNext();){
            ltab=(tab)i.next();
            if ((ltab).chatid==lid)break;
            else ltab=null;
        }

        return ltab;
    }

    public tab findTab(String user) {
        tab ltab=null;
        for (Iterator i=tabList.iterator();i.hasNext();){
            ltab=(tab)i.next();
            if ((ltab).chattobe.compareTo(user)==0){
                System.out.println(user+" found "+ltab.chattobe);
                break;
            }
            else ltab=null;
        }

        return ltab;
    }

    public class UsersCellRenderer extends javax.swing.JLabel implements javax.swing.ListCellRenderer {
      public java.awt.Component getListCellRendererComponent(javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            //if (index>0)
          boolean found=false;
          for (int i=0;i<chatusers.size();i++){
              if (chatusers.get(i).toString().compareTo(value.toString().trim())==0) {found=true;break;}
          }
          if (found){
              setBackground(isSelected ? Color.blue : Color.white);
              setForeground(isSelected ? Color.white : Color.gray);
          }else{
                setBackground(isSelected ? Color.red : Color.white);
                setForeground(isSelected ? Color.white : Color.black);          
          }
          setText(value.toString());
          /*
            if (value.toString().compareTo("nick_D\n")==0)setText(value.toString()+" tento");
            else    setText(value.toString());
            if (index==2) setBackground(java.awt.Color.MAGENTA);
            else setBackground(isSelected ? Color.red : Color.white);
         setForeground(isSelected ? Color.white : Color.black);
           */
            return this;
        }

        public UsersCellRenderer() {
         setOpaque(true);
        }

        private ArrayList chatusers=new ArrayList();
    }

    private UsersCellRenderer1 skRenderer=new UsersCellRenderer1();

    public  class UsersCellRenderer1 extends javax.swing.DefaultListCellRenderer  {
        public UsersCellRenderer1() {
            super();
         //   setFont(java.awt.Font.ITALIC);
        }

    }




}
