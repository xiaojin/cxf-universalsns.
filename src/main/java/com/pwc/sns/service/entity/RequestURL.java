package com.pwc.sns.service.entity;

public interface RequestURL {
	class FacebookUrl {
		public final static String GET_PROFILE_URL = "https://graph.facebook.com/{id}";
		public final static String POST_FEED_URL = "https://graph.facebook.com/{id}/feed";
	}
	class TwitterUrl{
		public final static String UPDATE_STATUS="http://api.twitter.com/1.1/statuses/update.json";
		public final static String GET_MYFAVLIST="https://api.twitter.com/1.1/favorites/list.json";
		public final static String GET_PEOPLE_PROFILE="https://api.twitter.com/1.1/account/verify_credentials.json";
		public final static String REQUEST_TOKEN_URL="https://api.twitter.com/oauth/request_token";
		public final static String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
	}
	
	class GooglePlusUrl{
		public final static String GET_PEROPLE_PROFILE="https://content.googleapis.com/plus/v1/people/{id}";
		public final static String ADD_MOMENTS="https://www.googleapis.com/plus/v1/people/{id}/moments/vault";
	}
	class LinkedinUrl{
		public final static String GET_COMPANY_PROFILE="https://api.linkedin.com/v1/companies/";
		public final static String GET_PEOPLE_PROFILE="https://api.linkedin.com/v1/people/~";
		public final static String CTEATE_COMPANY_SHARE="https://api.linkedin.com/v1/companies/{id}/shares";
		public final static String GET_FEED="https://api.linkedin.com/v1/people/~/network/updates";
		public final static String POST_FEED="https://api.linkedin.com/v1/people/~/network/updates/key={key}/update-comments";
	}
}
