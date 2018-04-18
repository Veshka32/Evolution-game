
var socket = new WebSocket("ws://localhost:8080/socket");
socket.onmessage = onMessage;

function onMessage(event) {
    var message = event.data;
    printMoves(message);
}

function printMoves(message) {
    var content = document.getElementById("content");
    content.innerHTML=message;
}

function formSubmit() {

    var form = document.getElementById("MakeMove");
    var move = form.elements["move"].value;
    document.getElementById("MakeMove").reset();
    socket.send(move);
}

