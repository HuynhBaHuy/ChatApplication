package server;/*..
 * server
 * Created by HuynhBaHuy
 * Date 1/10/2022 5:14 PM
 * Description:...
 */

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    ServerSocket ss;
    static final UserController userController = new UserController();
    static List<ClientThread> clientThreadList = new ArrayList<>();
    public ChatServer(){
        //load user list

        // start server
        try{
            System.out.println("Server booting ...");
            ss = new ServerSocket(3200);
            do{
                System.out.println("Waiting for client...");
                try {
                    Socket socket = ss.accept();
                    String ip=(((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
                    ClientThread clientThread = new ClientThread(socket);
                    clientThreadList.add(clientThread);
                    System.out.println("Accept client "+ip);
                    clientThread.start();
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }while(true);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) {
        new ChatServer();
    }
}
