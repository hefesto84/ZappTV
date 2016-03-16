package es.ubiqua.zapptv.manager.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.AvoidXfermode.Mode;
import android.util.Log;
import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.helpers.ChannelsDB;
import es.ubiqua.zapptv.helpers.ProgramsDB;
import es.ubiqua.zapptv.manager.DatabaseManagerInterface;
import es.ubiqua.zapptv.manager.model.AppItems;
import es.ubiqua.zapptv.manager.model.Channel;
import es.ubiqua.zapptv.manager.model.ChannelOrder;
import es.ubiqua.zapptv.manager.model.ChannelStatus;
import es.ubiqua.zapptv.manager.model.Checkin;
import es.ubiqua.zapptv.manager.model.Favourites;
import es.ubiqua.zapptv.manager.model.Score;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.TVUrl;
import es.ubiqua.zapptv.model.DynamicChannelModel;
import es.ubiqua.zapptv.utils.Utils;

public class DatabaseManager implements DatabaseManagerInterface {
	
	private ChannelsDB channelsDB;
	private ProgramsDB programsDB;
	
	public enum DAY {TODAY,TOMORROW,OVERMORROW};
	
	public DatabaseManager(){
		
	}

	public void initialize() {
		initializeChannelsDB();
		initializeProgramsDB();
	}
	
	public Program getFavourite(Program p){
		Gson gson = new Gson();
		SharedPreferences prefs = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		String program = prefs.getString(String.valueOf(p.getIdProgramme()), gson.toJson(new Program()));
		return gson.fromJson(program, Program.class);
	}
	
	public List<Program> getListProgramsFavourites() {
		List<Program> programs = new ArrayList<Program>();
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		Map<String,?> keys = preferences.getAll();
		for(Map.Entry<String,?> entry : keys.entrySet()){
			if(entry.getKey().contains("p-")){
				Program p = new Gson().fromJson(entry.getValue().toString(),Program.class);
				programs.add(p);
			}
		}
		return programs;
	}
	
	public List<AppItems> getAppsRecommended() {
		List<AppItems> items = new ArrayList<AppItems>();
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		Map<String,?> keys = preferences.getAll();
		for(Map.Entry<String,?> entry : keys.entrySet()){
			if(entry.getKey().contains("a-")){
				AppItems i = new Gson().fromJson(entry.getValue().toString(), AppItems.class);
				items.add(i);
			}
		}
		return items;
	}
	
	private Set<String> getFavouritesIdentifiers(){
		SharedPreferences prefs = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		Set<String> set = new HashSet<String>();
		set = prefs.getStringSet("programs", new HashSet<String>());
		return set;
	}
	
	private Set<String> getAlarmsIdentifiers(){
		SharedPreferences prefs = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		Set<String> set = new HashSet<String>();
		set = prefs.getStringSet("reminders", new HashSet<String>());
		return set;
	}
	
