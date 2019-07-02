package top.keyto.aqua.core;

import lombok.Data;

/**
 * @author Keyto
 * Created on 2019/6/29
 */
@Data
public class Context {
    private Request request;
    private Response response;
}
