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

        for (var k = 0; k < player.cards.length; k++) {
            var card = player.cards[k];
            personal.appendChild(buildCard(card));
        }
    }

    for (var i = 0; i < player.animals.length; i++) {
        var animalGroup = player.animals[i];
        for (let m = 0; m < animalGroup.length; m++) {
            var animDiv = buildAnimal(animalGroup[m]);
            animDiv.appendChild(document.createTextNode("group #" + i + "\n"));
            playerBlock.appendChild(animDiv);
        }

    }

    return playerBlock;
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
    var comm = document.createElement("div");
    comm.setAttribute("class","cooperation");
    comm.appendChild(document.createTextNode("Cooperation"));
    return comm;
}

function buildCommunication() {
    var comm = document.createElement("div");
    comm.setAttribute("class","communication");
    comm.appendChild(document.createTextNode("Communication"));
    return comm;
}

function buildAnimal(an) {
    var animDiv = document.createElement("div");
    animDiv.setAttribute("class", "animal");
    animDiv.innerHTML += an.id + "<br/>";

    var props = document.createElement("span")
    animDiv.appendChild(props);

    if (an.hasOwnProperty("propertyList")) {

        for (var i = 0; i < an.propertyList.length; i++) {
            if (prop == "Communication" && i !== 0 && i != an.propertyList.length - 1) {
                let comm = buildCommunication();
                animDiv.appendChild(comm);
            }

            else if (prop = "Cooperation" && i !== 0 && i !== an.propertyList.length - 1) {
                let comm = buildCooperation();
                animDiv.appendChild(comm);
            }
            else {
                var prop = an.propertyList[i];
                var text = document.createElement("span");
                text.appendChild(document.createTextNode(prop));
                props.appendChild(text);
            }

        }
    }

    var totalHungry = document.createElement("span");
    totalHungry.innerHTML = "Total Hungry: " + an.totalHungry + "<br/>";
    animDiv.appendChild(totalHungry);

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
        document.getElementById("doing").innerText = playerName+"end move";
        let json = buildMessage();
        clearFields();
        socket.send(json);
    }
}

function buildMessage() {
    let json = JSON.stringify({
        "player": playerName,
        "cardId": playedCardId,
        "animalId": targedAnimalId,
        "secondAnimalId": secondAnimalId,
        "move": move,
        "property": draggedProperty,
        "log": document.getElementById("doing").innerText
    });
    return json;
}

function clearFields() {
    targedAnimalId = null;
    draggedProperty = null;
    playedCardId = null;
    document.getElementById("doing").innerHTML = "";
    move = null;
    status = "";
}

function leave() {
    move = "Leave";
    socket.send(buildMessage());
    location.assign("/evo/signIn")
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




