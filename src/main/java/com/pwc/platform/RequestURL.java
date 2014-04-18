package com.pwc.platform;

public interface RequestURL {
	interface FacebookUrl {
		String GET_PROFILE_URL = "https://graph.facebook.com/{id}";
		String POST_FEED_URL = "https://graph.facebook.com/{id}/feed";
	}
	interface TwitterUrl{
		String UPDATE_STATUS="https://api.twitter.com/1.1/statuses/update.json";
		String GET_MYFAVLIST="https://api.twitter.com/1.1/favorites/list.json";
	}
	
	interface GooglePlusUrl{
		String GET_PEROPLE_PROFILE="https://content.googleapis.com/plus/v1/people/{id}";
		String ADD_MOMENTS="https://www.googleapis.com/plus/v1/people/{id}/moments/vault";
	}
	interface LinkedinUrl{
		String GET_COMPANY_PROFILE="https://api.linkedin.com/v1/companies/";
		String GET_PEOPLE_PROFILE="http://api.linkedin.com/v1/people/";
		String CTEATE_COMPANY_SHARE="https://api.linkedin.com/v1/companies/{id}/shares";
		String GET_FEED="https://api.linkedin.com/v1/people/~/network/updates";
		String POST_FEED="https://api.linkedin.com/v1/people/~/network/updates/key={key}/update-comments";
	}
}
