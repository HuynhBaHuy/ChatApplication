package server;/*..
 * server
 * Created by HuynhBaHuy
 * Date 1/10/2022 6:12 PM
 * Description:...
 */

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread{
    User user; // null when open client but not login => fix when code swing
    Socket socket;
    BufferedReader br;
    BufferedWriter bw;
    public ClientThread(Socket socket){
        user=null;
        this.socket = socket;
        try{
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public User getUser() {
        return user;
    }
    static ClientThread findClient(List<ClientThread> list, String username){
        for(ClientThread cThread: list){
            User user =cThread.getUser();
            if(user!=null){
                if(user.getUsername().equals(username)){
                    return cThread;
                }
            }
        }
        return null;
    }

    public void run(){
        try {
            while(true){
                String command = br.readLine();
                switch(command){
                    case "login": {
                        String username = br.readLine();
                        String password = br.readLine();
                        Boolean isValidateSuccess = ChatServer.userController.validate(username, password);
                        if (isValidateSuccess) {
                            System.out.println(username+" login successful");
                            bw.write("login success");
                            bw.newLine();
                            bw.flush();
                            this.user = new User(username, password);
                            // send to other users
                            for (ClientThread cThread : ChatServer.clientThreadList) {
                                if (cThread.equals(this)||cThread.user.getUsername().equals(username)) {
                                    continue;
                                }
                                cThread.bw.write("new online user");
                                cThread.bw.newLine();
                                cThread.bw.write(username);
                                cThread.bw.newLine();
                                cThread.bw.flush();
                                System.out.println("show "+username+" is online to "+cThread.user.getUsername());
                            }
                        } else {
                            // login failed
                            System.out.println(username+" login failed.");
                            bw.write("login failed");
                            bw.newLine();
                            bw.flush();
                        }
                        break;
                    }
                    case "load list online user": {
                        System.out.println(user.getUsername()+" is loading list online user");
                        bw.write("list online user");
                        bw.newLine();
                        List<User> onlineUsers = new ArrayList<>();
                        for (ClientThread cThread : ChatServer.clientThreadList) {
                            if (cThread.equals(this)) {
                                continue;
                            }
                            if(cThread.getUser()!=null){
                                onlineUsers.add(cThread.getUser());
                            }
                        }
                        bw.write(String.valueOf(onlineUsers.size()));
                        bw.newLine();
                        for(User user:onlineUsers){
                            bw.write(user.getUsername());
                            bw.newLine();
                        }
                        bw.flush();
                        System.out.println(user.getUsername()+" done loading");
                        break;
                    }
                    case "send message": {
                        System.out.println(user.getUsername()+" is sending message...");
                        String otherUsername = br.readLine();
                        String content = br.readLine();

                        //send to another User
                        Boolean isExist = ChatServer.userController.isExistUsername(otherUsername);
                        if (isExist&&!otherUsername.equals(user.getUsername())){
                            ClientThread anotherUserThread = ClientThread.findClient(ChatServer.clientThreadList, otherUsername);
                            if (anotherUserThread == null) {
                                System.out.println("Notify "+user.getUsername()+" that "+otherUsername+"is not online");
                                bw.write("username not online");
                                bw.newLine();
                                bw.write(otherUsername);
                                bw.newLine();
                                bw.flush();
                            } else {
                                System.out.println(user.getUsername()+" sends message success");
                                anotherUserThread.bw.write("receive message");
                                anotherUserThread.bw.newLine();
                                anotherUserThread.bw.write(this.user.getUsername());
                                anotherUserThread.bw.newLine();
                                anotherUserThread.bw.write(content);
                                anotherUserThread.bw.newLine();
                                anotherUserThread.bw.flush();
                            }
                        } else {
                            bw.write("username not existed");

                            bw.write(otherUsername);
                            bw.newLine();
                            bw.flush();
                        }
                        break;
                    }
                    case "sign up":{

                        String username = br.readLine();
                        String password = br.readLine();
                        Boolean success = ChatServer.userController.signUp(username,password);
                        if(success){
                            System.out.println(username+" sign up successful");
                            bw.write("sign up success");
                            bw.newLine();
                            bw.flush();
                        }
                        else{
                            System.out.println(username+" sign up failed");
                            bw.write("sign up failed");
                            bw.newLine();
                            bw.flush();
                        }
                        break;
                    }
                    case "logout" :{
                        System.out.println(user.getUsername()+" logout");
                        ChatServer.clientThreadList.remove(this);
                        for (ClientThread cThread : ChatServer.clientThreadList) {
                            // send disconnect to other user
                            cThread.bw.write("someone logout");
                            cThread.bw.newLine();
                            cThread.bw.write(this.user.getUsername());
                            cThread.bw.newLine();
                            cThread.bw.flush();
                        }
                    }
                    default:
                        System.out.println("Unknown package");
                }
            }
        }catch(Exception exception){
            try {
                if (socket.getInetAddress().isReachable(1000)) {
                    br.close();
                    bw.close();
                    ChatServer.clientThreadList.remove(this);
                    System.out.println(user.getUsername()+" is disconnected");
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }

    }
}
