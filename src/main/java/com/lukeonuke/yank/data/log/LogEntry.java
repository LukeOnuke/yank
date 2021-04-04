package com.lukeonuke.yank.data.log;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class LogEntry {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @Column(length = 2048)
    private String message;
    private Long timestamp;

    protected LogEntry(){}

    public LogEntry(String message, Long timestamp){
        this.message = message;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("LogEntry[id=%s, message=%s, timestamp=%d]", id, message, timestamp);
    }
}
