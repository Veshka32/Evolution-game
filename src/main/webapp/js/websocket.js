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
    if (game.hasOwnProperty("error")){
        alert(game.error);
        return;
    }

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

    var common = document.getElementById("common");
    common.innerText = "";

    for (var name in game.players){
        var player=game.players[name];
        common.appendChild(buildPlayerBlock(player));
        //build divider
    }

    var log = document.getElementById("log");
    var today = new Date();
    log.innerHTML += "<br/>" + game.moves + "   on " + today.toLocaleString();
}

function buildPlayerBlock(player) {
    let playerBlock=document.createElement("div");
    playerBlock.id=player.name;

    let playerName=document.createElement("span");
    playerName.innerText=player.name+"'s animals";

    playerBlock.appendChild(playerName);

    for (var i = 0; i < player.animals.length; i++) {
        var animal = player.animals[i];
        playerBlock.appendChild(buildAnimal(animal));
    }

    if (player.name==playerName){

        var personal = document.getElementById("personal");
        personal.innerHTML = "";

        for (var k = 0; k < player.cards.length; k++) {
            var card = player.cards[k];
            personal.appendChild(buildCard(card));
        }
    }


    return player;
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

    var totalHungry=document.createElement("span");
    totalHungry.innerHTML="Total Hungry: "+an.totalHungry+"<br/>";
    animDiv.appendChild(totalHungry);

    animDiv.addEventListener("click",function () {
        if (targedAnimalId==null || targedAnimalId==undefined){targedAnimalId=an.id;}
        else secondAnimalId=an.id;

        var doing=document.getElementById("doing");
        doing.innerHTML="play property="+draggedProperty+"<br/>"+"on animal #"+targedAnimalId;
        if (!(secondAnimalId==null || secondAnimalId==undefined)) doing.innerText+="and animal #"+secondAnimalId;
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
        socket.send(buildMessage());
        clearFields();
    }

}
function endPhase() {
    if (status)
    {
        move="EndPhase";
        socket.send(buildMessage());
    }
}

function buildMessage() {
    var json = JSON.stringify({"player": playerName, "cardId":playedCardId, "animalId":targedAnimalId,"secondAnimalId":secondAnimalId,"move": move,"property":draggedProperty,"log":document.getElementById("doing").innerText});
    return json;
}

function clearFields(){
    targedAnimalId=null;
    draggedProperty=null;
    playedCardId=null;
    document.getElementById("doing").innerHTML="";
    move=null;
}

function leave() {
    move="Leave";
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




