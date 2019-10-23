package top.keyto.aqua.middleware;

import java.util.Arrays;
import java.util.List;

/**
 * @author Keyto
 * Created on 2019/10/23
 */
public class MiddlewareChainFactory {

    private Middleware[] chain;

    public MiddlewareChainFactory(List<Middleware> middlewareList) {
        if (middlewareList.isEmpty()) {
            this.chain = new Middleware[0];
        } else {
            int size = middlewareList.size();
            this.chain = new Middleware[size];
            middlewareList.toArray(this.chain);
        }
    }

    /**
     * 初始化中间件
     */
    public void initChain() {
        Arrays.stream(this.chain).forEach(Middleware::init);
    }

    /**
     * 销毁中间件
     */
    public void destroyChain() {
        Arrays.stream(this.chain).forEach(Middleware::destroy);
    }


    public MiddlewareChain getMiddlewareChain() {
        return new MiddlewareChain(this.chain);
    }
}
