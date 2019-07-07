package psi.projName.classes.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
   
	public static String sdf_date = "yyyy-MM-dd";
	public static final String DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String DATEFORMAT_NO_T = "yyyy-MM-dd HH:mm:ss";

	public static SimpleDateFormat SD_FORMAT_DATE = new SimpleDateFormat( sdf_date );  
	public static SimpleDateFormat SD_FORMAT_DATE_TIME = new SimpleDateFormat( DATEFORMAT );  
	public static SimpleDateFormat SD_FORMAT_DATE_TIME_NO_T = new SimpleDateFormat( DATEFORMAT_NO_T );  

	
	//public static DateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
	
	public static Date getDateFromStr( String dateStr ) throws Exception
	{
		Date dateObj = null;
				
		try
		{
			dateObj = SD_FORMAT_DATE.parse( dateStr );		
		}
		catch( Exception ex )
		{
			Util.outputErr( "FAILED to convert string to date" );
			throw ex;
		}
		
		return dateObj;
	}
	

	
	// "yyMMddHHmmssSSS";
	public static String getDateStrByFormatStr( String dateFormatStr )
	{
		String outputStr = "";
				
		try
		{
			SimpleDateFormat sdFormat = new SimpleDateFormat( dateFormatStr ); 

			sdFormat.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
			outputStr = sdFormat.format( new Date() );
		}
		catch( Exception ex )
		{
			Util.outputErr( "FAILED to getDateFormatStr" );
		}
		
		return outputStr;
	}
	
	public static String getDateStrByFormatStr( String inputDateFormatStr, String inputDateStr, String outputDateFormatStr )
	{
		String outputStr = "";
				
		try
		{
			SimpleDateFormat inSdFormat = new SimpleDateFormat( inputDateFormatStr ); 			
			Date dateObj = inSdFormat.parse( inputDateStr );	
					
			SimpleDateFormat outSdFormat = new SimpleDateFormat( outputDateFormatStr );
			outSdFormat.setTimeZone( TimeZone.getTimeZone( "UTC" ) );						
			outputStr = outSdFormat.format( dateObj );
		}
		catch( Exception ex )
		{
			Util.outputErr( "FAILED to getDateStrByFormatStr" );
		}
		
		return outputStr;
	}
	
	public static Date getDateTimeFromStr( String dateStr ) throws Exception
	{
		Date dateTimeObj = null;
				
		try
		{
			if ( dateStr.length() <= 10 )
			{
				dateTimeObj = getDateFromStr( dateStr );					
			}
			else
			{
				String middleChar = dateStr.substring( 10, 11 );
								
				if ( middleChar.equals( "T" ) )
				{			
					dateTimeObj = SD_FORMAT_DATE_TIME.parse( dateStr );		
				}
				else if ( middleChar.equals( " " ) )
				{
					dateTimeObj = SD_FORMAT_DATE_TIME_NO_T.parse( dateStr );						
				}			
			}
		}
		catch( Exception ex )
		{
			Util.outputErr( "FAILED to convert string to date time" );
			throw ex;
		}
		
		return dateTimeObj;
	}
	
	
	public static String getUTCdatetimeAsString()
	{
	    return getUTCDateTimeStr( new Date() );
	}

	public static String getUTCDateTimeStr()
	{
	    return getUTCDateTimeStr( new Date() );
	}

	public static String getUTCDateTimeStr( Date date )
	{
	    //SimpleDateFormat sdf = new SimpleDateFormat( DATEFORMAT );
	    SD_FORMAT_DATE_TIME.setTimeZone( TimeZone.getTimeZone("UTC") );
	    String utcTime = SD_FORMAT_DATE_TIME.format( date );

	    return utcTime;
	}
	
	public static String getUTCDateStr()
	{
	    return getUTCDateStr( new Date() );
	}

	public static String getUTCDateStr( Date date )
	{
		SD_FORMAT_DATE.setTimeZone( TimeZone.getTimeZone("UTC") );
	    String utcTime = SD_FORMAT_DATE.format( date );

	    return utcTime;
	}

	
	public static String getUniqueDTStr()
	{
		String utcTime = "";

		try
		{
			SimpleDateFormat sdFormat = new SimpleDateFormat( "yyyyMMdd'T'HHmmssSSS" );  
			
			sdFormat.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
		    utcTime = sdFormat.format( new Date() );
		}
		catch( Exception ex )
		{
			Util.outputErr( "ERROR ON DateUtil.getUniqueDTStr - " + ex.getMessage() );
		}

	    return utcTime;	    
	}

	
	// ================================================
	
	public static int compareDateStr( String dtStr1, String dtStr2 ) throws Exception
	{
		int returnVal = 0;
		
		Date dt1 = getDateTimeFromStr( dtStr1 );
		Date dt2 = getDateTimeFromStr( dtStr2 );
		
		if ( dt1 == null || dt2 == null ) throw new Exception( "One of the date is emtpy" );
		else
		{
			// returnVal is positive if dt1 is bigger than dt2 - which is what we are looking for.
			returnVal = dt1.compareTo( dt2 );
		}
		
		return returnVal;
	}
	
	
	public static boolean isDateStrBigger( String dtStr1, String dtStr2 ) throws Exception
	{
		return ( compareDateStr( dtStr1, dtStr2 ) > 0 );
	}

	
	public static long getTimeDiff_Min( Date startDT, Date endDT )
	{
		long diff = endDT.getTime() - startDT.getTime();
		long diffMinutes = diff / (60 * 1000);  
		
		return diffMinutes;
	}

	public static long getTimeDiff_Min( String startDTStr, String endDTStr ) throws Exception
	{
		Date startDT = null;
		Date endDT = null;
		
		try {
			startDT = getDateTimeFromStr(startDTStr);
			endDT = getDateTimeFromStr(endDTStr);
		} catch ( Exception ex ) 
		{
			Util.outputErr( "ERROR - in getTimeDiff_Min()" );
		    throw ex;
		}    
		
		return getTimeDiff_Min( startDT, endDT );
	}	
	
    /**
     * <p>Checks if two dates are on the same day ignoring time.</p>
     * @param date1  the first date, not altered, not null
     * @param date2  the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is <code>null</code>
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }
    
    /**
     * <p>Checks if two calendars represent the same day ignoring time.</p>
     * @param cal1  the first calendar, not altered, not null
     * @param cal2  the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
    
    /**
     * <p>Checks if a date is today.</p>
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }
    
    /**
     * <p>Checks if a calendar date is today.</p>
     * @param cal  the calendar, not altered, not null
     * @return true if cal date is today
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }
    
    /**
     * <p>Checks if the first date is before the second date ignoring time.</p>
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is before the second date day.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isBeforeDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isBeforeDay(cal1, cal2);
    }
    
    /**
     * <p>Checks if the first calendar date is before the second calendar date ignoring time.</p>
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is before cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return false;
        return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * <p>Checks if the first date is after the second date ignoring time.</p>
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is after the second date day.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isAfterDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isAfterDay(cal1, cal2);
    }
    
    /**
     * <p>Checks if the first calendar date is after the second calendar date ignoring time.</p>
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is after cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true;
        return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * <p>Checks if a date is after today and within a number of days in the future.</p>
     * @param date the date to check, not altered, not null.
     * @param days the number of days.
     * @return true if the date day is after today and within days in the future .
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Date date, int days) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return isWithinDaysFuture(cal, days);
    }
    
    /**
     * <p>Checks if a calendar date is after today and within a number of days in the future.</p>
     * @param cal the calendar, not altered, not null
     * @param days the number of days.
     * @return true if the calendar date day is after today and within days in the future .
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Calendar cal, int days) {
        if (cal == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar today = Calendar.getInstance();
        Calendar future = Calendar.getInstance();
        future.add(Calendar.DAY_OF_YEAR, days);
        return (isAfterDay(cal, today) && ! isAfterDay(cal, future));
    }
    
    /** Returns the given date with the time set to the start of the day. */
    public static Date getStart(Date date) {
        return clearTime(date);
    }
    
    /** Returns the given date with the time values cleared. */
    public static Date clearTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }    

    /** Determines whether or not a date has any time values (hour, minute, 
     * seconds or millisecondsReturns the given date with the time values cleared. */

    /**
     * Determines whether or not a date has any time values.
     * @param date The date.
     * @return true iff the date is not null and any of the date's hour, minute,
     * seconds or millisecond values are greater than zero.
     */
    public static boolean hasTime(Date date) {
        if (date == null) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (c.get(Calendar.HOUR_OF_DAY) > 0) {
            return true;
        }
        if (c.get(Calendar.MINUTE) > 0) {
            return true;
        }
        if (c.get(Calendar.SECOND) > 0) {
            return true;
        }
        if (c.get(Calendar.MILLISECOND) > 0) {
            return true;
        }
        return false;
    }

    /** Returns the given date with time set to the end of the day */
    public static Date getEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /** 
     * Returns the maximum of two dates. A null date is treated as being less
     * than any non-null date. 
     */
    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.after(d2)) ? d1 : d2;
    }
    
    /** 
     * Returns the minimum of two dates. A null date is treated as being greater
     * than any non-null date. 
     */
    public static Date min(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.before(d2)) ? d1 : d2;
    }

    /** The maximum date possible. */
    public static Date MAX_DATE = new Date(Long.MAX_VALUE);
 
    
	public static Date addDays( Date current, int daysAfter )
	{	
		Calendar cal = Calendar.getInstance();
		cal.setTime( current );
		cal.add( Calendar.DATE, daysAfter );
		return cal.getTime();		
	}
	
		
	// -----------------------------------------------------
	// --- OLD CODE MOVED FROM Util Class
	

	@SuppressWarnings("deprecation")
	public static String getCurrentDateStr()
	{
		Date curDate = new Date();
		
		int year = curDate.getYear() + 1900;
		int month = curDate.getMonth() + 1;
		String monthStr = ( month < 10 ) ? "0" + month : month + "";
		
		return year + "-"  + monthStr + "-" + curDate.getDate();
	}
	

	@SuppressWarnings("deprecation")
	public static String getCurrentDateTimeStr()
	{
		Date curDate = new Date();
		
		int year = curDate.getYear() + 1900;
		int month = curDate.getMonth() + 1;
		String monthStr = ( month < 10 ) ? "0" + month : month + "";
		
		int hours = curDate.getHours();
		String hourStr = ( hours < 10 ) ? "0" + hours : hours + "";
		int minutes = curDate.getMinutes();
		String minuteStr =  ( minutes < 10 ) ? "0" + minutes : minutes + "";
		int seconds = curDate.getSeconds();
		String secondStr =  ( seconds < 10 ) ? "0" + seconds : seconds + "";
		
		return year + "-"  + monthStr + "-" + curDate.getDate() + "T" + hourStr + ":" + minuteStr + ":" + secondStr;
	}

	@SuppressWarnings("deprecation")
	public static String getDateTimeStr( Date inputDate )
	{		
		int year = inputDate.getYear() + 1900;
		int month = inputDate.getMonth() + 1;
		String monthStr = ( month < 10 ) ? "0" + month : month + "";
		
		int hours = inputDate.getHours();
		String hourStr = ( hours < 10 ) ? "0" + hours : hours + "";
		int minutes = inputDate.getMinutes();
		String minuteStr =  ( minutes < 10 ) ? "0" + minutes : minutes + "";
		int seconds = inputDate.getSeconds();
		String secondStr =  ( seconds < 10 ) ? "0" + seconds : seconds + "";
		
		return year + "-"  + monthStr + "-" + inputDate.getDate() + "T" + hourStr + ":" + minuteStr + ":" + secondStr;
	}
	

	// Should use DateUtil instead
	public static String getDateStrAfter( int daysAfter )
	{	
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime( current );
		cal.add( Calendar.DATE, daysAfter );
		Date expiryDate = cal.getTime();		
		
		SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
		return format.format( expiryDate );
	}

	public static String getTodayFormatted()
	{	
		Date current = new Date();

		SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
		return format.format( current );
	}
	
	public static String getStartDateInCurrentMonth()
	{	
		Calendar c = Calendar.getInstance();   // this takes current date
		c.set(Calendar.DAY_OF_MONTH, 1);
		Date firstDayOfMonth = c.getTime();
		
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(firstDayOfMonth);
	}
	
	public static String getEndDateInCurrentMonth()
	{	
		Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        Date firstDayOfMonth = calendar.getTime();

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(firstDayOfMonth);
	}

	public static String getCurrentYear()
	{
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		return df.format(date);		
	}
	
	public static int getCurrentYearInt()
	{
		return Integer.parseInt( getCurrentYear() );
	}
}

   