package client.data;/*..
 * client.data
 * Created by HuynhBaHuy
 * Date 1/13/2022 9:54 AM
 * Description:...
 */

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Boxchat {
    String username;
    JTextPane boxchat;
    DateTimeFormatter dtf;
    ArrayList<FileAttach> fileAttachments = new ArrayList<>();
    public Boxchat(String username){
        this.username = username;
        boxchat = new JTextPane();
        boxchat.setEditable(false);
        dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    }
    public JTextPane getBoxchat(){
        return boxchat;
    }
    public ArrayList<FileAttach> getListFileAttachments(){
        return fileAttachments;
    }
    public FileAttach getFileAttachment(String fileName){
        for(FileAttach fileAttachment : fileAttachments){
            if(fileAttachment.getFileName().equals(fileName)){
                return fileAttachment;
            }
        }
        return null;
    }
    public void receiveMessage(String message){
        StyledDocument doc = boxchat.getStyledDocument();
        SimpleAttributeSet leftAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(leftAttr, Color.BLUE);
        StyleConstants.setAlignment(leftAttr, StyleConstants.ALIGN_LEFT);
        StyleConstants.setFontSize(leftAttr,12);
        StyleConstants.setLeftIndent(leftAttr,5);
        LocalDateTime now = LocalDateTime.now();
        String head = username+" "+ dtf.format(now);
        SimpleAttributeSet headAttr = new SimpleAttributeSet(leftAttr);
        StyleConstants.setFontSize(headAttr,10);
        StyleConstants.setItalic(headAttr,true);
        try {
            int offset = doc.getLength();
            int length = head.length();
            doc.insertString(offset,head+"\n",null);
            doc.setParagraphAttributes(offset,length,headAttr,false);
            offset = doc.getLength();
            length = message.length()+1;
            doc.insertString(doc.getLength(),message+"\n",null);
            doc.setParagraphAttributes(offset,length,leftAttr,false);
        } catch (BadLocationException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void sendMessage(String message,String sender){
        StyledDocument doc = boxchat.getStyledDocument();
        SimpleAttributeSet rightAttr = new SimpleAttributeSet();
        StyleConstants.setAlignment(rightAttr, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setFontSize(rightAttr,12);
        StyleConstants.setRightIndent(rightAttr,5);

        LocalDateTime now = LocalDateTime.now();
        String head = sender+" "+dtf.format(now);
        SimpleAttributeSet headAttr = new SimpleAttributeSet(rightAttr);
        StyleConstants.setFontSize(headAttr,10);
        StyleConstants.setItalic(headAttr,true);
        try {

            int offset = doc.getLength();
            int length = head.length();
            doc.insertString(offset,head+"\n",null);
            doc.setParagraphAttributes(offset,length,headAttr,false);
            offset = doc.getLength();
            length = message.length()+1;
            doc.insertString(offset,message+"\n",null);
            doc.setParagraphAttributes(offset,length,rightAttr,false);
        } catch (BadLocationException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void sendFile(File file,String sender){
        StyledDocument doc = boxchat.getStyledDocument();
        SimpleAttributeSet rightAttr = new SimpleAttributeSet();
        StyleConstants.setAlignment(rightAttr, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setFontSize(rightAttr,12);
        StyleConstants.setRightIndent(rightAttr,5);

        LocalDateTime now = LocalDateTime.now();
        String head = sender+" "+dtf.format(now);
        SimpleAttributeSet headAttr = new SimpleAttributeSet(rightAttr);
        StyleConstants.setFontSize(headAttr,10);
        StyleConstants.setItalic(headAttr,true);
        String content = "Attach "+file.getName();
        try {

            int offset = doc.getLength();
            int length = head.length();
            doc.insertString(offset,head+"\n",null);
            doc.setParagraphAttributes(offset,length,headAttr,false);
            offset = doc.getLength();
            length = content.length()+1;
            doc.insertString(offset,content+"\n",null);
            doc.setParagraphAttributes(offset,length,rightAttr,false);
        } catch (BadLocationException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void receiveFile(String fileName,int fileSize){
        fileAttachments.add(new FileAttach(fileName,fileSize));
        StyledDocument doc = boxchat.getStyledDocument();
        SimpleAttributeSet leftAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(leftAttr, Color.BLUE);
        StyleConstants.setAlignment(leftAttr, StyleConstants.ALIGN_LEFT);
        StyleConstants.setFontSize(leftAttr,12);
        StyleConstants.setLeftIndent(leftAttr,5);
        LocalDateTime now = LocalDateTime.now();
        String head = username+" "+ dtf.format(now);
        SimpleAttributeSet headAttr = new SimpleAttributeSet(leftAttr);
        StyleConstants.setFontSize(headAttr,10);
        StyleConstants.setItalic(headAttr,true);
        StyleConstants.setUnderline(leftAttr,true);
        String content = "Attach "+fileName;
        try {
            int offset = doc.getLength();
            int length = head.length();
            doc.insertString(offset,head+"\n",null);
            doc.setParagraphAttributes(offset,length,headAttr,false);
            offset = doc.getLength();
            length = content.length()+1;
            doc.insertString(doc.getLength(),content+"\n",null);
            doc.setParagraphAttributes(offset,length,leftAttr,false);
        } catch (BadLocationException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Adding "+fileName+" with size "+fileSize+" to list attachment");
    }
}
