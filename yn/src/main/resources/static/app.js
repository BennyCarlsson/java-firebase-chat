var stompClient = null;

function connect() {
    //tells stomp.js to shut up in the console.
    stompClient.debug = null

    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/greetingList', function (greetings) {
                    var obj = JSON.parse(greetings.body)
                    clearChatList();
                    for(var key in obj){
                        if(obj[key].currentUsersGreeting){
                            showYourGreeting(obj[key]);
                        }else{
                            showGreeting(obj[key]);
                        }
                    }
        });
        startFirebaseListener();
    });
}
var idToken;
function startFirebaseListener(){
    firebase.auth().currentUser.getIdToken(/* forceRefresh */ true).then(function(idToken) {
      stompClient.send("/app/firebaselistener",{},JSON.stringify({'idToken':idToken}));
      this.idToken = idToken;
    }).catch(function(error) {
      // Handle error
    });
};
function sendDB() {
    stompClient.send("/app/saveDB", {}, JSON.stringify({'name': ""
        ,'content': $("#dbContent").val()
        ,'idToken': this.idToken}));
}
function clearChatList(){
    $("#greetings").empty();
}
function showGreeting(obj) {
    $("#greetings").append("<tr><td>" + obj.name + ": " + " Content: "+obj.content + "</td></tr>");
}
function showYourGreeting(obj) {
    $("#greetings").append("<tr class='myChatMessage'><td>" + "You: " + obj.content + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#saveDB" ).click(function() { sendDB(); });
    $( "#logoutButton" ).click(function() { logout(); });
});
function logout(){
    firebase.auth().signOut();
}
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
        connect();
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
window.onload = firebaseGmailLogin();