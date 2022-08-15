package com01.qqClient.Service;

import com01.QQcommon.Message;
import com01.QQcommon.MessageType;
import com01.QQcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

//登陆验证和用户注册  仅仅是服务端传给客户端 判断是否是正确的信息
// 服务端的判断还没写呢
public class UserClientService {
    private User user = new User();
    private Socket socket;

    public boolean checkUser(String userId, String userPwd) {
        boolean leap = false;
        //创建user对象 向服务器 传输 user 对象 信息
        user.setUseId(userId);
        user.setPwd(userPwd);
        try {
            socket = new Socket(InetAddress.getByName("192.168.1.152"), 9999);
            //传输user对象
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(user);
            //从服务端获取message对象
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            //向下转型
            Message ms = (Message) objectInputStream.readObject();

            if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {//登陆成功

                //连接成功后 用线程连接服务器和客户端
                ClientServiceThread clientServiceThread = new ClientServiceThread(socket);
                clientServiceThread.start();//当只有一个端口跟服务器连接时
                // 起线程  一个客户端的多个socket  这个线程都放在ManageClientServerThread 里
                ManageClientServerThread.addClientServiceThread(userId, clientServiceThread);//添加到集合
                leap = true;

            } else {//登录失败
                //关闭socket
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return leap;
    }
    //向服务器端请求在线用户列表  发出对象
    public void  onlineFriendList(){
        Message message = new Message();
        message.setSender(user.getUseId()+" 用户需求 ");
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);//告诉服务器我想要在线用户列表 就是Message对象的内容
        //要发给服务器 当前线程的(服务器也有可能有多个线程) 对应的 socket对象
        try {
            //为了处理客户端 多线程问题:多个socket端口
            //从线程里得到一个socket 和他的输出流
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(ManageClientServerThread.getClientServiceThread
                            (user.getUseId()).getSocket().getOutputStream());

            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //通过向服务端发送退出信息 实现退出系统
    public void loadOut(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        //体会面向对象
        message.setSender(user.getUseId());//一定要指明 我是哪个客户端
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());

        /*    //处理一个客户端多个socket
            ObjectOutputStream objectOutputStream1 =
                    new ObjectOutputStream(ManageClientServerThread.getClientServiceThread
                            (user.getUseId()).getSocket().getOutputStream());
            */
            objectOutputStream.writeObject(message);
            System.out.println(user.getUseId()+" 用户退出通信系统.");
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
