package es.ubiqua.zapptv.ui;

import java.util.Random;

import com.google.gson.Gson;
import com.loopj.android.image.SmartImageView;

import es.ubiqua.zapptv.BaseApplication;
import es.ubiqua.zapptv.R;
import es.ubiqua.zapptv.listeners.OnSocialListener;
import es.ubiqua.zapptv.manager.model.Favourites;
import es.ubiqua.zapptv.manager.model.Program;
import es.ubiqua.zapptv.manager.model.Score;
import es.ubiqua.zapptv.manager.model.ShareModel;
import es.ubiqua.zapptv.tasks.SendCheckinDataAsyncTask;
import es.ubiqua.zapptv.tasks.SendScoreDataAsyncTask;
import es.ubiqua.zapptv.tasks.SendShareModelAsyncTask;
import es.ubiqua.zapptv.utils.Utils;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProgramDetailFragment extends BaseFragment implements OnSocialListener, OnClickListener{
	
	private Bundle mFragmentArguments;
	private static final String BROADCAST_ARGUMENT = "broadcast";
	private static final String ID_ARGUMENT = "id";
	private Program mProgram;
	private SendCheckinDataAsyncTask mCheckinAsyncTask;
	private SendScoreDataAsyncTask mScoreAsyncTask;
	private SendShareModelAsyncTask mShareModelAsyncTask;
	private ShareModel mShareModel;
	private ProgressDialog mProgressDialog;
	private boolean isFavourite;
	private boolean isChecked;
	private boolean isReminded;
	private View mView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mFragmentArguments = this.getArguments();
		
		View v = (View)inflater.inflate(R.layout.program_detail, container,false);
		mView = v;
		mProgram = new Program();
	
		mProgram.setIdBroadcast(mFragmentArguments.getInt(BROADCAST_ARGUMENT));
		mProgram.setIdProgramme(mFragmentArguments.getInt(ID_ARGUMENT));
		
		mProgram = BaseApplication.getWebserviceManager().getProgram(mProgram);
		
		
		//Log.d(BaseApplication.TAG, "OBJECT: "+mProgram.toJson());
		isFavourite = BaseApplication.getWebserviceManager().isProgramFavourite(mProgram);
		isChecked = BaseApplication.getWebserviceManager().isProgramChecked(mProgram);
		isReminded = BaseApplication.getWebserviceManager().isProgramReminded(mProgram);
		
		TextView header = (TextView)v.findViewById(R.id.txtTitle);
		TextView title = (TextView)v.findViewById(R.id.txtTitleContent);
		TextView description = (TextView)v.findViewById(R.id.txtDescriptionContent);
		TextView category = (TextView)v.findViewById(R.id.txtCategoryContent);
		TextView rating = (TextView)v.findViewById(R.id.txtClassificationContent);
		TextView start = (TextView)v.findViewById(R.id.txtStartContent);
		TextView stop = (TextView)v.findViewById(R.id.txtStopContent);
		TextView presenters = (TextView)v.findViewById(R.id.txtPresentersContent);
		TextView guests = (TextView)v.findViewById(R.id.txtGuestsContent);
		TextView directors = (TextView)v.findViewById(R.id.txtDirectorsContent);
		TextView actors = (TextView)v.findViewById(R.id.txtActorsContent);
		TextView language = (TextView)v.findViewById(R.id.txtLanguageContent);
		RatingBar ratingBar = (RatingBar)v.findViewById(R.id.ratingBar);
		
		ratingBar.setProgress(/*new Random().nextInt((3 - 0) + 1) + 0*/3);
		mShareModel = new ShareModel(
				mProgram.getIdProgramme(),
				mProgram.getTitle(), 
				BaseApplication.WS_DOMAIN+"zapptv/"+mProgram.getIdProgramme(), 
				mProgram.getImage_path(), 
				mProgram.getDescription(), "");
		
		SmartImageView image = (SmartImageView)v.findViewById(R.id.imgProgram);
		
		header.setText(mProgram.getTitle());
		title.setText(mProgram.getTitle());
		description.setText(mProgram.getDescription());
		category.setText(mProgram.getCategory());
		rating.setText(mProgram.getRating());
		start.setText(Utils.parseEmisionTime(mProgram.getStart()));
		stop.setText(Utils.parseEmisionTime(mProgram.getStop()));
		language.setText(getString(Utils.getLanguage(mProgram.getLanguage())));
		
		if(!mProgram.getPresenter().equals("")){
			presenters.setText(mProgram.getPresenter());
		}else{
			(v.findViewById(R.id.detailItemPresenters)).setVisibility(View.GONE);
		}
		
		if(mProgram.getGuest().length()!=0){
			guests.setText(mProgram.getGuest());
		}else{
			(v.findViewById(R.id.detailItemGuests)).setVisibility(View.GONE);
		}
		
		if(mProgram.getDirector().length()!=0){
			directors.setText(mProgram.getDirector());	
		}else{
			(v.findViewById(R.id.detailItemDirectors)).setVisibility(View.GONE);
		}
		
		if(mProgram.getActor().length()!=0){
			actors.setText(mProgram.getActor());
		}else{
			(v.findViewById(R.id.detailItemActors)).setVisibility(View.GONE);
		}

	
		((RelativeLayout)v.findViewById(R.id.imgAlarmLayout)).setOnClickListener(this);
		((RelativeLayout)v.findViewById(R.id.imgFavouriteLayout)).setOnClickListener(this);
		((RelativeLayout)v.findViewById(R.id.imgCheckinLayout)).setOnClickListener(this);
		((RelativeLayout)v.findViewById(R.id.imgShareLayout)).setOnClickListener(this);
		((RatingBar)v.findViewById(R.id.ratingBar)).setOnClickListener(this);
		
		if(isFavourite){
			((ImageView)v.findViewById(R.id.imgFavourite)).setImageResource(R.drawable.favoritos_sel);
		}
		
		if(isChecked){
			((ImageView)v.findViewById(R.id.imgCheckin)).setImageResource(R.drawable.check_sel);
		}
		
		if(isReminded){
			((ImageView)v.findViewById(R.id.imgAlarm)).setImageResource(R.drawable.alarma_sel);
		}
		
		try{
			image.setImageUrl(mProgram.getImage_path());
		}catch(Exception e){
			//BaseApplication.getEventsManager().show("ERROR: "+e.getMessage());
		}
		
		BaseApplication.getEventsManager().sendEvent(R.string.app_ga_programdetail, R.string.app_ga_action_view,"");
		return v;
	}

	@Override
	public void onAlarm() {
		BaseApplication.getEventsManager().show(getString(R.string.app_scheduled_alarm));
	}

	@Override
	public void onFavourite(boolean status) {
		if(status){
			BaseApplication.getEventsManager().sendEvent(R.string.app_ga_programdetail, R.string.app_ga_action_alarm,"");
			BaseApplication.getEventsManager().show(getString(R.string.app_favourite_add));
		}else{
			BaseApplication.getEventsManager().show(getString(R.string.app_favourite_del));
		}
	}

	@Override
	public void onCheckin(boolean status) {
		if(status){
			BaseApplication.getEventsManager().sendEvent(R.string.app_ga_programdetail, R.string.app_ga_action_checkin,"");
			BaseApplication.getEventsManager().show(getString(R.string.app_checkin));
		}else{
			BaseApplication.getEventsManager().show(getString(R.string.app_error));
		}
	}

	@Override
	public void onRated(boolean status, int rating) {
		if(status){
			
			BaseApplication.getEventsManager().show(getString(R.string.app_rated));
		}else{
			BaseApplication.getEventsManager().show(getString(R.string.app_error));
		}
	}
	
	@Override
	public void onShared() {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public void onAlarmDeleted(int id) {
		//BaseApplication.getEventsManager().show("TAG: "+id);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.imgFavouriteLayout:
			addFavourite();
			break;
		case R.id.imgCheckinLayout:
			addCheckin();
			sendChekin();
			break;
		case R.id.imgAlarmLayout:
			addReminder();
			break;
		case R.id.imgShareLayout:
			createShareDialog();
			break;
		case R.id.ratingBar:
			addRating(1);
			break;
		}
	}
	
	private void createShareDialog(){
		BaseApplication.getEventsManager().sendEvent(R.string.app_ga_programdetail, R.string.app_ga_action_share,"");

		mProgressDialog = ProgressDialog.show(getActivity(), getString(R.string.app_sharing_post_title), getString(R.string.app_sharing_post_subtitle));		
		buildSharedModel(mShareModel);
	}
	
	private void sendChekin(){
		mCheckinAsyncTask = new SendCheckinDataAsyncTask(this);
		mCheckinAsyncTask.execute(mProgram);
	}
	
	private void addReminder(){
		
		if(isReminded){
			BaseApplication.getWebserviceManager().unsetReminder(mProgram);
			isReminded = false;
		}else{
			BaseApplication.getWebserviceManager().setReminder(mProgram);
			BaseApplication.getEventsManager().reminder(mProgram.getTimestamp_a(),mProgram);
			BaseApplication.getEventsManager().show(getString(R.string.app_alarm_setted));
			isReminded = true;
		}
		
		if(isReminded){
			((ImageView)mView.findViewById(R.id.imgAlarm)).setImageResource(R.drawable.alarma_sel);
		}else{
			((ImageView)mView.findViewById(R.id.imgAlarm)).setImageResource(R.drawable.alarma);
		}
	}
	
	private void addCheckin(){
		if(isChecked){
			BaseApplication.getWebserviceManager().unsetCheckin(mProgram);
			isChecked = false;
		}else{
			BaseApplication.getWebserviceManager().setCheckin(mProgram);
			isChecked = true;
		}
		
		if(isChecked){
			((ImageView)mView.findViewById(R.id.imgCheckin)).setImageResource(R.drawable.check_sel);
		}else{
			((ImageView)mView.findViewById(R.id.imgCheckin)).setImageResource(R.drawable.check);
		}
	}
	
	private void addFavourite(){ 
		if(isFavourite){
			BaseApplication.getWebserviceManager().unsetFavourite(mProgram);
			isFavourite = false;
		}else{
			BaseApplication.getWebserviceManager().setFavourite(mProgram);
			isFavourite = true;
			BaseApplication.getEventsManager().show(getString(R.string.app_favourite_add));
		}
		
		if(isFavourite){
			((ImageView)mView.findViewById(R.id.imgFavourite)).setImageResource(R.drawable.favoritos_sel);
		}else{
			((ImageView)mView.findViewById(R.id.imgFavourite)).setImageResource(R.drawable.favoritos);
		}
		
		//addRating(5);
	}
	
	private void addRating(int rating){
		mProgram.setScore(rating);
		mScoreAsyncTask = new SendScoreDataAsyncTask(this);
		mScoreAsyncTask.execute(mProgram);
	}

	private void buildSharedModel(ShareModel model){
		mShareModelAsyncTask = new SendShareModelAsyncTask(this);
		mShareModelAsyncTask.execute(model);
	}
	
	@Override
	public void onSharedBuilt(String url) {
		mProgressDialog.dismiss();
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"subject");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.app_share_with)));
	}
	
}
