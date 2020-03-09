package no.ntnu.sjakkarena.config;

import no.ntnu.sjakkarena.utils.UserStorage;
import no.ntnu.sjakkarena.data.User;
import no.ntnu.sjakkarena.utils.JWSHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// Adapted code from https://github.com/spring-projects/spring-framework/blob/master/src/docs/asciidoc/web/websocket.adoc#token-authentication
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("jwt");
                    Authentication authentication = JWSHelper.getAuthentication(token);
                    accessor.setUser(authentication);
                    User user = (User) authentication.getPrincipal();
                    UserStorage.addUser(user.getId(), user);
                }
                return message;
            }
        });
    }


}
