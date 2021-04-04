/*(function(angular) {
var yank = angular.module('yank', []);
var see = yank.controller('consoleController', function($scope) {
  // the last received msg
  $scope.console = {};

  // handles the callback from the received event
  var handlecallback = function(msg) {
    $scope.$apply(function() {
        $scope.console += "<p>" + json.parse(msg.message) + "</p>"
    });
  }

  var source = new eventsource('/stats');
  source.addeventlistener('message', handlecallback, false);
  return true;
});
})(window.angular);*/
var app = new Vue({
  el: "#app",
  data: {
    consoleLogs: [],
    command: "",
    msg: "hello"
  }
})

function setupConsoleEvent() {
  const consoleAddEvent = new EventSource("/api/v1/log/subscription", {
    withCredentials: true
  });
  consoleAddEvent.onmessage = function(event) {
    var object = JSON.parse(event.data);
    object.timestamp = format_time(object.timestamp);
    app.consoleLogs.push(object);
    console.log("Server sent console event " + event.data);
  }
  consoleAddEvent.onerror = function (err) {

  }
}

function send() {
  // POST request using fetch with error handling
  const requestOptions = {
    method: 'POST',
    credentials: 'include',
    headers: {
      'Content-Type': 'text/plain;charset=UTF-8'
      // 'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: app.command
  };
  fetch('/api/v1/server/send', requestOptions)
    .then(async response => {
      const data = await response.json();

      // check for error response
      if (!response.ok) {
        // get error message from body or default to response status
        const error = (data && data.message) || response.status;
        return Promise.reject(error);
      }

      app.command = "";
    })
    .catch(error => {
      this.errorMessage = error;
      console.error('Post error', error);
    });
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

function format_time(s) {
  return new Intl.DateTimeFormat().format(new Date(s));
}

window.onload = function() {
  setupConsoleEvent();
}
