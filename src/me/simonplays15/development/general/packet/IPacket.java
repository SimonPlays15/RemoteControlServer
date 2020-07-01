package me.simonplays15.development.general.packet;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONObject;

public interface IPacket extends Serializable{
	
	Integer getPacketSize();
	
	ArrayList<Object> open();
	
	void execute(Object...params);
	abstract JSONObject convertToJson(Packet packet);
	
}
