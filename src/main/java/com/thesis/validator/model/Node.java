package com.thesis.validator.model;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private String name;
    private List<Node> neighbourList;
    private boolean visited;
    private int lowLink;

    public Node(String name) {
        this.name = name;
        this.neighbourList = new ArrayList<>();
    }

    public void addNeighbour(Node node) {
        this.neighbourList.add(node);
    }

    public int getLowLink() { return lowLink; }
    public void setLowLink(int lowLink){ this.lowLink = lowLink; }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Node> getNeighbourList() { return neighbourList; }

    public boolean isVisited() { return this.visited; }
    public void setVisited(boolean visited) { this.visited = visited; }

    @Override
    public String toString(){ return this.name; }
}
