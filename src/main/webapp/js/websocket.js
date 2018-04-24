window.onload=init;
var socket = new WebSocket("ws://localhost:8080/evo/socket");
socket.onmessage = onMessage;

function onMessage(event) {
    var gameStatus = JSON.parse(event.data);
    printMoves(gameStatus);
}

function init() {
    document.getElementById("player").innerText=getCookie("player");
}


function printMoves(gameStatus) {
    var content = document.getElementById("content");
    content.innerText = "";


    var players = document.createElement("span");
    players.innerHTML = "<b>Players: </b> " + gameStatus.players + "<br>";
    content.appendChild(players);

    var moves = document.createElement("span");
    moves.innerHTML = "<b>Moves: </b> " + gameStatus.moves;
    content.appendChild(moves);

    var privat = document.getElementById("privat");
    privat.innerText="";

    /*var cardsArray=gameStatus.cards.split("/");
    cardsArray.forEach(function (value) {
        var child=buildCard(value);
        privat.appendChild(child)});*/

    var cards=gameStatus.cards;
    var arrayCards=cards.split("/");
    arrayCards.forEach(function (value) {
        var card=JSON.parse(value);
        privat.appendChild(buildCard(card));
    })

}

function buildCard(card) {
    var cardDiv=document.createElement("div");
    cardDiv.setAttribute("class", "card");

    cardDiv.appendChild(buildButton(card.property));

    if (card.extraProperty!="null"){
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
    alert(property);
}

function getCookie(player) {
    match = document.cookie.match(new RegExp(player + '=([^;]+)'));
    if (match) return match[1];
}


function formSubmit() {
    var form = document.getElementById("MakeMove");
    var move = form.elements["move"].value;
    document.getElementById("MakeMove").reset();
    var json = JSON.stringify({"player": "pl", "opponent": "dsfsf", "move": move});
    socket.send(json);
}

