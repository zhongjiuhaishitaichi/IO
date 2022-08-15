package com01.qqClient.Service;

import com01.QQcommon.Message;
import com01.QQcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class ManageClientService {
        public void sendMessagePrivate(String content,String senderId,String getterId){
            Message message = new Message();
            message.setSender(senderId);
            message.setMesType(MessageType.MESSAGE_COMM_MES);//发送的消息类型 !!! 体会
            message.setGetter(getterId);
            message.setContent(content);
            message.setSendTime(new Date().toString());
            System.out.println(senderId+" 对 "+getterId+" 说"+content);
            try {

                //面向对象
                ObjectOutputStream objectOutputStream =
                        new ObjectOutputStream(ManageClientServerThread.getClientServiceThread
                                (senderId).getSocket().getOutputStream());

                objectOutputStream.writeObject(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        public void sendMessagePublic(String content,String senderId){
            Message message = new Message();
            message.setSender(senderId);
            message.setMesType(MessageType.MESSAGE_TO_ALL_MES);//发送的消息类型 !!! 体会
            message.setContent(content);
            message.setSendTime(new Date().toString());
            System.out.println(senderId+" 对大家说 "+content);
            try {

                //面向对象
                ObjectOutputStream objectOutputStream =
                        new ObjectOutputStream(ManageClientServerThread.getClientServiceThread
                                (senderId).getSocket().getOutputStream());
                objectOutputStream.writeObject(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}
