%====================================================================================
% scalemock description   
%====================================================================================
event( num_RP, num_RP(N) ). %command that simulates an update in the number of RPs
dispatch( guicmd, guicmd(N) ). %message sent by the GUI containing the number of RPs
%====================================================================================
context(ctxscale, "localhost",  "TCP", "8200").
 qactor( scaledisplay, ctxscale, "it.unibo.scaledisplay.Scaledisplay").
 static(scaledisplay).
  qactor( scale, ctxscale, "it.unibo.scale.Scale").
 static(scale).
