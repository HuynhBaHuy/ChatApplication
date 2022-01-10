package server;/*..
 * server
 * Created by HuynhBaHuy
 * Date 1/10/2022 6:12 PM
 * Description:...
 */

public class User {
    String username;
    String password;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}
