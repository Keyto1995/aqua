package top.keyto.aqua;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

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
    public void testListen() {
        new Thread(() -> {
            Aqua aqua = new Aqua();
            aqua.listen(PORT);
        }).start();
        try {
            Socket s = new Socket("127.0.0.1", PORT);

            // 构建IO
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
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
