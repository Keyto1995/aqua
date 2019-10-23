package top.keyto.aqua;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import top.keyto.aqua.core.Context;
import top.keyto.aqua.middleware.Middleware;
import top.keyto.aqua.middleware.MiddlewareChain;

import java.io.*;
import java.net.Socket;


/**
 * @author Keyto
 * Created on 2019/6/26
 */
@Slf4j
public class AquaTest {
    private static final int PORT = 3001;

    @Test
    public void listen() {
        new Thread(() -> {
            Aqua aqua = new Aqua();
            aqua.use(new Middleware() {
                @Override
                public void callback(Context ctx, MiddlewareChain chain) {
                    log.info("第一个中间件前");
                    ctx.getResponse().setBody(ctx.getRequest().getBody());
                    chain.next(ctx);
                    log.info("第一个中间件后");
                }
            });
            aqua.listen(PORT);
        }).start();
        try {
            Socket s = new Socket("127.0.0.1", PORT);

            // 构建IO
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write("GET  HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Content-Type: text/plain\r\n" +
                "User-Agent: PostmanRuntime/7.18.0\r\n" +
                "Accept: */*\r\n" +
                "Cache-Control: no-cache\r\n" +
                "Postman-Token: 3b38c24a-9237-4d99-a133-3b435a457d78,1c2314e1-d30f-4c96-8182-dc99deeab14b\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
//                "Content-Length: 6\r\n" +
                "Connection: keep-alive\r\n" +
                "cache-control: no-cache\r\n\r\n");
            // 向服务器端发送一条消息
            bw.write("这是由客户端发送的一条消息\r\n");
            bw.flush();

            // 读取服务器返回的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String mess = br.readLine();
            log.debug("服务器：{}", mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
