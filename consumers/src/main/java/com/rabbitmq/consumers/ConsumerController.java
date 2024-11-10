package com.rabbitmq.consumers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

	@GetMapping
	public ResponseEntity<String> consumeMessages(@RequestBody MessageReq messageReq) {
		System.out.println("Cosuming the message "+messageReq.getMessage());
		return new ResponseEntity<>("Success",HttpStatus.OK);
	}
}
