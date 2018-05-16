window.onload = init;
var socket = new WebSocket("ws://localhost:8080/evo/socket");
socket.onmessage = onMessage;

var playerName;
var move;
var draggedProperty;
var targedAnimalId;
var playedCardId;

function onMessage(event) {
    var game = JSON.parse(event.data);
    if (game.hasOwnProperty("error") && game.player==playerName){
        alert(game.error);
        clearFields();
        return;
    }
    var content = document.getElementById("content");
    content.innerText = "";

    playerName=game.player;
    document.getElementById("player").innerText =playerName;
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

    var privat = document.getElementById("privat");
    privat.innerText = "";
    var common = document.getElementById("common");
    common.innerText = "";

    for (var key in game.players){
        var name=key;
        var player=game.players[key];
        for (var i = 0; i < player.animals.length; i++) {
            var animal = player.animals[i];
            common.appendChild(buildAnimal(animal));
        }

        if (player.name==playerName){
            for (var i = 0; i < player.cards.length; i++) {
                var card = player.cards[i];
                privat.appendChild(buildCard(card));
            }
        }
    }

    var log = document.getElementById("log");
    var today = new Date();
    var move = "<br/>" + game.moves + "   on " + today.toLocaleString();
    log.innerHTML += move;
}

function playProperty(property, cardId) {
    if (status) {
        if (property==="MakeAnimal") {
            move="MakeAnimal";
            playedCardId=cardId;
            document.getElementById("doing").innerHTML="Make animal from card # "+cardId;
        }
        else {
            move="PlayProperty";
            draggedProperty=property;
            playedCardId=cardId;
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

function buildAnimal(an) {
    var animDiv = document.createElement("div");
    animDiv.setAttribute("class", "animal");
    animDiv.innerHTML += an.id+"<br/>";

    var owner = document.createElement("span");
    owner.innerHTML = "Owner: " + an.owner+"<br/>";
    animDiv.appendChild(owner);

    var props=document.createElement("span");
    props.innerHTML="Properties:<br/>";
    animDiv.appendChild(props);

    if (an.hasOwnProperty("propertyList")) {

        for (var i = 0; i < an.propertyList.length; i++) {
            var prop = an.propertyList[i];
            props.innerHTML+=prop+"<br/>";
        }
    }
    animDiv.addEventListener("click",function () {
        targedAnimalId=an.id;
        document.getElementById("doing").innerHTML+="property="+draggedProperty+"<br/>"+"animal="+targedAnimalId;
        //alert("an="+targedAnimalId+" , prop="+draggedProperty);
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
    if(status)
    {
        var json = JSON.stringify({"player": playerName, "cardId":playedCardId, "animalId":targedAnimalId,"move": move,"property":draggedProperty});
        alert("Your move is: "+json);
    socket.send(json);
    clearFields();
    document.getElementById("doing").innerHTML="";
    }

}
function endPhase() {
    if (status)
    {var json = JSON.stringify({"player": playerName, "move": "EndPhase"});
    socket.send(json);
    }
}

function clearFields(){
    targedAnimalId=null;
    draggedProperty=null;
    playedCardId=null;
}

function leave() {
    var json=JSON.stringify({"player":playerName,"move":"Leave"});
    socket.send(json);
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




