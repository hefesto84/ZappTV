package es.ubiqua.zapptv.manager;

import java.util.List;

import es.ubiqua.zapptv.manager.impl.DatabaseManager;
import es.ubiqua.zapptv.manager.model.AppItems;
import es.ubiqua.zapptv.manager.model.Channel;
import es.ubiqua.zapptv.manager.model.ChannelOrder;
import es.ubiqua.zapptv.manager.model.ChannelStatus;
import es.ubiqua.zapptv.manager.model.Favourites;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.TVUrl;

public interface DatabaseManagerInterface {
	public void initialize();
	public List<Channel> getListChannels();
	public List<Program> getListProgramsFavourites();
	public List<Program> getListProgramsByAlarm();
	public List<Program> getListProgramsByScore();
	public List<Program> getListProgramsByCheckin();
	public List<Program> getListPrograms(Channel c, DatabaseManager.DAY day);
	public List<Program> getListProgramsRightNow();
	public TVUrl getTVUrl(Channel c);
	public Program getProgram(Program p);
	public Program getFavourite(Program p);
	public List<AppItems> getAppsRecommended();
	public void setAppRecommended(AppItems a);
	public void setReminder(Program p);
	public void unsetReminder(Program p);
	public void setFavourite(Program p);
	public void setFavourite(Channel c);
	public void unsetFavourite(Program p);
	public void unsetFavourite(Channel c);
	public void setCheckin(Program p);
	public void unsetCheckin(Program p);
	public void deleteReminder(Program p);
	public void saveReminder(Program p);
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
