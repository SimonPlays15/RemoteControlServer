package me.simonplays15.development.general.packet;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Packet extends ArrayList<Object> implements IPacket{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7400123481990106463L;
	private PacketInfo packetInfo;
	public Packet(PacketInfo packetInfo) {
		this.packetInfo = packetInfo;
		
		for(Object obj : packetInfo.getPacketData()){
			this.add(obj);
		}
		
	}
	
	public PacketInfo getPacketInfo(){
		return packetInfo;
	}
	
	public Object[] getPacketData(){
		return this.packetInfo.getPacketData();
	}
	
	public String getPacketName(){
		return this.packetInfo.getPacketName();
	}
	
	@Override
	public Integer getPacketSize() {
		return this.size();
	}

	@Override
	public ArrayList<Object> open() {
		return this;
	}
	
	
	
	@Override
	public void execute(Object... params) {
	}

	@Override
	public JSONObject convertToJson(Packet packet) {
		JSONObject obj = new JSONObject();
		
		JSONArray params = new JSONArray();
		
		for(Object packetParams : packet.getPacketData()) {
			params.put(packetParams);
		}
		
		obj.put("PacketName", packet.getPacketName());
		obj.put("PacketSize", packet.getPacketSize());
		
		PacketInfo packetInfo = packet.getPacketInfo();
		obj.put("IsEncrypted", packetInfo.isEncrypted());
		obj.put("IsDecrypted", packetInfo.isDecrypted());
		obj.put("From", packetInfo.getFrom());
		obj.put("PacketId", packetInfo.getPacketId());
		obj.put("PacketData", params);
		
		return obj;
	}

	@Override
	public String toString() {
		return convertToJson(this).toString();
	}

	
	
}
