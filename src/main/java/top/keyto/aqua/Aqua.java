package top.keyto.aqua;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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

    /**
     * @param port 监听端口号
     */
    public void listen(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            log.info("服务器开始监听端口: {}", port);
            ExecutorService executor = Executors.newFixedThreadPool(50);
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    Socket connection = server.accept();
                    log.debug("客户端: {} 已连接到服务器", connection.getInetAddress().getHostAddress());
                    // 处理 HTTP 协议
                    executor.submit(new HttpHandler(connection));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            log.error("服务器启动失败", e);
        }
    }
}
