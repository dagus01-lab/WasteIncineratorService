%====================================================================================
% waste_incinerator_service description   
%====================================================================================
dispatch( arrived_RP, arrived_RP(N) ). %command that simulate the arrival of new RP
dispatch( statoIncinerator, statoIncinerator(N) ). %Incinerator's burning status info: 0 is not burning, 1 otherwise
dispatch( activationCommand, activationCommand(N) ). %command that turns the incinerator on
dispatch( startBurning, startBurning(N) ). %command that makes the incinerator start burning the next RP
dispatch( rpInBurnin, rpInBurnin(N) ). %command that makes the wis start the incinerator
event( endBurning, endBurning(N) ). %event generated by the incinerator when the burning process is terminated
event( alarm, alarm(X) ).
dispatch( newAshes, newAshes(N) ). %command that simulate the arrival of new RP's ashes
dispatch( statoAshStorage, statoAshStorage(SENDER,N) ). %AshStorage info: 0 is not empty, 1 otherwise
request( engage, engage(OWNER,STEPTIME) ).
reply( engagedone, engagedone(ARG) ).  %%for engage
reply( engagerefused, engagerefused(ARG) ).  %%for engage
dispatch( disengage, disengage(ARG) ).
request( moverobot, moverobot(TARGETX,TARGETY) ).
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
%====================================================================================
context(ctx_waste_incinerator_service, "localhost",  "TCP", "8125").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxmonitoringdevice, "192.168.114.105",  "TCP", "8100").
context(ctxscale, "169.254.12.90",  "TCP", "8200").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( monitoringdevice, ctxmonitoringdevice, "external").
  qactor( scale, ctxscale, "external").
  qactor( wis, ctx_waste_incinerator_service, "it.unibo.wis.Wis").
 static(wis).
  qactor( oprobot, ctx_waste_incinerator_service, "it.unibo.oprobot.Oprobot").
 static(oprobot).
  qactor( incinerator, ctx_waste_incinerator_service, "it.unibo.incinerator.Incinerator").
 static(incinerator).