	private void initializeChannelsDB(){
		channelsDB = new ChannelsDB(BaseApplication.getInstance().getApplicationContext(), "zapptv_channels", null, 3);
		SQLiteDatabase db = channelsDB.getWritableDatabase();
		
		db.execSQL("DELETE FROM Channels");
		
		if(db!=null){
			db.beginTransaction();
				Channel[] channels = getChannelsFromJSON();
				for(Channel channel : channels){
					db.execSQL(
		    				"INSERT INTO Channels (id,language,name,icon_path) "
		    				+ "VALUES (" 
		    				+ channel.getId() + ",'"
		    				+ channel.getLanguage()+"','"
		    				+ channel.getName()+"','"
		    				+ channel.getIcon_path()+"')"
		    		);
				}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		channelsDB.close();
	}
	
	private void initializeProgramsDB(){

		programsDB = new ProgramsDB(BaseApplication.getInstance().getApplicationContext(), "zapptv_programs", null, 8);
		SQLiteDatabase db = programsDB.getWritableDatabase();
		
		db.execSQL("DELETE FROM Programs");

		if(db!=null){
			for(int i=0; i<2; i++){
				db.beginTransaction();
					Program[] programs = getProgramsFromJSON(i+"_programs.json");	
					for(Program program : programs){
						String query = "";
						query = 
							"INSERT INTO Programs (id,channel,start,stop,lang,title,subtitle,description,categories,broadcast,presenters,guests,actors,directors,year,country,rating,image_path,day,timestamp_a,timestamp_b) "
		    				+ "VALUES (" 
		    				+ program.getIdProgramme() + ","
		    				+ program.getChannel() + ",'"
		    				+ program.getStart()+"','"
		    				+ program.getStop()+"','"
		    				+ program.getLanguage()+"','"
		    				+ (program.getTitle()).replace("'", "`")+"','"
		    				+ (program.getSubtitle()).replace("'", "`")+"','"
		    				+ (program.getDescription()).replace("'", "`")+"','"
		    				+ Utils.toSimpleArray(program.getCategories())+"',"
		    				+ program.getIdBroadcast() + ",'"
		    				+ Utils.toSimpleArray(program.getPresenters())+"','"	
		    				+ Utils.toSimpleArray(program.getGuests())+"','"
		    				+ Utils.toSimpleArray(program.getActors())+"','"
		    				+ Utils.toSimpleArray(program.getDirectors())+"',"
		    				+ program.getDate()+",'"
		    				+ program.getCountry()+"','"
		    				+ program.getRating()+"','"
		    				+ program.getImage_path()+"',"
		    				+ i + ","
		    				+ Utils.toTimestamp(program.getStart())+","
		    				+ Utils.toTimestamp(program.getStop()) + ")";
						//Log.d(BaseApplication.TAG,"QUERY: "+query);
						db.execSQL(query);
					}
				db.setTransactionSuccessful();
				db.endTransaction();
			}	
		}
		programsDB.close();
	}

	public List<Channel> getListChannels() {
		
		List<Channel> channels = new ArrayList<Channel>();
		
		SQLiteDatabase db = channelsDB.getReadableDatabase();
		if(db!=null){
			Cursor c = db.rawQuery("SELECT * FROM Channels",null);
			if(c.moveToFirst()){
				do{
					Channel channel = new Channel();
					channel.setId(c.getInt(0));
					channel.setLanguage(c.getString(1));
					channel.setName(c.getString(2));
					channel.setIcon_path(c.getString(3));
					/* Workaround porque a veces nos viene un icon_path invalido */
					if(channel.getIcon_path().length() > 2){
						channels.add(channel);
					}
					
				}while(c.moveToNext());
			}
			c.close();
		}
		db.close();
		
		ChannelOrder order = loadChannelOrder();
		
		if(order!=null){
			List<Channel> tmp = new ArrayList<Channel>();
			for(int i = 0; i<order.getItems().size(); i++){
				for(int j = 0; j<channels.size(); j++){
					if(channels.get(j).getName().contains(order.getItems().get(i).getName())){
						tmp.add(channels.get(j));
					}
				}
			}
			return tmp;
		}else{
			for(int i = 0; i<6; i++){
				setFavourite(channels.get(i));
			}
		}
		return channels;
	}
	
	public List<Program> getListProgramsByCheckin(){
		List<Program> programs = new ArrayList<Program>();
		SQLiteDatabase db = programsDB.getReadableDatabase();
		
		if(db!=null){
			
			/* Creamos query */
			List<Checkin> checkins = getCheckinFromJSON();
			String query = "SELECT * FROM Programs ";
			if(checkins.size()!=0){
				query = query + " WHERE id IN (";
				
				for(Checkin c : checkins){
					query = query +c.getIdProgram()+",";
				}
				query = query + ") GROUP BY id";
				query = query.replace(",)", ")");
			}
			
			Cursor c = db.rawQuery(query, null);
			if(c.moveToFirst()){
				do{
					Program program = new Program();
					program.setIdProgramme(c.getInt(0));
					program.setChannel(c.getInt(1));
					program.setStart(c.getString(2));
					program.setStop(c.getString(3));
					program.setLanguage(c.getString(4));
					program.setTitle(c.getString(5).replace("`", "'"));
					program.setIdBroadcast(c.getInt(9));
					program.setSubtitle(c.getString(6).replace("`","'"));
					program.setTimestamp_a(c.getLong(19));
					program.setTimestamp_b(c.getLong(20));
					program.setImage_path(c.getString(17));
					programs.add(program);
				}while(c.moveToNext());
			}
			c.close();
		}
		db.close();
		return programs;
	}
	
	public List<Program> getListProgramsByScore() {
		List<Program> programs = new ArrayList<Program>();
		SQLiteDatabase db = programsDB.getReadableDatabase();
		
		if(db!=null){
			/* Creamos query */
			List<Score> scores = getScoreFromJSON();
			String query = "SELECT * FROM Programs ";
			if(scores.size()!=0){
				query = query + " WHERE id IN (";
				
				for(Score c : scores){
					query = query +c.getIdProgram()+",";
				}
				query = query + ") GROUP BY id";
				query = query.replace(",)", ")");
			}
			
			Cursor c = db.rawQuery(query, null);
			if(c.moveToFirst()){
				do{
					Program program = new Program();
					program.setIdProgramme(c.getInt(0));
					program.setChannel(c.getInt(1));
					program.setStart(c.getString(2));
					program.setStop(c.getString(3));
					program.setLanguage(c.getString(4));
					program.setTitle(c.getString(5).replace("`", "'"));
					program.setIdBroadcast(c.getInt(9));
					program.setSubtitle(c.getString(6).replace("`","'"));
					program.setTimestamp_a(c.getLong(19));
					program.setTimestamp_b(c.getLong(20));
					program.setImage_path(c.getString(17));
					programs.add(program);
				}while(c.moveToNext());
			}
			c.close();
		}
		db.close();
		return programs;
	}
	
	public List<Program> getListProgramsRightNow() {
		List<Program> programs = new ArrayList<Program>();
		SQLiteDatabase db = programsDB.getReadableDatabase();
		long current = Calendar.getInstance().getTimeInMillis();
		if(db!=null){
			Cursor c = db.rawQuery("SELECT * FROM Programs WHERE timestamp_a < "+current+" AND timestamp_b > "+current+"",null);
			if(c.moveToFirst()){
				do{
					Program program = new Program();
					program.setIdProgramme(c.getInt(0));
					program.setChannel(c.getInt(1));
					program.setStart(c.getString(2));
					program.setStop(c.getString(3));
					program.setLanguage(c.getString(4));
					program.setTitle(c.getString(5).replace("`", "'"));
					program.setIdBroadcast(c.getInt(9));
					program.setSubtitle(c.getString(6).replace("`","'"));
					program.setTimestamp_a(c.getLong(19));
					program.setTimestamp_b(c.getLong(20));
					programs.add(program);
				}while(c.moveToNext());
			}
			c.close();
		}
		db.close();
		return programs;
	}

	public List<Program> getListPrograms(Channel channel, DatabaseManager.DAY day){
		List<Program> programs = new ArrayList<Program>();
		SQLiteDatabase db = programsDB.getReadableDatabase();
		
		int d = 0;
		if(day==DAY.TODAY){ d = 0; }
		if(day==DAY.TOMORROW){ d = 1; }
		if(day==DAY.OVERMORROW){ d = 2; }
		
		if(db!=null){
			Cursor c = db.rawQuery("SELECT * FROM Programs WHERE channel = "+channel.getId()+" and day = "+d, null);
			if(c.moveToFirst()){
				do{
					Program program = new Program();
					program.setIdProgramme(c.getInt(0));
					program.setChannel(c.getInt(1));
					program.setStart(c.getString(2));
					program.setStop(c.getString(3));
					program.setLanguage(c.getString(4));
					program.setTitle(c.getString(5).replace("`", "'"));
					program.setDescription(c.getString(7));
					program.setIdBroadcast(c.getInt(9));
					program.setRating(c.getString(16));
					programs.add(program);
				}while(c.moveToNext());
			}
			c.close();
		}
		db.close();
		return programs;
	}
	
	public Program getProgram(Program p) {
		Program program = new Program();
		int channelId = 0;
		SQLiteDatabase db = programsDB.getReadableDatabase();
		if(db!=null){
			Cursor c = null;
			
			if(p.getIdBroadcast()!=0){
				c = db.rawQuery("SELECT * FROM Programs WHERE broadcast = "+p.getIdBroadcast(),null);
			}
			if(p.getIdProgramme()!=0){
				c = db.rawQuery("SELECT * FROM Programs WHERE id = "+p.getIdProgramme(),null);
			}
			
			if(c.moveToFirst()){
				do{
					
					program.setIdProgramme(c.getInt(0));
					channelId = c.getInt(1);
					program.setChannel(c.getInt(1));
					program.setStart(c.getString(2));
					program.setStop(c.getString(3));
					program.setLanguage(c.getString(4));
					program.setTitle(c.getString(5).replace("`", "'"));
					program.setDescription(c.getString(7).replace("`", "'"));
					program.setCategory(Utils.fromSimpleArray(c.getString(8)));	
					program.setPresenter(Utils.fromSimpleArray(c.getString(10))); 
					program.setGuest(Utils.fromSimpleArray(c.getString(11)));
					program.setActor(Utils.fromSimpleArray(c.getString(12)));
					program.setDirector(Utils.fromSimpleArray(c.getString(13)));
					program.setImage_path(c.getString(17));
					program.setRating(c.getString(16));
					program.setIdBroadcast(c.getInt(9));
					program.setSubtitle(c.getString(6).replace("`","'"));
					program.setTimestamp_a(c.getLong(19));
					program.setTimestamp_b(c.getLong(20));
					
				}while(c.moveToNext());
			}
			c.close();
		}
		db.close();
		
		for(Channel channel : getListChannels()){
			if(channel.getId()==channelId){
				program.setLanguage(channel.getLanguage());
			}
		}
		
		return program;
	}
	
    public List<Program> getListProgramsByAlarm() {
		List<Program> programs = new ArrayList<Program>();
		SQLiteDatabase db = programsDB.getReadableDatabase();
		
		if(db!=null){
			Cursor c = db.rawQuery("SELECT * FROM Programs GROUP BY id", null);
			if(c.moveToFirst()){
				do{
					Program program = new Program();
					program.setIdProgramme(c.getInt(0));
					program.setChannel(c.getInt(1));
					program.setStart(c.getString(2));
					program.setStop(c.getString(3));
					program.setLanguage(c.getString(4));
					program.setTitle(c.getString(5).replace("`", "'"));
					program.setIdBroadcast(c.getInt(9));
					program.setSubtitle(c.getString(6).replace("`","'"));
					program.setTimestamp_a(c.getLong(19));
					program.setTimestamp_b(c.getLong(20));
					program.setImage_path(c.getString(17));
					
					if(isProgramReminded(program)){
						programs.add(program);
					}
					
				}while(c.moveToNext());
			}
			c.close();
		}
		db.close();
		return programs;
	}

	public void deleteReminder(Program p) {
		
		SharedPreferences prefs = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		Set<String> set = new HashSet<String>();
		set = prefs.getStringSet("reminders", new HashSet<String>());
		
		if(set.contains(String.valueOf(p.getIdBroadcast()))){
			set.remove(String.valueOf(p.getIdBroadcast()));
		}
		
		SharedPreferences.Editor editor = prefs.edit();
		//editor.clear();
		Gson gson = new Gson();
		String serializedReminder = gson.toJson(p);
		editor.putStringSet("reminders", set);
		editor.putString(String.valueOf(p.getIdBroadcast()), serializedReminder);
		editor.commit();
	}

	public void saveReminder(Program p) {
		SharedPreferences prefs = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		Set<String> set = new HashSet<String>();
		set = prefs.getStringSet("reminders", new HashSet<String>());
		
		if(!set.contains(String.valueOf(p.getIdBroadcast()))){
			set.add(String.valueOf(p.getIdBroadcast()));
		}
		
		SharedPreferences.Editor editor = prefs.edit();
		//editor.clear();
		Gson gson = new Gson();
		String serializedReminder = gson.toJson(p);
		editor.putStringSet("reminders", set);
		editor.putString(String.valueOf(p.getIdBroadcast()), serializedReminder);
		editor.commit();
	}
	
	public void setFavourite(Program p) {
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		String id = String.valueOf(p.getIdProgramme());
		SharedPreferences.Editor editor = preferences.edit();
		String program = new Gson().toJson(p);
		editor.putString("p-"+id, program);
		editor.commit();
	}

	public boolean isChannelFavourite(Channel c) {
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		return preferences.contains("c-"+String.valueOf(c.getId()));
	}
	
	public void setCheckin(Program p) {
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		String id = String.valueOf(p.getIdProgramme());
		SharedPreferences.Editor editor = preferences.edit();
		String program = new Gson().toJson(p);
		editor.putString("ch-"+id, program);
		editor.commit();
	}

	public void unsetCheckin(Program p) {
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove("ch-"+p.getIdProgramme());
		editor.commit();
	}

	public void setReminder(Program p) {
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		String id = String.valueOf(p.getIdProgramme());
		SharedPreferences.Editor editor = preferences.edit();
		String program = new Gson().toJson(p);
		editor.putString("r-"+id, program);
		editor.commit();
	}

	public void unsetReminder(Program p) {
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove("r-"+p.getIdProgramme());
		editor.commit();
	}
	
	public void setFavourite(Channel c) {
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("c-"+c.getId(), true);
		editor.commit();
	}
	
	public void unsetFavourite(Channel c){
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove("c-"+c.getId());
		editor.commit();
	}
	
	public void unsetFavourite(Program p){
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		String id = String.valueOf("p-"+p.getIdProgramme());
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove(id);
		editor.commit();
	}
	
	public void setAppRecommended(AppItems a) {
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		String id = String.valueOf(a.getResponse()[0].getId());
		SharedPreferences.Editor editor = preferences.edit();
		String app = new Gson().toJson(a);
		editor.putString("a-"+id,app);
		editor.commit();
	}
	
	@Override
	public void saveChannelOrder(ChannelOrder order) {
		SharedPreferences prefs = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		Gson gson = new Gson();
		String sOrder = gson.toJson(order);
		editor.putString("order_1", sOrder);
		editor.commit();
	}
	
	@Override
	public void saveChannelStatus(ChannelStatus status) {
		Gson gson = new Gson();
		String sStatus = gson.toJson(status);
		try {
			FileOutputStream fos = BaseApplication.getInstance().getApplicationContext().openFileOutput("channel-status.json", Context.MODE_PRIVATE);
			fos.write(sStatus.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			Log.e(BaseApplication.TAG,e.getMessage());
		} catch (IOException e) {
			Log.e(BaseApplication.TAG,e.getMessage());
		}
	}
	
	@Override
	public ChannelStatus loadChannelStatus() {
		
		boolean result = false;
		ChannelStatus channelStatus = null;
		
		Gson gson = new Gson();
		
		try {
			FileInputStream fis = BaseApplication.getInstance().getApplicationContext().openFileInput("channel-status.json");
			StringBuffer sb = new StringBuffer("");
			int n = 0;
			byte[] buffer = new byte[1024];
			while((n = fis.read(buffer))!=-1){
				sb.append(new String(buffer,0,n));
			}
			fis.close();
			result = true; 
			channelStatus = gson.fromJson(sb.toString(),ChannelStatus.class);
		} catch (FileNotFoundException e) {
			result = false;
			channelStatus = new ChannelStatus();
		} catch (IOException e) {
			result = false;
			channelStatus = new ChannelStatus();
		}
		if(result){
			return channelStatus;
		}
		saveChannelStatus(channelStatus);
		return channelStatus;
	}
	
	@Override
	public ChannelOrder loadChannelOrder() {
		SharedPreferences prefs = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		if(prefs.contains("order_1")){
			return new Gson().fromJson(prefs.getString("order_1",""),ChannelOrder.class);
		}
		return null;
	}

	public Favourites loadFavourites() {
		boolean result = false;
		Favourites favourites = null;
		
		Gson gson = new Gson();
		
		try {
			FileInputStream fis = BaseApplication.getInstance().getApplicationContext().openFileInput("favourites.json");
			StringBuffer sb = new StringBuffer("");
			int n = 0;
			byte[] buffer = new byte[1024];
			while((n = fis.read(buffer))!=-1){
				sb.append(new String(buffer,0,n));
			}
			fis.close();
			result = true; 
			favourites = gson.fromJson(sb.toString(),Favourites.class);
		} catch (FileNotFoundException e) {
			result = false;
			favourites = new Favourites();
		} catch (IOException e) {
			result = false;
			favourites = new Favourites();
		}
		if(result){
			return favourites;
		}
		saveFavourites(favourites);
		return favourites;
	}

	public void saveFavourites(Favourites favourites) {
		Gson gson = new Gson();
		String sFavourites = gson.toJson(favourites);
		
		try {
			FileOutputStream fos = BaseApplication.getInstance().getApplicationContext().openFileOutput("favourites.json", Context.MODE_PRIVATE);
			fos.write(sFavourites.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			Log.e(BaseApplication.TAG,e.getMessage());
		} catch (IOException e) {
			Log.e(BaseApplication.TAG,e.getMessage());
		}
	}
	
	private List<Checkin> getCheckinFromJSON(){
		List<Checkin> mCheckins = new ArrayList<Checkin>();
		Checkin[] checkins = null;
		FileInputStream fin;
		try {
			fin = BaseApplication.getInstance().getApplicationContext().openFileInput("checkin.json");
			Gson gson = new Gson();		
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));
			checkins = (Checkin[])gson.fromJson(br, Checkin[].class);
			fin.close();
			br.close();
		} catch (FileNotFoundException e) {
			checkins = new Checkin[0];
		} catch (IOException e) {
			checkins = new Checkin[0];
		}
		
		for(int i = 0; i<checkins.length; i++){
			Checkin c = new Checkin();
			c.setIdProgram(checkins[i].getIdProgram());
			c.setTimes(checkins[i].getTimes());
			mCheckins.add(c);
		}
		
		return mCheckins;
	}
	
	private List<Score> getScoreFromJSON(){
		List<Score> score = new ArrayList<Score>();
		Score[] favourites = null;
		FileInputStream fin;
		try {
			fin = BaseApplication.getInstance().getApplicationContext().openFileInput("score.json");
			Gson gson = new Gson();		
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));
			favourites = (Score[])gson.fromJson(br, Score[].class);
			fin.close();
			br.close();
		} catch (FileNotFoundException e) {
			favourites = new Score[0];
		} catch (IOException e) {
			favourites = new Score[0];
		}
		for(int i = 0; i<favourites.length; i++){
			Score c = new Score();
			c.setIdProgram(favourites[i].getIdProgram());
			c.setScore(favourites[i].getScore());
			score.add(c);
		}
		return score;
	}
	
	private Channel[] getChannelsFromJSON(){
		Channel[] channels = null;
		FileInputStream fin;
		try {
			fin = BaseApplication.getInstance().getApplicationContext().openFileInput("channels.json");
			Gson gson = new Gson();		
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));
			channels = (Channel[])gson.fromJson(br, Channel[].class);
			fin.close();
			br.close();
		} catch (FileNotFoundException e) {
			channels = new Channel[0];
		} catch (IOException e) {
			channels = new Channel[0];
		}
		return channels;
	}

	public TVUrl getTVUrl(Channel c) {
		
		TVUrl[] tvurl = null;
		FileInputStream fin;
		
		try {
			fin = BaseApplication.getInstance().getApplicationContext().openFileInput("tvurls.json");
			Gson gson = new Gson();
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));
			tvurl = (TVUrl[])gson.fromJson(br, TVUrl[].class);
			fin.close();
			br.close();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		
		for(int i=0; i<tvurl.length; i++){
			if(c.getId()==tvurl[i].getId()){
				return tvurl[i];
			}
		}
		return null;
	}
	
	private Program[] getProgramsFromJSON(String file){
		Program[] programs = null;
		FileInputStream fin;
		try {
			fin = BaseApplication.getInstance().getApplicationContext().openFileInput(file);
			Gson gson = new Gson();		
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));
			programs = (Program[])gson.fromJson(br, Program[].class);
			fin.close();
			br.close();
		} catch (FileNotFoundException e) {
			//BaseApplication.getEventsManager().show("Error cargando datos de programas.: "+e.getMessage());
			programs = new Program[0];
		} catch (IOException e) {
			//BaseApplication.getEventsManager().show("Error leyendo datos de programas.: "+e.getMessage());
			programs = new Program[0];
		}
		return programs;
	}

	public boolean isProgramFavourite(Program p) {
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		return preferences.contains("p-"+String.valueOf(p.getIdProgramme()));
	}

	public boolean isProgramChecked(Program p) {
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		return preferences.contains("ch-"+String.valueOf(p.getIdProgramme()));
	}
	
	public boolean isProgramReminded(Program p) {
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		return preferences.contains("r-"+String.valueOf(p.getIdProgramme()));
	}

	public boolean isAppRecommended(AppItems a) {
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("zapptv_preferences", Context.MODE_PRIVATE);
		return preferences.contains("a-"+String.valueOf(a.getResponse()[0].getId()));
	}
	
}
