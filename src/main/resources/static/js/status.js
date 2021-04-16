let app = new Vue({
  el: "#status",
  data: {
    querry: undefined,
    loading: false,
    responseHost: {}
  }
});

window.onload = function reload() {
  get("/api/v1/server/host").then((responseHost) => {
    app.responseHost = responseHost;
    get("https://api.mcsrvstat.us/2/" + responseHost.ip + ":" + responseHost.port).then(response => {
      console.log(response);
      app.querry = response;
    });
  });
}
