var socket = connect();
function connect(){
  var host     = document.location.host;
  var pathname =  document.location.pathname;
  var addr     = "ws://" +host + pathname +"accessgui"  ;

  // Assicura che sia aperta un unica connessione
  if(socket!==undefined && socket.readyState!==WebSocket.CLOSED){
    alert("WARNING: Connessione WebSocket già stabilita");
  }
  socket = new WebSocket(addr); //CONNESSIONE
  socket.onerror = function (event) {
      console.error("WebSocket error observed:", event);
  };
  socket.onmessage = function (event) { //RICEZIONE
    updateGUI(""+`${event.data}`);
  };
  return socket;
}//connect
const incineratorState = document.getElementById("incineratorState")
const ashStorageState = document.getElementById("ashStorageLevel")
const opRobotState = document.getElementById("opRobotState")
const wasteStoragePackets = document.getElementById("wasteStoragePackets")
const opRobotJob = document.getElementById("opRobotJob")
function updateGUI(message) {
	if(message.includes("statoAshStorage")){
		ashStorageState.innerHTML = message.split(",")[1].split(")")[0]
	}
	else if(message.includes("statoIncinerator")){
		incineratorState.innerHTML = message.split("(")[1].split(")")[0]
	}
	else if(message.includes("opRobotState")){
		opRobotState.innerHTML = message.split("(")[1].split(")")[0]
	}
	else if(message.includes("opRobotJob")){
		opRobotJob.innerHTML = message.split("(")[1].split(")")[0].replace(/_/g, " ");
	}
	else if(message.includes("num_RP")){
		wasteStoragePackets.innerHTML = message.split("(")[1].split(")")[0]
	}
	else{
		alert("Unknown message: "+message)
	}
}