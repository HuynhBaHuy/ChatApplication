package client;/*..
 * client
 * Created by HuynhBaHuy
 * Date 1/10/2022 5:11 PM
 * Description:...
 */

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketController {
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;

    public Boolean sendLoginToServer(String username, String password){
        try {
            bw.write("login");
            bw.newLine();
            bw.write(username);
            bw.newLine();
            bw.write(password);
            bw.newLine();
            bw.flush();
            String command = br.readLine();
            if(command.equals("login success")){

                return true;

            }
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public ArrayList<String> loadListOnlineUsers(){
        ArrayList<String> listOnlineUsers = new ArrayList();
        try {
            bw.write("load list online user");
            bw.newLine();
            bw.flush();
            String command = br.readLine();
            if(command.equals("list online user")){
                String num = br.readLine();
                int numberOfOnlineUsers = Integer.parseInt(num);
                for(int i=0;i<numberOfOnlineUsers;i++){
                    String onlineUser = br.readLine();
                    listOnlineUsers.add(onlineUser);
                }
            }else{

            }
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return listOnlineUsers;
    }
    public BufferedWriter getSender(){
        return bw;
    }
    public  BufferedReader getReader(){
        return br;
    }
    public Boolean sendMessageToServer(String content, String otherUser){
        try {
            bw.write("send message");
            bw.newLine();
            bw.write(otherUser);
            bw.newLine();
            bw.write(content);
            bw.newLine();
            bw.flush();
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    public SocketController(){
        try{
            socket = new Socket("localhost",3200);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}

