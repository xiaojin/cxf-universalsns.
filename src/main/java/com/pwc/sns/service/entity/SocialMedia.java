package com.pwc.sns.service.entity;

import com.pwc.sns.service.entity.ApiEntity;
/**
 * Super class of the {@link Facebook} {@link GooglePlus}
 */

public abstract class SocialMedia {
	public ApiEntity entity;

	/**
	 * @return Instance of {@link ApiEntity}
	 */
	public ApiEntity getEntity() {
		return entity;
	}
	/**
	 * @param Instance of {@link ApiEntity}
	 */
	public void setEntity(ApiEntity entity) {
		this.entity = entity;
	}
	/**
	 * Abstract method getProfile
	 * 
	 * @return Profile data
	 */
	public abstract String getProfile();
	
	/**
	 * Abstract method postMessage
	 * 
	 * @return Status code of post result
	 */
	public abstract String postMessage();
}
