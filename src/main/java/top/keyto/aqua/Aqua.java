package top.keyto.aqua;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("服务器开始监听端口: {}", port);
            //noinspection InfiniteLoopStatement
            while (true) {
                try (Socket connection = serverSocket.accept()) {
                    log.debug("客户端: {} 已连接到服务器", connection.getInetAddress().getHostAddress());
                    // 读取客户端发送来的消息
                    int size = 4096;
                    byte[] buffer = new byte[size];
                    int read = connection.getInputStream().read(buffer);
                    log.info("length: {} buffer: \n{}", read, new String(buffer));
                    String mess = "<h1>hello</h1>";
                    log.debug("客户端：{}", mess);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    bw.write(mess + "\r\n");
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
