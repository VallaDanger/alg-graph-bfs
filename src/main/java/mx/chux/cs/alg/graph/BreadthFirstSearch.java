package mx.chux.cs.alg.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiFunction;

import mx.chux.cs.ds.graph.Graph;
import mx.chux.cs.ds.graph.Node;

public class BreadthFirstSearch<T extends Comparable<T>> implements BiFunction<T, T, Collection<T>> {

    final Graph<T> graph;
    
    public BreadthFirstSearch(final Graph<T> graph ) {
        this.graph = graph;
    }
    
    @Override
    public Collection<T> apply(T first, T last) {

        final Node<T> firstNode = this.graph.getNode(first);
        final Node<T> lastNode = this.graph.getNode(last);
        
        if( (firstNode == null) || (lastNode == null) ) {
            return Collections.emptyList();
        }
        
        if( firstNode.equals(lastNode) ) {
            return Collections.singleton(first);
        }
        
        final Collection<Node<T>> path = bfs(firstNode, lastNode);
        
        resetVisitedNodes(this.graph.getNodes());
        
        return extractPath(path);
    }
    
    private Collection<Node<T>> bfs(Node<T> first, Node<T> last) {
        
        final Queue<Node<T>> q = new LinkedList<>();
        final Map<Node<T>, Node<T>> parents = new HashMap<>(this.graph.sizeOfNodes());
        
        final T value = last.get();
        
        q.add(first);
        
        while( !q.isEmpty() ) {
            
            final Node<T> node = q.remove();
            
            if( node.hasBeenVisited() ) {
                continue;
            }

            node.setVisited(true);
            
            if( node.get().equals(value) ) {
                return backtrace(parents, first, last);
            }
            
            final Collection<Node<T>> adjecents = this.graph.getAdjacentNodes(node);
            
            for( Node<T> n : adjecents ) {
                parents.putIfAbsent(n, node);
                if( !q.contains(n) ) {
                    q.add(n);
                }
                
            }
            
        }
        
        return Collections.emptyList();
    }
    
    private Collection<Node<T>> backtrace(Map<Node<T>, Node<T>> parents, 
                                            Node<T> first, Node<T> last) {
        
        final List<Node<T>> path = new ArrayList<>(parents.size());
        
        path.add(last);
        
        Node<T> parent = parents.get(last);
        
        while( !parent.equals(first) ) {
            path.add(parent);
            parent = parents.get(parent);
        }
         
        path.add(parent);
        
        Collections.reverse(path);
        
        return path;
    }
    
    private Collection<T> extractPath(Collection<Node<T>> path) {
        final List<T> extractedPath = new ArrayList<>(path.size());
        for( Node<T> node : path ) {
            extractedPath.add(node.get());
        }
        return extractedPath;
    }
    
    private void resetVisitedNodes(Collection<Node<T>> nodes) {
        for( Node<T> node : nodes ) {
            if( node.hasBeenVisited() ) {
                node.setVisited(false);
            }
        }
    }

}
