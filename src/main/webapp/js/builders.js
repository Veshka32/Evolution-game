function buildFood() {
    let img = document.createElement('IMG');
    img.setAttribute('src', '../images/food.png');
    img.setAttribute('class', 'food active');
    return img;
}

function buildFat(int) {
    let img = document.createElement('IMG');
    img.setAttribute('src', '../images/fat.png');
    img.setAttribute("class","active");
    return img;
}

function buildPlayerBlock(player) {
    let playerBlock = document.createElement("div");
    playerBlock.id = player.name;

    let playerName = document.createElement("div");
    playerBlock.appendChild(playerName);

    if (player.name == this.playerName) {
        if (player.doEat)
            document.getElementById("feedPanel").style.pointerEvents = "none";
        else
            document.getElementById("feedPanel").style.pointerEvents = "auto";//disable food and attack buttons}

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
    let animDiv = document.createElement("div");
    animDiv.setAttribute("class", "animal");
    animDiv.setAttribute("id", animal.id);
    for (let key in animal) {
        if (key == "propertyList") {
            for (let m in animal.propertyList) {
                let span = document.createElement("span");
                span.setAttribute("class", "property");

                if (flag) {
                    let property = animal.propertyList[m];
                    span.className+=" active";
                    buttonOnAnimal(span, property, animal.id);
                } else
                    span.appendChild(document.createTextNode(animal.propertyList[m]));


                animDiv.appendChild(span);
            }
        }
        else if (key == "currentFatSupply") {
            let span = document.createElement("span");
            span.setAttribute("class", "fat");
            for (let i = 0; i < animal[key]; i++) {
                span.appendChild(buildFat());
            }
            span.addEventListener("click", function () {
                move = "eatFat";
                firstAnimalId = animal.id;
                document.getElementById("doing").innerText = "animal #" + firstAnimalId + " eats fat supply";
            });
            animDiv.appendChild(span);

        }
        else if (animal[key] !== null || animal[key] !== undefined) {
            let span = document.createElement("span");
            span.setAttribute("class", "parameter");
            if (Array.isArray(animal[key])) {
                let arr = animal[key];
                if (arr.length > 0)
                    span.innerText = key + ": " + arr.toString();
            } else {
                span.innerText = key + ": " + animal[key];
            }
            animDiv.appendChild(span);
        }
    }

    animDiv.addEventListener("click", function () {
        if (mimicry) {
            move = "playMimicry";
            if (mimicryVictims.includes(animal.id)) {
                document.getElementById("doing").innerText = "Redirect predator to animal #" + animal.id;
                firstAnimalId = animal.id;
            }
            else {
                (alert("You can't redirect the predator to this animal"));
                return;
            }
        }

        else if (firstAnimalId == null) {
            firstAnimalId = animal.id;
            document.getElementById("doing").innerText += " animal #" + firstAnimalId;
        }
        else {
            secondAnimalId = animal.id;
            let text;
            if (document.getElementById("phase").innerText == "EVOLUTION") text = " and animal #";
            else if (document.getElementById("phase").innerText == "FEED") text = " attack animal #";
            document.getElementById("doing").innerText += text + secondAnimalId;
        }

    });

    return animDiv;
}

function buttonOnAnimal(span, name, id) {
    span.addEventListener("click", function (event) {
        event.stopPropagation();
        if (document.getElementById("phase").innerText == "FEED" || tailLoss) { //active only in feed phase
            playAnimalProperty(name, id);
        }
    });
    span.innerText = name;
}

function playAnimalProperty(property, animalId) {
    draggedProperty = property;
    firstAnimalId = animalId;
    if (tailLoss) {
        move = "DeleteProperty";
        document.getElementById("doing").innerText = "Delete property  ";
    }
    else {
        move = "playAnimalProperty";
        document.getElementById("doing").innerText = "Play property "
    }
    document.getElementById("doing").innerText += draggedProperty + " from animal #" + animalId;
}

function playProperty(property, cardId) {
    if (document.getElementById("phase").innerText == "EVOLUTION") {
        playedCardId = cardId;
        if (property === "MakeAnimal") {
            move = "MakeAnimal";
            document.getElementById("doing").innerText = "Make animal from card # " + cardId;
        } else if (property === "DeleteProperty") {
            alert("Click property on any animal to delete");
            tailLoss = true;
            move = "DeleteProperty";
        }
        else {
            move = "PlayProperty";
            draggedProperty = property;
            document.getElementById("doing").innerText = "play property " + draggedProperty + " from card #" + cardId;
        }
    }
}

function buildButton(name, cardId) {
    let property = document.createElement("button");
    property.addEventListener("click", function () {
        playProperty(name, cardId);
    });
    property.innerText = name;
    return property;
}

function buildCard(card) {
    let cardDiv = document.createElement("div");
    cardDiv.setAttribute("class", "card");
    let number = document.createElement("span");
    number.innerText = card.id;
    cardDiv.appendChild(number);
    cardDiv.appendChild(buildButton(card.property, card.id));

    if (card.hasOwnProperty("extraProperty")) {
        if (card.extraProperty == "DeleteProperty")
            cardDiv.appendChild(buildButton("DeleteProperty", card.id));
        else
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