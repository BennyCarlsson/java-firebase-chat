var stompClient = null;

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/topic/posts', function (post) {
                    var obj = JSON.parse(post.body)
                    for(var key in obj){
                        showGreeting("Name: "+obj[key].name + " Content: "+obj[key].content);
                    }
        });
    });
}
function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}
function sendDB() {
    stompClient.send("/app/saveDB", {}, JSON.stringify({'name': $("#dbName").val(), 'content': $("#dbContent").val()}));
}
function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#send" ).click(function() { sendName(); });
    $( "#saveDB" ).click(function() { sendDB(); });
});
window.onload = connect();