package com.admin.media.election.server.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.TreeSet;

import com.admin.media.election.server.ServerGlobalResources;

public class Utils {
	private static final String SALT = "134119601Hello";
	private static final String HASH_ALGO = "SHA-256";
	private static Properties props = new Properties();
	//private static Session session = Session.getDefaultInstance(props, null);
	private static final String FROM = "ChurchCradle";
	private static final String EMAIL = "fetz.selby@gmail.com";
	private static SecureRandom random = new SecureRandom(); 

	public static String getSHA256(String text){
		if(text == null){
			return null;
		}

		String SHA_256 = null;

		text += SALT;
		try{
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGO);
			digest.update(text.getBytes(), 0, text.length());
			SHA_256 = new BigInteger(1, digest.digest()).toString(16);
		} catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return SHA_256;
	}

	public static String getToken(){
		return new BigInteger(130, random).toString(32);
	}

	public static String getToken(int length){
		return new BigInteger(length, random).toString(32);
	}

	public static String getExpireDate(int numberOfDays){
		Date date = new Date();
		date.setDate(date.getDate()+numberOfDays);

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		return dateFormat.format(date);
	}

	public static String getTodayDate(){
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		return dateFormat.format(date);
	}

	public static String getTodayDateTime(){
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:s");

		return dateFormat.format(date);
	}

	public static String getFullDate(String date){
		String[] dateTokens = date.split("[-]");

		String year = dateTokens[0];
		String month = dateTokens[1];
		String day = dateTokens[2];

		Date tmpDate = new Date();
		tmpDate.setYear(Integer.parseInt(year));
		tmpDate.setMonth(Integer.parseInt(month));
		tmpDate.setDate(Integer.parseInt(day));

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy zzzz", Locale.ENGLISH);

		return dateFormat.format(tmpDate);
	}

	public static ArrayList<String> getList(String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getFilter(String column, String values, String delimiter){
		String str = "", or = " || ";

		String[] valueArr = values.split(delimiter);

		for(String value : valueArr){
			str += column + " = "+ value + or;
		}

		str = str.substring(0, str.lastIndexOf(or));

		return str;
	}

	public static boolean isAppValid(boolean isContinue, String date){

		String[] dateToken = date.split("-");
		String year = dateToken[0];
		String month = dateToken[1];
		String day = dateToken[2];

		Date appDate = new Date();
		appDate.setYear(Integer.parseInt(year));
		appDate.setMonth(Integer.parseInt(month));
		appDate.setDate(Integer.parseInt(day));

		String[] todayDateToken = getTodayDate().split("-");

		Date todayDate = new Date();
		todayDate.setYear(Integer.parseInt(todayDateToken[0]));
		todayDate.setMonth(Integer.parseInt(todayDateToken[1]));
		todayDate.setDate(Integer.parseInt(todayDateToken[2]));


		double appTime = appDate.getTime();
		double dayTime = todayDate.getTime();

		if(appTime >= dayTime){
			return true;
		}else if(isContinue){
			return true;
		}

		return false;
	}

	public static ArrayList<Integer> getTokenList(String delimiter, String value){
		if(value != null && !value.trim().isEmpty()){
			ArrayList<Integer> valueList = new ArrayList<Integer>();
			String[] valueTokens = value.split(delimiter);
			for(String token : valueTokens){
				valueList.add(Integer.parseInt(token));
			}
			return valueList;
		}
		return null;
	}

	public static String getRegexp(String delimiter, int value){
		return "["+delimiter+"]?"+value+"["+delimiter+"]?";
	}

	public static String getRegexp(String delimiter, String value){
		return "["+delimiter+"]?"+value+"["+delimiter+"]?";
	}

	public static String getCapitalizedWord(String word){
		return word.substring(0,1).toUpperCase()+word.substring(1).toLowerCase();
	}

	public static String getClientConvertedDateOfBirth(String date, String clientDelimiter, String serverDelimiter){
		String[] dateTokens = date.split(clientDelimiter);

		if(dateTokens.length == 3){
			String day = dateTokens[0];
			String month = dateTokens[1];
			String year = dateTokens[2];

			//Reformat date
			return year+serverDelimiter+month+serverDelimiter+day;
		}

		return date;
	}

	public static boolean isBulkMessage(String message){
		if(message.contains(",")){ 
			String[] msisdnTokens = message.split("[,][\\s]*");
			if(msisdnTokens.length > 1){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public static int getSMSLength(String message){
		if(message.trim().length() <= 160){
			return 1;
		}else if(message.trim().length() > 160 && message.trim().length() < 306){
			return 2;
		}else if(message.trim().length() > 306 && message.trim().length() < 459){
			return 3;
		}else if(message.trim().length() > 459 && message.trim().length() < 621){
			return 4;
		}
		return 1;
	}


	

	public static String getBeginDate(String delimiter, String date){
		String[] tokens = date.split(delimiter);

		return tokens[1]+"-"+tokens[0]+"-"+"01";
	}

	

	public static BufferedImage scale(BufferedImage img, int targetWidth, int targetHeight) {

		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = img;
		BufferedImage scratchImage = null;
		Graphics2D g2 = null;

		int w = img.getWidth();
		int h = img.getHeight();

		int prevW = w;
		int prevH = h;

		do {
			if (w > targetWidth) {
				w /= 2;
				w = (w < targetWidth) ? targetWidth : w;
			}

			if (h > targetHeight) {
				h /= 2;
				h = (h < targetHeight) ? targetHeight : h;
			}

			if (scratchImage == null) {
				scratchImage = new BufferedImage(w, h, type);
				g2 = scratchImage.createGraphics();
			}

			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

			prevW = w;
			prevH = h;
			ret = scratchImage;
		} while (w != targetWidth || h != targetHeight);

		if (g2 != null) {
			g2.dispose();
		}

		if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
			scratchImage = new BufferedImage(targetWidth, targetHeight, type);
			g2 = scratchImage.createGraphics();
			g2.drawImage(ret, 0, 0, null);
			g2.dispose();
			ret = scratchImage;
		}

		return ret;
	}

	public static String getDateSuffix(int day){
		switch(day){
		case 1:
			return "st";
		case 2:
			return "nd";
		case 3:
			return "rd";
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
			return "th";
		case 21:
			return "st";
		case 22:
			return "nd";
		case 23:
			return "rd";
		case 24:
		case 25:
		case 26:
		case 27:
		case 28:
		case 29:
		case 30:
			return "th";
		case 31:
			return "st";
		}

		return null;
	}

	public static String getWeekdayName(int day){
		switch(day){
		case 0:
			return "Monday";
		case 1:
			return "Tuesday";
		case 2:
			return "Wednesday";
		case 3:
			return "Thursday";
		case 4:
			return "Friday";
		case 5:
			return "Saturday";
		case 6:
			return "Sunday";

		}
		return null;
	}

	public static boolean isUsernameMsisdn(String username){
		if(username.contains("@")){
			return false;
		}
		return true;
	}

	public static String getReturnUrl(int id, String token, String type, String url){
		String devUrl = url+"#confirmation/";

		if(id > 0){
			devUrl += "id="+id+";";
		}

		if(token != null){
			devUrl += "g="+token+";";
		}

		if(type != null){
			devUrl += "t="+type+";";
		}

		return devUrl;
	}

	public static String getCancelUrl(int id, String token, String type, String url){
		String devUrl = url+"#cancel/";

		if(id > 0){
			devUrl += "id="+id+";";
		}

		if(token != null){
			devUrl += "g="+token+";";
		}

		if(type != null){
			devUrl += "t="+type+";";
		}

		return devUrl;
	}

	public static String getAnnoymousReturnUrl(int id, String token, String type, String url){
		String devUrl = url+"#confirm/";

		if(id > 0){
			devUrl += "id="+id+";";
		}

		if(token != null){
			devUrl += "g="+token+";";
		}

		if(type != null){
			devUrl += "t="+type+";";
		}

		return devUrl;
	}

	public static String getAnnoymousCancelUrl(int id, String token, String type, String url){
		String devUrl = url+"#cancel/";

		if(id > 0){
			devUrl += "id="+id+";";
		}

		if(token != null){
			devUrl += "g="+token+";";
		}

		if(type != null){
			devUrl += "t="+type+";";
		}

		return devUrl;
	}

	

	public static String getStringifiedMsisdn(ArrayList<String> msisdnList){
		String msisdn = "";
		if(msisdnList.size() > 0){
			for(String tmpMsisdn : msisdnList){
				if(tmpMsisdn.charAt(0) == '+' || tmpMsisdn.contains("+")){
					msisdn += tmpMsisdn.substring(1)+",";
				}else{
					msisdn += tmpMsisdn+",";
				}
			}

			return msisdn.substring(0, msisdn.length()-1);
		}

		return null;
	}

	public static String getStringifiedMsisdn(TreeSet<String> msisdnList){
		String msisdn = "";
		if(msisdnList.size() > 0){
			for(String tmpMsisdn : msisdnList){
				if(tmpMsisdn.charAt(0) == '+' || tmpMsisdn.contains("+")){
					msisdn += tmpMsisdn.substring(1)+",";
				}else{
					msisdn += tmpMsisdn+",";
				}
			}

			return msisdn.substring(0, msisdn.length()-1);
		}

		return null;
	}

	public static String getStringifiedMsisdn(String msisdnList, String delimiter){
		String msisdn = "";

		if(msisdnList != null && msisdnList.contains(",")){
			String[] msisdnToken = msisdnList.split(delimiter);

			for(String tmpMsisdn : msisdnToken){
				if(tmpMsisdn.charAt(0) == '+' || tmpMsisdn.contains("+")){
					msisdn += tmpMsisdn.substring(1)+",";
				}else{
					msisdn += tmpMsisdn+",";
				}
			}

			return msisdn.substring(0, msisdn.length()-1);

		}else if(msisdnList != null){
			if(msisdnList.charAt(0) == '+' || msisdnList.contains("+")){
				return msisdnList.trim().substring(1);
			}else{
				return msisdnList.trim();
			}
		}

		return null;
	}	

}
