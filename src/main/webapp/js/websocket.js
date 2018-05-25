window.onload = init;
var socket = new WebSocket("ws://localhost:8080/evo/socket");
socket.onmessage = onMessage;

var playerName;
var move;
var draggedProperty;
var firstAnimalId = null;
var secondAnimalId = null;
var playedCardId;
var status;

function eatFood() {
    if (status) {
        move = "eatFood";
        document.getElementById("doing").innerText = "Feed ";// set firstAnimalId
    }
}

function attack() {
    if (status)
        move = "attack"; //set firstAnimalId and secondAnimalId

}

function endMove() {
    if (status){
        move="endMove";
        document.getElementById("doing").innerText = "end move";
        let json=buildMessage();
        clearFields();
        socket.send(json);
    }
}

function endPhase() {
    if (status) {
        move = "EndPhase";
        document.getElementById("doing").innerText = "end "+document.getElementById("phase");
        let json = buildMessage();
        clearFields(); //clear fields after message is built!
        socket.send(json);
    }
}

function onMessage(event) {
    clearFields();
    var game = JSON.parse(event.data);
    if (game.hasOwnProperty("error")) {
        alert(game.error);
        status = "true";
        return;
    }
    playerName = game.player;
    document.getElementById("player").innerText = playerName;
    document.getElementById("phase").innerText = game.phase;
    if (game.phase == "FEED") {
        document.getElementById("movePanel").style.display = 'block';
        document.getElementById("feedPanel").style.display = 'block'; //show panel
        let food = document.getElementById("food");
        while (food.firstChild) {
            food.removeChild(food.firstChild);
        }
        for (let i = 0; i < game.food; i++) food.appendChild(buildFood())


    } else if (game.phase == "START") {
        document.getElementById("movePanel").style.display = 'none';
        document.getElementById("feedPanel").style.display = 'none';//hide panel
    }
    else {
        document.getElementById("movePanel").style.display = 'block'; //evolution phase
        document.getElementById("feedPanel").style.display = 'none';
    }

    document.getElementById("players").innerText = game.playersList;
    var yourStatus = document.getElementById("status");

    if (game.status == true) {
        yourStatus.innerText = "It's your turn!";
        status = true;
    }
    else {
        yourStatus.innerText = "Please, wait...";
        status = false; //mean false in js
    }

    var common = document.getElementById("common");
    common.innerText = "";

    for (var name in game.players) {
        var player = game.players[name];
        common.appendChild(buildPlayerBlock(player));
        //build divider
    }

    document.getElementById("log").innerHTML += "<br/>" + game.moves + "   on " + new Date().toLocaleString();

    if (game.hasOwnProperty("last")) document.getElementById("last").style.display="block";
    if (game.hasOwnProperty("winners")) alert(game.winners + " win!");
}

function makeMove() {
    if (status) {
        let json = buildMessage();
        clearFields();//clear fields after message is built!
        socket.send(json);
    }
}

function clearFields() {
    clearMove();
    status = false;
}

function clearMove() {
    move = null;
    draggedProperty = null;
    firstAnimalId = null;
    secondAnimalId = null;
    playedCardId = null;
    document.getElementById("doing").textContent = "";
}

function leave() {
    move = "Leave";
    socket.send(buildMessage());
    location.assign("/evo/signIn")
}

function restart() {
    if (status) {
        move = "Restart";
        socket.send(buildMessage());
    }
}

function init() {
    // document.getElementById("player").innerText = getCookie("player");
    // player = getCookie("player");
    // Object.freeze(player);
}


// function getCookie(player) {
//     match = document.cookie.match(new RegExp(player + '=([^;]+)'));
//     if (match) return match[1];
// }




