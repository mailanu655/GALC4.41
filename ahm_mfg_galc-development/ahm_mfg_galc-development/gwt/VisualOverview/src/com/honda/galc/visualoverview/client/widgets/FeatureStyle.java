package com.honda.galc.visualoverview.client.widgets;

import java.util.List;

import org.gwtopenmaps.openlayers.client.Style;

import com.honda.galc.visualoverview.shared.Feature;
import com.honda.galc.visualoverview.shared.PrintAttributeFormat;
import com.honda.galc.visualoverview.shared.feature.AreaFeature;
import com.honda.galc.visualoverview.shared.feature.DefectFeature;
import com.honda.galc.visualoverview.shared.feature.ImageFeature;
import com.honda.galc.visualoverview.shared.feature.LabelFeature;
import com.honda.galc.visualoverview.shared.feature.LineFeature;
import com.honda.galc.visualoverview.shared.feature.PointFeature;
import com.google.gwt.user.client.Window;

public class FeatureStyle extends Style {
		
	public FeatureStyle(Feature feature, final List<PrintAttributeFormat> attributes)
	{
		if(feature instanceof LabelFeature)
		{                                               
			setStrokeWidth(0);
			setPointRadius(0);
			setFillOpacity(0);
			setLabelSelect(true);
			setLabel(feature.getReferenceId());
			for(PrintAttributeFormat attribute : attributes)
			{
				if(attribute.getId().getAttribute().trim() == "LABEL_TEXT")
					setLabel(attribute.getAttributeValue());
				if(attribute.getId().getAttribute().trim() == "LABEL_ALIGN")
					setLabelAlign(attribute.getAttributeValue());
				if(attribute.getId().getAttribute().trim() == "FONT_COLOR")
					setFontColor(attribute.getAttributeValue());
				if(attribute.getId().getAttribute().trim() == "FONT_SIZE")
					setFontSize(attribute.getAttributeValue());
				if(attribute.getId().getAttribute().trim() == "FONT_WEIGHT")
					setFontWeight(attribute.getAttributeValue());
				if(attribute.getId().getAttribute().trim() == "FONT_FAMILY")
					setFontFamily(attribute.getAttributeValue());
			}
		}
		else if (feature instanceof PointFeature)
		{
			setLabelSelect(true);
			setLabel(feature.getReferenceId());
			setStrokeWidth(2);
			setPointRadius(2);
			setFillOpacity(1);
			for(PrintAttributeFormat attribute : attributes)
			{
				if(attribute.getId().getAttribute().trim() == "LABEL_TEXT")
					setLabel(attribute.getAttributeValue());
				if(attribute.getId().getAttribute().trim() == "LABEL_ALIGN")
					setLabelAlign(attribute.getAttributeValue());
				if(attribute.getId().getAttribute().trim() == "STROKE_COLOR")
					setStrokeColor(attribute.getAttributeValue());
				if(attribute.getId().getAttribute().trim() == "FILL_COLOR")
					setFillColor(attribute.getAttributeValue());
				if(attribute.getId().getAttribute().trim() == "STROKE_WIDTH")
					setStrokeWidth(Double.parseDouble(attribute.getAttributeValue()));
				if(attribute.getId().getAttribute().trim() == "POINT_RADIUS")
					setPointRadius(Double.parseDouble(attribute.getAttributeValue()));
				if(attribute.getId().getAttribute().trim() == "FILL_OPACITY")
					setFillOpacity(Double.parseDouble(attribute.getAttributeValue()));
			}
		}
		else if (feature instanceof LineFeature)
		{
			for(PrintAttributeFormat attribute : attributes)
			{
				if(attribute.getId().getAttribute().trim() == "STROKE_COLOR")
					setStrokeColor(attribute.getAttributeValue());
				if(attribute.getId().getAttribute().trim() == "STROKE_WIDTH")
					setStrokeWidth(Double.parseDouble(attribute.getAttributeValue()));
				if(attribute.getId().getAttribute().trim() == "POINT_RADIUS")
					setPointRadius(Double.parseDouble(attribute.getAttributeValue()));
			}
		}
		else if (feature instanceof AreaFeature)
		{
			setLabelSelect(true);
			setLabel(feature.getReferenceId());
			for(PrintAttributeFormat attribute : attributes)
			{
				if(attribute.getId().getAttribute().trim() == "STROKE_COLOR")
					setStrokeColor(attribute.getAttributeValue());
				if(attribute.getId().getAttribute().trim() == "STROKE_WIDTH")
					setStrokeWidth(Double.parseDouble(attribute.getAttributeValue()));
				if(attribute.getId().getAttribute().trim() == "POINT_RADIUS")
					setPointRadius(Double.parseDouble(attribute.getAttributeValue()));
			}	
		}
		else if (feature instanceof DefectFeature)
		{
			setLabelSelect(true);
			setLabel(feature.getReferenceId());
			setStrokeWidth(2);
			setPointRadius(2);
			setFillOpacity(1);
			
			for(PrintAttributeFormat attribute : attributes)
			{
				if(attribute.getId().getAttribute().trim() == "STROKE_COLOR")
					setStrokeColor(attribute.getAttributeValue());
				if(attribute.getId().getAttribute().trim() == "FILL_COLOR")
					setFillColor(attribute.getAttributeValue());
				if(attribute.getId().getAttribute().trim() == "STROKE_WIDTH")
					setStrokeWidth(Double.parseDouble(attribute.getAttributeValue()));
				if(attribute.getId().getAttribute().trim() == "POINT_RADIUS")
					setPointRadius(Double.parseDouble(attribute.getAttributeValue()));
				if(attribute.getId().getAttribute().trim() == "FILL_OPACITY")
					setFillOpacity(Double.parseDouble(attribute.getAttributeValue()));
			}
		}
		else if (feature instanceof ImageFeature)
		{


		}
	}
	
}
