window.onload = init;
var socket = new WebSocket("ws://localhost:8080/evo/socket");
socket.onmessage = onMessage;

var playerName;
var move;
var draggedProperty;
var firstAnimalId = null;
var secondAnimalId = null;
var playedCardId;
var tailLoss=false;

function eatFood() {
    move = "eatFood";
    document.getElementById("doing").innerText = "Feed ";// set firstAnimalId
}

function attack() {
    move = "attack"; //set firstAnimalId and secondAnimalId
}

function endMove() {
    move = "endMove";
    document.getElementById("doing").innerText = "end move";
    let json = buildMessage();
    clearFields();
    socket.send(json);
}

function endPhase() {
    move = "EndPhase";
    document.getElementById("doing").innerText = "end " + document.getElementById("phase");
    let json = buildMessage();
    clearFields(); //clear fields after message is built!
    socket.send(json);
}

function onMessage(event) {
    clearFields();
    var game = JSON.parse(event.data);
    if (game.hasOwnProperty("error")) {
        alert(game.error);
        document.getElementById("wrapper").style.pointerEvents = "auto";
        return;
    }

    playerName = game.player;
    document.getElementById("player").innerText = playerName;
    document.getElementById("phase").innerText = game.phase;
    if (game.phase == "FEED") {
        document.getElementById("personal").style.pointerEvents = "none"; //card non-clickable
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
    else { //case evolution
        document.getElementById("personal").style.pointerEvents = "auto"; // card clickable
        document.getElementById("movePanel").style.display = 'block'; //evolution phase
        document.getElementById("feedPanel").style.display = 'none'; //hide panel
    }

    document.getElementById("players").innerText = game.playersList;
    var yourStatus = document.getElementById("status");

    var common = document.getElementById("common");
    common.innerText = "";

    for (var name in game.players) {
        var player = game.players[name];
        common.appendChild(buildPlayerBlock(player));
    }

    document.getElementById("log").innerHTML += "<br/>" + game.moves + "   on " + new Date().toLocaleString();

    if (game.hasOwnProperty("last")) document.getElementById("last").style.display = "block";
    if (game.hasOwnProperty("winners")) alert(game.winners + " win!");

    if (game.status == true) {
        yourStatus.innerText = "It's your turn!";
        document.getElementById("wrapper").style.pointerEvents = "auto"; //clickable whole page
    }
    else {
        yourStatus.innerText = "Please, wait...";
        document.getElementById("wrapper").style.pointerEvents = "none"; //disable whole page
        document.getElementById("personal").style.pointerEvents="none";//why does not inherit from wrapper?
    }
}

function makeMove() {
    let json = buildMessage();
    clearFields();//clear fields after message is built!
    socket.send(json);
}

function clearFields() {
    clearMove();
    document.getElementById("wrapper").style.pointerEvents = "none";
}

function clearMove() {
    move = null;
    draggedProperty = null;
    firstAnimalId = null;
    secondAnimalId = null;
    playedCardId = null;
    tailLoss=false;
    document.getElementById("doing").textContent = "";
}

function leave() {
    move = "Leave";
    socket.send(buildMessage());
    location.assign("/evo/signIn")
}

function restart() {
    move = "Restart";
    socket.send(buildMessage());
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




