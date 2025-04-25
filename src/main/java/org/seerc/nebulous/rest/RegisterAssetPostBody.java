package org.seerc.nebulous.rest;

public class RegisterAssetPostBody {
	private String assetName;
	private long timestamp;
	
	public RegisterAssetPostBody(String assetName, long timestamp) {
		super();
		this.assetName = assetName;
		this.timestamp = timestamp;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
