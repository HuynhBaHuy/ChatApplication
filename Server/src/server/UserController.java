package server;/*..
 * server
 * Created by HuynhBaHuy
 * Date 1/10/2022 6:48 PM
 * Description:...
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    List<User> listUser;
    public UserController() {
        listUser = new ArrayList<>();
        File folder = new File(SaveFileData.folderDB);
        if(!folder.exists()){
            folder.mkdirs();
        }
        SaveFileData.fileAccount = SaveFileData.folderDB+"\\"+SaveFileData.fileAccount;
        File file = new File(SaveFileData.fileAccount);
        try {
            file.createNewFile();
            BufferedReader readFile = new BufferedReader(new FileReader(SaveFileData.fileAccount));
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
    public Boolean signUp(String username,String password){
        for(User user:listUser){
            if(user.getUsername().equals(username)){
                return false;
            }
        }
        User newUser = new User(username,password);
        listUser.add(newUser);
        try {
            PrintWriter writer = new PrintWriter(SaveFileData.fileAccount);
            synchronized(writer){
                for(User user : listUser){
                    writer.println(user.getUsername()+" "+user.getPassword());
                }
            }
            writer.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }

        return true;
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
