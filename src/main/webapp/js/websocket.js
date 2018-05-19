window.onload = init;
var socket = new WebSocket("ws://localhost:8080/evo/socket");
socket.onmessage = onMessage;

var playerName;
var move;
var draggedProperty;
var targedAnimalId;
var secondAnimalId;
var playedCardId;

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
    playerName.innerText = player.name + "'s animals";

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
        let animal=player.animals[id];
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
        else if(an[key]!==0)
            {
                let span = document.createElement("span");
                span.setAttribute("class","parameter");
                span.innerText = key + ": " + an[key];
                animDiv.appendChild(span);
            }
        }

        animDiv.addEventListener("click", function () {
        if (targedAnimalId == null || targedAnimalId == undefined) {
            targedAnimalId = an.id;
        }
        else secondAnimalId = an.id;

        var doing = document.getElementById("doing");
        doing.innerHTML = "play property=" + draggedProperty + "<br/>" + "on animal #" + targedAnimalId;
        if (!(secondAnimalId == null || secondAnimalId == undefined)) doing.innerText += "and animal #" + secondAnimalId;
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
        "animalId": targedAnimalId,
        "secondAnimalId": secondAnimalId,
        "move": move,
        "property": draggedProperty,
        "log": document.getElementById("doing").innerText
    });
}

function clearFields() {
    targedAnimalId = null;
    draggedProperty = null;
    playedCardId = null;
    document.getElementById("doing").innerText = "";
    move = null;
    status = "";
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




