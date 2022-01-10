package server;/*..
 * server
 * Created by HuynhBaHuy
 * Date 1/10/2022 6:48 PM
 * Description:...
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    List<User> listUser;
    public UserController() {
        listUser = new ArrayList<User>();
        try {
            BufferedReader readFile = new BufferedReader(new FileReader("data.txt"));
            while(true){
                String account = readFile.readLine();
                if(account==null){
                    break;
                }
                String[]client = account.split(" ");
                listUser.add(new User(client[0],client[1]));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public Boolean isExistUsername(String username){
        for(User user:listUser){
            if(user.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }
    public Boolean validate(String username, String password){
        for(User user:listUser){
            if(user.getUsername().equals(username)&&user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }
}
