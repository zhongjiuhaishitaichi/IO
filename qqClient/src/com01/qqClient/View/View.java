package com01.qqClient.View;

import com01.qqClient.Service.FileClientService;
import com01.qqClient.Service.ManageClientService;
import com01.qqClient.Service.UserClientService;
import com01.qqClient.Utils.ClientUtils;

@SuppressWarnings({"all"})
public class View {

    private boolean loop = true;//控制是否显示界面
    private String key = "";
    private UserClientService userClientService = new UserClientService();//用户登陆 注册

    private ManageClientService manageClientService = new ManageClientService();//用户群发和私聊
    private FileClientService fileClientService = new FileClientService();

    public static void main(String[] args) {
        new View().MainMenu();
    }

    private void MainMenu() {
        while (loop) {
            System.out.println("=============欢迎登陆网络通信系统=================");
            System.out.println("\t\t 1 登陆系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("输入你的选择: ");
            key = ClientUtils.readString(1);
            switch (key) {
                case "1":
                    System.out.println("请输入用户号: ");
                    String userID = ClientUtils.readString(50);
                    System.out.println("请输入密 码: ");
                    String userPwd = ClientUtils.readString(50);
                    //这里需要到服务端测试用户信息是否合法
                    if (userClientService.checkUser(userID, userPwd)) {
                        System.out.println("===========欢迎 (用户 " + userID + " 登录成功) ===========");
                        //进入到二级菜单
                        while (loop) {
                            System.out.println("\n=========网络通信系统二级菜单(用户 " + userID + " )=======");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("请输入你的选择: ");
                            key = ClientUtils.readString(1);
                            switch (key) {
                                case "1":
                                    //写一个方法，来获取在线用户列表
                                    userClientService.onlineFriendList();
                                    break;
                                case "2"://群发
                                    System.out.println("请输入想对大家说的话: ");
                                    String s = ClientUtils.readString(100);
                                    manageClientService.sendMessagePublic(s, userID);
                                    break;
                                case "3"://私聊
                                    System.out.print("请输入想聊天的用户号(在线): ");
                                    String getterId = ClientUtils.readString(50);
                                    System.out.print("请输入想说的话: ");
                                    String content = ClientUtils.readString(100);
                                    //编写一个方法，将消息发送给服务器端
                                    manageClientService.sendMessagePrivate(content, userID, getterId);
                                    break;
                                case "4"://发文件
                                    System.out.print("请输入你想把文件发送给的用户(在线用户): ");
                                    getterId = ClientUtils.readString(50);
                                    System.out.print("请输入发送文件的路径(形式 d:\\xx.jpg)");
                                    String src = ClientUtils.readString(100);
                                    System.out.print("请输入把文件发送到对应的路径(形式 d:\\yy.jpg)");
                                    String dest = ClientUtils.readString(100);
                                    fileClientService.sendFile(src, dest, userID, getterId);
                                    break;
                                case "9":
                                    //调用方法，给服务器发送一个退出系统的message
                                    loop = false;
                                    userClientService.loadOut();
                                    break;
                            }
                        }
                    } else { //登录服务器失败
                        System.out.println("=========登录失败=========");
                    }

                    break;
                case "9":
                    System.out.println("退出系统");
                    loop = false;
                    break;
            }
        }
    }
}

