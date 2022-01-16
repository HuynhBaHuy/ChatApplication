package server;/*..
 * server
 * Created by HuynhBaHuy
 * Date 1/10/2022 6:12 PM
 * Description:...
 */

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread{
    User user; // null when open client but not login => fix when code swing
    Socket socket;
    BufferedReader br;
    BufferedWriter bw;
    JTextArea log;
    public ClientThread(Socket socket,JTextArea log){
        this.log = log;
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
                        Boolean isLoginAlready = false;
                        for (ClientThread cThread : ChatServer.clientThreadList) {
                            if(cThread!=this&&cThread.user!=null) {
                                if (cThread.user.getUsername().equals(username)) {
                                    isLoginAlready = true;
                                    break;
                                }
                            }
                        }
                        if (isValidateSuccess&&!isLoginAlready) {
                            log.append(username+" login successful \n");
                            bw.write("login success");
                            bw.newLine();
                            bw.flush();
                            this.user = new User(username, password);
                            // send to other users
                            for (ClientThread cThread : ChatServer.clientThreadList) {
                                if (cThread.equals(this)) {
                                    continue;
                                }
                                cThread.bw.write("new online user");
                                cThread.bw.newLine();
                                cThread.bw.write(username);
                                cThread.bw.newLine();
                                cThread.bw.flush();
                            }
                        } else {
                            ChatServer.clientThreadList.remove(this);
                            // login failed
                            log.append(username+" login failed. \n");
                            bw.write("login failed");
                            bw.newLine();
                            bw.flush();
                        }
                        break;
                    }
                    case "load list online user": {
                        log.append(user.getUsername()+" is loading list online user \n");
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
                        log.append(user.getUsername()+" done loading \n");
                        break;
                    }
                    case "send message": {
                        log.append(user.getUsername()+" is sending message... \n");
                        String otherUsername = br.readLine();
                        String content = br.readLine();

                        //send to another User
                        Boolean isExist = ChatServer.userController.isExistUsername(otherUsername);
                        if (isExist&&!otherUsername.equals(user.getUsername())){
                            ClientThread anotherUserThread = ClientThread.findClient(ChatServer.clientThreadList, otherUsername);
                            if (anotherUserThread == null) {
                                log.append("Notify "+user.getUsername()+" that "+otherUsername+"is not online \n");
                                bw.write("username not online");
                                bw.newLine();
                                bw.write(otherUsername);
                                bw.newLine();
                                bw.flush();
                            } else {
                                log.append(user.getUsername()+" sends message success \n");
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
                            log.append(username+" sign up successful \n");
                            bw.write("sign up success");
                            bw.newLine();
                            bw.flush();
                        }
                        else{
                            ChatServer.clientThreadList.remove(this);
                            log.append(username+" sign up failed \n");
                            bw.write("sign up failed");
                            bw.newLine();
                            bw.flush();
                        }
                        break;
                    }
                    case "logout" :{
                        log.append(user.getUsername()+" logout\n");
                        ChatServer.clientThreadList.remove(this);
                        for (ClientThread cThread : ChatServer.clientThreadList) {
                            // send disconnect to other user
                            cThread.bw.write("someone logout");
                            cThread.bw.newLine();
                            cThread.bw.write(this.user.getUsername());
                            cThread.bw.newLine();
                            cThread.bw.flush();
                        }
                        break;
                    }
                    case "download file":{
                        try {
                            String username = br.readLine();
                            String fileName = br.readLine();
                            String path = br.readLine();
                            log.append("Download "+fileName+" from "+username+"\n");
                            bw.write("receive download file");
                            bw.newLine();
                            bw.write(path);
                            bw.newLine();
                            bw.write(fileName);
                            bw.newLine();
                            bw.flush();
                            File saveFile = new File(SaveFileData.folderDB+"/"+username+"/"+fileName);
                            FileInputStream fis = new FileInputStream(saveFile);
                            OutputStream os = socket.getOutputStream();
                            byte[] bytes = new byte[1024];
                            int numberBytesRead;
                            while ((numberBytesRead = fis.read(bytes))>0){
                                os.write(bytes, 0, numberBytesRead);
                            }
                            fis.close();
                            os.flush();
                        }catch (Exception e) {
                            log.append("Exception: "+e.getMessage()+"\n");
                        }
                        break;
                    }
                    case "send file":{
                        try{
                            String otherUsername = br.readLine();
                            String fileName = br.readLine();
                            File directory = new File(SaveFileData.folderDB+"\\"+otherUsername);
                            if(!directory.exists()){
                                directory.mkdir();
                            }
                            File saveFile = new File(SaveFileData.folderDB+"\\"+otherUsername+"/"+fileName);
                            int fileSize = Integer.parseInt(br.readLine());
                            log.append("Send " + fileName+" with "+fileSize+" bytes \n");
                            System.out.println("Size: " + fileSize + "Byte");
                            InputStream is = socket.getInputStream();
                            FileOutputStream fos = new FileOutputStream(saveFile);
                            byte[] bytes = new byte[fileSize];
                            int totalBytes = 0;
                            int numberBytesRead;
                            while((numberBytesRead = is.read(bytes))>0){
                                fos.write(bytes, 0, numberBytesRead);
                                totalBytes += numberBytesRead;
                                if(totalBytes>=fileSize){
                                    break;
                                }
                            }
                            fos.close();
                            // send buffer to other clients
                            Boolean isExist = ChatServer.userController.isExistUsername(otherUsername);
                            if (isExist&&!otherUsername.equals(user.getUsername())){
                                ClientThread anotherUserThread = ClientThread.findClient(ChatServer.clientThreadList, otherUsername);
                                if (anotherUserThread == null) {
                                    log.append("Notify "+user.getUsername()+" that "+otherUsername+"is not online \n");
                                    bw.write("username not online");
                                    bw.newLine();
                                    bw.write(otherUsername);
                                    bw.newLine();
                                    bw.flush();
                                } else {
                                    log.append("Notify "+user.getUsername()+" sends files success \n");
                                    anotherUserThread.bw.write("receive file");
                                    anotherUserThread.bw.newLine();
                                    anotherUserThread.bw.write(this.user.getUsername());
                                    anotherUserThread.bw.newLine();
                                    anotherUserThread.bw.write(fileName);
                                    anotherUserThread.bw.newLine();
                                    anotherUserThread.bw.write(""+fileSize);
                                    anotherUserThread.bw.newLine();
                                    anotherUserThread.bw.flush();
                                }
                            } else {
                                bw.write("username not existed");

                                bw.write(otherUsername);
                                bw.newLine();
                                bw.flush();
                            }
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                    default:
                        log.append("Unknown package \n");
                }
            }
        }catch(Exception exception){
            try {
                log.append("Exception: "+exception.getMessage()+" \n");
                if (socket.isConnected()) {
                    br.close();
                    bw.close();
                    ChatServer.clientThreadList.remove(this);
                    if(this.user != null){
                        for (ClientThread cThread : ChatServer.clientThreadList) {
                            // send disconnect to other user
                            cThread.bw.write("someone logout");
                            cThread.bw.newLine();
                            cThread.bw.write(this.user.getUsername());
                            cThread.bw.newLine();
                            cThread.bw.flush();
                        }
                        log.append(user.getUsername()+" is disconnected \n");
                    }
                }
            }catch (Exception e){
                log.append(e.getMessage()+" \n");
            }

        }

    }
}
