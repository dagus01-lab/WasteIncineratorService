%====================================================================================
% raspberrymock description   
%====================================================================================
event( statoAshStorage, statoAshStorage(N,L) ). %Event that simulate the status of the ash Storage (0=normal, 1=full)
event( num_RP, num_RP(N) ). %command that simulates an update in the number of RPs
dispatch( ashStorageState, ashStorageState(N) ). %messaggio dalla GUI per notificare l'aggiornamento dello stato dell'ash storage
dispatch( wasteStorageState, wasteStorageState(N) ). %messaggio dalla GUI per notificare l'aggiornamento del numero di RP presenti nel waste storage
%====================================================================================
context(ctxraspberrymock, "localhost",  "TCP", "8100").
 qactor( raspberrymockproxy, ctxraspberrymock, "it.unibo.raspberrymockproxy.Raspberrymockproxy").
 static(raspberrymockproxy).
  qactor( monitoringdevice, ctxraspberrymock, "it.unibo.monitoringdevice.Monitoringdevice").
 static(monitoringdevice).
  qactor( scale, ctxraspberrymock, "it.unibo.scale.Scale").
 static(scale).
