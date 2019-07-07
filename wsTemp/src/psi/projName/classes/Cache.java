package psi.projName.classes;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.json.JSONArray;
import org.json.JSONObject;

import psi.projName.classes.utils.*;

public class Cache {	

	public static final String PRE_MONGO_DATA = "MD_";

	// 1. SAVE
	public static void addToCache( ServletContext servletContext, JSONObject jsonData )
	{
		String currentDt = DateUtil.getUTCdatetimeAsString();

		JSONObject jsonSummary = new JSONObject();
		jsonSummary.put( "requestTime", currentDt );
		jsonSummary.put( "jsonData", jsonData );

		// Create date time by ....
		String uniqueNumStr = PRE_MONGO_DATA + DateUtil.getDateStrByFormatStr( "yyMMddHHmmssSSS" ) + "_" + Util.generateRandomNumbers( 4 );		
		//Util.outputDebug( "uniqueNumStr: " + uniqueNumStr );

		servletContext.setAttribute( uniqueNumStr, jsonSummary );		
	}


	// 2. RETRIEVAL All
	public static JSONObject getCachedListAll( ServletContext servletContext ) throws ServletException
	{
		JSONObject resultJson = new JSONObject();

		int displayNum = 5;
		int count = 0;
		float totalSpentf = 0;
		float minSpentf = 0;
		float maxSpentf = 0;

		Enumeration e = servletContext.getAttributeNames();
		while ( e.hasMoreElements() ) 
		{
			String attrName = (String) e.nextElement();	      

			if ( attrName.indexOf( PRE_MONGO_DATA ) >= 0 ) 
			{
				count++;
				
				JSONObject attrJson = (JSONObject)servletContext.getAttribute( attrName );
				JSONObject mongoResult = JsonUtil.getJsonObject( JsonUtil.getJsonObject( attrJson, "jsonData" ), "mongoResult" );
				
				// Do min & max & avg..
				float spentf = Float.parseFloat( JsonUtil.getJSONStrVal( mongoResult, "spent" ) );
								
				totalSpentf += spentf;

				if ( count == 1 ) 
				{	    		  
					minSpentf = spentf;
					maxSpentf = spentf;
				}
				else
				{
					if ( spentf > maxSpentf ) maxSpentf = spentf;
					if ( spentf < minSpentf ) minSpentf = spentf;					
				}


				if ( count <= displayNum )
				{		    	  
					resultJson.put( attrName, attrJson );	    		  
				}
			}
		}

		
		JSONObject stat = new JSONObject();
		
		stat.put( "avgSpent", String.format("%.02f", totalSpentf / count ) );
		stat.put( "minSpent", String.format("%.02f", minSpentf ) );
		stat.put( "maxSpent", String.format("%.02f", maxSpentf ) );
		
		
		resultJson.put( "stat", stat );	    		  
		
		
		return resultJson;		
	}		


	// 2. RETRIEVAL All
	public static int removeCachedListAll( ServletContext servletContext ) throws ServletException
	{
		ArrayList<String> attrList = new ArrayList<String>();
		int count = 0;
		
		Enumeration e = servletContext.getAttributeNames();	    
		while (e.hasMoreElements()) {
			String attrName = (String) e.nextElement();	      
			if ( attrName.indexOf( PRE_MONGO_DATA ) >= 0 ) attrList.add( attrName );
		}

		for (String attrName: attrList )
		{
			servletContext.removeAttribute( attrName );
			count++;
		}	    
		
		return count;
	}		

	// ========== END Translation Cache Related ==========    
	// ================================================



	// ========== SUPPRT METHODS ==========

	public static Map<String, JSONObject> getCachedConfigList( ServletContext servletContext, String keyPrefix  )
	{
		Map<String, JSONObject> list = new HashMap<String, JSONObject>();

		try
		{
			Enumeration<String> attrNames = servletContext.getAttributeNames();

			while (attrNames.hasMoreElements()) {

				String attrName = attrNames.nextElement();

				if ( attrName.startsWith( keyPrefix ) ) {

					Object attrValue = servletContext.getAttribute( attrName );

					if (attrValue instanceof JSONObject) {
						list.put( attrName, (JSONObject) attrValue );
					}
				}
			}
		}
		catch ( Exception ex )
		{
			Util.outputErr( "Error during getCachedConfigList" );
		}

		return list;
	}

	// ========== OTHER SHORT METHODS - FOR OTHER PURPOSE ==========


}
