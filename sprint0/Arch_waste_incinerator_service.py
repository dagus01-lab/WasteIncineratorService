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
with Diagram('waste_incinerator_serviceArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctx_waste_incinerator_service', graph_attr=nodeattr):
          op_robot=Custom('op_robot','./qakicons/symActorSmall.png')
          incinerator=Custom('incinerator','./qakicons/symActorSmall.png')
          waste_storage=Custom('waste_storage','./qakicons/symActorSmall.png')
          ash_storage=Custom('ash_storage','./qakicons/symActorSmall.png')
          service_status_gui=Custom('service_status_gui','./qakicons/symActorSmall.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxrasp', graph_attr=nodeattr):
          monitoring_device=Custom('monitoring_device','./qakicons/symActorSmall.png')
     incinerator >> Edge( label='endBurning', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     op_robot >> Edge(color='magenta', style='solid', decorate='true', label='<step<font color="darkgreen"> stepdone stepfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
     op_robot >> Edge(color='blue', style='solid',  decorate='true', label='<updategui &nbsp; >',  fontcolor='blue') >> service_status_gui
     op_robot >> Edge(color='blue', style='solid',  decorate='true', label='<startBurning &nbsp; >',  fontcolor='blue') >> incinerator
     op_robot >> Edge(color='blue', style='solid',  decorate='true', label='<cmd &nbsp; >',  fontcolor='blue') >> basicrobot
     waste_storage >> Edge(color='blue', style='solid',  decorate='true', label='<updategui &nbsp; >',  fontcolor='blue') >> service_status_gui
     ash_storage >> Edge(color='blue', style='solid',  decorate='true', label='<updategui &nbsp; >',  fontcolor='blue') >> service_status_gui
     incinerator >> Edge(color='blue', style='solid',  decorate='true', label='<updategui &nbsp; >',  fontcolor='blue') >> service_status_gui
diag
