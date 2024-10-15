%====================================================================================
% scale description   
%====================================================================================
event( scaledata, weight(W) ). %emitted  by scale
event( num_RP, num_RP(N) ). %command for communicating the number of new RP
%====================================================================================
context(ctxscale, "localhost",  "TCP", "8200").
 qactor( scaledevice, ctxscale, "it.unibo.scaledevice.Scaledevice").
 static(scaledevice).
  qactor( scale, ctxscale, "it.unibo.scale.Scale").
 static(scale).
