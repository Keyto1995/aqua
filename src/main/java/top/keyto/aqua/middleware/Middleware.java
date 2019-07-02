package top.keyto.aqua.middleware;

import top.keyto.aqua.core.Context;

/**
 * @author Keyto
 * Created on 2019/6/29
 */
public abstract class Middleware {
    public void init() {

    }

    public abstract void callback(Context ctx, MiddlewareChain chain);

    public void destroy() {

    }
}
