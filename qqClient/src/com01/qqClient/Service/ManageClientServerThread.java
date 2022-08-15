package com01.qqClient.Service;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

//管理线程类
public class ManageClientServerThread {
    //把多个线程放入HashMap集合 key-用户名,value-线程
    //ConcurrentHashMap 用于处理并发的集合  线程同步 线程安全
    // 所以这玩意比HashMap安全
    private static ConcurrentHashMap<String, ClientServiceThread> hashMap = new ConcurrentHashMap<>();
    //线程加入集合
    //因为是key value  封装成方法
    public static void addClientServiceThread(String userId, ClientServiceThread clientServiceThread) {
            hashMap.put(userId,clientServiceThread);
    }
    public static ClientServiceThread getClientServiceThread(String userId){
        return hashMap.get(userId);
    }
}
