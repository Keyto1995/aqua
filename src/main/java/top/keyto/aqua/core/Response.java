package top.keyto.aqua.core;

import lombok.Data;

import java.util.Map;

/**
 * @author Keyto
 * Created on 2019/6/29
 */
@Data
public class Response {
    private Map<String, String> head;
    private Object body;
}
