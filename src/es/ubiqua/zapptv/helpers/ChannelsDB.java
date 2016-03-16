package es.ubiqua.zapptv.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ChannelsDB extends SQLiteOpenHelper{

	String sqlCreate = "CREATE TABLE Channels (id INTEGER, language TEXT, name TEXT, icon_path TEXT)";
	
	public ChannelsDB(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int pre, int post) {
		db.execSQL("DROP TABLE IF EXISTS Channels");
		db.execSQL(sqlCreate);
	}

}
