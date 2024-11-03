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
with Diagram('monitoringdevicemockArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxmonitoringdevice', graph_attr=nodeattr):
          monitoringdevicedisplay=Custom('monitoringdevicedisplay','./qakicons/symActorWithobjSmall.png')
          monitoringdevice=Custom('monitoringdevice','./qakicons/symActorWithobjSmall.png')
     monitoringdevicedisplay >> Edge( label='statoAshStorage', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='statoAshStorage', **evattr, decorate='true', fontcolor='darkgreen') >> monitoringdevice
diag
