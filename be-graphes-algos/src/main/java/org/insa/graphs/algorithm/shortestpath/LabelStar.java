package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
import org.insa.graphs.algorithm.AbstractInputData.Mode;

public class LabelStar extends Label {

    private double costADestin;

    public LabelStar(Node node, Node destin, Mode mode, int maxSpeed){
        super(node);
        if(mode==Mode.LENGTH){
            costADestin = destin.getPoint().distanceTo(super.getNode().getPoint());
        } else if(mode==Mode.TIME){
            costADestin = destin.getPoint().distanceTo(super.getNode().getPoint())*3.6/maxSpeed;
        }
    }

    public double getTotalCost(){
        return (super.getCost()+this.costADestin);
    }

}