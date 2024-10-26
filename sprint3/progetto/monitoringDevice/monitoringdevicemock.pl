%====================================================================================
% monitoringdevicemock description   
%====================================================================================
event( statoAshStorage, statoAshStorage(N) ). %Event that simulate the status of the ash Storage (0=normal, 1=full)
dispatch( guicmd, guicmd(N) ).
%====================================================================================
context(ctxmonitoringdevice, "localhost",  "TCP", "8200").
 qactor( monitoringdevicedisplay, ctxmonitoringdevice, "it.unibo.monitoringdevicedisplay.Monitoringdevicedisplay").
 static(monitoringdevicedisplay).
  qactor( monitoringdevice, ctxmonitoringdevice, "it.unibo.monitoringdevice.Monitoringdevice").
 static(monitoringdevice).
