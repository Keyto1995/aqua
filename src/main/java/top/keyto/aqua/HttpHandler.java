package top.keyto.aqua;

import lombok.extern.slf4j.Slf4j;
import top.keyto.aqua.core.Context;
import top.keyto.aqua.core.Request;
import top.keyto.aqua.core.RequestMethod;
import top.keyto.aqua.core.Response;
import top.keyto.aqua.middleware.MiddlewareChain;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read = socket.getInputStream().read(buffer);
            String message = new String(buffer);
            log.info("length: {} buffer: \n{}", read, message);
            String ResponseHead = "HTTP/1.0 200 \r\n" +
                "Name: Keyto\r\n" +
                "\r\n";
            parse(context, message);

            middlewareChain.poi(context);
            log.debug("服务端：{}", context.getResponse().getBody());
            Writer bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(ResponseHead);
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

    /**
     * 解析HTTP请求报文
     *
     * @param context 上下文
     * @param message HTTP请求报文
     */
    private void parse(Context context, String message) {
        int indexOf = message.indexOf("\r\n\r\n");


        String[] requestHeaders = message.substring(0, indexOf).split("\r\n");
        // 处理请求行
        String[] requestLine = requestHeaders[0].split(" ");
        Request contextRequest = context.getRequest();
        contextRequest.setMethod(RequestMethod.valueOf(requestLine[0]));
        contextRequest.setUri(requestLine[1]);
        contextRequest.setVersion(requestLine[2]);
        // 处理请求头
        Map<String, String> params = contextRequest.getParams();
        if (null == params) {
            contextRequest.setParams(new HashMap<>());
            params = contextRequest.getParams();
        }
        for (int i = 1; i < requestHeaders.length; i++) {
            int indexOf1 = requestHeaders[i].indexOf(':');
            String key = requestHeaders[i].substring(0, indexOf1);
            String value = requestHeaders[i].substring(indexOf1 + 1).trim();
            params.put(key, value);
        }
        // 处理请求体
        String contentLength = contextRequest.getParams().get("Content-Length");
        if (contentLength != null) {
            int length = Integer.parseInt(contentLength);
            contextRequest.setBody(message.substring(indexOf + 4, indexOf + 4 + length));
        } else {
            contextRequest.setBody(message.substring(indexOf + 4));
        }

        log.info("contextRequest: {}", contextRequest);
    }
}
