package psi.projName;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import psi.projName.classes.DataStore;
import psi.projName.classes.DateTimeRecord;
import psi.projName.classes.utils.*;

public class ApiController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiController() {
        super();
    }
    
	// -----------------------------------------------------------
	// ----- Servlet 'GET'/'POST' Methods  -----------------------
	    
    // 'GET' SERVLET - INFORMATION LOOK-UP
	protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException 
	{	
		JSONObject jsonMsg = null;
		String msgOut = "";		
		
		String key1 = Util.getRequestPath( request, 1 );
		String key2 = Util.getRequestPath( request, 2 );
		
		if ( !key1.isEmpty() )
		{				
			try 
			{				
				switch( key1 )
				{												
					case "mongoDB":
						
						jsonMsg = mongoDoGet( key2 );
						break;
				}
											
			} catch (Exception ex) 
			{
				Util.outputErr( "Exception Occurred!! " + ex.getMessage() );
				msgOut = ex.getMessage();
			}			
		}
		else
		{
			msgOut = "Query Path does not have 2 parts and more";
		}
				
		
		if ( jsonMsg == null )
		{
			jsonMsg = new JSONObject();
		}

		if ( !msgOut.isEmpty() ) jsonMsg.put( "message", msgOut );			

		Util.respondMsgOut(response, jsonMsg );
	}
	
	

	// ===============================================================
	// 'POST' SERVLET - RECORD (Register) - Worker registers/updates the client/patient
	protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws UnsupportedEncodingException, ServletException, IOException
	{	
		JSONObject outputJson = new JSONObject();
		String key1 = "";
		String key2 = "";
		
		try
		{
			key1 = Util.getRequestPath(request, 1);		
			key2 = Util.getRequestPath(request, 2);		
			
			
			// if key1 equals to something?  perform that..
			if ( key1.equals( "mongoDB" ) )
			{
				// get body json and send it to localhost:3000 with json..
				outputJson = mongoDoPost( request, response, key2 );
			}

		}
		catch ( Exception ex )
		{
			outputJson.put( "msg", "ERROR on path " + key1 + " " + key2 + " - " + ex.getMessage() );
		}
		
		// Output Data..
		Util.respondMsgOut(response, outputJson );
	}
	
	
	// =======================================
	// ====== METHODS ========================	
	
	
	private JSONObject mongoDoGet( String key2 ) throws Exception
	{
		JSONObject outputJson = new JSONObject();
		
		try
		{
			DataStore dataStore = DataStore.createDataStore_JsonGet( "http://localhost:3000/" + key2 );
						
			String dataReturn = Util.sendRequest( dataStore );

			outputJson.put( "mongoResultStr", dataReturn );		
			outputJson.put( "mongoResult", new JSONObject( dataReturn ) );		
		}
		catch( Exception ex )
		{
			// Util.outputErr( "ERROR on mongoDoGet: " + ex.getMessage() );
			//throw ex;
			outputJson.put( "ErrMsg", "ERROR on mongoDoGet: " + ex.getMessage() );
		}

		return outputJson;
	}

	
	private JSONObject mongoDoPost( HttpServletRequest request, HttpServletResponse response, String key2 ) throws Exception
	{
		JSONObject outputJson = new JSONObject();
		
		try
		{
			JSONObject dtRecord_actionJson = new JSONObject();
			DateTimeRecord dtRecord_RESTCall = new DateTimeRecord( "RESTCall" );	
			// ------------------------------------
			
			
			JSONObject receivedData = Util.getJsonFromInputStream( request.getInputStream() );
			
			DataStore dataStore = DataStore.createDataStore_JsonPost( "http://localhost:3000/" + key2, receivedData );
						
			String dataReturn = Util.sendRequest( dataStore );

			outputJson.put( "mongoResultStr", dataReturn );		
			outputJson.put( "mongoResult", new JSONObject( dataReturn ) );	
			
			try
			{							
				JSONArray jsonArrOutput = new JSONArray( dataReturn );
				
				outputJson.put( "mongoResult", jsonArrOutput );				
			}
			catch ( Exception ex )
			{
				Util.outputErr( "ERROR on processing output result json - " + ex.toString() );
			}
			
			
			// ------------------------------------
			dtRecord_RESTCall.addTimeMark_WtCount( dtRecord_actionJson );
			outputJson.put( "Time", dtRecord_actionJson );

		}
		catch( Exception ex )
		{
			//Util.outputErr( "ERROR on mongoDBHanle: " + ex.getMessage() );
			//throw ex;
			outputJson.put( "ErrMsg", "ERROR on mongoDoPost: " + ex.getMessage() );			
		}

		return outputJson;
	}
	
}
