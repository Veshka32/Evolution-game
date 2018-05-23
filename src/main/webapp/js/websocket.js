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
        document.getElementById("feedPanel").style.display = 'block'; //show panel
        document.getElementById("food").innerText = game.food;
    }
    else document.getElementById("feedPanel").style.display = 'none';

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

function buildPlayerBlock(player) {
    let playerBlock = document.createElement("div");
    playerBlock.id = player.name;

    let playerName = document.createElement("div");
    playerName.innerText = player.name + "'s animals:";

    playerBlock.appendChild(playerName);

    if (player.name == this.playerName) {

        var personal = document.getElementById("personal");
        personal.innerHTML = "";

        for (let k = 0; k < player.cards.length; k++) {
            var card = player.cards[k];
            personal.appendChild(buildCard(card));
        }
    }

    for (let id in player.animals) {
        let animal = player.animals[id];
        playerBlock.appendChild(buildAnimal(animal));
    }

    return playerBlock;
}

function buildAnimal(an) {
    var animDiv = document.createElement("div");
    animDiv.setAttribute("class", "animal");
    for (var key in an) {
        if (key == "propertyList") {
            for (let m in an.propertyList) {
                let span = document.createElement("span");
                span.setAttribute("class", "property");
                span.appendChild(document.createTextNode(an.propertyList[m]));
                animDiv.appendChild(span);
            }
        }
        else if (an[key] !== null) {
            let span = document.createElement("span");
            span.setAttribute("class", "parameter");
            span.innerText = key + ": " + an[key];
            animDiv.appendChild(span);
        }
    }

    animDiv.addEventListener("click", function () {
        if (firstAnimalId == null || firstAnimalId == undefined) {
            firstAnimalId = an.id;
        }
        else secondAnimalId = an.id;

        var doing = document.getElementById("doing");
        if (secondAnimalId == null || secondAnimalId == undefined)
            doing.innerText += firstAnimalText[document.getElementById("phase").innerText] + firstAnimalId;
        else doing.innerText += secondAnimalText[document.getElementById("phase").innerText] + secondAnimalId;
    });

    return animDiv;
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

function buildButton(name, cardId) {
    var property = document.createElement("button");
    property.addEventListener("click", function () {
        playProperty(name, cardId);
    });
    property.innerText = name;
    return property;
}

function buildCooperation() {
    var comm = document.createElement("span");
    comm.setAttribute("class", "cooperation");
    comm.appendChild(document.createTextNode("Cooperation"));
    return comm;
}

function buildCommunication() {
    var comm = document.createElement("span");
    comm.setAttribute("class", "communication");
    comm.appendChild(document.createTextNode("Communication"));
    return comm;
}

function buildCard(card) {
    var cardDiv = document.createElement("div");
    cardDiv.setAttribute("class", "card");
    cardDiv.innerHTML = card.id + "<br/>";

    cardDiv.appendChild(buildButton(card.property, card.id));

    if (card.hasOwnProperty("extraProperty")) {
        cardDiv.appendChild(buildButton(card.extraProperty, card.id));
    }

    cardDiv.appendChild(buildButton("MakeAnimal", card.id));
    return cardDiv;
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

function buildMessage() {
    return JSON.stringify({
        "player": playerName,
        "cardId": playedCardId,
        "animalId": firstAnimalId,
        "secondAnimalId": secondAnimalId,
        "move": move,
        "property": draggedProperty,
        "log": document.getElementById("doing").innerText
    });
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




