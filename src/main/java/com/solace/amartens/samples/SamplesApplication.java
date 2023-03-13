package com.solace.amartens.samples;

import com.solace.spring.cloud.stream.binder.messaging.SolaceHeaders;
import com.solace.spring.cloud.stream.binder.util.SolaceAcknowledgmentException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.StaticMessageHeaderAccessor;
import org.springframework.integration.acks.AckUtils;
import org.springframework.integration.acks.AcknowledgmentCallback;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@SpringBootApplication
public class SamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamplesApplication.class, args);
	}

	@Bean
	public Consumer<Message<String>> trackAndStoreSolaceMessage() {
		return message -> {
			AcknowledgmentCallback acknowledgmentCallback = StaticMessageHeaderAccessor.getAcknowledgmentCallback(message);
			acknowledgmentCallback.noAutoAck();

			System.out.println("Received Message: ");
			System.out.println("\tMessage Id = "+message.getHeaders().get(SolaceHeaders.APPLICATION_MESSAGE_ID));
			System.out.println("\tMessage Type = "+message.getHeaders().get(SolaceHeaders.APPLICATION_MESSAGE_TYPE));
			System.out.println("\tReplication Group Message Id = "+message.getHeaders().get(SolaceHeaders.REPLICATION_GROUP_MESSAGE_ID));
			System.out.println("\tSender Timestamp = "+message.getHeaders().get(SolaceHeaders.SENDER_TIMESTAMP));
			System.out.println("\n");

			//Store this information

			try {
				AckUtils.accept(acknowledgmentCallback);
			} catch (SolaceAcknowledgmentException e) {
				e.printStackTrace();
			}
		};
	}
}
