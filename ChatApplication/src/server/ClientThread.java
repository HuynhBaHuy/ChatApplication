package server;/*..
 * server
 * Created by HuynhBaHuy
 * Date 1/10/2022 6:12 PM
 * Description:...
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

public class ClientThread extends Thread{
    User user;
    Socket socket;
    BufferedReader br;
    BufferedWriter bw;
    public ClientThread(Socket socket){
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
            if(cThread.getUser().getUsername()==username){
                return cThread;
            }

        }
        return null;
    }

    public void run(){
        try {
            while(true){
                System.out.println("Child Thread start");
                String command = br.readLine();
                System.out.println(command);
                switch(command){
                    case "login":

                         String username = br.readLine();
                         String password = br.readLine();
                         User user = ChatServer.userController.login(username,password);
                         if(user != null){
                             bw.write("login success");
                             bw.flush();
                             this.user = new User(username,password);
                             // send to other users
                             for(ClientThread cThread: ChatServer.clientThreadList){
                                 cThread.bw.write("new online user");
                                 cThread.bw.write(username);
                                 cThread.bw.flush();
                             }
                         }
                         else{
                             bw.write("login failed");
                             bw.flush();
                         }
                         break;
                    case "send message":
                        String anotherUser = br.readLine();
                        String content = br.readLine();

                        //send to another User
                        ClientThread anotherUserThread = ClientThread.findClient(ChatServer.clientThreadList,anotherUser);
                        anotherUserThread.bw.write("receive message");
                        anotherUserThread.bw.write(this.user.getUsername());
                        anotherUserThread.bw.write(content);
                        anotherUserThread.bw.flush();
                        break;
                    default:
                        System.out.println("default");
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
}
