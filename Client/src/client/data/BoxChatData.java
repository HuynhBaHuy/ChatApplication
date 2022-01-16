package client.data;/*..
 * client.data
 * Created by HuynhBaHuy
 * Date 1/12/2022 9:51 AM
 * Description:...
 */

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BoxChatData {
    public static List<Boxchat> boxChat = new ArrayList<>();
    public Boxchat findBoxChat(String username){
        for(Boxchat box:boxChat){
            if(box.username.equals(username)){
                return box;
            }
        }
        return null;
    }
    public void appendBoxChat(String username){
        for(Boxchat box:boxChat){
            if(box.username.equals(username)){
                return;
            }
        }
        Boxchat newBoxChat = new Boxchat(username);
        boxChat.add(newBoxChat);
    }
    public void removeBoxChat(String username){
        boxChat.removeIf(box -> box.username.equals(username));
        return;
    }
}
