
var socket = new WebSocket("ws://localhost:8080/evo/socket");
socket.onmessage = onMessage;

function onMessage(event) {
    var gameStatus = JSON.parse(event.data);

    printMoves(gameStatus);
}

function printMoves(gameStatus) {
    var content = document.getElementById("content");
    content.innerHTML="";

    var deviceDiv = document.createElement("div");
        content.appendChild(deviceDiv);

        var players = document.createElement("span");
        players.setAttribute("class", "names");
        players.innerHTML = "<b>Players: </b> "+gameStatus.players+"<br>";
        deviceDiv.appendChild(players);

         var moves = document.createElement("span");
            moves.innerHTML = "<b>Moves: </b> " + gameStatus.moves;
            deviceDiv.appendChild(moves);
}

function formSubmit() {

    var form = document.getElementById("MakeMove");
    var move = form.elements["move"].value;
    document.getElementById("MakeMove").reset();
    socket.send(move);
}
