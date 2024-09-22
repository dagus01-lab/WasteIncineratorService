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
          wis=Custom('wis','./qakicons/symActorSmall.png')
          oprobot=Custom('oprobot','./qakicons/symActorSmall.png')
          incinerator=Custom('incinerator','./qakicons/symActorSmall.png')
          scalemock=Custom('scalemock','./qakicons/symActorSmall.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxmonitoringdevice', graph_attr=nodeattr):
          monitoringdevice=Custom('monitoringdevice(ext)','./qakicons/externalQActor.png')
     sys >> Edge( label='endBurning', **evattr, decorate='true', fontcolor='darkgreen') >> wis
     sys >> Edge( label='endBurning', **evattr, decorate='true', fontcolor='darkgreen') >> oprobot
     oprobot >> Edge( label='alarm', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     incinerator >> Edge( label='endBurning', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     oprobot >> Edge(color='magenta', style='solid', decorate='true', label='<engage<font color="darkgreen"> engagedone engagerefused</font> &nbsp; moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
     oprobot >> Edge(color='blue', style='solid',  decorate='true', label='<rpInBurnin &nbsp; newAshes &nbsp; >',  fontcolor='blue') >> wis
     scalemock >> Edge(color='blue', style='solid',  decorate='true', label='<arrived_RP &nbsp; >',  fontcolor='blue') >> wis
     wis >> Edge(color='blue', style='solid',  decorate='true', label='<activationCommand &nbsp; startBurning &nbsp; >',  fontcolor='blue') >> incinerator
     wis >> Edge(color='blue', style='solid',  decorate='true', label='<arrived_RP &nbsp; >',  fontcolor='blue') >> oprobot
     monitoringdevice >> Edge(color='blue', style='solid',  decorate='true', label='<statoAshStorage &nbsp; >',  fontcolor='blue') >> wis
diag
