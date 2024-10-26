### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
evattr = {
    'color': 'darkgreen',
    'style': 'dotted'
}
with Diagram('raspberrymockArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxRaspberryMock', graph_attr=nodeattr):
          raspberrymockproxy=Custom('raspberrymockproxy','./qakicons/symActorSmall.png')
          monitoringdevice=Custom('monitoringdevice','./qakicons/symActorWithobjSmall.png')
          scale=Custom('scale','./qakicons/symActorWithobjSmall.png')
     raspberryMockFacade=Custom('raspberryMockFacade','./qakicons/server.png')
     sys >> Edge( label='statoAshStorage', **evattr, decorate='true', fontcolor='darkgreen') >> monitoringdevice
     sys >> Edge( label='num_RP', **evattr, decorate='true', fontcolor='darkgreen') >> scale
     raspberryMockFacade >> Edge(color='blue', style='solid', decorate='true', label='< &harr; >',  fontcolor='blue') >> raspberrymockproxy
diag
