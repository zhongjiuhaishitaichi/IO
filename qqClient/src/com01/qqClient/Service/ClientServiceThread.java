package com01.qqClient.Service;

import com01.QQcommon.Message;
import com01.QQcommon.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

//把每一个客户端的socket做成线程 多线程就能实现不同类型的传输
public class ClientServiceThread extends Thread {
    private Socket socket;
    public ClientServiceThread(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        while(true){
            System.out.println("客户端等待读取服务器输出的信息");
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送Message对象  线程会阻塞在这
                Message  message = (Message) objectInputStream.readObject();//得到message对象

                //如果客户端接受到的message对象的类型是 RET  说明是服务端发来的 获取在线好友请求的信息
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    String[] onlineFriend = message.getContent().split(" ");
                    System.out.println("===========当前在线用户列表=============");
                    for (int i = 0; i < onlineFriend.length; i++) {
                        System.out.println("用户: "+onlineFriend[i]);
                    }
                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){//私聊
                        //把服务器转发的信息展示到控制台
                    System.out.println("\n"+message.getSender()+" 对 "+message.getGetter()+ "说 "+message.getContent()+
                            "\t  时间: " +message.getSendTime() );
                }else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)){//群发
                    System.out.println("\n"+message.getSender()+" 对你说 "+message.getContent()+"\t  时间: "
                            +message.getSendTime());
                }else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {//如果是文件消息
                    System.out.println("\n" + message.getSender() + " 给 " + message.getGetter()
                            + " 发文件: " + message.getSrcFilePath() + " 到我的电脑的目录 " + message.getDestFilePath());

                    //取出message的文件字节数组，通过文件输出流写出到磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDestFilePath(), true);
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\n 保存文件成功~");

                } else{//其他类型的信息?

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
