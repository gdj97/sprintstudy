package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("chat")
public class ChatController {
	@RequestMapping("*")
	public String getView() {
		return null;
	}
	/*
	 * Chatbot 구현하기
	 * 1. chatgpt를 활용한 챗봇 구현하기 : https://openai.com/ko-KR/
	 */
}
