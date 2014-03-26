package com.nozomi.util;

public class CommDef {

	public static final String SP_USER_ID = "user_id";

	//public static final String SERVER = "http://192.168.1.24/playtogether";
	 public static final String SERVER = "http://nozomi.sinaapp.com";
	 
	public static final String LOGIN = SERVER
			+ "/playtogether.php?function=Login";
	public static final String REGISTER = SERVER
			+ "/playtogether.php?function=Register";
	public static final String GET_EVENT = SERVER
			+ "/playtogether.php?function=GetEvent&latitude=%f&longitude=%f&offset=%d&count=%d";
	public static final String GET_EVENTDETAIL = SERVER
			+ "/playtogether.php?function=GetEventdetail&user_id=%d&event_id=%d";
	public static final String SET_ATTEND = SERVER
			+ "/playtogether.php?function=SetAttend";
	public static final String SET_MARK = SERVER
			+ "/playtogether.php?function=SetMark";
	public static final String GET_REVIEW = SERVER
			+ "/playtogether.php?function=GetReview&event_id=%d&offset=%d&count=%d";
	public static final String ADD_REVIEW = SERVER
			+ "/playtogether.php?function=AddReview";
	public static final String GET_MARK = SERVER
			+ "/playtogether.php?function=GetMark&user_id=%d&offset=%d&count=%d";
	public static final String GET_ATTEND = SERVER
			+ "/playtogether.php?function=GetAttend&user_id=%d&offset=%d&count=%d";
	public static final String CREATE_EVENT = SERVER
			+ "/playtogether.php?function=CreateEvent";
}
