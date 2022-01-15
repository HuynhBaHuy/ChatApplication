package client;/*..
 * client
 * Created by HuynhBaHuy
 * Date 1/11/2022 3:43 PM
 * Description:...
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginFrame extends JFrame implements ActionListener {


    JPanel mainPanel;
    JTextField usernameTextField;
    JPasswordField passwordField;
    JButton signupButton;
    JButton loginButton;
    JPanel controlPanel;
    JPanel inputPanel;
    public  LoginFrame(){
        // init panel
        mainPanel = new JPanel(new BorderLayout());
        inputPanel = new JPanel(new GridBagLayout());
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel,BoxLayout.X_AXIS));
        JPanel footerPanel = new JPanel(new FlowLayout());
        footerPanel.add(controlPanel);
        //init component
        usernameTextField = new JTextField();
        usernameTextField.addKeyListener(new KeyListener(){

            /**
             * Invoked when a key has been typed.
             * See the class description for {@link KeyEvent} for a definition of
             * a key typed event.
             *
             * @param e the event to be processed
             */
            @Override
            public void keyTyped(KeyEvent e) {

            }

            /**
             * Invoked when a key has been pressed.
             * See the class description for {@link KeyEvent} for a definition of
             * a key pressed event.
             *
             * @param e the event to be processed
             */
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    loginButton.doClick();
                }
            }

            /**
             * Invoked when a key has been released.
             * See the class description for {@link KeyEvent} for a definition of
             * a key released event.
             *
             * @param e the event to be processed
             */
            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        passwordField = new JPasswordField();
        passwordField.addKeyListener(new KeyListener(){

            /**
             * Invoked when a key has been typed.
             * See the class description for {@link KeyEvent} for a definition of
             * a key typed event.
             *
             * @param e the event to be processed
             */
            @Override
            public void keyTyped(KeyEvent e) {

            }

            /**
             * Invoked when a key has been pressed.
             * See the class description for {@link KeyEvent} for a definition of
             * a key pressed event.
             *
             * @param e the event to be processed
             */
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    loginButton.doClick();
                }
            }

            /**
             * Invoked when a key has been released.
             * See the class description for {@link KeyEvent} for a definition of
             * a key released event.
             *
             * @param e the event to be processed
             */
            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        signupButton = new JButton("Sign Up");
        loginButton = new JButton("Login");
        signupButton.addActionListener(this);
        signupButton.setActionCommand("sign up");
        loginButton.addActionListener(this);
        loginButton.setActionCommand("login");
        signupButton.setSize(88,40);
        loginButton.setSize(80,40);
        loginButton.setBackground(Themes.secondaryColor);
        signupButton.setBackground(Themes.secondaryColor);
        //add component to control panel
        controlPanel.add(signupButton);
        controlPanel.add(Box.createRigidArea(new Dimension(20,0)));
        controlPanel.add(loginButton);

        // add component to input panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5,5,5,5);
        gbc.weightx = 1;
        gbc.weighty = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Username"),gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Password"),gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.ipadx = 200;
        gbc.ipady = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(usernameTextField,gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(passwordField,gbc);
        JSplitPane contentPanel = new JSplitPane (JSplitPane.VERTICAL_SPLIT,inputPanel,footerPanel);
        //add to main panel
        JLabel loginHeader = new JLabel("LOGIN");
        loginHeader.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(loginHeader,BorderLayout.PAGE_START);
        mainPanel.add(contentPanel,BorderLayout.CENTER);
        //mainPanel.add(footerPanel,BorderLayout.PAGE_END);
        prepareGUI();
    }

    private void prepareGUI(){
        setTitle("Login");
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
        if(command.equals("login")){
            SocketController socketController;
            do {
                socketController = new SocketController();
                if(socketController.isConnectedToServer()){
                    JOptionPane.showMessageDialog(this,"Server is not running"," Lost Connection",JOptionPane.ERROR_MESSAGE);
                }
            }while(socketController.isConnectedToServer());
            String username = usernameTextField.getText();
            String password = new String(passwordField.getPassword());
            Boolean isSuccess = socketController.sendLoginToServer(username, password);
            if(isSuccess){
                this.dispose();
                new ChatFrame(socketController,username);
            }
            else{
                JOptionPane.showMessageDialog(this,"Something wrong. Try it later!","Login Failed",JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(command.equals("sign up")){
            new SignupFrame();
            this.setVisible(false);
            this.dispose();
        }
        else{
            System.out.println("Unknown clicked - not define event handling");
        }
    }
}
