package com.itu.activityrecord;

public class SensorData {

	public long time;
	public float x;
	public float y;
	public float z;
	
	public SensorData() {
	}
	
	public SensorData(long _t, float _x, float _y, float _z) {
		this.time = _t;
		this.x = _x;
		this.y = _y;
		this.z = _z;
	}
		
}
