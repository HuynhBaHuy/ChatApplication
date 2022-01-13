package client;/*..
 * client
 * Created by HuynhBaHuy
 * Date 1/11/2022 1:30 PM
 * Description:...
 */

import client.data.BoxChatData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.util.ArrayList;

public class ChatFrame extends JFrame implements ActionListener {
    String usernameOfClient;
    BoxChatData data;
    JPanel mainPanel;
    JScrollPane listOnlineUsersPane;
    JList<String> listOnlineUsers;
    DefaultListModel<String> listOnlineUsersModel;
    JTextField messageTextField;
    JLabel nameUserLabel;
    JButton sendButton;
    JButton attachButton;
    JButton voiceButton;
    JButton emojiButton;
    JTextPane boxChatPane;
    JButton logoutButton;
    SocketController socketController;
    public ChatFrame(SocketController socketController, String username){
        usernameOfClient = username;
        this.socketController = socketController;
        data=new BoxChatData();
        // init panel
        mainPanel = new JPanel(new BorderLayout());
        //content panel: list user online and logout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        //send Panel: wrap all button for sending
        JPanel sendPanel = new JPanel(new GridBagLayout());

        nameUserLabel = new JLabel("Chat Box");
        nameUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        listOnlineUsersModel = new DefaultListModel<>();
        listOnlineUsers = new JList<>(listOnlineUsersModel);
        listOnlineUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ArrayList<String> onlineUsers = socketController.loadListOnlineUsers();
        listOnlineUsers.setFixedCellWidth(3);
        listOnlineUsers.setFixedCellHeight(20);
        boxChatPane = new JTextPane();
        boxChatPane.setEditable(false);
        if(!onlineUsers.isEmpty()){
            for (String user : onlineUsers) {
                // create list box chat
                data.appendBoxChat(user);
                listOnlineUsersModel.addElement(user);
            }
        }
        listOnlineUsers.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()>=1){
                    JList target = (JList)e.getSource();
                    int index = target.locationToIndex(e.getPoint());
                    if(index>=0){
                        Object usernameObj = target.getModel().getElementAt(index);
                        String username = usernameObj.toString();
                        boxChatPane.setDocument(data.findBoxChat(username).getBoxchat().getDocument());
                        nameUserLabel.setText(username);
                    }
                }

            }
        });
        listOnlineUsersPane = new JScrollPane(listOnlineUsers);
        JScrollPane boxchatScrollPane = new JScrollPane(boxChatPane);

        sendButton = new JButton(new ImageIcon("images/sendIcon.png"));
        sendButton.addActionListener(this);
        sendButton.setActionCommand("send");
        sendButton.setBackground(Themes.secondaryColor);
        attachButton = new JButton(new ImageIcon("images/attachIcon.png"));
        attachButton.addActionListener(this);
        attachButton.setActionCommand("attach");
        attachButton.setBackground(Themes.secondaryColor);
        emojiButton = new JButton(new ImageIcon("images/emojiIcon.png"));
        emojiButton.addActionListener(this);
        emojiButton.setActionCommand("emoji");
        emojiButton.setBackground(Themes.secondaryColor);
        voiceButton = new JButton(new ImageIcon("images/voiceIcon.png"));
        voiceButton.addActionListener(this);
        voiceButton.setActionCommand("voice");
        voiceButton.setBackground(Themes.secondaryColor);
        logoutButton = new JButton("Log out");
        logoutButton.addActionListener(this);
        logoutButton.setActionCommand("logout");
        logoutButton.setBackground(Themes.themeColor);
        messageTextField = new JTextField(20);
        Action action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                sendButton.doClick();

            }
        };
        messageTextField.addActionListener(action);
        createLeftPanel(contentPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx=1;
        gbc.weighty=1;
        gbc.insets = new Insets(0,5,5,5);
        gbc.gridx = 1;
        gbc.gridy= 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(nameUserLabel,gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.ipadx= 5;
        gbc.gridheight = 3;
        contentPanel.add(boxchatScrollPane,gbc);
        gbc.ipady=0;
        gbc.ipadx= 0;
        createSendPanel(sendPanel);
        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPanel.add(sendPanel,gbc);

        mainPanel.add(contentPanel,BorderLayout.CENTER);
        JLabel header = new JLabel("User: "+username);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(header,BorderLayout.PAGE_START);
        prepareGUI();
        startReceivingThread();
    }
    private void startReceivingThread() {
        BufferedReader br = socketController.getReader();
        new Thread(() -> {
            try {
                while(true){
                    System.out.println("receiving thread is running...");
                    String command = br.readLine();
                    switch (command) {
                        case "new online user" -> {
                            String username = br.readLine();
                            // create list box chat
                            data.appendBoxChat(username);
                            listOnlineUsersModel.addElement(username);
                        }
                        case "receive message" -> {
                            String username = br.readLine();
                            String content = br.readLine();
                            data.findBoxChat(username).receiveMessage(content);
                        }
                        case "username not online" -> {
                            String otherUser = br.readLine();
                            System.out.println(otherUser + " is offline now");
                        }
                        case "username not existed" -> {
                            String otherUser = br.readLine();
                            System.out.println(otherUser + " is not existed");
                        }
                        case "someone logout" ->{
                            String username = br.readLine();
                            data.removeBoxChat(username);
                            listOnlineUsersModel.removeElement(username);
                        }
                    }
                }
            }catch (Exception e){
                System.out.println(e.getMessage());

            }
        }).start();
    }
    private void createLeftPanel(JPanel container){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0,5,5,5);
        gbc.weightx=1;
        gbc.weighty=0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel listOnlineUsersLabel = new JLabel("List online users");
        listOnlineUsersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(listOnlineUsersLabel,gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight=5;
        container.add(listOnlineUsersPane,gbc);
        gbc.gridx = 0;
        gbc.gridy=6;
        gbc.ipady=0;
        container.add(logoutButton,gbc);
    }
    private void createSendPanel(JPanel container) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,2,2,2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        container.add(attachButton,gbc);

        gbc.gridx = 1;
        container.add(emojiButton,gbc);

        gbc.gridx = 2;
        container.add(voiceButton,gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        container.add(messageTextField,gbc);

        gbc.gridx=2;
        gbc.gridwidth=1;
        container.add(sendButton,gbc);
    }
    private void prepareGUI(){
        setTitle("Chat app");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setSize(500,500);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }
    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command){
            case "send":{
                String content = messageTextField.getText();
                String username = listOnlineUsers.getSelectedValue();
                if(username==null){
                    JOptionPane.showMessageDialog(this,"Please choose who to send message","Cannot send",JOptionPane.WARNING_MESSAGE);
                    messageTextField.setText("");
                    break;
                }
                data.findBoxChat(username).sendMessage(content,usernameOfClient);
                messageTextField.setText("");
                socketController.sendMessageToServer(content,username);
                break;
            }
            case "attach":{
                break;
            }
            case  "voice":{
                break;
            }
            case "logout":{
                socketController.sendLogoutToServer();
                this.dispose();
                new LoginFrame();
                break;
            }
        }
    }
}
