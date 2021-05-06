let app = new Vue({
  el: "#app",
  data: {
    consoleLogs: [],
    command: "",
    deltaT: 0,
    authorised: {},
    connected: true
  },
  methods: {
    handleScroll: function(e) {
      //console.log(e.target.scrollTop);
      if (e.target.scrollTop < 50) {
        let respId = app.consoleLogs[0].id;
        respId--;
        get("/api/v1/log/id/" + respId).then((resp) => {
          console.log("Got response as user scrolled up " + JSON.stringify(resp));
          app.consoleLogs.unshift(resp);
        });
      }
    },
    formatTime: function(timestamp) {
      return formatTime(timestamp);
    },
    scrollConsoleToBottom: function scrollConsoleToBottom(){
      let consoleElement = app.$el.querySelector("#console");
      consoleElement.scrollTop = consoleElement.scrollHeight;
    }
  }
})

function setupConsoleEvent() {
  const consoleAddEvent = new EventSource("/api/v1/log/subscription", {
    withCredentials: true
  });
  consoleAddEvent.onmessage = function(event) {
    var object = JSON.parse(event.data);
    //object.timestamp = formatTime(object.timestamp);
    app.consoleLogs.push(object);
    console.log("Server sent console event " + event.data);
    app.scrollConsoleToBottom();
  }
  consoleAddEvent.onerror = function(err) {
    app.connected = false;
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

function formatTime(s) {
  return new Date(s).toLocaleString();
}

window.onload = function() {
  get("/api/v1/user/authorised").then((resp) => {
    app.authorised = resp;

    setupConsoleEvent();

      console.log("Sending request to fill up the console");
      get("/api/v1/log/latest/20").then((resp) => {
        console.log("Content response : " + JSON.stringify(resp));
        console.log("Content string : " + resp);
        let i = 0;
        for(let respObj of resp){
          app.consoleLogs.unshift(respObj);
        }
        let container = app.$el.querySelector("#console");
        container.scrollTop = container.scrollHeight;
      });
  });
}
