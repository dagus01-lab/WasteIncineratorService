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
          raspberryinfocontroller=Custom('raspberryinfocontroller','./qakicons/symActorWithobjSmall.png')
          wis=Custom('wis','./qakicons/symActorSmall.png')
          oprobot=Custom('oprobot','./qakicons/symActorWithobjSmall.png')
          incinerator=Custom('incinerator','./qakicons/symActorWithobjSmall.png')
     sys >> Edge( label='endBurning', **evattr, decorate='true', fontcolor='darkgreen') >> wis
     sys >> Edge( label='endBurning', **evattr, decorate='true', fontcolor='darkgreen') >> oprobot
     oprobot >> Edge( label='alarm', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     incinerator >> Edge( label='endBurning', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     raspberryinfocontroller >> Edge(color='blue', style='solid',  decorate='true', label='<ashesLevel &nbsp; arrived_RP &nbsp; >',  fontcolor='blue') >> wis
     oprobot >> Edge(color='blue', style='solid',  decorate='true', label='<rpInBurnin &nbsp; newAshes &nbsp; >',  fontcolor='blue') >> wis
     wis >> Edge(color='blue', style='solid',  decorate='true', label='<activationCommand &nbsp; startBurning &nbsp; >',  fontcolor='blue') >> incinerator
     wis >> Edge(color='blue', style='solid',  decorate='true', label='<arrived_RP &nbsp; >',  fontcolor='blue') >> oprobot
     wis >> Edge(color='blue', style='solid',  decorate='true', label='<waitingForUpdates &nbsp; >',  fontcolor='blue') >> raspberryinfocontroller
diag
