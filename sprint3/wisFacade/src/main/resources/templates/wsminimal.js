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

function updateGUI(message) {
	payload = message.split("(")[1].split(")")[0]
	element = message.split("(")[0]
	document.getElementById(element).innerHTML = payload
}