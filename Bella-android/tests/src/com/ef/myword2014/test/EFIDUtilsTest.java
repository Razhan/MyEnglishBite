package com.ef.myword2014.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import android.test.AndroidTestCase;

import com.ef.myword2014.business.ChunkBiz;
import com.ef.myword2014.business.LoginBiz;
import com.ef.myword2014.business.SyncBiz;
import com.ef.myword2014.business.TraceBiz;
import com.ef.myword2014.business.ServerAPI;
import com.ef.myword2014.business.UserScoreBiz;
import com.ef.myword2014.dataacces.dao.ChunkDao;
import com.ef.myword2014.dataacces.mode.Chunk;
import com.ef.myword2014.dataacces.mode.httpMode.ProfileData;
import com.ef.myword2014.dataacces.mode.httpMode.HttpGetFriends;
import com.ef.myword2014.dataacces.mode.httpMode.HttpBaseMessage;
import com.ef.myword2014.dataacces.mode.httpMode.HttpPostTracking;
import com.ef.myword2014.dataacces.mode.httpMode.HttpTrackData;
import com.ef.myword2014.dataacces.mode.httpMode.LoginMessage;
import com.ef.myword2014.dataacces.mode.httpMode.LogoutMessage;
import com.ef.myword2014.dataacces.mode.httpMode.Progress;
import com.ef.myword2014.dataacces.mode.httpMode.RegistInfoMessage;
import com.ef.myword2014.dataacces.mode.httpMode.Rehearsal;
import com.ef.myword2014.dataacces.mode.httpMode.User;
import com.ef.myword2014.dataacces.mode.httpMode.UserMode;

public class EFIDUtilsTest extends AndroidTestCase {
	private static final String Tag = "MyTest";

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	// @Test
	// public void RequestingAnAuthorizationCodeTest(){
	// String url=EFIDUtils.BuildUrlForRequestingAnAuthorizationCode("code",
	// "demo", "scope_1 scope_2", "demo_state_value",
	// "http://b2cglobaluat.englishtown.com/mockupservice/oauth2/jump");
	// String
	// target="http://b2cglobaluat.englishtown.com/mockupservice/oauth2/auth?response_type=code&client_id=demo&scope=scope_1%20scope_2&state=demo_state_value&redirect_uri=http%3A%2F%2Fb2cglobaluat.englishtown.com%2Fmockupservice%2Foauth2%2Fjump";
	// org.junit.Assert.assertEquals(url,target);
	// }

	@Test
	public void ArrayJSONStringTest() {
		JSONArray array = new JSONArray();
		array.put("test1");
		array.put("test1");
		array.put("test1");
		array.put("test1");
		String json = array.toString();
	}

	@Test
	public void chunkTest() {
		ChunkDao chunkDao = new ChunkDao(getContext());
		// Chunk chunkDefinition1=new Chunk(1,"1","3","1","1","en");
		// Chunk chunkDefinition2=new Chunk(2,"1","1","1","1","en");
		// Chunk chunkDefinition3=new Chunk(3,"1","1","1","1","en");
		// Chunk chunkDefinition4=new Chunk(4,"1","1","1","1","en");
		// Chunk chunkDefinition5=new Chunk(6,"1","1","1","1","en");
		// Chunk chunkDefinition6=new Chunk(5,"1","1","1","1","en");
		// Chunk chunkDefinition7=new Chunk(7,"1","1","1","1","1");
		// Chunk chunkDefinition8=new Chunk(8,"1","1","1","1","2");
		// Chunk chunkDefinition9=new Chunk(9,"1","1","1","1","1");
		// Chunk chunkDefinition10=new Chunk(10,"1","1","1","1","aaa");
		// Chunk chunkDefinition11=new Chunk(11,"1","1","1","1","aaa");
		// chunkDao.addChunk(chunkDefinition1);
		// chunkDao.addChunk(chunkDefinition2);
		// chunkDao.addChunk(chunkDefinition3);
		// chunkDao.addChunk(chunkDefinition4);
		// chunkDao.addChunk(chunkDefinition5);
		// chunkDao.addChunk(chunkDefinition6);
		// chunkDao.addChunk(chunkDefinition7);
		// chunkDao.addChunk(chunkDefinition8);
		// chunkDao.addChunk(chunkDefinition9);
		// chunkDao.addChunk(chunkDefinition11);

		Chunk chunk = chunkDao.getChunk(8, "001");
		// List<ChunkDefinition> chunkDefinitions= chunkDao.getAll(4,1);
		// chunkDao.updateChunkStatus(10, "1");
		// chunkDao.updateRehearsalStatus(10, "1");
		// ChunkDefinition chunkDefinition= chunkDao.selectChunkByText("3");
		// Chunk chunkDefinition1= chunkDao.selectChunkByID(1);
	}

