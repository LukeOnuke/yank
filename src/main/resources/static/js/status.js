var app = new Vue({
  el: "#status",
  data: {
    querry: undefined,
    loading: false
  }
});

window.onload = function reload() {
  get("https://api.mcsrvstat.us/2/play.beocraft.net").then(response => {
    console.log(response);
    app.querry = response;
  });
}

async function get(requestPath) {
  var resp = await fetch(requestPath, {
    method: 'GET',
    credentials: 'same-origin'
  });
  return resp.json();
}
