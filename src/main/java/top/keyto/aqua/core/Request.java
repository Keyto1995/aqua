package top.keyto.aqua.core;

import lombok.Data;

import java.util.Map;

/**
 * @author Keyto
 * Created on 2019/6/29
 */
@Data
public class Request {
    private RequestMethod method;
    private String uri;
    private String version;
    private Map<String, String> params;
    private Object body;
}
