package client;/*..
 * client
 * Created by HuynhBaHuy
 * Date 1/11/2022 1:30 PM
 * Description:...
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatFrame extends JFrame implements ActionListener {
    public static void main(String[] args){
        new ChatFrame();
    }
    JPanel mainPanel;
    JScrollPane listOnlineUsers;
    JTextField messageTextField;
    JLabel nameUserLabel;
    JButton sendButton;
    JButton attachButton;
    JButton voiceButton;
    JButton emojiButton;
    JTextArea boxChatArea;
    JButton logoutButton;
    public  ChatFrame(){
        // init panel
        mainPanel = new JPanel(new BorderLayout());
        //left panel: list user online and logout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        //sendPanel: wrap all button for sending
        JPanel sendPanel = new JPanel(new GridBagLayout());
        nameUserLabel = new JLabel("Huynh Ba Huy");
        nameUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        listOnlineUsers = new JScrollPane();
        boxChatArea = new JTextArea();
        messageTextField = new JTextField();
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

        createLeftPanel(contentPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx=1;
        gbc.weighty=1;
        gbc.insets = new Insets(0,5,0,5);
        gbc.gridx = 1;
        gbc.gridy= 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(nameUserLabel,gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 4;
        contentPanel.add(boxChatArea,gbc);
        gbc.ipady=0;

        createSendPanel(sendPanel);
        gbc.gridx = 1;
        gbc.gridy = 5;
        contentPanel.add(sendPanel,gbc);
        mainPanel.add(contentPanel,BorderLayout.CENTER);
        prepareGUI();
    }
    private void createLeftPanel(JPanel container){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx=1;
        gbc.weighty=0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel listOnlineUsersLabel = new JLabel("List online users");
        listOnlineUsersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(listOnlineUsersLabel,gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady=300;
        gbc.gridheight=5;
        container.add(listOnlineUsers,gbc);
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

        gbc.gridx=2;
        container.add(voiceButton,gbc);


        gbc.gridx=0;
        gbc.gridy=2;
        gbc.gridwidth=2;
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

    }
}
