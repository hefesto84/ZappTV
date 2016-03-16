package es.ubiqua.zapptv.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.manager.impl.DatabaseManager.DAY;
import es.ubiqua.zapptv.manager.model.Channel;
import es.ubiqua.zapptv.manager.model.ChannelList;
import es.ubiqua.zapptv.manager.model.ChannelOrder;
import es.ubiqua.zapptv.manager.model.ChannelStatus;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.ProgramList;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.DateTimeKeyListener;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

public class Utils {
	
	public static final String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public static final String GET(String url) {
        InputStream inputStream = null;
        String result = "";

        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "";
        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * Hace una llamada POST
     *
     * Ejemplo de contrucción de nameValuePairs
     * List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
     * nameValuePairs.add(new BasicNameValuePair("registrationid", "123456789"));
     *
     * @param url
     * @param nameValuePairs
     * @return
     */
    public static final String POST(String url, List<NameValuePair> nameValuePairs) {
        HttpClient httpClient     = null;
        HttpPost httpPost         = null;
        HttpResponse httpResponse = null;
        InputStream inputStream   = null;
        String result =           "";

        // create HttpClient
        httpClient = new DefaultHttpClient();

        // create HttpPost
        httpPost = new HttpPost(url);

        // add parameters
        if( nameValuePairs != null ) {
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e) {
            }
        }

        // make POST request to the given URL
        try {
            httpResponse = httpClient.execute(httpPost);
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        // receive response as inputStream
        if( httpResponse != null ) {
            try {
                inputStream = httpResponse.getEntity().getContent();
            } catch (IOException e) {
            }
        }

        // convert inputStream to string
        if(inputStream != null)
            try{
                result = convertInputStreamToString(inputStream);
            } catch (IOException e) {
            }
        else
            result = "";

        return result;
    }
    
	public static String SpanTextColor(CharSequence text, String seq, int color){
		int from = text.toString().indexOf(seq);
		int to = from + seq.length()-1;
		Spannable s = new SpannableString(text);
		s.setSpan(new ForegroundColorSpan(color), from, to, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return s.toString();
	}
	
	public static String toSimpleArray(String[] str){
		String s= "";
		for(String st : str){
			s = s + st + "#";
		}
		return (s).replace("'", "`");
	}
	
	/*
	public static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }  */
	
	public static String fromSimpleArray(String str){
		String s = "";
		for(String token : str.split("#")){
			s = token + ", " + s;
		}
		try{
			s = s.substring(0,s.length()-2);
		}catch(Exception e){
			
		}
		return s;
	}
	
	public static String parseEmisionTime(String time){
		String m_emissionTimeHour = "";
		String m_emissionTimeMinute = "";
		
		m_emissionTimeHour = time.substring(8,10);
		m_emissionTimeMinute = time.substring(10, 12);
		
		return m_emissionTimeHour+":"+m_emissionTimeMinute;
	} 
	
	public static int toMinutes(String start, String stop){
		long f = toTimestamp(start);
		long t = toTimestamp(stop);
		return (int)(((t-f)/1000)/60);
	}
	
	public static long toTimestamp(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm Z");
		try {
			Date d = sdf.parse(time);
			return d.getTime();
		} catch (ParseException e) {
			BaseApplication.getEventsManager().show("Error recuperando fechas");
		}
		return Calendar.getInstance().getTimeInMillis();
	}
	
	public static int getEmissionProgress(long time_a, long time_b){
		double total = time_b - time_a;
		double emitted = Calendar.getInstance().getTimeInMillis() - time_a;
		return (int)((emitted/total)*100);
	}
	
	public static void createScheduler_v2(ChannelList channels){
		float pixelsPerMinute = 3f;
		String header = "<html><head><script src=\"file:///android_asset/jquery.js\"></script><link rel=\"stylesheet\" href=\"file:///android_asset/main.css\"/></head><body>";
		DateTime now = new DateTime();
		DateTime midnight = now.withTimeAtStartOfDay();
		Duration duration = new Duration(midnight,now);
		int line_offset = duration.toStandardMinutes().getMinutes();
		
		String hours = "<div id=\"horario\">";
		
		for(int i = 0; i<24; i++){
			hours = hours + "<div id=\"h\"><div id=\"hc\">"+i+":00"+"</div></div>";
		}
		
		for(int i = 0; i<4; i++){
			hours = hours + "<div id=\"h\"><div id=\"hc\">"+i+":00"+"</div></div>";
		}
		
		hours = hours + "</div>";
		
		/* Data */
		
		String s_channels = "<div id=\"channels\">";
		for(Channel c : channels.getChannels()){	
			if(BaseApplication.getWebserviceManager().isChannelFavourite(c)){
				s_channels = s_channels + "<div id=\"canal\"><div id=\"icono\"><img style=\"width:32px;height:32px;\" src=\"file:///android_asset/"+c.getIcon_path()+".png\"/></div></div>";
			}
		}
		s_channels = s_channels + "</div>";
		
		String s_programs = "";
		String s_programs_header = "<div id=\"programas\">";
		
		int numChannels = 1;
		
		for(Channel c : channels.getChannels()){
			if(BaseApplication.getWebserviceManager().isChannelFavourite(c)){
				numChannels++;
				
				List<Program> ps = BaseApplication.getWebserviceManager().getProgramList(c, DAY.TODAY).getProgrammes();
				float offset = minutesPassedFromMidnight(toTimestamp(ps.get(0).getStart()))*pixelsPerMinute+1; // Le metemos un pixel de más para crear un espacio
				s_programs = s_programs + "<div id=\"fila\" style=\"margin-left: "+offset+"px; width: #px; \">";
				int total_duration = 0;
				for(Program p : ps){
					int time = duration(toTimestamp(p.getStart()), toTimestamp(p.getStop()));
					total_duration = total_duration + time;
					s_programs = s_programs + "<div onclick=\"onclickbridge("+p.getIdBroadcast()+")\" id=\"hora\" style=\"width: "+time*3+"px;\"><div id=\"titulo\">"+p.getTitle()+"</div><div id=\"inicio\">"+Utils.parseEmisionTime(p.getStart())+" - "+Utils.parseEmisionTime(p.getStop())+"</div></div>";
				}
				s_programs = s_programs.replace("#", String.valueOf(((total_duration*3)-8)));
				s_programs = s_programs + "</div>";
			}
		}
		
		String s_programs_footer = "</div>";
		
		/* End of data */
		
		String footer = "<script>$(window).scroll(function(){ $('#channels').css({'left': $(this).scrollLeft() -2});}); $(document).ready(function(){ window.scrollTo("+(line_offset-25)*3+", 0); }); function onclickbridge(id){ window.SchedulerBridge.onProgramSelected(id); } </script></body></html>";
		String line = "<div id=\"linea\" style=\"left:"+line_offset*3+"px; height:"+((58*numChannels)-20)+"px; \"></div>";
		
		saveFile("scheduler.html", header + line + hours + s_channels + s_programs_header + s_programs + s_programs_footer + footer);
	}
	
	private static void saveFile(String name, String data){
		try {
			FileOutputStream fout = BaseApplication.getInstance().getApplicationContext().openFileOutput(name, Context.MODE_PRIVATE);
			fout.write(data.getBytes());
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static int duration(long a, long b){
		Duration duration = new Duration(a,b);
		return duration.toStandardMinutes().getMinutes();
	}
	
	public static int minutesPassedFromMidnight(long c){
		DateTime now = new DateTime(c);
		DateTime midnight = now.withTimeAtStartOfDay();
		Duration duration = new Duration(midnight,now);
		return duration.toStandardMinutes().getMinutes();
	}
	
	public static List<Channel> orderChannels(ChannelOrder order, List<Channel> channels){
		List<Channel> tmp = new ArrayList<Channel>();
		List<Channel> chn = channels;
		
		for(int i=0; i<order.getItems().size(); i++){
			int id = order.getItems().get(i).getId();
			for(int j=0; j<chn.size(); j++){
				if(chn.get(j).getId()==id){
					tmp.add(chn.get(j));
				}
			}
		}
		return tmp;
	}
	
	public static int getLanguage(String locale){
		
		if(locale.equals("ca")){
			return R.string.app_locale_ca;
		}else if(locale.equals("es")){
			return R.string.app_locale_es;
		}else if(locale.equals("gl")){
			return R.string.app_locale_gl;
		}else if(locale.equals("eu")){
			return R.string.app_locale_eu;
		}else if(locale.equals("en")){
			return R.string.app_locale_en;
		}else{
			return R.string.app_locale_xx;
		}
	}
	
	
}