	@Test
	public void GetAllAvailableChunkCountTest() {
		ChunkDao chunkDao = new ChunkDao(getContext());
		// int num= chunkDao.GetAllAvailableChunkCount("001");
		// num=chunkDao.GetRehearseChunkCount("001");
	}

	@Test
	public void addChunkTest() throws JSONException {
		ChunkDao chunkDao = new ChunkDao(getContext());
		String jString = "{\"id\":1,\"language\": \"EN\",\"status\":0,\"chunk\":{\"chunk\":\"well done\",\"definition\":\"fully cooked.  Used to describe meat.\",\"hint\": [{\"content\": \"Well done 1 is only used to describe beef, as most other meats are fully cooked for health reasons.\",\"example\": \"I like my steak1 <h>well done</h>\"},{\"content\": \"Well done 2 is only used to describe beef,as most other meats are fully cooked for health reasons.\",\"example\": \"I like my steak2 <h>well done</h>\"},{\"content\": \"Well done 3 is only used to describe beef,as most other meats are fully cooked for health reasons.\",\"example\": \"I like my steak3 <h>well done</h>\"}],\"audio\": \"well done_chunk.mp3\",\"pronounce\": \"[wel don]\"},\"presentation\":{\"dialogue\":[{\"content\": \"How do you usually like your steak?\",\"speakericon\": \"speaker_a.png\"},{\"content\": \"Oh I always like my steak <h>well done</h>.I don’t eat anything less than that.\",\"speakericon\": \"speaker_b.png\"},{\"content\": \"Really?! That’s a shame,<h>well done</h> is way too dry for me.\",\"speakericon\": \"speaker_a.png\"}],\"audio\": \"welldone_dialogue.mp3\",\"score\": \"20\"},\"multi-choice\":[{\"question\": \"Which of the following can be used to describe fully cooked meat?\",\"audio\": \"welldone_dialogue.mp3\",\"limit-time\": \"20\",\"items\": [{\"item\": \"welldone\",\"index\": \"a\",\"errorhint\":\" \"},{\"item\": \"donewell\",\"index\": \"b\",\"errorhint\": \"We don't say it as done well\"},{\"item\": \"cooked done\",\"index\": \"c\",\"errorhint\": \"We don't say it as cooked done\"}],\"answer\": \"a\",\"score\": \"20\",\"random\": \"Y\"},{\"question\": \"What kind of meat is usually described as well done?\",\"limit-time\": \"20\",\"audio\": \"welldone_dialogue.mp3\",\"items\": [{\"item\": \"pork\",\"index\": \"a\",\"errorhint\": \"We don't say it as pork\"},{\"item\": \"chicken\",\"index\": \"b\",\"errorhint\": \"We don't say it as chicken\"},{\"item\": \"beef\",\"index\": \"c\",\"errorhint\":\" \"}],\"answer\": \"c\",\"score\": \"20\",\"random\": \"N\"},{\"question\": \"If you order your burger well done, how pink will the meat be inside?\",\"audio\": \"well done_dialogue.mp3\",\"limit-time\": \"20\",\"items\": [{\"item\": \"It will be very pink inside.\",\"index\": \"a\", \"iscorrect\":\"Y\",\"errorhint\": \"We don't say it as 'It will be very pink inside'\"},{\"item\": \"It will be a little pink inside.\",\"index\": \"b\",\"errorhint\": \"We don'tsayitas'Itwillbealittlepinkinside'\"},{\"item\": \"There will be no pink inside.\",\"errorhint\":\" \",\"index\": \"c\"}],\"answer\": \"c\",\"score\": \"20\",\"random\": \"N\"}]}";
		JSONObject jsonObject = new JSONObject(jString);
		chunkDao.addChunk(jsonObject);
	}

