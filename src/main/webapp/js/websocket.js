window.onload = init;
var socket = new WebSocket("ws://localhost:8080/evo/socket");
socket.onmessage = onMessage;


function onMessage(event) {
    var game = JSON.parse(event.data);
    var content = document.getElementById("content");
    content.innerText = "";

    document.getElementById("phase").innerText=game.phase;
    document.getElementById("players").innerText=game.players;
    var yourStatus=document.getElementById("status");

    if (game.status==true){
        yourStatus.innerText="It's your turn!"
        status=true;
    }
    else {
        yourStatus.innerText="Please, wait...";
        status=""; //mean false in js
    }

    var privat = document.getElementById("privat");
    privat.innerText = "";
    var common = document.getElementById("common");
    common.innerText = "";


    if (game.hasOwnProperty("cards")){
        var cards = game.cards;
    var arrayCards = cards.split("/");
    arrayCards.forEach(function (value) {
        var card = JSON.parse(value);
        privat.appendChild(buildCard(card));
    })}

    if (game.hasOwnProperty("animals")) {
        var animals = game.animals;
        var animalArray = animals.split("/");
        animalArray.forEach(function (value) {
            var an = JSON.parse(value);
            common.appendChild(buildAnimal(an));
        })
    }

    var log = document.getElementById("log");
    var today = new Date();
    var move = "<br/>" + game.moves + "   on " + today.toLocaleString();
    log.innerHTML += move;
}

function playProperty(property,id) {
    if (status){
        var json = JSON.stringify({"player": player, "move": property,"id":id});
        alert(json);
        socket.send(json);}
}

function buildButton(name,id) {
    var property = document.createElement("button");
    property.addEventListener("click", function () {
        playProperty(name,id);
    });
    property.innerText = name;
    return property;
}

function done(){
    if (status)
    var json=JSON.stringify({"player":player,"move":"Done"});
    socket.send(json);
}

function init() {
    document.getElementById("player").innerText = getCookie("player");
    player = getCookie("player");
    Object.freeze(player);
}

function buildAnimal(an) {
    var animDiv = document.createElement("div");
    animDiv.setAttribute("class", "animal");
    var id = an.id;
    animDiv.innerText += id;

    var owner=document.createElement("span");
    owner.innerHTML="<br/>Owner: "+an.owner;
    animDiv.appendChild(owner);

    if (an.hasOwnProperty("properties")) {
        var properties = an.properties;
        var propArray = properties.split(",");
        propArray.forEach(function (value) {
            animDiv.innerText += value + "<br/>";
        })
    }
    return animDiv;
}

function buildCard(card) {
    var cardDiv = document.createElement("div");
    cardDiv.setAttribute("class", "card");
    cardDiv.innerHTML=card.id+"<br/>";

    cardDiv.appendChild(buildButton(card.property,card.id));

    if (card.hasOwnProperty("extraProperty")) {
        cardDiv.appendChild(buildButton(card.extraProperty,card.id));
    }

    cardDiv.appendChild(buildButton("Make animal",card.id));
    return cardDiv;
}




function getCookie(player) {
    match = document.cookie.match(new RegExp(player + '=([^;]+)'));
    if (match) return match[1];
}




