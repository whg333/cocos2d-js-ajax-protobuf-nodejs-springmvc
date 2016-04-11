package com.why.game.web.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.why.game.http.HttpServiceCaller;
import com.why.game.protobuf.TestProtobuf.TestProto;


@Controller
@RequestMapping("/")
public class TestController {

	@RequestMapping(value="/protobuf")
	@ResponseBody
	public void protobuf(HttpServletResponse response, @RequestParam String userIdStr) throws IOException{
		System.out.println("userIdStr="+userIdStr);
		TestProto testProto = HttpServiceCaller.newTestProto();
		
		String s = new String(testProto.toByteArray());
		System.out.println(s);
		System.out.println(testProto);
		HttpServiceCaller.printProtoStr(s);
		
		OutputStream out = response.getOutputStream();
		//response.setContentType("application/octet-stream");
		out.write(testProto.toByteArray());
		out.close();
	}
	
	@RequestMapping(value="/param")
	@ResponseBody
	public ResponseEntity<TestProto> protobuf2(@RequestParam String userIdStr){
		System.out.println("userIdStr="+userIdStr);
		TestProto testProto = HttpServiceCaller.newTestProto();
		
		String s = new String(testProto.toByteArray());
		System.out.println(s);
		System.out.println(testProto);
		HttpServiceCaller.printProtoStr(s);
		
		return ResponseEntity.ok(testProto);
	}
	
	@RequestMapping(value="/test")
	@ResponseBody
	public ResponseEntity<TestProto> protobuf3(RequestEntity<TestProto> requestEntity){
		TestProto testProto = requestEntity.getBody();
		//TestProto testProto = HttpServiceCaller.newTestProto();
		
		String s = new String(testProto.toByteArray());
		System.out.println(s);
		System.out.println(testProto);
		HttpServiceCaller.printProtoStr(s);
		
		return ResponseEntity.ok(testProto);
	}
	
}
