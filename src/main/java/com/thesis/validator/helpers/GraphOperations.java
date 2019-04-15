package com.thesis.validator.helpers;

import com.thesis.validator.enums.Direction;
import com.thesis.validator.model.Node;
import com.thesis.validator.model.Relation;
import com.thesis.validator.model.Service;

import java.util.ArrayList;
import java.util.Stack;
import java.util.List;

public class GraphOperations {

    private Stack<Node> stack;
    private List<Node> nodes;
    private List<List<Node>> connectedComponentList;
    private int time = 0; // used for ordering the traversal

    private GraphOperations(List<Node> nodes) {
        this.stack = new Stack<>();
        this.nodes = nodes;
        this.connectedComponentList = new ArrayList<>();
    }

    private void runAlgorithm() {
        for(Node node : this.nodes)
            if(!node.isVisited())
                dfs(node);
    }

    private void dfs(Node node) {
        node.setLowLink(time++);
        node.setVisited(true);
        this.stack.push(node);
        boolean isComponentRoot = true;

        for(Node v : node.getNeighbourList()) {
            if(!v.isVisited()) {
                dfs(v);
            }
            // there is an edge back
            if(node.getLowLink() > v.getLowLink()) {
                node.setLowLink(v.getLowLink());
                isComponentRoot = false;
            }
        }
        // root has special treatment
        if(isComponentRoot) {
            List<Node> component = new ArrayList<>();
            while(true) {
                Node actualNode = stack.pop();
                component.add(actualNode);
                actualNode.setLowLink(Integer.MAX_VALUE);

                // iterate until root is reached
                if(actualNode.getName().equals(node.getName())) {
                    break;
                }
            }
            connectedComponentList.add(component);
        }
    }

    private static Node getVertexWithName(List<Node> vertices, String name){
        for(Node node : vertices){
            if(node.getName().equals(name)){
                return node;
            }
        }

        return null;
    }

    private List<List<Node>> getComponents(){
        return connectedComponentList;
    }

    public static List<List<Node>> searchStronglyConnectedComponents(List<Service> services, List<Relation> relations) {
        List<Node> vertices = new ArrayList<>(services.size());
        Node node1, node2;

        for(Service service:services){
            Node node = new Node(service.name);
            vertices.add(node);
        }

        for(Relation relation:relations){
            if(relation.direction.equals(Direction.OUTGOING)){
                node1 = getVertexWithName(vertices,relation.serviceA);
                node2 = getVertexWithName(vertices,relation.serviceB);
            } else {
                node1 = getVertexWithName(vertices,relation.serviceB);
                node2 = getVertexWithName(vertices,relation.serviceA);
            }
            assert node1 != null;
            node1.addNeighbour(node2);
        }

        GraphOperations graphOperations = new GraphOperations(vertices);
        graphOperations.runAlgorithm();

        return graphOperations.getComponents();
    }
}
