package com.why.game.web.controller;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.why.game.http.HttpServiceCaller;
import com.why.game.http.User;
import com.why.game.protobuf.TestProtobuf.TestProto;

@Controller
@RequestMapping("/")
public class TestController {

	@RequestMapping(value="/param")
	@ResponseBody
	public User getParam(@RequestParam int id, @RequestParam String name){
		User user = new User(id, name);
		System.out.println(user);
		return user;
	}
	
	@RequestMapping(value="/json")
	@ResponseBody
	public User json(@RequestBody User user){
		System.out.println(user);
		return user;
	}
	
	@RequestMapping(value="/protobuf")
	@ResponseBody
	public ResponseEntity<TestProto> protobuf(RequestEntity<TestProto> requestProto){
		TestProto testProto = requestProto.getBody();
		String s = new String(testProto.toByteArray());
		System.out.println(s);
		System.out.println(testProto);
		HttpServiceCaller.printProtoStr(s);
		return ResponseEntity.ok(testProto);
	}
	
}
