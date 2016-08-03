package model;

public enum ColorCodeEnum {
	RED(		'1', 0xff,0x00,0x00),
	ORANGE(		'2', 0xff,0xa5,0x00),
	YELLOW(		'3', 0xff,0xff,0x00),
	GREEN(		'4', 0x00,0x80,0x00),
	LIGHT_BLUE(	'5', 0xad,0xd8,0xe6),
	DARK_BLUE(	'6', 0x00,0x00,0x8b),
	PURPLE(		'7', 0x80,0x00,0x80),
	ROSE(		'8', 0xff,0xc0,0xcb),
	WHITE(		'9', 0xff,0xff,0xff),
	BLACK(		'0', 0x00,0x00,0x00);
	
	private char charCode;
	private int r; 
	private int g;
	private int b;
	
	ColorCodeEnum(char code, int r, int g, int b) {
		this.setCharCode(code);
		this.r =r;
		this.g =g;
		this.b =b;
	}

	public static char transformRgbToCharCode (int r, int g, int b) {
		char code = 0; // null char
		
		for (ColorCodeEnum color : ColorCodeEnum.values()) {
			if(color.r == r && color.g == g && color.b == b) {
				code = color.getCharCode();
			}
		}
		
		return code;
	}
	
	public static ColorCodeEnum getColor (char c) {
		ColorCodeEnum colorCode = null; // null char
		
		for (ColorCodeEnum color : ColorCodeEnum.values()) {
			if(c == color.getCharCode()) {
				colorCode = color;
			}
		}
		
		return colorCode;
	}
	
	public char getCharCode() {
		return charCode;
	}

	public void setCharCode(char charCode) {
		this.charCode = charCode;
	}

	public int getRed() {
		return r;
	}

	public int getGreen() {
		return g;
	}

	public int getBlue() {
		return b;
	}


}
