%====================================================================================
% monitoringdevicemock description   
%====================================================================================
event( statoAshStorage, statoAshStorage(N) ). %Event that simulate the status of the ash Storage (0=normal, 1=full)
%====================================================================================
context(ctxscale, "localhost",  "TCP", "8200").
 qactor( inputreader, ctxscale, "it.unibo.inputreader.Inputreader").
 static(inputreader).
  qactor( scale, ctxscale, "it.unibo.scale.Scale").
 static(scale).
