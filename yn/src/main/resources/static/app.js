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
window.onload = connect();