package com.iit.oop_coursework.Real_Time_Event_Ticketing_System.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * WebSocket configuration class that sets up WebSocket handlers and manages WebSocket sessions.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfiguration.class);

    // List to store WebSocket sessions for ticket pool
    private final List<WebSocketSession> ticketPoolSessions = new CopyOnWriteArrayList<>();
    // List to store WebSocket sessions for system status
    private final List<WebSocketSession> systemStatusSessions = new CopyOnWriteArrayList<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Registers WebSocket handlers for ticket pool and system status endpoints.
     *
     * @param registry the WebSocketHandlerRegistry to register handlers
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new TicketPoolWebSocketHandler(), "/ws/ticketpool")
                .setAllowedOrigins("*");
        registry.addHandler(new SystemStatusWebSocketHandler(), "/ws/systemstatus")
                .setAllowedOrigins("*");
    }

    /**
     * Broadcasts the current number of tickets to all connected ticket pool WebSocket sessions.
     *
     * @param noOfTickets the number of tickets to broadcast
     */
    public void broadcastingCurrentNoOfTickets(int noOfTickets) {
        broadcast(noOfTickets, ticketPoolSessions);
    }

    /**
     * Broadcasts the system status to all connected system status WebSocket sessions.
     *
     * @param isRunning the system status to broadcast
     */
    public void broadcastSystemStatus(boolean isRunning) {
        broadcast(isRunning, systemStatusSessions);
    }

    /**
     * Broadcasts a message to all connected WebSocket sessions in the provided list.
     *
     * @param message  the message to broadcast
     * @param sessions the list of WebSocket sessions to broadcast to
     */
    public void broadcast(Object message, List<WebSocketSession> sessions) {
        sessions.removeIf(session -> !session.isOpen());

        for (WebSocketSession session : sessions) {
            synchronized (session) {
                try {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
                } catch (IOException e) {
                    logger.error("Error broadcasting message", e);
                }
            }
        }
    }

    /**
     * WebSocket handler for managing ticket pool WebSocket connections.
     */
    private class TicketPoolWebSocketHandler extends TextWebSocketHandler {
        /**
         * Called after a new WebSocket connection is established.
         *
         * @param session the WebSocket session that was established
         * @throws IOException if an I/O error occurs
         */
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws IOException {
            ticketPoolSessions.add(session);
            logger.info("New WebSocket Connection: " + session.getId());
        }

        /**
         * Called after a WebSocket connection is closed.
         *
         * @param session the WebSocket session that was closed
         * @param status  the status of the WebSocket connection closure
         * @throws Exception if an error occurs
         */
        @Override
        public void afterConnectionClosed(
                WebSocketSession session, org.springframework.web.socket.CloseStatus status
        ) throws Exception {
            ticketPoolSessions.remove(session);
            logger.info("WebSocket connection closed: " + session.getId());
        }
    }

    /**
     * WebSocket handler for managing system status WebSocket connections.
     */
    private class SystemStatusWebSocketHandler extends TextWebSocketHandler {
        /**
         * Called after a new WebSocket connection is established.
         *
         * @param session the WebSocket session that was established
         * @throws IOException if an I/O error occurs
         */
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws IOException {
            systemStatusSessions.add(session);
            logger.info("New WebSocket Connection: " + session.getId());
        }

        /**
         * Called after a WebSocket connection is closed.
         *
         * @param session the WebSocket session that was closed
         * @param status  the status of the WebSocket connection closure
         * @throws Exception if an error occurs
         */
        @Override
        public void afterConnectionClosed(
                WebSocketSession session, org.springframework.web.socket.CloseStatus status
        ) throws Exception {
            systemStatusSessions.remove(session);
            logger.info("WebSocket connection closed: " + session.getId());
        }
    }
}

