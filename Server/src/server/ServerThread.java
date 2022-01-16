package server;/*..
 * server
 * Created by HuynhBaHuy
 * Date 1/16/2022 6:06 PM
 * Description:...
 */

import javax.swing.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    JTextArea log = ChatServer.log;
    ServerSocket ss;
    public ServerThread(ServerSocket ss) {
        this.ss = ss;
    }
    public void run() {
        // start server
        try{

            do{
                log.append("Waiting for client... \n");
                try {
                    Socket socket = ss.accept();
                    String ip=(((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
                    ClientThread clientThread = new ClientThread(socket,log);
                    ChatServer.clientThreadList.add(clientThread);
                    log.append("Accept client "+ip+" \n");
                    clientThread.start();
                }catch(Exception e){
                    log.append(e.getMessage()+" \n");
                }
            }while(true);
        }catch(Exception e){
            log.append(e.getMessage()+" \n");;
        }
    }
}
