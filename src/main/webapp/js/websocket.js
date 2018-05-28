window.onload = init;
var socket = new WebSocket("ws://localhost:8080/evo/socket");
socket.onmessage = onMessage;

var playerName;
var move;
var draggedProperty;
var firstAnimalId = null;
var secondAnimalId = null;
var playedCardId;
var tailLoss = false;

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
    document.getElementById("doing").innerText = "end " + document.getElementById("phase").innerText;
    let json = buildMessage();
    clearFields(); //clear fields after message is built!
    socket.send(json);
}

function onMessage(event) {
    doing = document.getElementById("doing");
    clearFields();
    var game = JSON.parse(event.data);

    playerName = game.player;
    document.getElementById("player").innerText = playerName;
    document.getElementById("phase").innerText = game.phase;
    document.getElementById("log").innerText = game.log;
    document.getElementById("players").innerText = game.playersList;

    var common = document.getElementById("common");
    common.innerText = "";

    for (let name in game.players) {
        let player = game.players[name];
        common.appendChild(buildPlayerBlock(player));
    }

    if (game.phase == "FEED") {
        forFeed();
        setFood(game);

    }
    else if (game.phase=="EVOLUTION")
        forEvolution(game.status);

    if (game.status == true) {
        document.getElementById("status").innerText = "It's your turn!";
        document.getElementById("wrapper").style.pointerEvents = "auto"; //clickable whole page
    }
    else {
        document.getElementById("status").innerText = "Please, wait...";
        document.getElementById("wrapper").style.pointerEvents = "none"; //disable whole page
    }

    if (game.hasOwnProperty("tailLoss")) {
        let message = game.tailLoss;

        if (message.playerOnAttack === playerName) {
            document.getElementById("status").innerText = "Please, wait for victim answer...";
            document.getElementById("wrapper").style.pointerEvents = "none"; //disable whole page
            document.getElementById("personal").style.pointerEvents = "none";//why does not inherit from wrapper?
        }
        else if (message.playerUnderAttack === playerName) {
            alert("Animal #" + message.predator + " attack your animal #" + message.victim + " with tail loss property. Choose property to loose or click animal to die");
            tailLoss = true;
            let animals = Array.from(document.getElementsByClassName("animal"));
            let animal = animals.find(x => x.id == message.victim);
            animal.style.pointerEvents = "auto"; //clikable only animals
            document.getElementById("Make move").style.pointerEvents = "auto";
            document.getElementById("Clear").style.pointerEvents = 'auto';
        }
    }

    if (game.hasOwnProperty("error")) alert(game.error);
    if (game.hasOwnProperty("last")) document.getElementById("last").style.display = "block";
    if (game.hasOwnProperty("winners")) alert(game.winners + " win!");
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

function setFood(game) {
    let food = document.getElementById("food");
    while (food.firstChild)
        food.removeChild(food.firstChild);
    for (let i = 0; i < game.food; i++) food.appendChild(buildFood())
}

function clearMove() {
    move = null;
    draggedProperty = null;
    firstAnimalId = null;
    secondAnimalId = null;
    playedCardId = null;
    tailLoss = false;
    document.getElementById("doing").innerText = "";
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




