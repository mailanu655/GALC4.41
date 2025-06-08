package com.honda.galc.client.teamleader.let;

import static com.honda.galc.service.ServiceFactory.getDao;
import java.util.*;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetProgramCategoryDao;
import com.honda.galc.entity.product.LetProgramCategory;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
@SuppressWarnings(value = { "all" })
public class LetProgramCategoryUtility {

	private String applicationId = null;

	public LetProgramCategoryUtility(String applicationId) {
		this.applicationId = applicationId;
	}

	public Vector<Hashtable<String, String>> getColorExplanation(String[] argument)  {

		List<LetProgramCategory> list = new ArrayList<LetProgramCategory>();
		Vector<Hashtable<String, String>> vecColorExps = new Vector<Hashtable<String, String>>();

		try
		{
			if (argument.length<=0) 
			{

				list = getDao(LetProgramCategoryDao.class).getColorExplanation("");

			}  else 
			{
				for(int i =0; i < argument.length; i++)
				{
					if(("00").contains(argument[i]))
					{
						return vecColorExps;
					}
				}


				StringBuffer param = new StringBuffer();


				for (int i = 0; i < argument.length; i++) 
				{

					param.append("'" + argument[i] + "'");

					if (i < argument.length - 1) 
					{
						param.append(",");
					}
				}

				list = getDao(LetProgramCategoryDao.class).getColorExplanation(param.toString());

			}

			int rowCount = list.size();

			if (rowCount == 0) {

				Logger.getLogger(applicationId).error("Let Program Category information not available");
				throw new Exception("Let Program Category information not available");

			}

			Iterator<LetProgramCategory> it = list.iterator();			
			while (it.hasNext()) 
			{
				Hashtable<String, String> colorExplanation = new Hashtable<String, String>();
				LetProgramCategory category = (LetProgramCategory) it.next();
				colorExplanation.put(LETDataTag.PGM_CATEGORY_NAME,(String) category.getPgmCategoryName());
				colorExplanation.put(LETDataTag.BG_COLOR, (String) category.getBgColor());
				vecColorExps.add(colorExplanation);

			}
		} catch (Exception e) {
			Logger.getLogger(applicationId).error(e,"An error occurred while fetching Let Program Category information");
			e.printStackTrace();
		}


		return vecColorExps;
	}
}
