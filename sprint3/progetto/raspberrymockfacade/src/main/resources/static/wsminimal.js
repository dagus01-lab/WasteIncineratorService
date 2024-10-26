// Declare WebSocket outside the function to avoid redeclaration
var socket = null;

var statoAshStorage = 0;
var statoWasteStorage = 0;

function connect() {
    // Build WebSocket address
    var host = document.location.host;
    var pathname = document.location.pathname;
    var addr = "ws://" + host + pathname + "accessgui";

    // Prevent multiple connections by checking the socket state
    if (socket && socket.readyState !== WebSocket.CLOSED) {
        alert("WARNING: WebSocket connection already established.");
        return socket;
    }

    // Open WebSocket connection
    socket = new WebSocket(addr);

    // Error handling
    socket.onerror = function (event) {
        console.error("WebSocket error observed:", event);
    };

    // Message handler
    socket.onmessage = function (event) {
        try {
			console.log(event.data)
            const jsonObject = JSON.parse(event.data);

            // Determine which data is present in the message
            if (jsonObject.hasOwnProperty("ashStorageState")) {
                updateStatoAshStorage(jsonObject.ashStorageState.toString());
            } else if (jsonObject.hasOwnProperty("wasteStorageState")) {
                updateStatoWasteStorage(jsonObject.wasteStorageState.toString());
            }
        } catch (e) {
            console.error("Failed to parse WebSocket message:", e);
        }
    };

    return socket;
}

// Update Ash Storage State
function updateStatoAshStorage(state) {

    if (socket && socket.readyState === WebSocket.OPEN && state != statoAshStorage) {
		const updateJson = JSON.stringify({ ashStorageState: state });
		console.log("statoAshStorageUpdate:", updateJson);
        socket.send(updateJson);
    }
    statoAshStorage = state;
    document.getElementById("md-textbox").innerHTML = statoAshStorage;
}

// Update Waste Storage State
function updateStatoWasteStorage(state) {
    // Prevent the state from going negative
    if (state >= 0) {


        if (socket && socket.readyState === WebSocket.OPEN && state != statoWasteStorage) {
			const updateJson = JSON.stringify({ wasteStorageState: state });
			console.log("statoWasteStorageUpdate:", updateJson);
            socket.send(updateJson);
        }
        statoWasteStorage = parseInt(state);
        document.getElementById("scale-textbox").innerHTML = statoWasteStorage + " RP";
    }
}

// Attach event listeners using jQuery
$(function () {
    $("#statoASFull").click(function () { updateStatoAshStorage("FULL"); });
    $("#statoASNormal").click(function () { updateStatoAshStorage("NORMAL"); });
    $("#statoASEmpty").click(function () { updateStatoAshStorage("EMPTY"); });
    $("#newRP").click(function () { updateStatoWasteStorage(statoWasteStorage + 1); });
    $("#lessRP").click(function () { updateStatoWasteStorage(statoWasteStorage - 1); });
});

// Initialize WebSocket connection
connect();

