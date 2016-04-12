package com.why.game.web.controller;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.why.game.http.HttpServiceCaller;
import com.why.game.http.User;
import com.why.game.protobuf.TestProtobuf.TestProto;


@Controller
@RequestMapping("/")
public class TestController {

	@RequestMapping(value="/json")
	@ResponseBody
	public User json(@RequestBody User u){
		System.out.println(u);
		return u;
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
