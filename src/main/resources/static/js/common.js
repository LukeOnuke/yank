async function get(requestPath) {
  let resp = await fetch(requestPath, {
    method: 'GET',
    credentials: 'same-origin'
  });
  return resp.json();
}
