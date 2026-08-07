package com.example.demo.controller;

import java.util.List;
import java.util.Scanner;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Question;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Controller
public class HomeController {
	private final ChatClient chatClient;

	public HomeController(ChatClient.Builder chatClient) {
		this.chatClient = chatClient.build();
	}
	
	@GetMapping
	public String show(ModelMap model, HttpSession session) {

	String s1 = """

	Output ONLY a valid raw JSON array.
	Do not include any conversational text,
	Markdown formatting, intro, or outro.

	Generate 5 random Java True/False declarative statements.

	CRITICAL: Do NOT generate questions starting with "What", "Why", "How",
	"Which", or "Is". Every item must be a fact-based statement.

	Each JSON object must have exactly these three keys:

	1. "qno" (integer: sequential number 1 to 5)
	2. "question" (the statement string)
	3. "answer" (boolean: true/false)

	Strict format example:
	[
	  {
	    "qno": 1,
	    "question": "An interface in Java can have private methods since Java 9.",
	    "answer": true
	  }
	] """;
		String s2 = chatClient.prompt(s1).call().content();
		
		
		ObjectMapper mapper = new ObjectMapper();
		List<Question> list = mapper.readValue(s2, new TypeReference<List<Question>>() {} );
		
		session.setAttribute("list", list);
		session.setAttribute("count", 0);
		
		model.addAttribute("current_question", list.get(0));
		
		return "question";
		
	}
	@GetMapping("/next")
	public String nextQuestion(ModelMap model,HttpSession session) {
		List<Question> list = (List<Question>)session.getAttribute("list");
		int count = (int) session.getAttribute("count");
		session.setAttribute("count",++count);
		
		model.addAttribute("current_question",list.get(count));
		
		return "question";
		
	}
	
	
}
