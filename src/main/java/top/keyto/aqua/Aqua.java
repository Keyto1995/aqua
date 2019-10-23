package top.keyto.aqua;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import top.keyto.aqua.core.Context;
import top.keyto.aqua.executor.TraceThreadPoolExecutor;
import top.keyto.aqua.middleware.Middleware;
import top.keyto.aqua.middleware.MiddlewareChain;
import top.keyto.aqua.middleware.MiddlewareChainFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;


/**
 * @author Keyto
 * Created on 2019/6/26
 */
@Slf4j
public class Aqua {
    private List<Middleware> middlewares = new ArrayList<>();

    public static void main(String[] args) {
        Aqua aqua = new Aqua();
        aqua.use(new Middleware() {
            @Override
            public void callback(Context ctx, MiddlewareChain chain) {
                log.info("第一个中间件前");
                chain.next(ctx);
                log.info("第一个中间件后");
            }
        }).use(new Middleware() {
            @Override
            public void callback(Context ctx, MiddlewareChain chain) {
                log.info("第二个中间件前");
                chain.next(ctx);
                log.info("第二个中间件后");
            }
        }).use(new Middleware() {
            @Override
            public void callback(Context ctx, MiddlewareChain chain) {
                log.info("第三个中间件前");
                ctx.getResponse().setBody("Hello World");
                chain.next(ctx);
                log.info("第三个中间件后");
            }
        });
        aqua.listen(8080);
    }

    /**
     * @param port 监听端口号
     */
    public void listen(int port) {
        MiddlewareChainFactory middlewareChainFactory = new MiddlewareChainFactory(this.middlewares);
        middlewareChainFactory.initChain();

        this.middlewares = null;

        try (ServerSocket server = new ServerSocket(port)) {
            log.info("服务器开始监听端口: {}", server.getLocalPort());
            ExecutorService executor = new TraceThreadPoolExecutor(0, Integer.MAX_VALUE, 0L, TimeUnit.SECONDS, new SynchronousQueue<>());
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    Socket connection = server.accept();
                    log.debug("客户端: {} 已连接到服务器", connection.getInetAddress().getHostAddress());
                    // 处理 HTTP 协议
                    executor.execute(new HttpHandler(connection, middlewareChainFactory.getMiddlewareChain()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            log.error("服务器启动失败", e);
        } finally {
            middlewareChainFactory.destroyChain();
        }
    }

    public Aqua use(@NonNull Middleware middleware) {
        this.middlewares.add(middleware);
        return this;
    }
}
