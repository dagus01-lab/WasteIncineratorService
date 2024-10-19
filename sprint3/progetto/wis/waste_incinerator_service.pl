%====================================================================================
% waste_incinerator_service description   
%====================================================================================
event( num_RP, num_RP(N) ). %event that simulates the arrival of a new RP
dispatch( arrived_RP, arrived_RP(N) ).
event( statoIncinerator, statoIncinerator(N) ). %Incinerator's burning status info: 0 is not burning, 1 otherwise
dispatch( incineratorState, incineratorState(N) ).
dispatch( activationCommand, activationCommand(N) ). %command that turns the incinerator on
dispatch( startBurning, startBurning(N) ). %command that makes the incinerator start burning the next RP
dispatch( rpInBurnin, rpInBurnin(N) ). %command that makes the wis start the incinerator
event( endBurning, endBurning(N) ). %event generated by the incinerator when the burning process is terminated
dispatch( waitingForNewRPs, waitingForNewRPs(N) ). %observable state that indicates whether the WIS is ready to receive and handle new RPs
event( opRobotState, opRobotState(N) ).
event( opRobotJob, opRobotJob(N) ).
event( alarm, alarm(X) ).
dispatch( newAshes, newAshes(N) ). %command that simulate the arrival of new RP's ashes
dispatch( ashesLevel, ashesLevel(N) ). %Event that simulates the level of the ashstorage container
event( statoAshStorage, statoAshStorage(N,D) ). %AshStorage info: 0 is not empty, 1 otherwise
dispatch( monitoringDeviceOff, monitoringDeviceOff(N) ). %message that represents the level of the ash storage (1 if it is up, 0 otherwise)
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
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( wisscaleproxy, ctx_waste_incinerator_service, "it.unibo.wisscaleproxy.Wisscaleproxy").
 static(wisscaleproxy).
  qactor( wismonitoringdeviceproxy, ctx_waste_incinerator_service, "it.unibo.wismonitoringdeviceproxy.Wismonitoringdeviceproxy").
 static(wismonitoringdeviceproxy).
  qactor( raspberryinfocontroller, ctx_waste_incinerator_service, "it.unibo.raspberryinfocontroller.Raspberryinfocontroller").
 static(raspberryinfocontroller).
  qactor( wis, ctx_waste_incinerator_service, "it.unibo.wis.Wis").
 static(wis).
  qactor( oprobot, ctx_waste_incinerator_service, "it.unibo.oprobot.Oprobot").
 static(oprobot).
  qactor( incinerator, ctx_waste_incinerator_service, "it.unibo.incinerator.Incinerator").
 static(incinerator).