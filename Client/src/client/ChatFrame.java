package client;/*..
 * client
 * Created by HuynhBaHuy
 * Date 1/11/2022 1:30 PM
 * Description:...
 */

import client.data.BoxChatData;
import client.data.FileAttach;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class ChatFrame extends JFrame implements ActionListener {
    String usernameOfClient;
    BoxChatData data;
    JPanel mainPanel;
    JScrollPane listOnlineUsersPane;
    JList<String> listOnlineUsers;
    DefaultListModel<String> listOnlineUsersModel;
    JTextField messageTextField;
    JScrollPane boxchatScrollPane;
    JButton sendButton;
    JButton attachButton;
    JButton voiceButton;
    JButton emojiButton;
    JButton showMoreButton;
    JTextPane boxChatPane;
    JButton logoutButton;
    DefaultListModel<String> listAttachmentModel;
    JScrollPane attachmentFilePane;
    JList<String> attachFileList;
    final JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    SocketController socketController;
    public ChatFrame(SocketController socketController, String username){
        usernameOfClient = username;
        this.socketController = socketController;
        data=new BoxChatData();
        // init panel
        mainPanel = new JPanel(new BorderLayout());
        //content panel: list user online and logout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        JPanel sendPanel = new JPanel(new GridBagLayout());
        listAttachmentModel = new DefaultListModel<>();
        attachFileList = new JList<>(listAttachmentModel);
        attachFileList.setFixedCellWidth(250);
        attachFileList.setFixedCellHeight(20);
        attachmentFilePane = new JScrollPane(attachFileList);
        attachmentFilePane.setBorder(BorderFactory.createTitledBorder("Incoming Attachments"));
        attachFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listOnlineUsersModel = new DefaultListModel<>();
        listOnlineUsers = new JList<>(listOnlineUsersModel);
        listOnlineUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ArrayList<String> onlineUsers = socketController.loadListOnlineUsers();
        listOnlineUsers.setFixedCellWidth(250);
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

        listOnlineUsersPane = new JScrollPane(listOnlineUsers);
        listOnlineUsersPane.setBorder(BorderFactory.createTitledBorder("List Online User"));
        boxchatScrollPane = new JScrollPane(boxChatPane);
        TitledBorder titleChatBox = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.black), "Chat box");
        titleChatBox.setTitleJustification(TitledBorder.CENTER);
        boxchatScrollPane.setBorder(titleChatBox);
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
        showMoreButton = new JButton(new ImageIcon("images/showmore.png"));
        showMoreButton.addActionListener(this);
        showMoreButton.setActionCommand("show more");
        showMoreButton.setBackground(Themes.secondaryColor);
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
        gbc.weighty=0.1;
        gbc.insets = new Insets(0,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.ipadx= 5;
        gbc.gridheight = 3;
        contentPanel.add(boxchatScrollPane,gbc);
        gbc.ipady=0;
        gbc.ipadx= 0;
        createSendPanel(sendPanel);
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(sendPanel,gbc);
        mainPanel.add(contentPanel,BorderLayout.CENTER);
        JLabel header = new JLabel("Hello "+username+". Welcome to chat app");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(header,BorderLayout.PAGE_START);
        prepareGUI();

        // event listener
        startReceivingThread();
        attachFileList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()>=1){
                    JList target = (JList)e.getSource();
                    int index = target.locationToIndex(e.getPoint());
                    if(index>=0){
                        Object fileNameObj = target.getModel().getElementAt(index);
                        String fileName = fileNameObj.toString();
                        JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int returnVal = fc.showSaveDialog(boxChatPane);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            String path = file.getPath();
                            socketController.sendDownloadFile(usernameOfClient,fileName,path);
                        }
                    }
                }

            }
        });
        listOnlineUsers.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()>=1){
                    JList target = (JList)e.getSource();
                    int index = target.locationToIndex(e.getPoint());
                    if(index>=0){
                        listAttachmentModel.clear();
                        Object usernameObj = target.getModel().getElementAt(index);
                        String username = usernameObj.toString();
                        boxChatPane.setDocument(data.findBoxChat(username).getBoxchat().getDocument());
                        header.setText("Chat with: "+username);
                        ArrayList<FileAttach> listFileAttachments = data.findBoxChat(username).getListFileAttachments();
                        if(!listFileAttachments.isEmpty()){
                            for (FileAttach fileAttachment : listFileAttachments) {
                                listAttachmentModel.addElement(fileAttachment.getFileName());
                            }
                        }
                    }
                }

            }
        });
    }
    private void startReceivingThread() {
        BufferedReader br = socketController.getReader();
        new Thread(() -> {
            try {
                while(true){
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
                        case "receive download file"->{

                            String path = br.readLine();
                            String fileName = br.readLine();
                            String location = path +"\\"+fileName;

                            int fileSize = data.findBoxChat(listOnlineUsers.getSelectedValue()).getFileAttachment(fileName).getFileSize();
                            FileOutputStream fos = new FileOutputStream(location);
                            InputStream is = socketController.getInputStream();
                            int totalBytes = 0;
                            int numberBytesRead;
                            byte[] bytes = new byte[1024];
                            while ((numberBytesRead = is.read(bytes))>0){
                                fos.write(bytes, 0,numberBytesRead);
                                totalBytes +=numberBytesRead;
                                if(totalBytes>=fileSize){
                                    break;
                                }
                            }
                            JOptionPane.showMessageDialog(this,"Download file "+fileName+" successful","Download File",JOptionPane.INFORMATION_MESSAGE);
                            fos.close();
                        }
                        case "receive file"->{
                            String username = br.readLine();
                            String fileName = br.readLine();
                            int fileSize = Integer.parseInt(br.readLine());
                            System.out.println(fileName+fileSize);
                            data.findBoxChat(username).receiveFile(fileName,fileSize);
                            System.out.println(listOnlineUsers.getSelectedValue()+" "+username);
                            if(Objects.equals(listOnlineUsers.getSelectedValue(), username)){
                                listAttachmentModel.addElement(fileName);
                            }
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
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        container.add(listOnlineUsersPane,gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        container.add(attachmentFilePane,gbc);
        gbc.gridx = 0;
        gbc.gridy=5;
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
        gbc.gridx = 3;
        container.add(showMoreButton,gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        container.add(messageTextField,gbc);

        gbc.gridx=3;
        gbc.gridwidth=1;
        container.add(sendButton,gbc);
    }
    private void prepareGUI(){
        setTitle("Chat app: "+usernameOfClient);
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
            case "send"->{
                String content = messageTextField.getText();
                String username = listOnlineUsers.getSelectedValue();
                if(username==null){
                    JOptionPane.showMessageDialog(this,"Please choose who to send message","Cannot send message",JOptionPane.WARNING_MESSAGE);
                    messageTextField.setText("");
                    break;
                }
                data.findBoxChat(username).sendMessage(content,usernameOfClient);
                messageTextField.setText("");
                socketController.sendMessageToServer(content,username);
            }
            case "attach"->{
                String username = listOnlineUsers.getSelectedValue();
                if(username==null){
                    JOptionPane.showMessageDialog(this,"Please choose who to send files","Cannot send message",JOptionPane.WARNING_MESSAGE);
                    messageTextField.setText("");
                    break;
                }
                int returnVal = fileChooser.showDialog(this,"Attach");
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    socketController.sendFilesToServer(file,username);
                    data.findBoxChat(username).sendFile(file,username);
                }
                else{
                    System.out.println("Open command cancelled by user.");
                }

            }
            case  "voice"->{
            }
            case "logout"->{
                socketController.sendLogoutToServer();
                this.dispose();
                new LoginFrame();
            }
            case "show more"->{
                break;
            }
        }
    }
}
