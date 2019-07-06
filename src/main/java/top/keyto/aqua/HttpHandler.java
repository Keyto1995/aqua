package top.keyto.aqua;

import lombok.extern.slf4j.Slf4j;
import top.keyto.aqua.core.Context;
import top.keyto.aqua.core.Request;
import top.keyto.aqua.core.Response;
import top.keyto.aqua.middleware.MiddlewareChain;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

/**
 * @author Keyto
 * Created on 2019/6/29
 */
@Slf4j
public class HttpHandler implements Runnable {

    private static final int BUFFER_SIZE = 4096;

    private Socket socket;
    private MiddlewareChain middlewareChain;

    public HttpHandler(Socket socket, MiddlewareChain middlewareChain) {
        this.socket = socket;
        this.middlewareChain = middlewareChain;
    }

    @Override
    public void run() {
        Context context = new Context();
        context.setRequest(new Request());
        context.setResponse(new Response());
        middlewareChain.poi(context);
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read = socket.getInputStream().read(buffer);
            log.info("length: {} buffer: \n{}", read, new String(buffer));
            String mess = "HTTP/1.0 200 \r\n" +
                "Name: Keyto\r\n" +
                "\r\n";
            log.debug("客户端：{}", context.getResponse().getBody());
            Writer bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(mess);
            bw.write(context.getResponse().getBody().toString());
            bw.write("\r\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
