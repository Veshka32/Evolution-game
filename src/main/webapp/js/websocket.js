window.onload = init;
var socket = new WebSocket("ws://localhost:8080/evo/socket");
socket.onmessage = onMessage;

var playerName;
var move;
var draggedProperty;
var firstAnimalId;
var secondAnimalId;
var playedCardId;
var firstAnimalText = JSON.parse('{"EVOLUTION":" on animal #","FEED":"Feed animal #"}');
var secondAnimalText = JSON.parse('{"EVOLUTION":" and animal #","FEED": "attack animal #"}');

function eatFood() {
    move = "eatFood";
    alert("click animal"); // set firstAnimalId
}

function attack() {
    move = "attack";
    alert("click your predator, then victim"); //set firstAnimalId and secondAnimalId
}

function playExtraProp() {
    alert("click animal property");
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
        document.getElementById("food").innerText = game.food;
    } else if(game.phase=="START"){
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
        status = ""; //mean false in js
    }

    var common = document.getElementById("common");
    common.innerText = "";

    for (var name in game.players) {
        var player = game.players[name];
        common.appendChild(buildPlayerBlock(player));
        //build divider
    }

    var log = document.getElementById("log");
    var today = new Date();
    log.innerHTML += "<br/>" + game.moves + "   on " + today.toLocaleString();
}

function playProperty(property, cardId) {
    if (status) {
        if (property === "MakeAnimal") {
            move = "MakeAnimal";
            playedCardId = cardId;
            document.getElementById("doing").innerText = "Make animal from card # " + cardId;
        }
        else {
            move = "PlayProperty";
            draggedProperty = property;
            playedCardId = cardId;
            document.getElementById("doing").innerText = "play property " + draggedProperty + " from card #" + cardId;
            alert("Click animal");
        }
    }
}

function makeMove() {
    if (status) {
        let json = buildMessage();
        clearFields();
        socket.send(json);
    }
}

function endPhase() {
    if (status) {
        move = "EndPhase";
        document.getElementById("doing").innerText = "end move";
        let json = buildMessage();
        clearFields();
        socket.send(json);
    }
}

function clearFields() {
    clearMove();
    status = "";
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




