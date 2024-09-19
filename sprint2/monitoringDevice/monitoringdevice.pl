%====================================================================================
% monitoringdevice description   
%====================================================================================
event( sonardata, distance(D) ). %emitted  by sonardevice or (better) by datacleaner
event( ashStorageLevel, ashStorageLevel(D) ). %emitted  by datacleaner
dispatch( statoAshStorage, statoAshStorage(N) ). %AshStorage info: 0 is not empty, 1 otherwise
dispatch( led_on, led_on(N) ). %messaggio di accensione del led
dispatch( led_blink, led_blink(N) ). %messaggio di lampeggio del led
dispatch( led_off, led_off(N) ). %messaggio di spegnimento del led
dispatch( statoIncinerator, statoIncinerator(SENDER,N) ). %Incinerator's burning status info: 0 is not burning, 1 otherwise
%====================================================================================
context(ctxmonitoringdevice, "localhost",  "TCP", "8100").
context(ctx_waste_incinerator_service, "192.168.114.28",  "TCP", "8125").
 qactor( incinerator, ctx_waste_incinerator_service, "external").
  qactor( wis, ctx_waste_incinerator_service, "external").
  qactor( sonardevice, ctxmonitoringdevice, "it.unibo.sonardevice.Sonardevice").
 static(sonardevice).
  qactor( datacleaner, ctxmonitoringdevice, "it.unibo.datacleaner.Datacleaner").
 static(datacleaner).
  qactor( monitoringdevice, ctxmonitoringdevice, "it.unibo.monitoringdevice.Monitoringdevice").
 static(monitoringdevice).
  qactor( led, ctxmonitoringdevice, "it.unibo.led.Led").
 static(led).
