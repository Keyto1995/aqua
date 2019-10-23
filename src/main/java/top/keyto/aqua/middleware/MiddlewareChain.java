package top.keyto.aqua.middleware;

import lombok.extern.slf4j.Slf4j;
import top.keyto.aqua.core.Context;

/**
 * @author Keyto
 * Created on 2019/6/30
 */
@Slf4j
public class MiddlewareChain {
    private Middleware[] chain;
    private int point = 0;

    public MiddlewareChain(Middleware[] chain) {
        this.chain = chain;
        log.info("chain length: {}", this.chain.length);
    }

    public void poi(Context context) {
        this.next(context);
    }

    public void next(Context context) {
        if (this.point < chain.length) {
            chain[this.point++].callback(context, this);
        }
    }
}
