package com01.qqClient.Service;

import com01.QQcommon.Message;
import com01.QQcommon.MessageType;

import java.io.*;

//传输文件
public class FileClientService {
    public void sendFile(String src, String dest, String SenderId, String getterId) {
        //读取src文件 --> message对象
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(SenderId);
        message.setGetter(getterId);
        message.setSrcFilePath(src);
        message.setDestFilePath(dest);

        //从电脑读入的客户端
        FileInputStream fileInputStream = null;
        byte[] bytes = new byte[(int) new File(src).length()];
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(bytes);//将源文件读入到字节数组
            message.setFileBytes(bytes);//现在bytes里就有了 src文件 再作为message对象得属性
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("\n"+SenderId+" 给 "+getterId+"发送文件: "+src+" 到对方电脑目录 "+dest);
        //发送
        try {
            ObjectOutputStream objectOutputStream =
                   new ObjectOutputStream
                           (ManageClientServerThread.getClientServiceThread(SenderId).getSocket().getOutputStream());
            //转发
         objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
