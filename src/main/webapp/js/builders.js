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
            playerBlock.appendChild(buildAnimal(animal, true));
        }

    } else {
        playerName.innerText = player.name + "'s animals:";

        for (let id in player.animals) {
            let animal = player.animals[id];
            playerBlock.appendChild(buildAnimal(animal, false));
        }
    }
    return playerBlock;
}

function buildAnimal(animal, flag) {
    var animDiv = document.createElement("div");
    animDiv.setAttribute("class", "animal");
    for (var key in animal) {
        if (key == "propertyList") {
            for (let m in animal.propertyList) {

                if (flag) {
                    let prop = animal.propertyList[m];
                    animDiv.appendChild(buttonOnAnimal(prop,animal.id));
                }

                else {
                    let span = document.createElement("span");
                    span.setAttribute("class", "property");
                    span.appendChild(document.createTextNode(animal.propertyList[m]));
                    animDiv.appendChild(span);
                }
            }
        }
        else if (animal[key] !== null) {
            let span = document.createElement("span");
            span.setAttribute("class", "parameter");
            span.innerText = key + ": " + animal[key];
            animDiv.appendChild(span);
        }
    }

    animDiv.addEventListener("click", function () {
        var doing = document.getElementById("doing");
        if (firstAnimalId == null || firstAnimalId == undefined) {
            firstAnimalId = animal.id;
            doing.innerText += " animal #"+firstAnimalId;
        }
        else {
            secondAnimalId = animal.id;
            var text;
            if (document.getElementById("phase").innerText=="EVOLUTION") text=" and animal #";
            else if (document.getElementById("phase").innerText=="FEED") text=" attack animal #";
            doing.innerText += text + secondAnimalId;
        }

    });

    return animDiv;
}

function buttonOnAnimal(name,id) {
    var property = document.createElement("button");
    property.addEventListener("click", function (event) {
        event.stopPropagation();
        playAnimalProperty(name,id);
    });
    property.innerText = name;
    return property;
}

function playAnimalProperty(property,animalId) {
    if (status){
            move = "playAnimalProperty";
            draggedProperty = property;
            firstAnimalId=animalId;
            document.getElementById("doing").innerText = "Play property " + draggedProperty + " from animal #" + animalId;
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