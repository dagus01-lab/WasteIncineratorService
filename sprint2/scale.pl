%====================================================================================
% scale description   
%====================================================================================
event( scaledata, weight(W) ). %emitted  by scale or (better) by datacleaner
event( wasteStorageRPs, wasteStorageRPs(N) ). %emitted  by datacleaner
dispatch( arrived_RP, arrived_RP(N) ). %command that simulate the arrival of new RP
%====================================================================================
context(ctxscale, "localhost",  "TCP", "8200").
context(ctx_waste_incinerator_service, "192.168.1.110",  "TCP", "8125").
 qactor( wis, ctx_waste_incinerator_service, "external").
  qactor( scaledevice, ctxscale, "it.unibo.scaledevice.Scaledevice").
 static(scaledevice).
  qactor( scaleusage, ctxscale, "it.unibo.scaleusage.Scaleusage").
 static(scaleusage).
