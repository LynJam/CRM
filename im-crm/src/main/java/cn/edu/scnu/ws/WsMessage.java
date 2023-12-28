package cn.edu.scnu.ws;

import lombok.Builder;
import lombok.Data;

@Data
public class WsMessage<T> {
    String type;
    T data;
}