	public void JsonTest() {
		ChunkDao chunkDao = new ChunkDao(getContext());
		// Chunk chunk= chunkDao.getChunk(1, "1","EN");
		// String jsonString= chunk.toJson();
		//
		// Chunk chunk2=new Chunk();
		// chunk2.parse(jsonString);
		ChunkBiz chunkBiz = new ChunkBiz(mContext);
		// List<Chunk> chunks= chunkDao.getAll(1,2,"EN");
		// chunkDao.SetChunkStatus(1,"001",11,1,8,8);
		// chunkDao.SetChunkStatus(1,"001",11,2,8,8);
		// chunkDao.SetChunkStatus(1,"001",11,3,8,8);
		// Boolean flag= chunkDao.SetChunkStatus(1,"001",11,4,8,8);
		// List<Chunk> count=chunkBiz.getMasteredChunkList("001","en");
	}

	public void JsonFileTest() {
		ChunkBiz chunkBiz = new ChunkBiz(mContext);
		chunkBiz.loadChunkFromResource(mContext);
		//
		// Chunk chunk=chunkBiz.getChunk(1, "-1", "EN");
		// String json=chunk.toJson();
		// chunk.parse(json);
	}

	public void levelTest() {
		// LevelScoreBiz userLevelDao=new LevelScoreBiz();
		// int level= userLevelDao.getLevel(2100);
		// int score=userLevelDao.getLevelUpScore(2000);
		// int scroce=userLevelDao.getCurrentLevelScore(2000);
		//
		// ChunkDao chunkDao=new ChunkDao(mContext);
		// List<Chunk> chunks=new ArrayList<Chunk>();
		// chunks=chunkDao.getFutrueRehearseChunks("001","en", 1, 5, 10);
		// List<Chunk> chunks1=new ArrayList<Chunk>();
		// chunks1=chunkDao.getToRehearseChunks("001", "en", 1, 5, 10);
		// chunkDao.getAll("EN");
		// // chunkDao.getMasteredChunkList("001","en")
		// List<Chunk> chunks= chunkDao.getNewChunk("001", "en", 10000000);
		// UserScoreBiz userScoreBiz=new UserScoreBiz(mContext);
		// userScoreBiz.increaseScore(50,"1");
		//
		 TraceBiz traceBiz=new TraceBiz(mContext);
		 traceBiz.insertTrace("1","click","1");
		 traceBiz.insertTrace("1","click","2");
		 traceBiz.insertTrace("1","click","3");
		
	}

