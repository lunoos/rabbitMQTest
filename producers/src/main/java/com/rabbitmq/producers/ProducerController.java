package com.rabbitmq.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
public class ProducerController {
	
	@Autowired
	private ProducerService producerService;

	@GetMapping
	public ResponseEntity<String> produceMessages(@RequestParam int noOfMessaages) {
		System.out.println("Producing message count "+noOfMessaages);
		while(noOfMessaages>0) {
			producerService.sendMessage(new MessageReq("message "+noOfMessaages));
			noOfMessaages--;
		}
		return new ResponseEntity<>("Messages sent!",HttpStatus.OK);
	}
}
