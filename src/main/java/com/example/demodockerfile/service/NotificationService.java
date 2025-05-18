package com.example.demodockerfile.service;

import com.example.demodockerfile.entity.Categoria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sentNotificationSocket(Categoria categoria) {
        log.info("Enviando notificacion por socket: {}", categoria);
        messagingTemplate.convertAndSend("/topic/registrations", categoria);
    }
}
