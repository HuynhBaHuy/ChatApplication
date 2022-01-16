package server;/*..
 * server
 * Created by HuynhBaHuy
 * Date 1/10/2022 5:14 PM
 * Description:...
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer extends JFrame implements ActionListener {
    JPanel mainPanel;
    JButton startBtn = new JButton("Start Server");
    public static JTextArea log = new JTextArea();
    static final UserController userController = new UserController();
    static List<ClientThread> clientThreadList = new ArrayList<>();
    ServerThread waitingThread;
    ServerSocket ss;
    private void prepareGUI() {
        setTitle("Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(400, 400));
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }

    public ChatServer() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        startBtn.addActionListener(this);
        startBtn.setActionCommand("start");
        log.setEditable(false);
        log.setMinimumSize(new Dimension(300, 300));
        log.setBorder(BorderFactory.createTitledBorder("Log Server"));
        JLabel header = new JLabel("Server Chat Application");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(header, BorderLayout.PAGE_START);
        mainPanel.add(log, BorderLayout.CENTER);
        mainPanel.add(startBtn, BorderLayout.PAGE_END);
        prepareGUI();
        try {
            ss = new ServerSocket(3200);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("start")) {
            log.append("Server booting ... \n");
            waitingThread = new ServerThread(ss);
            waitingThread.start();
            startBtn.setText("Close");
            startBtn.setActionCommand("close");

        } else {
            waitingThread.stop();
            log.append("Stop server \n");
            startBtn.setText("Start");
            startBtn.setActionCommand("start");
        }


    }
}
