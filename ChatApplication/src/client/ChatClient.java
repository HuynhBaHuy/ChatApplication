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

public class ChatClient {
    public static void main(String[] args) {
        new ChatClient();
    }
    public ChatClient(){
        try{
            Socket socket = new Socket("localhost",3200);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connect to server successful "+ 3200);
            System.out.println("Login");
            BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Username: ");
            String username = read.readLine();
            System.out.print("Password: ");
            String password = read.readLine();
            bw.write("login");
            bw.write(username);
            bw.write(password);
            bw.flush();
            String command = br.readLine();
            if(command == "login success"){
                System.out.println("Login successful");
                new ThreadReceviedHandler(socket).start();
                while (true){
                    System.out.println("Enter user to send message: ");
                    String otherUser = read.readLine();
                    if(otherUser == "quit"){
                        break;
                    }
                    while(true){
                        System.out.println("Enter user to send message: ");
                        String content = read.readLine();
                        if(content!="quit"){
                            bw.write("send message");
                            bw.write(otherUser);
                            bw.write(content);
                            bw.flush();
                        }
                        else{
                            break;
                        }
                    }
                }

            }
            else if(command == "login failed") {
                System.out.println("Login failed, Try again");
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
        while(true){
            try {
                String command = br.readLine();
                switch(command){
                    case "new user online":
                        String username = br.readLine();
                        System.out.println(username + " is online");
                    case "receive message":
                        String user = br.readLine();
                        String content = br.readLine();
                        System.out.println("Received from " +user+ ":"+content);
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
