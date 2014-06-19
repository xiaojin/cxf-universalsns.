package com.pwc.sns.service.entity;
/**
 * 
 * Linkedin entity 
 *
 */
public class LinkedinEntity {
	private String message;
	private String parameters;
	private String companyID;
	private String personID;
	private String oauthToken;
	/**
	 * 
	 * @return String message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @return parameters
	 */
	public String getParameters() {
		return parameters;
	}
	
	/**
	 * 
	 * @param parameters
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	/**
	 * 
	 * @return companyID
	 */
	public String getCompanyID() {
		return companyID;
	}

	/**
	 * 
	 * @param companyID
	 */
	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}
	
	/**
	 * @return personID
	 */
	public String getPersonID() {
		return personID;
	}
	
	/**
	 * @param personID
	 */
	public void setPersonID(String personID) {
		this.personID = personID;
	}
	
	/**
	 * 
	 * @return oauthToken
	 */
	public String getOauthToken() {
		return oauthToken;
	}
	
	/**
	 * 
	 * @param oauthToken
	 */
	public void setOauthToken(String oauthToken) {
		this.oauthToken = oauthToken;
	}
	
	
	
}
