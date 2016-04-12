package com.why.game.web.controller;

import java.util.Map;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.why.game.http.HttpServiceCaller;
import com.why.game.http.JSONUtils;
import com.why.game.protobuf.TestProtobuf.TestProto;


@Controller
@RequestMapping("/")
public class TestController {

	@RequestMapping(value="/json")
	@ResponseBody
	public Map<String, String> test2(@RequestParam String requestJson){
		return JSONUtils.fromJSONToMap(requestJson, String.class);
	}
	
	@RequestMapping(value="/proto")
	@ResponseBody
	public ResponseEntity<TestProto> test(RequestEntity<TestProto> requestProto){
		TestProto testProto = requestProto.getBody();
		String s = new String(testProto.toByteArray());
		System.out.println(s);
		System.out.println(testProto);
		HttpServiceCaller.printProtoStr(s);
		return ResponseEntity.ok(testProto);
	}
	
}
