%====================================================================================
% scale description   
%====================================================================================
event( scaledata, weight(W) ). %emitted  by scale or (better) by datacleaner
event( wasteStorageRPs, wasteStorageRPs(N) ). %emitted  by datacleaner
event( new_RP, new_RP(N) ). %command that simulate the arrival of new RP
%====================================================================================
context(ctxscale, "localhost",  "TCP", "8200").
 qactor( scaledevice, ctxscale, "it.unibo.scaledevice.Scaledevice").
 static(scaledevice).
  qactor( scale, ctxscale, "it.unibo.scale.Scale").
 static(scale).
