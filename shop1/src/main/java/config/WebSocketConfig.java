package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import websocket.EchoHandler;

@Configuration  //환경설정
@EnableWebSocket //웹소켓의 기능 활성화
public class WebSocketConfig implements WebSocketConfigurer{
	@Autowired
	EchoHandler handler;
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(handler,"/chatting") // ws://서버주소/chatting 요청시 EchoHandler 활용
//		.setAllowedOriginPatterns("http://localhost:8080", "http://127.0.0.1:8080");
		.setAllowedOrigins("*") //외부 접속 허용. 모든 도메인 허용. => 보안에 취약함
		.addInterceptors(new HttpSessionHandshakeInterceptor());
	}
}
