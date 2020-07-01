package me.simonplays15.development.tests;

import me.simonplays15.development.general.packet.packets.authentication.AuthenticationPacket;

public class Test {

	public static void main(String[] args) {
		AuthenticationPacket packet = new AuthenticationPacket("test", "tegdfgd");
		
		System.out.println(packet.toString());
		
	}

}
