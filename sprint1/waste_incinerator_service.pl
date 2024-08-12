%====================================================================================
% waste_incinerator_service description   
%====================================================================================
dispatch( arrived_RP, arrived_RP(N) ). %command that simulate the arrival of new RP
dispatch( statoIncinerator, statoIncinerator(N) ). %Incinerator's burning status info: 0 is not burning, 1 otherwise
dispatch( activationCommand, activationCommand(N) ). %command that turns the incinerator on
dispatch( startBurning, startBurning(N) ). %command that makes the incinerator start burning the next RP
dispatch( rpInBurnin, rpInBurnin(N) ). %command that makes the wis start the incinerator
event( endBurning, endBurning(N) ). %event generated by the incinerator when the burning process is terminated
dispatch( newAshes, newAshes(N) ). %command that simulate the arrival of new RP's ashes
dispatch( statoAshStorage, statoAshStorage(N) ). %AshStorage info: 0 is not empty, 1 otherwise
request( engage, engage(OWNER,STEPTIME) ).
reply( engagedone, engagedone(ARG) ).  %%for engage
reply( engagerefused, engagerefused(ARG) ).  %%for engage
dispatch( disengage, disengage(ARG) ).
request( doplan, doplan(PATH,STEPTIME) ).
reply( doplandone, doplandone(ARG) ).  %%for doplan
reply( doplanfailed, doplanfailed(ARG) ).  %%for doplan
dispatch( cmd, cmd(MOVE) ). %MOVE = a|d|l|r|h   
dispatch( end, end(ARG) ).
request( step, step(TIME) ).
reply( stepdone, stepdone(V) ).  %%for step
reply( stepfailed, stepfailed(DURATION,CAUSE) ).  %%for step
%====================================================================================
context(ctx_waste_incinerator_service, "localhost",  "TCP", "8125").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( wis, ctx_waste_incinerator_service, "it.unibo.wis.Wis").
 static(wis).
  qactor( oprobot, ctx_waste_incinerator_service, "it.unibo.oprobot.Oprobot").
 static(oprobot).
  qactor( incinerator, ctx_waste_incinerator_service, "it.unibo.incinerator.Incinerator").
 static(incinerator).
  qactor( scalemock, ctx_waste_incinerator_service, "it.unibo.scalemock.Scalemock").
 static(scalemock).
  qactor( monitoring_device_mok, ctx_waste_incinerator_service, "it.unibo.monitoring_device_mok.Monitoring_device_mok").
 static(monitoring_device_mok).