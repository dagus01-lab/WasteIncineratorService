%====================================================================================
% waste_incinerator_service description   
%====================================================================================
dispatch( startBurning, startBurning(N) ).
event( endBurning, endBurning(N) ).
dispatch( todocmd, todocmd(N) ).
dispatch( cmd, cmd(MOVE) ). %MOVE = a|d|l|r|h   
request( step, step(TIME) ).
reply( stepdone, stepdone(V) ).  %%for step
reply( stepfailed, stepfailed(DURATION,CAUSE) ).  %%for step
dispatch( updategui, updategui(N) ).
%====================================================================================
context(ctx_waste_incinerator_service, "localhost",  "TCP", "8125").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8120").
context(ctxrasp, "127.0.0.2",  "TCP", "8122").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( wis, ctx_waste_incinerator_service, "it.unibo.wis.Wis").
 static(wis).
  qactor( op_robot, ctx_waste_incinerator_service, "it.unibo.op_robot.Op_robot").
 static(op_robot).
  qactor( incinerator, ctx_waste_incinerator_service, "it.unibo.incinerator.Incinerator").
 static(incinerator).
  qactor( waste_storage, ctx_waste_incinerator_service, "it.unibo.waste_storage.Waste_storage").
 static(waste_storage).
  qactor( ash_storage, ctx_waste_incinerator_service, "it.unibo.ash_storage.Ash_storage").
 static(ash_storage).
  qactor( monitoring_device, ctxrasp, "it.unibo.monitoring_device.Monitoring_device").
 static(monitoring_device).
  qactor( service_status_gui, ctx_waste_incinerator_service, "it.unibo.service_status_gui.Service_status_gui").
 static(service_status_gui).
