package com.honda.galc.oif.task.common;

import java.util.ArrayList;

public class PruningValues implements Comparable
{
    private String kdLotNumber = new String();

    private ArrayList<String> prodLots = new ArrayList<String>();

    private ArrayList<String> prodIds = new ArrayList<String>();

    public PruningValues()
    {
        super();
    }

    public PruningValues(String kdLotNumber)
    {
        super();
        this.kdLotNumber = kdLotNumber;
    }

    public int compareTo(Object anotherPruningValues) throws ClassCastException 
    {
        PruningValues other = new PruningValues();
        
        if (!(anotherPruningValues instanceof PruningValues))
            throw new ClassCastException("A PruningValues Object was expected.");
        else
            other = (PruningValues) anotherPruningValues;

        return getKdLotNumber().compareTo((other.getKdLotNumber()));
    }
    
    public String getKdLotNumber()
    {
        return kdLotNumber;
    }

    public void setKdLotNumber(String kdLotNumber)
    {
        this.kdLotNumber = kdLotNumber;
    }

    public ArrayList<String> getProdIds()
    {
        return prodIds;
    }

    public void setProdIds(ArrayList<String> prodIds)
    {
        this.prodIds = prodIds;
    }

    public ArrayList<String> getProdLots()
    {
        return prodLots;
    }

    public void setProdLots(ArrayList<String> prodLots)
    {
        this.prodLots = prodLots;
    }
}
