package com.honda.galc.locationTracking.client;

public class MapKey implements Comparable<MapKey> {

	int x;
	int y;
	int levelOfDetail;
	String image;
	String imageID;
	
	public MapKey(int paramX, int paramY, int paramLevelOfDetail)
	{
		this.x = paramX;
		this.y = paramY;
		this.levelOfDetail = paramLevelOfDetail;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getLevelOfDetail() {
		return levelOfDetail;
	}
	
	public String getImageString() {
		return image;
	}
	
	public void setImageString(String value)
	{
		image = value;
	}
	
	public String getImageID() {
		return imageID;
	}
	
	public void setImageID(String value)
	{
		imageID = value;
	}
	
	public int compareTo(MapKey compareKey)
	{
		return levelOfDetail < compareKey.levelOfDetail?-1: levelOfDetail>compareKey.levelOfDetail?1:doSecondaryOrderSort(compareKey);
	}
	public int doSecondaryOrderSort(MapKey compareKey)
	{
		return y < compareKey.getY()?-1:y > compareKey.getY()?1:doTertiaryOrderSort(compareKey);
	}
	public int doTertiaryOrderSort(MapKey compareKey)
	{
		return x < compareKey.getX()?-1:x > compareKey.getX()?1:0;
	}
}
