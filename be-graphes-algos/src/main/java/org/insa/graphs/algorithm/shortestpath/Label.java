package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;

public class Label implements Comparable<Label>{
    
    private Node currentNode;

    private boolean mark = false;

    private double currentCost = Double.POSITIVE_INFINITY;

    private Arc father = null;

    public Label(Node node){
        this.currentNode = node;
    }

    public Node getNode(){
        return this.currentNode;
    }

    public boolean getMark(){
        return this.mark;
    }

    public double getCost(){
        return this.currentCost;
    }

    public Arc getFather(){
        return this.father;
    }

    public void marked(){
        this.mark = true;
    }

    public void setCost(double cout){
        this.currentCost = cout;
    }

    public void setFather(Arc pere){
        this.father = pere;
    }

    public int compareTo(Label label){
        if(Math.abs(this.getCost() - label.getCost()) < (double)0.000001){
            return 0;
        } else if(this.getCost() < label.getCost()){
            return -1;
        } else {
            return 1;
        }
    }

}
