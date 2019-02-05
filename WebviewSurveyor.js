
function _Appium_getWebElementCoordinates(){

    var all = document.getElementsByTagName('*');

    //var overlay = _Appium_addOverlayToScreen()

    for (var i = 0, max = all.length; i < max; i++) {
        
        var element = all[i];
        var eleRect = element.getBoundingClientRect();
        
        element.setAttribute("_APPIUM_ELEMENT_COORDS",  JSON.stringify(eleRect));

        // Draw the lines using the bounding rectangle for a element
        _Appium_displayBordersForElement(eleRect);
    }
 
};

function _Appium_displayBordersForElement(eleRect){
        var elemDiv = document.createElement('div');
        elemDiv.style.borderStyle = "solid";
        elemDiv.style.borderWidth = "1px";
        elemDiv.style.borderColor = "red";
        document.body.appendChild(elemDiv);

        elemDiv.style.position = "absolute";
        elemDiv.style.zindex = 999;
        elemDiv.style.width = eleRect.width + "px";
        elemDiv.style.height = eleRect.height+ "px";
        elemDiv.style.top = eleRect.top + "px";
        elemDiv.style.left = eleRect.left + "px";
}

function _Appium_addOverlayToScreen(){
    var overlay = document.createElement('div');
    overlay.style.position = "fixed";
    overlay.style.width = "100%";
    overlay.style.height = "100%";
    overlay.style.top = 0;
    overlay.style.left = 0;
    overlay.style.right = 0;
    overlay.style.bottom = 0;
    overlay.style.backgroundColor = "rgba(0,0,0,0.3)";
    overlay.style.zindex = 500;
    document.body.appendChild(overlay);
    return overlay;
}

_Appium_getWebElementCoordinates();