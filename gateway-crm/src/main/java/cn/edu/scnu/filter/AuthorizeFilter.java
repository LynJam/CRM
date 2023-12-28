package cn.edu.scnu.filter;

import cn.edu.scnu.util.RespRes;
import cn.edu.scnu.util.TokenUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String uri = request.getURI()
            .getPath();

        // options 放行
        if ("OPTIONS".equals(request.getMethod())) {
            return chain.filter(exchange);
        }

        // 检查白名单（配置）
        log.info("authorizeFilter request uri: {}", uri);
        if (uri.startsWith("/account")) {
            return chain.filter(exchange);
        }

        // TODO 仅方便测试使用
        String test = request.getHeaders()
            .getFirst("x-test-token");
        if("000000".equals(test)) {
            return chain.filter(exchange);
        }

        // 校验 token
        String token = request.getHeaders()
            .getFirst("Authorization");
        String userId = TokenUtil.getUserIdByToken(token);
        if (userId == null) {
            // token 异常
            response.getHeaders()
                .add("Content-Type", "application/json;charset=UTF-8");
            RespRes<String> res = RespRes.error(20000, "token 异常");
            DataBuffer dataBuffer = response.bufferFactory()
                .wrap(JSON.toJSONString(res)
                    .getBytes());
            return response.writeWith(Flux.just(dataBuffer));
        }

        ServerHttpRequest mutableReq = request.mutate()
            .header("userId", userId)
            .build();
        ServerWebExchange mutableExchange = exchange.mutate()
            .request(mutableReq)
            .build();
        return chain.filter(mutableExchange);
    }

    @Override
    public int getOrder() {
        return -10;
    }
}
