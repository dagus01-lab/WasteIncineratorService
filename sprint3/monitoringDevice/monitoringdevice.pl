%====================================================================================
% monitoringdevice description   
%====================================================================================
event( sonardata, distance(D) ). %emitted  by sonardevice or (better) by datacleaner
event( ashStorageLevel, ashStorageLevel(L,D) ). %emitted  by datacleaner. Contains level and distance
event( statoAshStorage, statoAshStorage(N,D) ). %AshStorage info: 0 is not empty, 1 otherwise
dispatch( led_on, led_on(N) ). %messaggio di accensione del led
dispatch( led_blink, led_blink(N) ). %messaggio di lampeggio del led
dispatch( led_off, led_off(N) ). %messaggio di spegnimento del led
event( statoIncinerator, statoIncinerator(N) ). %Incinerator's burning status info: 0 is not burning, 1 otherwise
dispatch( incineratorState, incineratorState(N) ).
%====================================================================================
context(ctxmonitoringdevice, "localhost",  "TCP", "8100").
 qactor( monitoringdeviceproxy, ctxmonitoringdevice, "it.unibo.monitoringdeviceproxy.Monitoringdeviceproxy").
 static(monitoringdeviceproxy).
  qactor( sonardevice, ctxmonitoringdevice, "it.unibo.sonardevice.Sonardevice").
 static(sonardevice).
  qactor( datacleaner, ctxmonitoringdevice, "it.unibo.datacleaner.Datacleaner").
 static(datacleaner).
  qactor( monitoringdevice, ctxmonitoringdevice, "it.unibo.monitoringdevice.Monitoringdevice").
 static(monitoringdevice).
  qactor( led, ctxmonitoringdevice, "it.unibo.led.Led").
 static(led).
