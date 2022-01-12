package client;/*..
 * client
 * Created by HuynhBaHuy
 * Date 1/11/2022 3:44 PM
 * Description:...
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SignupFrame extends JFrame implements ActionListener {
    public static void main(String[] args){
        new SignupFrame();
    }
    JPanel mainPanel;
    JTextField usernameTextField;
    JPasswordField passwordField;
    JPasswordField confirmPasswordField;
    JButton signupButton;
    JButton backToLoginBtn;
    JPanel controlPanel;
    JPanel inputPanel;
    public  SignupFrame(){
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
                    signupButton.doClick();
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
                    signupButton.doClick();
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
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.addKeyListener(new KeyListener(){

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
                    signupButton.doClick();
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
        backToLoginBtn = new JButton("Back to login");
        signupButton.addActionListener(this);
        signupButton.setActionCommand("sign up");
        backToLoginBtn.addActionListener(this);
        backToLoginBtn.setActionCommand("back to login");
        signupButton.setSize(88,40);
        backToLoginBtn.setSize(80,40);
        backToLoginBtn.setBackground(Themes.secondaryColor);
        signupButton.setBackground(Themes.secondaryColor);
        //add component to control panel
        controlPanel.add(backToLoginBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(20,0)));
        controlPanel.add(signupButton);

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
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Confirm Password"),gbc);
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

        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(confirmPasswordField,gbc);
        JSplitPane contentPanel = new JSplitPane (JSplitPane.VERTICAL_SPLIT,inputPanel,footerPanel);
        //add to main panel
        JLabel loginHeader = new JLabel("SIGN UP");
        loginHeader.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(loginHeader,BorderLayout.PAGE_START);
        mainPanel.add(contentPanel,BorderLayout.CENTER);
        prepareGUI();
    }

    private void prepareGUI(){
        setTitle("Sign up");
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
        if(command.equals("back to login")){
            new LoginFrame();
            this.setVisible(false);
            this.dispose();
        }
        else if(command.equals("sign up")){
            String username = usernameTextField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            if(password.isEmpty()||confirmPassword.isEmpty()||username.isEmpty()){
                JOptionPane.showMessageDialog(this,"Please fill in all fields","Blank detection",JOptionPane.WARNING_MESSAGE);
            }
            else {
                if (password.equals(confirmPassword)) {
                    SocketController socketController;
                    do {
                        socketController = new SocketController();
                        if (!socketController.isConnected()) {
                            JOptionPane.showMessageDialog(this, "Server is not running", " Lost Connection", JOptionPane.ERROR_MESSAGE);
                        }
                    } while (!socketController.isConnected());

                    Boolean isSuccess = socketController.sendRegisterToServer(username, password);
                    if (isSuccess) {
                        new LoginFrame();
                        this.setVisible(false);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "This username have already existed", "Register failed", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Confirm password is not match", "Didn't match password", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        else{
            System.out.println("Unknown clicked - not define event handling");
        }
    }
}
