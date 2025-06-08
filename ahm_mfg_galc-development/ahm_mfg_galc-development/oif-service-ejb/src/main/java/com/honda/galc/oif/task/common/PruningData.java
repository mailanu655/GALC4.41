package com.honda.galc.oif.task.common;

import java.util.ArrayList;

public class PruningData implements Comparable
{
    private String prodLotNumber = new String();

    private ArrayList<String> kdLots = new ArrayList<String>();

    private ArrayList<String> prodIds = new ArrayList<String>();

    public PruningData()
    {
        super();
    }

    public PruningData(String prodLotNumber)
    {
        super();
        this.prodLotNumber = prodLotNumber;
    }

    public int compareTo(Object anotherPruningData) throws ClassCastException 
    {
        PruningData other = new PruningData();
        
        if (!(anotherPruningData instanceof PruningData))
            throw new ClassCastException("A PruningData Object was expected.");
        else
            other = (PruningData) anotherPruningData;

        return getProdLotNumber().compareTo((other.getProdLotNumber()));
    }
    
    public String getProdLotNumber()
    {
        return prodLotNumber;
    }

    public void setProdLotNumber(String prodLotNumber)
    {
        this.prodLotNumber = prodLotNumber;
    }

    public ArrayList<String> getProdIds()
    {
        return prodIds;
    }

    public void setProdIds(ArrayList<String> prodIds)
    {
        this.prodIds = prodIds;
    }

    public ArrayList<String> getKdLots()
    {
        return kdLots;
    }

    public void setKdLots(ArrayList<String> kdLots)
    {
        this.kdLots = kdLots;
    }
}
