package es.ubiqua.zapptv.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ProgramsDB extends SQLiteOpenHelper{

	String sqlCreate = "CREATE TABLE Programs ("
			+ "id INTEGER, "
			+ "channel INTEGER, "
			+ "start TEXT, "
			+ "stop TEXT, "
			+ "lang TEXT, "
			+ "title TEXT, "
			+ "subtitle TEXT, "
			+ "description TEXT, "
			+ "categories TEXT, "
			+ "broadcast INTEGER, "
			+ "presenters TEXT, "
			+ "guests TEXT, "
			+ "actors TEXT, "
			+ "directors TEXT, "
			+ "year INTEGER, "
			+ "country TEXT, "
			+ "rating TEXT, "
			+ "image_path TEXT,"
			+ "day INTEGER,"
			+ "timestamp_a INTEGER,"
			+ "timestamp_b INTEGER"
			+ ")";
	
	public ProgramsDB(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Programs");
		db.execSQL(sqlCreate);
	}

}
