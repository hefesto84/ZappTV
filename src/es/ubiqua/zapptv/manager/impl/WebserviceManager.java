package es.ubiqua.zapptv.manager.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import android.content.Context;
import android.util.Log;
import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.manager.WebserviceManagerInterface;
import es.ubiqua.zapptv.manager.impl.DatabaseManager.DAY;
import es.ubiqua.zapptv.manager.model.AppItems;
import es.ubiqua.zapptv.manager.model.Channel;
import es.ubiqua.zapptv.manager.model.ChannelList;
import es.ubiqua.zapptv.manager.model.ChannelOrder;
import es.ubiqua.zapptv.manager.model.ChannelStatus;
import es.ubiqua.zapptv.manager.model.Favourites;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.ProgramList;
import es.ubiqua.zapptv.manager.model.TVUrl;

public class WebserviceManager implements WebserviceManagerInterface {
	
	public WebserviceManager(){
		
	}
	
	public boolean checkNewChannels() {
		// TODO: Check if new channels are available. 
		return false;
	}

	public ChannelList getChannelList() {
		ChannelList list = new ChannelList();
		list.setChannels(BaseApplication.getDatabaseManager().getListChannels());
		return list;
	}

	public Channel getChannel(Channel c) {
		// TODO Returns a complete data by a given channel
		return new Channel();
	}

	public boolean addCheckin(Program p) {
		
		//SendCheckinDataAsyncTask sendCheckin = new SendCheckinDataAsyncTask(BaseApplication.getInstance().getApplicationContext());
		//sendCheckin.execute(p);

		return false;
	}

	public int addScore(Program p, int score) {
		// TODO Auto-generated method stub
		return 0;
	}

	public ProgramList getProgramList(Channel c, DatabaseManager.DAY day){
		ProgramList list = new ProgramList();
		list.setProgrammes(BaseApplication.getDatabaseManager().getListPrograms(c,day));
		return list;
	}

	public Program getProgram(Program p) {
		Program program = new Program();
		program = BaseApplication.getDatabaseManager().getProgram(p);
		return program;
	}

	public ProgramList getProgramListRightNow() {
		ProgramList list = new ProgramList();
		list.setProgrammes(BaseApplication.getDatabaseManager().getListProgramsRightNow());
		return list;
	}

	public ProgramList getProgramListFavourites() {
		ProgramList list = new ProgramList();
		list.setProgrammes(BaseApplication.getDatabaseManager().getListProgramsFavourites());
		return list;
	}

	public void setFavourite(Channel c) {
		BaseApplication.getDatabaseManager().setFavourite(c);
	}

	public void setFavourite(Program p) {
		BaseApplication.getDatabaseManager().setFavourite(p);
	}
	
	public void unsetFavourite(Channel c) {
		BaseApplication.getDatabaseManager().unsetFavourite(c);
	}

	public void unsetFavourite(Program p) {
		BaseApplication.getDatabaseManager().unsetFavourite(p);
	}

	public ProgramList getListProgramsByScore() {
		ProgramList list = new ProgramList();
		List<Program> programs = BaseApplication.getDatabaseManager().getListProgramsByScore();
		list.setProgrammes(programs);
		return list;
	}
	
	public ProgramList getListProgramsByCheckin(){
		ProgramList list = new ProgramList();
		List<Program> programs = BaseApplication.getDatabaseManager().getListProgramsByCheckin();
		list.setProgrammes(programs);
		return list;
	}

	public Program getFavourite(Program p) {
		return BaseApplication.getDatabaseManager().getFavourite(p);
	}

	public String getScheduler() {
		String header = "<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"></head>";
		String header_body = "<body><table>";
		String content = "";
		List<Channel> channels = BaseApplication.getDatabaseManager().getListChannels();
		for(Channel c : channels){
			Log.d(BaseApplication.TAG,"C: "+c.getFull_path());
			String entry = "";
			String fields = "";
			ProgramList programs = BaseApplication.getWebserviceManager().getProgramList(c, DAY.TODAY);
			for(Program p : programs.getProgrammes()){
				String field = "<td>"+p.getTitle()+"</td>";
				fields = fields + field;
			}
			entry = "<tr><td><img src=\""+c.getFull_path()+"\"/></td>" + fields + "</tr>";
			content = content + entry;
		}
		String footer_body = "</table></body>";
		String footer = "</html>";
		return header + header_body + content + footer_body + footer;
	}

	public ProgramList getListProgramsByAlarm() {
		ProgramList list = new ProgramList();
		List<Program> programs = BaseApplication.getDatabaseManager().getListProgramsByAlarm();
		list.setProgrammes(programs);
		return list;
	}

	public void saveReminder(Program p) {
		BaseApplication.getDatabaseManager().saveReminder(p);
	}

	public void deleteReminder(Program p) {
		BaseApplication.getDatabaseManager().deleteReminder(p);
	}

	@Override
	public void saveChannelOrder(ChannelOrder order) {
		BaseApplication.getDatabaseManager().saveChannelOrder(order);
	}

	@Override
	public ChannelOrder loadChannelOrder() {
		return BaseApplication.getDatabaseManager().loadChannelOrder();
	}

	@Override
	public boolean isChannelFavourite(Channel c) {
		return BaseApplication.getDatabaseManager().isChannelFavourite(c);
	}

	@Override
	public void saveChannelStatus(ChannelStatus status) {
		BaseApplication.getDatabaseManager().saveChannelStatus(status);
	}

	@Override
	public ChannelStatus loadChannelStatus() {
		return BaseApplication.getDatabaseManager().loadChannelStatus();
	}

	@Override
	public Favourites loadFavourites() {
		return BaseApplication.getDatabaseManager().loadFavourites();
	}

	@Override
	public void saveFavourites(Favourites favourites) {
		BaseApplication.getDatabaseManager().saveFavourites(favourites);
	}

	@Override
	public boolean isProgramFavourite(Program p) {
		return BaseApplication.getDatabaseManager().isProgramFavourite(p);
	}

	@Override
	public void setCheckin(Program p) {
		BaseApplication.getDatabaseManager().setCheckin(p);
	}

	@Override
	public void unsetCheckin(Program p) {
		BaseApplication.getDatabaseManager().unsetCheckin(p);
	}

	@Override
	public boolean isProgramChecked(Program p) {
		return BaseApplication.getDatabaseManager().isProgramChecked(p);
	}

	@Override
	public void setReminder(Program p) {
		BaseApplication.getDatabaseManager().setReminder(p);
	}

	@Override
	public void unsetReminder(Program p) {
		BaseApplication.getDatabaseManager().unsetReminder(p);
	}

	@Override
	public boolean isProgramReminded(Program p) {
		return BaseApplication.getDatabaseManager().isProgramReminded(p);
	}

	@Override
	public void setAppRecommended(AppItems a) {
		BaseApplication.getDatabaseManager().setAppRecommended(a);
	}

	@Override
	public boolean isAppRecommended(AppItems a) {
		return BaseApplication.getDatabaseManager().isAppRecommended(a);
	}

	@Override
	public List<AppItems> getAppsRecommended() {
		return BaseApplication.getDatabaseManager().getAppsRecommended();
	}

	@Override
	public TVUrl getTVUrl(Channel c) {
		return BaseApplication.getDatabaseManager().getTVUrl(c);
	}

}
