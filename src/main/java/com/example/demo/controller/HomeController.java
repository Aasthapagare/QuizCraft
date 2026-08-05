package com.example.demo.controller;

import java.util.List;
import java.util.Scanner;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Question;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Controller
public class HomeController {
	private final ChatClient chatClient;

	public HomeController(ChatClient.Builder chatClient) {
		this.chatClient = chatClient.build();
	}
	@ResponseBody
	@GetMapping
	public String show() {

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
		
		Scanner sc = new Scanner(System.in);
		for(Question q : list) {
			System.out.println(q.getQuestion());
			System.out.println("-----------------------------------");
			System.out.println("Enter answer true/false=> ");
			boolean b = sc.nextBoolean();
			if(b==q.isAnswer()) {
				System.out.println("correct");
			}
			else {
				System.out.println("incorrect");
			}
		}
		
		return s2;
		
	}
	
	
}
