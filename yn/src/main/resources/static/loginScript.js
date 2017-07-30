function firebaseGmailLogin(){
// Initialize Firebase
  var config = {
    apiKey: "AIzaSyCZKQQT1l3tF7IW3JycMsYIYpSc2-b_u98",
    authDomain: "yesno-2dbf9.firebaseapp.com",
    databaseURL: "https://yesno-2dbf9.firebaseio.com",
    storageBucket: "yesno-2dbf9.appspot.com",
  };
  firebase.initializeApp(config);
    var provider = new firebase.auth.GoogleAuthProvider();

    firebase.auth().onAuthStateChanged(function(user) {
      if (user) {
        // User is signed in.
        user.getIdToken(true).then(function(idToken){
            window.location.href=`/in?idToken=${idToken}`;
        });


      } else {
        // No user is signed in.
        firebase.auth().signInWithRedirect(provider).then(function(result) {
                          // This gives you a Google Access Token. You can use it to access the Google API.
                          var token = result.credential.accessToken;
                          // The signed-in user info.
                          var user = result.user;
                          // ...
                        }).catch(function(error) {
                          // Handle Errors here.
                          var errorCode = error.code;
                          var errorMessage = error.message;
                          // The email of the user's account used.
                          var email = error.email;
                          // The firebase.auth.AuthCredential type that was used.
                          var credential = error.credential;
                          // ...
                        });
      }
    });
}
(() => firebaseGmailLogin())();
//window.onload = firebaseGmailLogin();