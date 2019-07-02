package top.keyto.aqua.middleware;

import lombok.extern.slf4j.Slf4j;
import top.keyto.aqua.core.Context;

import java.util.List;

/**
 * @author Keyto
 * Created on 2019/6/30
 */
@Slf4j
public class MiddlewareChain {
    private Middleware[] chain;

    private ThreadLocal<Integer> indexLocal = new ThreadLocal<>();

    public MiddlewareChain(List<Middleware> middlewareList) {
        if (middlewareList.isEmpty()) {
            this.chain = new Middleware[0];
        } else {
            int size = middlewareList.size();
            this.chain = new Middleware[size];
            middlewareList.toArray(this.chain);
        }
        log.info("chain length: {}", this.chain.length);
    }

    public void poi(Context context) {
        if (indexLocal.get() == null) {
            indexLocal.set(0);
        }
        this.next(context);
        indexLocal.set(0);
    }

    public void next(Context context) {
        Integer index = indexLocal.get();
        if (index < chain.length) {
            indexLocal.set(index + 1);
            chain[index].callback(context, this);
        }
    }
}
