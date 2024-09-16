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
with Diagram('monitoringdeviceArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxmonitoringdevice', graph_attr=nodeattr):
          sonardevice=Custom('sonardevice','./qakicons/symActorSmall.png')
          datacleaner=Custom('datacleaner','./qakicons/symActorSmall.png')
          monitoringdevice=Custom('monitoringdevice','./qakicons/symActorSmall.png')
          led=Custom('led','./qakicons/symActorSmall.png')
     with Cluster('ctx_waste_incinerator_service', graph_attr=nodeattr):
          incinerator=Custom('incinerator(ext)','./qakicons/externalQActor.png')
          wis=Custom('wis(ext)','./qakicons/externalQActor.png')
     sonardevice >> Edge( label='sonardata', **eventedgeattr, decorate='true', fontcolor='red') >> datacleaner
     datacleaner >> Edge( label='ashStorageLevel', **eventedgeattr, decorate='true', fontcolor='red') >> monitoringdevice
     sys >> Edge( label='statoIncinerator', **evattr, decorate='true', fontcolor='darkgreen') >> monitoringdevice
     monitoringdevice >> Edge(color='blue', style='solid',  decorate='true', label='<led_on &nbsp; led_off &nbsp; led_blink &nbsp; >',  fontcolor='blue') >> led
diag
