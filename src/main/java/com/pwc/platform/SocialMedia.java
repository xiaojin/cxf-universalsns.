package com.pwc.platform;

import com.pwc.ApiEntity;


public abstract class SocialMedia {
	public ApiEntity entity;

	public ApiEntity getEntity() {
		return entity;
	}

	public void setEntity(ApiEntity entity) {
		this.entity = entity;
	}

	public abstract String getProfile();
	
	public abstract String postMessage();
}
