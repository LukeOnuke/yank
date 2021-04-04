package com.lukeonuke.yank.data.log;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface LogEntryRepository extends PagingAndSortingRepository<LogEntry, Long> {
    LogEntry findById(long id);
    List<LogEntry> findByTimestamp(long timestamp);
}
