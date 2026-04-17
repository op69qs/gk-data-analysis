package org.triber.analysis.util;


import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 
 * @author pangzt
 * @version v1.0
 */
public class Utils {
	

	/**
	 * �ж��ַ�����Ϊ��
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(String obj)
	{
		return obj != null && !obj.trim().equals("") && !obj.equals("null")&&!obj.equals("undefined");
	}
	/**
	 * �ж��ַ���Ϊ��
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(String obj)
	{
		return obj == null || obj.equals("") || obj.equals("null")|| obj.equals("undefined");
	}
	/**
	 * ΪNULL��ֵתΪ���ַ���
	 * @param str
	 * @return
	 */
	public static String convertNullToEmpty(String str)
	{
		if(isEmpty(str.trim())){
			return "";
		}
		return isEmpty(str)?"":str;
	}
	/**
	 * ���ڸ�ʽ��
	 * @param date ��������
	 * @param pattern ��ʽ�� �磺yyyy-MM-dd
	 * @return
	 */
	public static String getFmtDate(Date date, String pattern)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	/**
	 * �ж��ַ����ǲ�������
	 * @param arg0
	 * @return
	 */
	public static boolean isNumber(String arg0){
		Pattern pattern = Pattern.compile("-?\\d*\\.*\\d*"); 
		return pattern.matcher(arg0).matches();
	}
	/**
	 * �õ��������������
	 * @param obj
	 * @return
	 */
	public static String getDataType(Object obj){
		String dataType = String.valueOf(obj);
		if(isNotEmpty(dataType)){
			int lastD = dataType.lastIndexOf(".");
			if(lastD>0&&lastD<dataType.length()-1){
				dataType = dataType.substring(lastD+1);
				return dataType;
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	/**
	 * ���������������Ϊ���ĵĸ�ΪӢ��
	 * Բ�ǿո񡢶��š��ֺš�ð�š�����
	 * @param arg0
	 * @return
	 */
	public static String getSpecialSymbol(String arg0){
		if(isNotEmpty(arg0)){
			String content = arg0.replace("��", " ");
			content = content.replace("��", ",");
			content = content.replace("��", ";");
			content = content.replace("��", ":");
//			content = content.replace("��", "\"");
//			content = content.replace("��", "\"");
//			content = content.replace("��", "!");
//			content = content.replace("��", ".");
//			content = content.replace("��", "=");
			content = content.replace("��", "-");
			content = content.replace("��", "+");
//			content = content.replace("��", "\\");
//			content = content.replace("��", "?");
//			content = content.replace("��", "<");
//			content = content.replace("��", ">");
//			content = content.replace("��", "'");
//			content = content.replace("��", "'");
//			content = content.replace("��", "[");
//			content = content.replace("��", "]");
//			content = content.replace("��", "{");
//			content = content.replace("��", "}");
			return content;
		}else{
			return "";
		}
	}
	/***���㶨ʱ������ʱ����뵱ǰʱ��**/
    public static Date firstTime(String checkTime)
    {
      boolean  flag  =  isBeforeCheakTime(checkTime);
      GregorianCalendar gc = new GregorianCalendar();
      gc.setTime(new Date());
      if(flag){
        gc.add(GregorianCalendar.DAY_OF_MONTH,1);
      }else{
        gc.add(GregorianCalendar.DAY_OF_MONTH,0);
      }
      gc.set(GregorianCalendar.HOUR_OF_DAY, getHour(checkTime));
      gc.set(GregorianCalendar.MINUTE,getMinute(checkTime));
      return gc.getTime();
    }

     /***���㶨ʱ������ʱ���Ƿ��ڵ�ǰʱ��֮ǰ**/
    private static boolean isBeforeCheakTime(String checkTime){
      boolean flag = false;
      GregorianCalendar gc = new GregorianCalendar();
      gc.setTime(new Date());
      gc.add(GregorianCalendar.DAY_OF_MONTH, 0);
      gc.set(GregorianCalendar.HOUR_OF_DAY, getHour(checkTime));
      gc.set(GregorianCalendar.MINUTE, getMinute(checkTime));
      Date checkDate = gc.getTime();
      if(checkDate.equals(new Date())||checkDate.before(new Date())){
    	  flag=true;
      }else{
    	  flag = false;
      }
      System.out.println(checkDate+"---------------"+flag+"-------------:"+new Date());
      return flag;
    }
//    ------------------------------
    /***���㶨ʱ������ʱ����뵱ǰʱ��**/
    public static Date firstDayTime(String checkTime)
    {
      boolean  flag  =  isBeforeDayTime(checkTime);
      
      GregorianCalendar gc = new GregorianCalendar();
      try {
	      String nowYearAndMonth = getFmtDate(new Date(),"yyyy-MM");
	      String chaeckDateTime_str = nowYearAndMonth+"-"+getDay(checkTime)+" "+getHour(checkTime.substring(checkTime.indexOf("-")+1))+":"+getMinute(checkTime.substring(checkTime.indexOf("-")+1));
	      System.out.println(chaeckDateTime_str);
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	      Date chaeckDateTime = sdf.parse(chaeckDateTime_str);
      
		  gc.setTime(chaeckDateTime);
		  if(flag){
		    gc.add(GregorianCalendar.MONTH,1);
		  }
//		  gc.add(GregorianCalendar.DAY_OF_MONTH,getDay(checkTime));
//		  gc.set(GregorianCalendar.HOUR_OF_DAY, getHour(checkTime.substring(checkTime.indexOf("-")+1)));
//		  gc.set(GregorianCalendar.MINUTE, getMinute(checkTime.substring(checkTime.indexOf("-")+1)));
      } catch (ParseException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}
      System.out.println(flag+"=--------------"+gc.getTime());
      return gc.getTime();
    }
    /***���㶨ʱ��������+ʱ���Ƿ��ڵ�ǰ��+ʱ��֮ǰ
     * @throws ParseException **/
    private static boolean isBeforeDayTime(String checkTime) {
      boolean flag = false;
      try {
	      String nowYearAndMonth = getFmtDate(new Date(),"yyyy-MM");
	      String chaeckDateTime_str = nowYearAndMonth+"-"+getDay(checkTime)+" "+getHour(checkTime.substring(checkTime.indexOf("-")+1))+":"+getMinute(checkTime.substring(checkTime.indexOf("-")+1));
	      System.out.println(chaeckDateTime_str);
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	      Date chaeckDateTime = sdf.parse(chaeckDateTime_str);
//	      GregorianCalendar gc = new GregorianCalendar();
//	      gc.setTime(new Date());
//	      
//	      gc.add(GregorianCalendar.DAY_OF_MONTH,getDay(checkTime));
//	      gc.set(GregorianCalendar.HOUR_OF_DAY, getHour(checkTime.substring(checkTime.indexOf("-")+1)));
//	      gc.set(GregorianCalendar.MINUTE, getMinute(checkTime.substring(checkTime.indexOf("-")+1)));
//	      Date checkDate = gc.getTime();
	      System.out.println(chaeckDateTime+"---------------"+chaeckDateTime.before(new Date())+"------------:"+new Date());
	      if(chaeckDateTime.equals(new Date())||chaeckDateTime.before(new Date())){
	    	  flag=true;
	      }else{
	    	  flag = false;
	      }
//	      System.out.println(getFmtDate(gc.getTime(),"yyyy-MM-dd HH:mm:ss")+"---------------flag-------------:"+flag);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      return flag;
    }
    
    /***������ʱ��:��**/
    private static int getDay(String day)
    {
      day = day.substring(0,day.indexOf("-"));
      return Integer.parseInt(day);
    }
    /***������ʱ��:Сʱ**/
    private static int getHour(String hour)
    {
      hour = hour.substring(0,hour.indexOf(":"));
      return Integer.parseInt(hour);
    }
     /***������ʱ��:����**/
    private static int getMinute(String minute)
    {
      minute = minute.substring(minute.indexOf(":") + 1);
      return Integer.parseInt(minute);
    }
    /**
     * �ձ�
     * @return
     */
    public static List<Map> getDays(){
    	List<Map> days = new ArrayList<Map>();
    	SimpleDateFormat sf  =new SimpleDateFormat("yyyy��MM��dd��");
    	SimpleDateFormat sf1  =new SimpleDateFormat("yyyyMMdd");
    	Date date=new Date();
    	GregorianCalendar gc =new GregorianCalendar();
    	gc.setTime(date);
    	GregorianCalendar gc1 =new GregorianCalendar();
    	gc1.setTime(date);
    	gc1.add(2,-1);
    	int nDay=(int)((gc.getTime().getTime()-gc1.getTime().getTime())/(24*60*60*1000)); 
    	Map map = new HashMap();
    	map.put("value",sf1.format(gc.getTime()));
    	map.put("text",sf.format(gc.getTime()));
    	days.add(map);
    	for(int i=0;i<nDay;i++){
    		gc.add(5, -1);
    		map = new HashMap();
        	map.put("value",sf1.format(gc.getTime()));
        	map.put("text",sf.format(gc.getTime()));
        	days.add(map);
    	}
    	return days;
    }
    /**
     * �±�
     * @return
     */
    public static List<Map> getMonths(){
    	List<Map> months = new ArrayList<Map>();
    	SimpleDateFormat sf  =new SimpleDateFormat("yyyy��MM��");
    	SimpleDateFormat sf1  =new SimpleDateFormat("yyyyMM");
    	Date date=new Date();
    	GregorianCalendar gc =new GregorianCalendar();
    	gc.setTime(date);
    	gc.add(2,-1);
    	Map map = new HashMap();
    	map.put("value",sf1.format(gc.getTime()));
    	map.put("text",sf.format(gc.getTime()));
    	months.add(map);
    	for(int i=0;i<12;i++){
    		gc.add(2,-1);
    		map = new HashMap();
        	map.put("value",sf1.format(gc.getTime()));
        	map.put("text",sf.format(gc.getTime()));
        	months.add(map);
    	}
    	return months;
    }
    /**
     * Ѯ��
     * @return
     */
    public static List<Map> getXuns(){
    	List<Map> xuns = new ArrayList<Map>();
    	SimpleDateFormat sf  =new SimpleDateFormat("yyyy��MM��");
    	SimpleDateFormat sf1 =new SimpleDateFormat("yyyyMM");
    	Date date=new Date();
    	GregorianCalendar gc =new GregorianCalendar();
    	gc.setTime(date);
//    	gc.add(2,-6);
    	
    	Map map = new HashMap();
    	map.put("value",sf1.format(gc.getTime())+"01");
    	map.put("text",sf.format(gc.getTime())+"��Ѯ");
    	xuns.add(map);
    	
    	int day = gc.get(5);
    	if(day>10){
    		map = new HashMap();
        	map.put("value",sf1.format(gc.getTime())+"02");
        	map.put("text",sf.format(gc.getTime())+"��Ѯ");
        	xuns.add(map);
    	}
    	if(day>20){
    		map = new HashMap();
        	map.put("value",sf1.format(gc.getTime())+"03");
        	map.put("text",sf.format(gc.getTime())+"��Ѯ");
        	xuns.add(map);
    	}
    	for(int i=0;i<5;i++){
    		gc.add(2,-1);
    		
    		map = new HashMap();
        	map.put("value",sf1.format(gc.getTime())+"01");
        	map.put("text",sf.format(gc.getTime())+"��Ѯ");
        	xuns.add(map);
    		
        	map = new HashMap();
        	map.put("value",sf1.format(gc.getTime())+"02");
        	map.put("text",sf.format(gc.getTime())+"��Ѯ");
        	xuns.add(map);
        	
        	map = new HashMap();
        	map.put("value",sf1.format(gc.getTime())+"03");
        	map.put("text",sf.format(gc.getTime())+"��Ѯ");
        	xuns.add(map);
    	}
    	return xuns;
    }
    /**
     * ����
     * @return
     */
    public static List<Map> getQuarters(){
    	List<Map> quarters = new ArrayList<Map>();
    	Date date=new Date();
    	GregorianCalendar gc =new GregorianCalendar();
    	gc.setTime(date);
    	int month = gc.get(2);
    	int year = gc.get(1);
    	
    	Map map = new HashMap();
    	map.put("value",year+"0"+(month/3+1));
    	map.put("text",year+"��"+(month/3+1)+"����");
    	quarters.add(map);
    	for(int i=0;i<12;i++){
    		gc.add(2,-1);
    		month = gc.get(2);
    		year = gc.get(1);
    		
        	boolean isExist = false;
        	for(Map m:(List<Map>)quarters){
        		String value = String.valueOf(m.get("value"));
        		if(value.equals(year+"0"+(month/3+1))){
        			isExist = true;
        		}
        	}
        	if(!isExist){
        		map = new HashMap();
            	map.put("value",year+"0"+(month/3+1));
            	map.put("text",year+"��"+(month/3+1)+"����");
            	quarters.add(map);
        	}
    	}
    	return quarters;
    }
    /**
     * �걨
     * @return
     */
    public static List<Map> getYears(){
    	List<Map> years = new ArrayList<Map>();
    	SimpleDateFormat sf  =new SimpleDateFormat("yyyy��");
    	SimpleDateFormat sf1 =new SimpleDateFormat("yyyy");
    	
    	Date date=new Date();
    	GregorianCalendar gc =new GregorianCalendar();
    	gc.setTime(date);
    	
    	Map map = new HashMap();
    	map.put("value",sf1.format(gc.getTime()));
    	map.put("text",sf.format(gc.getTime()));
    	years.add(map);
    	for(int i=0;i<1;i++){
    		gc.add(1, -1);
    		
    		map = new HashMap();
        	map.put("value",sf1.format(gc.getTime()));
        	map.put("text",sf.format(gc.getTime()));
        	years.add(map);
    	}
    	return years;
    }
    /**
     * ������
     * java��ȡʱ�������תΪ�����ƣ�Ȼ�����ɶ�����תΪʮ���ƣ���ʱ�ͻ���־�����ʧ����
     * @param value
     * @return
     */
    public static String Precision(double value){
    	//�Ȱѿ�ѧ��������ͨ����תΪ�ַ���
    	DecimalFormat df = new DecimalFormat("#.########");
//    	DecimalFormat df = new DecimalFormat("#0.0000");
		String num = df.format(value);
		//��������������κδ���
//		if(!num.contains(".")){
//			return num;
//		}else{//�Ӻ���ǰ�ң�С������һ������0��9�����ֵ�λ��
//			int start = num.indexOf(".")+1;
//			int end = num.length();
//			int no9 = start;
//			for(int i=end;i>start;i--){
//				String str = num.substring(i-1, i);
//				if(!"9".equals(str)){
//					no9 = i;
//					break;
//				}
//			}
//			//����С�����λ��
//			int w = no9 - start;
//			BigDecimal b = new BigDecimal(new Double(num).doubleValue());
//			double result = b.setScale(w,BigDecimal.ROUND_HALF_UP).doubleValue();
//			//�жϽ������Դ������Χ
//			double f = result - value;
//			if(f<=0.00000001&&f>=-0.00000003){//��Χ֮�ڣ����ؽ��
//				return df.format(result);
//			}else{//��Χ���⣬��������
//				return num;
//			}
//		}
		return num;
    }
//    ����KEY
    public static String generateKey(String startStr)
	{
//		String key = "";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssms");
//    	key = sdf.format(new Date()) + String.valueOf((int)(Math.random()*10000));  	
//    	for (;key.length() < 20; )
//    	{
//    		key = key + "0";
//    	}
//		return startStr+key;
    	return generateKey();
	}
    
    /**
     * 
     * @return 35λ�����
     * caoyg
     */
	public static String generateKey() {
		int length = 35;
		String key = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yMMddHHmmssms");
		key = sdf.format(new Date()) + (Math.random() * 10000);
		for (; key.length() < length+1;) {
			key = key + "0";
		}
		return key.replace(".", "").substring(0, length);
	}
	/**
     * 
     * @return 35λ�����
     * caoyg
     */
	public static String generateShortKey() {
		int length =12;
		String key = "";
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmssms");
		key = sdf.format(new Date()) + (Math.random() * 1000);
		for (; key.length() < length+1;) {
			key = key + "0";
		}
		return key.replace(".", "").substring(0, length);
	}

	    public static String replaceString(String paramString1, String paramString2, String paramString3)
	    {
	      String str1 = "";
	      String str2 = paramString1;
	      String str3 = "";
	      int i = paramString2.length();
	      int j = str2.indexOf(paramString2);
	      int k = -1;
	      while (j != -1)
	      {
	        k = j + i;
	        str3 = str3 + str2.substring(0, j) + paramString3;
	        str2 = str2.substring(k);
	        j = str2.indexOf(paramString2);
	      }
	      str1 = str3 + str2;
	      return str1;
	    }
	    

    public static void main(String[] args) {
//    	String  date = Utils.getFmtDate(Utils.firstTime("23:00"), "yyyy-MM-dd HH:mm");
//    	System.out.println(date);
//    	List<String> years = getDays();
//    	for(String year:years){
//    		System.out.println(year);
//    	}
    	
	}
    
    /**
	 * ���������������Ϊ���ĵĸ�ΪӢ��
	 * Բ�ǿո񡢶��š��ֺš�ð�š����š�����
	 * @param arg0
	 * @return
	 */
	public static String SpecialSymbol_con(String arg0){
		if(isNotEmpty(arg0)){
			String content = arg0.replaceAll("��", " ");
			content = content.replaceAll("��", ",");
			content = content.replaceAll("��", ";");
			content = content.replaceAll("��", ":");
			content = content.replaceAll("��", "\"");
			content = content.replaceAll("��", "\"");
			content = content.replaceAll("��", "!");
			content = content.replaceAll("��", ".");
			content = content.replaceAll("��", "=");
			content = content.replaceAll("��", "-");
			content = content.replaceAll("��", "+");
			content = content.replaceAll("��", "\\");
			content = content.replaceAll("��", "?");
			content = content.replaceAll("��", "<");
			content = content.replaceAll("��", ">");
			content = content.replaceAll("��", "'");
			content = content.replaceAll("��", "'");
			content = content.replaceAll("��", "[");
			content = content.replaceAll("��", "]");
			content = content.replaceAll("��", "{");
			content = content.replaceAll("��", "}");
			content = content.replaceAll("��", "(");
			content = content.replaceAll("��", ")");
			return content;
		}else{
			return "";
		}
	}
	
	/**
	 * ���ؽ��ڻ������� 
	 * @param areaId ʡ��ID
	 * @param randomLength ��֯�����������������
	 * @return 
	 */
	public static String initPBCOrgCode(String areaId,int randomLength ){
		String randomNum =  String.valueOf(Math.random());
		String prefix = "ZZZ"+areaId+randomNum.substring(randomNum.length()-randomLength);
		return prefix;
	}
}
