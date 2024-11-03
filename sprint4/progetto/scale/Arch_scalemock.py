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
with Diagram('scalemockArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxscale', graph_attr=nodeattr):
          scaledisplay=Custom('scaledisplay','./qakicons/symActorWithobjSmall.png')
          scale=Custom('scale','./qakicons/symActorWithobjSmall.png')
     scaledisplay >> Edge( label='num_RP', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='num_RP', **evattr, decorate='true', fontcolor='darkgreen') >> scale
diag
