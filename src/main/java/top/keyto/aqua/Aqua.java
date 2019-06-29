package top.keyto.aqua;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


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
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            log.info("服务器开始监听端口: {}", port);
            while (true) {
                Socket connection = null;
                try {
                    connection = serverSocket.accept();
                    log.debug("客户端: {} 已连接到服务器", connection.getInetAddress().getHostAddress());

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    // 读取客户端发送来的消息
                    String mess = br.readLine();
                    log.debug("客户端：{}", mess);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    bw.write(mess + "\r\n");
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
