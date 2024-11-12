# Waste Incinerator Service
Project work for the final examination of Software Systems Engineering M at the University of Bologna.
The project has been developed using the SCRUM agile method.

## Intro
The subject is a Waste Incinerator Service: A robot is placed in it, it takes waste packets, puts them
into an incinerator, waits until the burn is finished, and then places the ashes in an ash storage.
Here is an illustration:
![WIS map](sprint0/userDocs/_images/TF24Annotated.PNG)

## QAK
The main language used for this project is QAK (Quasi Actor Kotlin), a Domain Specific Language that has
allowed us to think in terms of actors and messages, thereby avoiding many low-level details.
Moreover, it has enabled us to produce prototypes that can be executed immediately, making it a very
effective choice considering the method we have adopted.
You can find some useful documentation on this language  [here](sprint0/userDocs/qak_documentation.pdf).

## Roadmap
1. **Sprint 0**: Requirements analysis. Latest Release: [sprint0.qak](sprint0/src/sprint0.qak).
2. **Sprint 1**: Waste Incinerator Service. Latest Release: [sprint1.qak](sprint1/src/sprint1.qak).
3. **Sprint 2**: Scale and MonitoringDevice. Latest Release: [sprint2](sprint2).
4. **Sprint 3**: WISFacade and RaspberryMock. Latest Release: [sprint3](sprint3).
5. **Sprint 4**: Physical Robot. Latest Release: [sprint4](sprint4).
