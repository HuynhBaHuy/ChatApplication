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
    JButton sendButton;
    JButton fileButton;
    JButton voiceButton;
    JButton emojiButton;
    JTextArea boxChatArea;
    public  ChatFrame(){
        // init panel
        mainPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new GridBagLayout());
        JPanel chatPanel = new JPanel(new GridBagLayout());
        JPanel contentPanel = new JPanel(new GridBagLayout());

        prepareGUI();
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
