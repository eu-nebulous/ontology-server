package org.seerc.nebulous.rest;

import org.apache.commons.lang3.StringUtils;

public class Logger {
	
	final static int part1Length = 25;
	final static int part2Length = 45;
	final static int part3Length = 45;
	final static int Part4Length = 45;
	
	public static void post(String part1, String part2, String part3) {
		
		part1 = StringUtils.rightPad(part1, part1Length);
		part2 = StringUtils.rightPad(part2, part2Length);


		System.out.println("POST | " + part1 + " | " + part2 + " | " + part3);
	}
	public static void post(String part1, String part2, String part3, String part4) {
		
		part1 = StringUtils.rightPad(part1, part1Length);
		part2 = StringUtils.rightPad(part2, part2Length);
		part3 = StringUtils.rightPad(part3, part3Length);
		
		System.out.println("POST | " + part1 + " | " + part2 + " | " + part3 + " | " + part4);
	}
	
	public static void get(String part1, String part2, String part3, String part4) {
		
		part1 = StringUtils.rightPad(part1, part1Length);
		part2 = StringUtils.rightPad(part2, part2Length);
		part3 = StringUtils.rightPad(part3, part3Length);
		
		System.out.println("GET  | " + part1 + " | " + part2 + " | " + part3 + " | " + part4);
	}
	public static void get(String part1, String part2, String part3) {
		
		part1 = StringUtils.rightPad(part1, part1Length);
		part2 = StringUtils.rightPad(part2, part2Length);

		System.out.println("GET  | " + part1 + " | " + part2 + " | " + part3);
	}

}
