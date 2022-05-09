package com.ws.netty.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Copyright: Copyright (c) 兆日科技股份有限公司
 * @Date 2022/3/13 9:52
 * @Des
 */
public class BioService {

    public static void main(String[] args) throws Exception {

        ExecutorService threadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(8888); //监听8888端口
        System.out.println("服务启动了");

        while (true){
//          没有连接时,会一直阻塞
            System.out.println("connect......wait");
            Socket client = serverSocket.accept();
//          连接成功后,当前连接会执行下面的代码,然后while会一直等待下一个连接
            System.out.println("connect.......success");

            threadPool.execute(
                    /*new Runnable() {
                        @Override
                        public void run() {
                            BioService.handler(socket);
                        }
                    }*/
//                    使用lamda表达式的方式创建runable
                    () -> BioService.handler(client)
            );
        }
    }


    public static void handler(Socket client) {
        //使用try-with-resource 模式关闭io,注意try后面的括号
        try(InputStream in = client.getInputStream()){
            Thread currentTh = Thread.currentThread();
//           打印日志,获取连接的信息
            System.out.println(MessageFormat.format("1111线程id:{0},线程名称:{1}",currentTh.getId(),currentTh.getName()));
            byte[] bytes = new byte[1024];
            while (true){
//               打印日志,获取连接的信息
                System.out.println(MessageFormat.format("22线程id:{0},线程名称:{1}",currentTh.getId(),currentTh.getName()));
//               连接上了,但是一直没有数据发送过来,也会一直阻塞
                System.out.println("read......wait");
                int read = in.read(bytes);
                if (read != -1){
//                   指定输出字符串的字符编码,不然会有中文乱码的问题
                    System.out.println(new String(bytes,0,read,"GBK"));
                }else {
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
