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
     sys >> Edge( label='endBurning', **evattr, decorate='true', fontcolor='darkgreen') >> wis
     sys >> Edge( label='endBurning', **evattr, decorate='true', fontcolor='darkgreen') >> oprobot
     incinerator >> Edge( label='endBurning', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     oprobot >> Edge(color='blue', style='solid',  decorate='true', label='<rpInBurnin &nbsp; >',  fontcolor='blue') >> wis
     scalemock >> Edge(color='blue', style='solid',  decorate='true', label='<arrived_RP &nbsp; >',  fontcolor='blue') >> wis
     wis >> Edge(color='blue', style='solid',  decorate='true', label='<activationCommand &nbsp; startBurning &nbsp; >',  fontcolor='blue') >> incinerator
     wis >> Edge(color='blue', style='solid',  decorate='true', label='<arrived_RP &nbsp; >',  fontcolor='blue') >> oprobot
diag
