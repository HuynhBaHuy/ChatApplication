package client;/*..
 * client
 * Created by HuynhBaHuy
 * Date 1/10/2022 5:11 PM
 * Description:...
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatClient {
    public static void main(String[] args) {
        new ChatClient();
    }
    public ChatClient(){
        try{
            Socket socket = new Socket("localhost",3200);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                System.out.println("Login");

                System.out.print("Username: ");
                String username = read.readLine();
                System.out.print("Password: ");
                String password = read.readLine();
                bw.write("login");
                bw.newLine();
                bw.write(username);
                bw.newLine();
                bw.write(password);
                bw.newLine();
                bw.flush();
                String command = br.readLine();
                if(command.equals("login success")){
                    System.out.println("Login successful");
                    new ThreadReceviedHandler(socket).start();
                    // load list online user
                    bw.write("load list online user");
                    bw.newLine();
                    bw.flush();
                    command = br.readLine();
                    if(command.equals("list online user")){
                        System.out.println("List online user");
                        List<String> listOnlineUsers = new ArrayList<String>();
                        String num = br.readLine();
                        int numberOfOnlineUsers = Integer.parseInt(num);
                        for(int i=0;i<numberOfOnlineUsers;i++){
                          String onlineUser = br.readLine();
                          listOnlineUsers.add(onlineUser);
                        }
                        if(listOnlineUsers.isEmpty()){
                            System.out.println("No one is online");
                        }
                        else{
                            for(String onlineUser : listOnlineUsers){
                                System.out.println(onlineUser+" is Online");
                            }
                        }

                    }
                    while (true){
                        System.out.print("Enter user to send message: ");
                        String otherUser = read.readLine();
                        if(otherUser == "quit"){
                            break;
                        }
                        while(true){
                            System.out.print("Enter message: ");
                            String content = read.readLine();
                            if(content!="quit"){
                                bw.write("send message");
                                bw.newLine();
                                bw.write(otherUser);
                                bw.newLine();
                                bw.write(content);
                                bw.newLine();
                                bw.flush();
                            }
                            else{
                                break;
                            }
                        }
                    }

                }
                else if(command.equals("login failed")) {
                    System.out.println("Login failed, Try again");
                }
                else{
                    System.out.println("Unknow command. Quit program");
                    break;
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
class ThreadReceviedHandler extends Thread {
    Socket socket;
    BufferedReader br;
    BufferedWriter bw;
    public ThreadReceviedHandler(Socket s){
        this.socket = s;
        try {
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void run(){
        try {
        while(true){

                String command = br.readLine();
                switch(command) {
                    case "new online user": {
                        String username = br.readLine();
                        System.out.println(username + " is online");
                        break;
                    }
                    case "receive message": {
                        String username = br.readLine();
                        String content = br.readLine();
                        System.out.println("Received from " + username + ":" + content);
                        break;
                    }
                    case "username not online": {
                        String otherUser = br.readLine();
                        System.out.println("");
                        System.out.println(otherUser + " is offline now");
                        break;
                    }
                    case "username not existed": {
                        String otherUser = br.readLine();
                        System.out.println("");
                        System.out.println(otherUser + " is not existed");
                        break;
                    }
                }
        }
        }catch (Exception e){
            System.out.println(e.getMessage());

        }
    }
}
