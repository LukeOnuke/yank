/*
@since 1.0.1a
Use id or class to select img and p elements and fill them with profile info.
https://www.w3schools.com/jsref/met_document_getelementsbyclassname.asp
Ex.

<img class="pw-pi"></img> //Will get profile image.
<p class="pw-fin"></p> //Will get user first name.
<p class="pw-fam"></p> //Will get user family name.
*/

(function() {
  console.log("profilewigets.js included, looking for widgets.");

  let images = document.querySelectorAll(".pw-pi");
  let firstNames = document.querySelectorAll(".pw-fin");
  let lastNames = document.querySelectorAll(".pw-fam");

  console.log("All elements found by profilewigets");
  console.log(JSON.stringify(images));
  console.log(JSON.stringify(firstNames));
  console.log(JSON.stringify(lastNames));
  let i = 0;

  get("/api/v1/user/authorised").then(authorised => {
    if (authorised.authorised) {
      get("/api/v1/user/me").then(resp => {
        images.forEach(function(image) {
          console.log("Image " + JSON.stringify(image));
          image.src = resp.picture;
        });
        firstNames.forEach(function(firstName) {
          console.log("First name paragraph " + JSON.stringify(firstName));
          firstName.innerHTML = resp.first_name;
        });
        lastNames.forEach(function(lastName) {
          console.log("Last name paragraph " + JSON.stringify(lastName));
          lastName.innerHTML = resp.family_name;
        });
      });
    } else {
      images.forEach(function(image) {
        console.log("Image " + JSON.stringify(image));
        image.src = "/img/user.png";
      });
      firstNames.forEach(function(firstName) {
        firstName.innerHTML = "Anonymous";
        console.log("First name paragraph " + JSON.stringify(firstName));
      });
      lastNames.forEach(function(lastName) {
        console.log("Last name paragraph " + JSON.stringify(lastName));
        lastName.innerHTML = "User";
      });
    }
  });
})();
