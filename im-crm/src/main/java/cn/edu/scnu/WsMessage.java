package cn.edu.scnu;

import lombok.Builder;
import lombok.Data;

@Data
public class WsMessage<T> {
    String type;
    T data;
}
