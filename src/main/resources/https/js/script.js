





function centerArrows() {

let rightArrow = document.getElementById("r-arrow");
let leftArrow = document.getElementById("l-arrow");

let rightArrowComputed = getComputedStyle(rightArrow);
let leftArrowComputed = getComputedStyle(leftArrow);


//rightArrow.style.left = removePx(rightArrowComputed.left) - 10 + "px";


let availQuarters = getComputedStyle("available-quarters");

leftArrow.style.top =  removePx(availQuarters.height) / 1.5 + "px";
rightArrow.style.top =  removePx(availQuarters.height) / 1.5 + "px";

}





function removePx(s) {
    return parseFloat(s.substring(0, s.length - 2));
}

function addPx(s, px) {
    return parseFloat(removePx(s) + px) + "px";
}

function removePercent(s) {
    return s.substring(0, s.length - 1);
}


addEventListener("resize", (event) => {
    adjustHexagonWidth();
});



var menus = ["quarters-supply", "generators-supply"];
var selectedMenu = 0;

function nextMenu() {
    let activeMenu = document.getElementById(menus[selectedMenu]);
    activeMenu.style.left = "300%";
    selectedMenu++;
    if (selectedMenu >= menus.length) {
        selectedMenu = 0;
    }
    activeMenu = document.getElementById(menus[selectedMenu]);
    activeMenu.style.transitionDuration = "0s";

    setTimeout(function() {
        activeMenu.style.left = "-300%";
        setTimeout(function() {
            activeMenu.style.transitionDuration = "1s";
            activeMenu.style.left = "50%"}, 1);


    }, 1);
}


adjustHexagonWidth();
function adjustHexagonWidth() {

    Array.from(document.getElementsByClassName("quarter-template")).forEach(
    (e) => {
        e.style.setProperty('--adjusted-width', addPx(getComputedStyle(e).width, -29*2 + 1));
    }
    );
}