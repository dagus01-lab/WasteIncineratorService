var socket = connect();

function connect(){
  var host     = document.location.host;
  var pathname =  document.location.pathname;
  var addr     = "ws://" +host + pathname + "socket"  ;

  // Assicura che sia aperta un unica connessione
  if(socket!==undefined && socket.readyState!==WebSocket.CLOSED){
    alert("WARNING: Connessione WebSocket già stabilita");
  }
  socket = new WebSocket(addr); //CONNESSIONE

  socket.onmessage = function (event) { //RICEZIONE
    updateGUI(""+`${event.data}`);
  };
  return socket;
}//connect

const incineratorStatus = document.getElementById("incineratorStatus")
const ashStorageStatus = document.getElementById("ashStorageStatus")
const opRobotStatus = document.getElementById("opRobotStatus")
const wasteStoragePackets = document.getElementById("wasteStoragePackets")

function updateGUI(message) {
	if(message.includes("wasteStorageStatus")){
		wasteStorageStatus.innerHTML = message.split("(")[1].split(")")[0]
	}
	else if(message.includes("ashStorageStatus")){
		ashStorageStatus.innerHTML = message.split("(")[1].split(")")[0]
	}
	else if(message.includes("opRobotStatus")){
		opRobotStatus.innerHTML = message.split("(")[1].split(")")[0]
	}
	else if(message.includes("wasteStoragePackets")){
		wasteStoragePackets.innerHTML = message.split("(")[1].split(")")[0]
	}
	else{
		alert("Unknown message: "+message)
	}
}