package es.ubiqua.zapptv.manager;

import java.util.List;

import es.ubiqua.zapptv.manager.impl.DatabaseManager;
import es.ubiqua.zapptv.manager.model.AppItems;
import es.ubiqua.zapptv.manager.model.Channel;
import es.ubiqua.zapptv.manager.model.ChannelList;
import es.ubiqua.zapptv.manager.model.ChannelOrder;
import es.ubiqua.zapptv.manager.model.ChannelStatus;
import es.ubiqua.zapptv.manager.model.Favourites;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.ProgramList;
import es.ubiqua.zapptv.manager.model.TVUrl;

public interface WebserviceManagerInterface {
	public boolean checkNewChannels();
	public ChannelList getChannelList();
	public Channel getChannel(Channel c);
	public String getScheduler();
	public ProgramList getProgramList(Channel c, DatabaseManager.DAY day);
	public ProgramList getListProgramsByAlarm();
	public ProgramList getListProgramsByScore();
	public ProgramList getListProgramsByCheckin();
	public ProgramList getProgramListFavourites();
	public ProgramList getProgramListRightNow();
	public TVUrl getTVUrl(Channel c);
	public Program getProgram(Program p);
	public boolean addCheckin(Program p);
	public int addScore(Program p, int score);
	public Program getFavourite(Program p);
	public void setAppRecommended(AppItems a);
	public List<AppItems> getAppsRecommended();
	public void setReminder(Program p);
	public void unsetReminder(Program p);
	public void setCheckin(Program p);
	public void unsetCheckin(Program p);
	public void setFavourite(Channel c);
	public void setFavourite(Program p);
	public void unsetFavourite(Channel c);
	public void unsetFavourite(Program p);
	public void saveReminder(Program p);
	public void deleteReminder(Program p);
	public void saveChannelOrder(ChannelOrder order);
	public ChannelOrder loadChannelOrder();
	public void saveChannelStatus(ChannelStatus status);
	public ChannelStatus loadChannelStatus();
	public Favourites loadFavourites();
	public void saveFavourites(Favourites favourites);
	public boolean isChannelFavourite(Channel c);
	public boolean isProgramFavourite(Program p);
	public boolean isProgramChecked(Program p);
	public boolean isProgramReminded(Program p);
	public boolean isAppRecommended(AppItems a);
}
