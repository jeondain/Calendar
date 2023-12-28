package sku.sw.util;

import java.io.IOException;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

public class SlackMessenger {

	public static void send(String message) throws IOException, SlackApiException {
	    String token = "xoxb-6357845576179-6358002034946-vcSAYfr0h2VfYhV0QkVSW3cy";
	
	    Slack slack = Slack.getInstance();
	    // Initialize an API Methods client with the given token
	    MethodsClient methods = slack.methods(token);
	
	    // Build a request object
	    ChatPostMessageRequest request = ChatPostMessageRequest.builder()
	      .channel("C06AHQWFDJ7") // Use a channel ID `C1234567` is preferable
	      .text(message)
	      .build();
	
	    // Get a response as a Java object
	    ChatPostMessageResponse response = methods.chatPostMessage(request);
	}
}
