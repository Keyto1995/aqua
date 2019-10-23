package top.keyto.aqua.core;

import lombok.Data;

import java.util.Map;

/**
 * @author Keyto
 * Created on 2019/6/29
 */
@Data
public class Context {
    private Request request;
    private Response response;
    private Map<String, String> cookies;
}
