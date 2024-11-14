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
          wisscaleproxy=Custom('wisscaleproxy','./qakicons/symActorWithobjSmall.png')
          wismonitoringdeviceproxy=Custom('wismonitoringdeviceproxy','./qakicons/symActorWithobjSmall.png')
          raspberryinfocontroller=Custom('raspberryinfocontroller','./qakicons/symActorSmall.png')
          wis=Custom('wis','./qakicons/symActorSmall.png')
          oprobot=Custom('oprobot','./qakicons/symActorWithobjSmall.png')
          incinerator=Custom('incinerator','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     sys >> Edge( label='num_RP', **evattr, decorate='true', fontcolor='darkgreen') >> wisscaleproxy
     sys >> Edge( label='statoAshStorage', **evattr, decorate='true', fontcolor='darkgreen') >> wismonitoringdeviceproxy
     sys >> Edge( label='endBurning', **evattr, decorate='true', fontcolor='darkgreen') >> wis
     sys >> Edge( label='endBurning', **evattr, decorate='true', fontcolor='darkgreen') >> oprobot
     oprobot >> Edge( label='alarm', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     incinerator >> Edge( label='endBurning', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     oprobot >> Edge(color='magenta', style='solid', decorate='true', label='<engage<font color="darkgreen"> engagedone engagerefused</font> &nbsp; moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
     raspberryinfocontroller >> Edge(color='blue', style='solid',  decorate='true', label='<ashesLevel &nbsp; arrived_RP &nbsp; >',  fontcolor='blue') >> wis
     oprobot >> Edge(color='blue', style='solid',  decorate='true', label='<rpInBurnin &nbsp; newAshes &nbsp; >',  fontcolor='blue') >> wis
     wis >> Edge(color='blue', style='solid',  decorate='true', label='<activationCommand &nbsp; startBurning &nbsp; >',  fontcolor='blue') >> incinerator
     wisscaleproxy >> Edge(color='blue', style='solid',  decorate='true', label='<arrived_RP &nbsp; >',  fontcolor='blue') >> raspberryinfocontroller
     wis >> Edge(color='blue', style='solid',  decorate='true', label='<arrived_RP &nbsp; >',  fontcolor='blue') >> oprobot
     wis >> Edge(color='blue', style='solid',  decorate='true', label='<waitingForUpdates &nbsp; >',  fontcolor='blue') >> raspberryinfocontroller
     wismonitoringdeviceproxy >> Edge(color='blue', style='solid',  decorate='true', label='<ashesLevel &nbsp; monitoringDeviceOff &nbsp; >',  fontcolor='blue') >> raspberryinfocontroller
diag
