%====================================================================================
% cold_storage_service description   
%====================================================================================
request( store, store(N) ).
reply( storeAccepted, storeAccepted(motivazione) ).  %%for store
reply( storeRefused, storeRefused(motivazione) ).  %%for store
%====================================================================================
context(ctx_cold_storage_service, "localhost",  "TCP", "8125").
 qactor( coldstorageservice, ctx_cold_storage_service, "it.unibo.coldstorageservice.Coldstorageservice").
 static(coldstorageservice).
