package client.data;/*..
 * client.data
 * Created by HuynhBaHuy
 * Date 1/14/2022 10:35 PM
 * Description:...
 */

import java.io.FileOutputStream;

public class FileAttach {
    String fileName;
    int size;
    public FileAttach(String fileName, int size){
        this.fileName = fileName;
        this.size = size;
    }
    public String getFileName(){return fileName;}
    public void saveAt(String location){
        try{
            String path = location+"\\"+fileName;
            FileOutputStream outputStream = new FileOutputStream(path);
            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public int getFileSize(){
        return size;
    }
}