	public void longinTest() {
		//LoginBiz userBiz = new LoginBiz();
		// UserMode userMode=new UserMode();
		// userMode.access_token="aaaaa";
		// userMode.auth_code="sdsdsds";
		// userMode.login_type="bella";
		// userMode.password="123";
		// userMode.provider_type="wechat";
		// userMode.username="aaa";
		// LoginMessage loginMessage= userBiz.login(userMode);
		//
		// User user= new User();
		// user.email="aaa";
		// user.username="aaa";
		// user.password="123";
		// RegistInfoMessage registInfoMessage= userBiz.registerUser(user);
		//
//		LogoutMessage logoutMessag = userBiz
//				.Logout("d79cc4a2-c62c-4290-b404-b3c6b68ad961");
		// User user=new User();
		// user.email="auh10";
		// user.password="aaa11";
		// userBiz.resigtUser(user);
		// UserProgressStatusDao userProgressStatusDao=new
		// UserProgressStatusDao(mContext);
		//
		// // userProgressStatusDao.SetChunkStatus(2, "1", 2, 2, 501, new
		// Date().getTime());
		// // userProgressStatusDao.SetChunkStatus(2, "1", 3, 1, 502, new
		// Date().getTime());
		// // userProgressStatusDao.SetChunkStatus(2, "1", 3, 1, 503, new
		// Date().getTime());
		// // userProgressStatusDao.SetChunkStatus(2, "1", 3, 2, 504, new
		// Date().getTime());
		// userProgressStatusDao.SetChunkStatus(2, "1", 3, 3, 504, new
		// Date().getTime());

		 ServerAPI serverAPI=new ServerAPI(mContext);
		 ProfileData profileData=new ProfileData();
		 profileData.age=15;
		 profileData.alias="aa";
		 profileData.avatar="aaa";
		 profileData.bella_id="d79cc4a2-c62c-4290-b404-b3c6b68ad961";
		 profileData.city="sdsdsd";
		 profileData.country="sdsd";
		 profileData.created_at="2014-08-12T00:54:56.5520524-04:00";
		 profileData.email="dasdadsa";
		 profileData.first_name="dsdsadsa";
		 profileData.friend_count=5;
		 profileData.gender="5";
		 profileData.language="en";
		 profileData.last_name="dasdsadsa";
		 profileData.page_index=1;
		 profileData.page_size=1;
		 profileData.email="sdsad@ads";
		 profileData.province="sdsad";
		 profileData.registered_city="dasd";
		 profileData.registered_country="asdsasdsadsa";
		 profileData.registered_province="sdsads";
		 profileData.language="en";
		 profileData.partner="sdasdsadas";
		 profileData.score=150;
		 profileData.updated_at="2014-08-12T00:54:56.5520524-04:00";
		 
		
		 Progress progress=new Progress();
		 progress.bella_id="asdsadsad";
		 progress.collected_at="2014-08-12T00:54:56.5520524-04:00";
		 progress.ended_at="2014-08-12T00:54:56.5520524-04:00";
		 progress.lesson_id="1";
		 progress.score=52;
		 progress.synced_at="2014-08-12T00:54:56.5520524-04:00";
		 
		 Rehearsal rehearsal=new Rehearsal();
		 rehearsal.bella_id="asdsadsad";
		 rehearsal.collected_at="2014-08-12T00:54:56.5520524-04:00";
		 rehearsal.ended_at="2014-08-12T00:54:56.5520524-04:00";
		 rehearsal.activity_id="1";
		 rehearsal.score=52;
		 rehearsal.synced_at="2014-08-12T00:54:56.5520524-04:00";
		 //User  friends=userBiz.postProgress(data);
	}
			
	
	public void progessTest() throws ParseException{
//		SyncBiz syncBiz=new  SyncBiz(mContext);
//		UserBiz userBiz=new UserBiz(mContext);
//		HttpGetFriends httpGetFriends= userBiz.GetAllFiendsProfile("d3fea165-56bd-4d77-a5e5-b46e30b06194");
		ServerAPI serverAPI=new ServerAPI(mContext);
		HttpPostTracking httpPostTracking=new HttpPostTracking();
		httpPostTracking.data=new ArrayList<HttpTrackData>();
		HttpTrackData httpTrackData=new HttpTrackData();
		httpTrackData.bella_id="aaaa";
		httpTrackData.channel="1";
		httpTrackData.device_id="sadassadsa";
		httpTrackData.data=new HashMap<String, String>();
		httpTrackData.data.put("aaa","sadsds");
		httpTrackData.data.put("sdsadsadsa","sadsadsa");
		httpPostTracking.data.add(httpTrackData);
		HttpBaseMessage httpMessage=serverAPI.postTrack(httpPostTracking, "1");
		
	}
}
