var stompClient = null;

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/greetingList', function (greetings) {
                    var obj = JSON.parse(greetings.body)
                    clearChatList();
                    for(var key in obj){
                        showGreeting("Name: "+obj[key].name + " Content: "+obj[key].content);
                    }
        });
        startFirebaseListener();
    });
}
function startFirebaseListener(){
    stompClient.send("/app/firebaselistener")
};
function sendDB() {
    stompClient.send("/app/saveDB", {}, JSON.stringify({'name': $("#dbName").val(), 'content': $("#dbContent").val()}));
}
function clearChatList(){
    $("#greetings").empty();
}
function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#saveDB" ).click(function() { sendDB(); });
});
function firebaseGmailLogin(){
// Initialize Firebase
  // TODO: Replace with your project's customized code snippet
  var config = {
    apiKey: "AIzaSyCZKQQT1l3tF7IW3JycMsYIYpSc2-b_u98",
    authDomain: "yesno-2dbf9.firebaseapp.com",
    databaseURL: "https://yesno-2dbf9.firebaseio.com",
    storageBucket: "yesno-2dbf9.appspot.com",
  };
  firebase.initializeApp(config);
    var provider = new firebase.auth.GoogleAuthProvider();

    firebase.auth().signInWithPopup(provider).then(function(result) {
      // This gives you a Google Access Token. You can use it to access the Google API.
      var token = result.credential.accessToken;
      // The signed-in user info.
      var user = result.user;
      connect();
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
window.onload = firebaseGmailLogin();