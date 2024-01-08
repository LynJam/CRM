package cn.edu.scnu.ws;

import lombok.Data;

@Data
public class WsProtocol<T> {
    Integer type;
    T data;

    public static <T> WsProtocol<T> of(Integer type, T data) {
        WsProtocol<T> tWsProtocol = new WsProtocol<>();
        tWsProtocol.setData(data);
        tWsProtocol.setType(type);
        return tWsProtocol;
    }
}
