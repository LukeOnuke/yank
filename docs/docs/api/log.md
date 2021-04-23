# Log endpoint (/api/v1/log/**)

Log objects look like this
```json
{"id":255, "message": "Example", "timestamp": 152223}
```

## Get by id (/api/v1/log/id/{id})
`GET` `/api/v1/log/id/255`

Will return a log with id 255
```json
{"id":255, "message": "Example", "timestamp": 152223}
```

## Get by timestamp (/api/v1/log/timestamp/{timestamp})
`GET` `/api/v1/log/timestamp/152223`

Get log entries using the timestamp.
Will return a list of logs with the timestamp 152223.
```json
[{"id":255, "message": "Example 1", "timestamp": 152223},
  {"id":256, "message": "Example 2", "timestamp": 152223},
  {"id":257, "message": "Example 3", "timestamp": 152223}]
```

## Get latest (/api/v1/log/latest/{pageSize})
Gets the {pageSize} amount of the latest logs, page size can't be more than 20.

`GET` `/api/v1/log/latest/3`

Will return a list `3` of the latest logs.
```json
[{"id":255, "message": "Example 1", "timestamp": 152223},
  {"id":256, "message": "Example 2", "timestamp": 152223},
  {"id":257, "message": "Example 3", "timestamp": 152223}]
```

## Subscribe (/api/v1/log/subscription)
Will subscribe you to an event stream of logs. It sends an event when a log
is sent to the console.