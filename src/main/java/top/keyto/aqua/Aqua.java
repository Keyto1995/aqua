package top.keyto.aqua;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Keyto
 * Created on 2019/6/26
 */
@Slf4j
public class Aqua {

    public static void main(String[] args) {
        Aqua aqua = new Aqua();
        aqua.listen(8080);
    }

    public void listen(int port) {
        log.debug("port:{}", port);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("启动服务器....");

            Socket clientSocket = serverSocket.accept();
            System.out.println("客户端:" + clientSocket.getInetAddress().getHostAddress() + "已连接到服务器");

            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // 读取客户端发送来的消息
            String mess = br.readLine();
            System.out.println("客户端：" + mess);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            bw.write(mess + "\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
