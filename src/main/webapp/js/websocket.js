window.onload=init;
var socket = new WebSocket("ws://localhost:8080/evo/socket");
socket.onmessage = onMessage;

function onMessage(event) {
    var gameStatus = JSON.parse(event.data);
    printMoves(gameStatus);
}

function init() {
    document.getElementById("player").innerText=getCookie("player");
    player=getCookie("player");
    Object.freeze(player);
}

function printMoves(gameStatus) {
    var content = document.getElementById("content");
    content.innerText = "";

    var players = document.createElement("span");
    players.innerHTML = "<b>Players: </b> " + gameStatus.players + "<br>";
    content.appendChild(players);

    var privat = document.getElementById("privat");
    privat.innerText="";
    var common=document.getElementById("common");
    common.innerText="";

    var cards=gameStatus.cards;
    var arrayCards=cards.split("/");
    arrayCards.forEach(function (value) {
        var card=JSON.parse(value);
        privat.appendChild(buildCard(card));
    })

    if (gameStatus.hasOwnProperty("animals")){
        var animals=gameStatus.animals;
        var animalArray=animals.split("/");
        animalArray.forEach(function (value) {
            var an=JSON.parse(value);
            common.appendChild(buildAnimal(an));
        })
    }


    var log=document.getElementById("log");
    var move= "<br/>" + gameStatus.moves+Date;
    log.innerHTML+=move;

}

function buildAnimal(an){
    var animDiv=document.createElement("div");
    animDiv.setAttribute("class","animal");
    var id=an.id;
    animDiv.innerText+=id;

    if (an.hasOwnProperty("properties")){
        var properties=an.properties;
        var propArray=properties.split(",");
        propArray.forEach(function (value) {
            animDiv.innerText+=value+"<br/>";
        })
    }
    return animDiv;

}

function buildCard(card) {
    var cardDiv=document.createElement("div");
    cardDiv.setAttribute("class", "card");

    cardDiv.appendChild(buildButton(card.property));

    if (card.hasOwnProperty("extraProperty")){
        cardDiv.appendChild(buildButton(card.extraProperty));
    }

    cardDiv.appendChild(buildButton("Make animal"));
    return cardDiv;
}

function buildButton(name){
    var property=document.createElement("button");
    property.addEventListener ("click", function() {
        playProperty(name);
    });
    property.innerHTML=name;
    return property;
}

function playProperty(property){
    var json = JSON.stringify({"player": player, "opponent": "dsfsf", "move": property});
    socket.send(json);


}

function formSubmit() {
    // var form = document.getElementById("MakeMove");
    // var move = form.elements["move"].value;
    //
    // var json = JSON.stringify({"player": "pl", "opponent": "dsfsf", "move": move});
    // socket.send(json);
}

function getCookie(player) {
    match = document.cookie.match(new RegExp(player + '=([^;]+)'));
    if (match) return match[1];
}




