function buildPlayerBlock(player) {
    let playerBlock = document.createElement("div");
    playerBlock.id = player.name;

    let playerName = document.createElement("div");
    playerBlock.appendChild(playerName);

    if (player.name == this.playerName) {
        playerName.innerText = 'Your animals:';

        var personal = document.getElementById("personal");
        personal.innerHTML = "";

        for (let k = 0; k < player.cards.length; k++) {
            var card = player.cards[k];
            personal.appendChild(buildCard(card));
        }
        for (let id in player.animals) {
            let animal = player.animals[id];
            playerBlock.appendChild(buildAnimalWithButtons(animal));
        }

    } else {
        playerName.innerText = player.name + "'s animals:";

        for (let id in player.animals) {
            let animal = player.animals[id];
            playerBlock.appendChild(buildAnimal(animal));
        }
    }
    return playerBlock;
}

function buildAnimalWithButtons(an) {
    var animDiv = document.createElement("div");
    animDiv.setAttribute("class", "animal");
    for (var key in an) {
        if (key == "propertyList") {
            for (let m in an.propertyList) {
                let prop = an.propertyList[m];
                animDiv.appendChild(buttonOnAnimal(prop));
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

function buttonOnAnimal(name) {
    var property = document.createElement("button");
    property.addEventListener("click", function () {
        playAnimalProperty(name);
    });
    property.innerText = name;
    return property;
}

function buildButton(name, cardId) {
    var property = document.createElement("button");
    property.addEventListener("click", function () {
        playProperty(name, cardId);
    });
    property.innerText = name;
    return property;
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