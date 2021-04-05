var status = new Vue({
  el: "#status",
  data: {
    status: {},
  }
})

window.onload = function reload(){
  var resp = get("https://api.mcsrvstat.us/2/play.beocraft.net");
  console.log(resp);
  status.status = JSON.parse(resp);
}

function get(requestPath){
  const requestOptions = {
    method: 'GET',
    credentials: 'same-origin',
  };
  fetch(requestPath, requestOptions)
    .then(async response => {
      const data = await response.json();

      // check for error response
      if (!response.ok) {
        // get error message from body or default to response status
        const error = (data && data.message) || response.status;
        return Promise.reject(error);
      }

      return data.message;
    })
    .catch(error => {
      this.errorMessage = error;
      console.error('Post error', error);
    });
}
