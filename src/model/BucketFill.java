package model;


public class BucketFill {
	
	private char[][] pixels;

	private boolean isDiagonallyConnected = true;

	public BucketFill(char[][] pixels) {
		this.pixels = pixels;
	}

	public void paintPixel(int x, int y, char color) {
		if(pixels == null || y < 0 || y >= pixels.length || x < 0 || x >= pixels[y].length) {
			System.err.println("Invalid state.");
			return;
		}
		
		pixels[y][x] = color;
	}
	
	public void fill(int x, int y, char color) {
		if(pixels == null || y < 0 || y >= pixels.length || x < 0 || x >= pixels[y].length) {
			System.err.println("Invalid state.");
			return;
		}
		
		// We need information on which color we will be replacing
		char colorForReplace = pixels[y][x];

		// Start coloring pixels
		fill(x, y, color, colorForReplace);
	}
	
	private void fill(int x, int y, char newColor, char colorForReplace) {
		/* Paint pixel only if it contains color we are changing and is not already in new color */
		if(pixels[y][x] == colorForReplace && pixels[y][x] != newColor) {
			pixels[y][x] = newColor;
		} else {
			// Stop with filling if pixel is of same color
			// or has different color than the one we are replacing
			return;
		}
		
		// Top neighbor
		if (y - 1 >= 0) {
			fill(x, y - 1, newColor, colorForReplace);
		}
		// Bottom neighbor
		if (y + 1 < pixels.length) {
			fill(x, y + 1, newColor, colorForReplace);
		}
		// Left neighbor
		if (x - 1 >= 0) {
			fill(x - 1, y, newColor, colorForReplace);
		}
		// Right neighbor
		if (x + 1 < pixels[y].length) {
			fill(x + 1, y, newColor, colorForReplace);
		}
		
		if (isDiagonallyConnected == true) {
			// Top left neighbor
			if (y - 1 >= 0 && x - 1 >= 0) {
				fill(x - 1, y - 1, newColor, colorForReplace);
			}
			// Top right neighbor
			if (y - 1 >= 0 && x + 1 < pixels[y].length) {
				fill(x + 1, y - 1, newColor, colorForReplace);
			}
			// Bottom left neighbor
			if (y + 1 < pixels.length && x - 1 >= 0) {
				fill(x - 1, y + 1, newColor, colorForReplace);
			}
			// Bottom right neighbor
			if (y + 1 < pixels.length && x + 1 < pixels[y].length) {
				fill(x + 1, y + 1, newColor, colorForReplace);
			}
		}
	}

	public void inspect() {
		if (pixels != null) {
			for (int y = 0; y < pixels.length; y++) {
				for (int x = 0; x < pixels[y].length; x++) {
					System.out.print(pixels[y][x]);
				}
				System.out.print("\n");
			} 
		}
	}

	public char[][] getPixels() {
		return pixels;
	}
	
	public boolean isDiagonallyConnected() {
		return isDiagonallyConnected;
	}

	public void setDiagonallyConnected(boolean isDiagonallyConnected) {
		this.isDiagonallyConnected = isDiagonallyConnected;
	}
}