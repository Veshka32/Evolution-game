function forEvolution(status) {
    if (status) document.getElementById("personal").style.pointerEvents = "auto"; // card clickable
    else document.getElementById("personal").style.pointerEvents="none";

    document.getElementById("movePanel").style.display = 'block'; //evolution phase
    document.getElementById("feedPanel").style.display = 'none'; //hide panel
    document.getElementById("End move").style.display='none'; //hide button
}

function forFeed() {
    document.getElementById("personal").style.pointerEvents = "none"; //card non-clickable
    document.getElementById("feedPanel").style.display = 'block'; //show panel
    document.getElementById("End move").style.display='block'; //show button
}