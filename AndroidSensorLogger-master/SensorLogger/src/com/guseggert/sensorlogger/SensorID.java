package com.guseggert.sensorlogger;

import java.util.ArrayList;
import java.util.Arrays;

import android.hardware.Sensor;
import android.util.SparseArray;

public enum SensorID {
	ACC_X, ACC_Y, ACC_Z;

	public static ArrayList<SensorID> getSensorIDs(int sensorType) {
		switch (sensorType) {
		case Sensor.TYPE_ACCELEROMETER:
			return new ArrayList<SensorID>(Arrays.asList(SensorID.ACC_X, SensorID.ACC_Y, SensorID.ACC_Z));
		default:
			throw new IllegalArgumentException("Invalid sensor type in getSensorIDs()");
		}
	}
	
	public static ArrayList<SensorID> getSensorIDs(int[] sensorTypes) {
		ArrayList<SensorID> sensorIDs = new ArrayList<SensorID>();
		for (int sensorType : sensorTypes)
			sensorIDs.addAll(getSensorIDs(sensorType));
		return sensorIDs;
	}
	
	public static ArrayList<SensorID> getSensorIDs(SparseArray<Sensor> sensors) {
		ArrayList<SensorID> sensorIDs = new ArrayList<SensorID>();
		for (int i = 0; i < sensors.size(); i++)
			sensorIDs.addAll(getSensorIDs(sensors.valueAt(i).getType()));
		return sensorIDs;
	}
}
