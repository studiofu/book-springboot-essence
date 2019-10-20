package com.bee.sample.ch1.controller;

import com.bee.sample.ch1.annotation.Function;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 访问:http://127.0.0.1:8080/sayhello.html?name=springboot
 * @author xiandafu
 *
 */
@Controller
public class HelloworldController {
	
	
	@RequestMapping("/hello")
	@Function()
	public @ResponseBody String say(String name){
		
		//sayHello();

		return "hello "+name;
	}


	@RequestMapping("/bye")
	@ResponseBody
	public String bye() {
		System.out.println("say bye");

		return "bye";
	}
}
