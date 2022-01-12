package client.data;/*..
 * client.data
 * Created by HuynhBaHuy
 * Date 1/12/2022 9:51 AM
 * Description:...
 */
import java.util.ArrayList;
import java.util.List;

public class BoxChatData {
    String username;
    List<String> listOnlineUsers;
    Message message;
    Boolean isNewMessage;
    public BoxChatData(String username){
        this.username = username;
        listOnlineUsers = new ArrayList<>();
        message = new Message();
    }
    public void addNewOnlineUser(String username){
        listOnlineUsers.add(username);
    }
    public void newMessage(String message,String username){
        this.message.username = username;
        this.message.content = message;
        isNewMessage = true;
    }
}
