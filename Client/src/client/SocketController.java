package client;/*..
 * client
 * Created by HuynhBaHuy
 * Date 1/10/2022 5:11 PM
 * Description:...
 */

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SocketController {
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    public Boolean sendRegisterToServer(String username, String password){
        try {
            bw.write("sign up");
            bw.newLine();
            bw.write(username);
            bw.newLine();
            bw.write(password);
            bw.newLine();
            bw.flush();
            String command = br.readLine();
            if(command.equals("sign up success")){

                return true;

            }
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
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
    public  BufferedReader getReader(){
        return br;
    }
    public void sendFilesToServer(File file,String otherUsername){
        try {
            OutputStream os = socket.getOutputStream();
                bw.write("send file");
                bw.newLine();
                bw.write(otherUsername);
                bw.newLine();
                bw.write(file.getName());
                bw.newLine();
                bw.write(""+file.length());
                bw.newLine();
                bw.flush();
                FileInputStream fis = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int numberByteRead;
                while ((numberByteRead = fis.read(bytes)) > 0) {
                    os.write(bytes, 0, numberByteRead);
                }
                fis.close();
                os.flush();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    public void sendDownloadFile(String username,String fileName,String path){
        try {
            bw.write("download file");
            bw.newLine();
            bw.write(username);
            bw.newLine();
            bw.write(fileName);
            bw.newLine();
            bw.write(path);
            bw.newLine();
            bw.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public InputStream getInputStream(){
        try{
            return socket.getInputStream();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void sendMessageToServer(String content, String otherUser){
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
    }
    public void sendLogoutToServer(){
        try {
            bw.write("logout");
            bw.newLine();
            bw.flush();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public boolean isConnectedToServer() {
         if(socket==null){
             return true;
         }
         return false;
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

