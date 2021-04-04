package com.lukeonuke.yank.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface SseService {
    boolean add(SseEmitter sseEmitter);

    boolean remove(SseEmitter sseEmitter);

    List<SseEmitter> getSsEmitters();
}
