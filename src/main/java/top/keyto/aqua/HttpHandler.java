package top.keyto.aqua;

import lombok.extern.slf4j.Slf4j;

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

    public HttpHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read = socket.getInputStream().read(buffer);
            log.info("length: {} buffer: \n{}", read, new String(buffer));
            String mess = "<h1>hello</h1>";
            log.debug("客户端：{}", mess);
            Writer bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(mess + "\r\n");
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
