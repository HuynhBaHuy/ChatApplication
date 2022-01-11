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

public class LoginFrame extends JFrame implements ActionListener {

    public static void main(String[] args){
        new LoginFrame();
    }
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
        passwordField = new JPasswordField();
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
