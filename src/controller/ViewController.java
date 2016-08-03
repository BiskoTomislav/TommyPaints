package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.BucketFill;
import model.ColorCodeEnum;
import model.ToolEnum;
import view.Main;

public class ViewController {
	private static final int PIXEL_SIZE = 10;
	private static final int PIXEL_GAP = 3;
	private static final int NUMBER_OF_PIXELS_X = 32;
	private static final int NUMBER_OF_PIXELS_Y = 32;

	// Reference to the main application.
    private Main main;
    
	@FXML
	private Button penButton;
	@FXML
	private Button fillButton;
	@FXML
	private Button clearButton;
	@FXML
	private Button saveButton;
	
	@FXML
	private Circle red;
	@FXML
	private Circle orange;
	@FXML
	private Circle yellow;
	@FXML
	private Circle green;
	@FXML
	private Circle lightBlue;
	@FXML
	private Circle darkBlue;
	@FXML
	private Circle purple;
	@FXML
	private Circle rose;
	@FXML
	private Circle white;
	@FXML
	private Circle black;
	
	@FXML
	private Circle selectedColor;
	
	@FXML
	private TilePane tile;

	private ToolEnum selectedTool = ToolEnum.BUCKET;	
	private BucketFill bucketFill;
	/**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ViewController() {
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

		setButtons();
		
		setColorPickers();
		
	    setCanvasPixels();
	    
    }

	private void setButtons() {
		penButton.setOnAction(e ->  {System.out.println("PEN");  selectedTool =  ToolEnum.PEN;});
		fillButton.setOnAction(e -> {System.out.println("FILL");  selectedTool =  ToolEnum.BUCKET;});
		clearButton.setOnAction(e -> {
			bucketFill = new BucketFill(getCanvasWithColor(
						ColorCodeEnum.transformRgbToCharCode(
							(int)( Color.LIGHTBLUE.getRed() * 255 ),
							(int)( Color.LIGHTBLUE.getGreen() * 255 ),
							(int)( Color.LIGHTBLUE.getBlue() * 255 )),
						32, 32)
				);
			printPixelsToScreen(bucketFill.getPixels());
		});
		saveButton.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save icon as png ...");
            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);
            
            //Show save file dialog
            File fileName = fileChooser.showSaveDialog(main.getPrimaryStage());
            if (fileName != null) {
				savePixelsToPngFile(fileName.getPath());
            }
			});
	}
    
    private void savePixelsToPngFile(String filename) {
    	final int height = bucketFill.getPixels().length;
    	final int width = bucketFill.getPixels()[0].length;
    	final BufferedImage image =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	
    	for (int y = 0; y < width; ++y) {
    		for (int x = 0; x < height; ++x) {
    			
    	    	char c = bucketFill.getPixels()[y][x];
    	    	
    	    	int red = ColorCodeEnum.getColor(c).getRed();
    	    	int green = ColorCodeEnum.getColor(c).getGreen();
    	    	int blue = ColorCodeEnum.getColor(c).getBlue();
    	    	
    	    	int rgb = red;
    	    	rgb = (rgb << 8) + green; 
    	    	rgb = (rgb << 8) + blue;
    	    	
    	    	image.setRGB(x, y, rgb);
    	    }
    	}
    	File outputFile = new File(filename);
    	try {
			ImageIO.write(image, "png", outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/**
     * Creates canvas with rectangular pixels
     */
	private void setCanvasPixels() {
		tile.setHgap(PIXEL_GAP);
	    tile.setVgap(PIXEL_GAP);
	    tile.setPrefColumns(NUMBER_OF_PIXELS_X);
	    
	    for (int y = 0; y < NUMBER_OF_PIXELS_Y; y++) {
	    	for (int x = 0; x < NUMBER_OF_PIXELS_X; x++) {
		    	Rectangle r = new Rectangle();

			    r.setWidth(PIXEL_SIZE);
			    r.setHeight(PIXEL_SIZE);
			    r.setArcWidth(7);
			    r.setArcHeight(5);
			    //r.setFill(x == 0 ? Color.LIGHTBLUE : Color.RED);
			    r.setFill(Color.LIGHTBLUE);
		    	r.setId(y + "-" + x);
		    	
		    	final String[] coordinates = new String[]{String.valueOf(y), String.valueOf(x)};
		    	
		    	/* Paint or fill with mouse click */
		    	r.setOnMouseClicked(event -> {
		    		actionClickOnPixel(Integer.valueOf(coordinates[0]), Integer.valueOf(coordinates[1]), selectedTool);
		    	});
		    	/* Paint with mouse hover while button clicked */
		    	r.setOnMouseDragOver(event -> {
		    		actionDragOverPixel(Integer.valueOf(coordinates[0]), Integer.valueOf(coordinates[1]), selectedTool);
		    	});
		    	
		        tile.getChildren().add(r);
	    	}
	    }
	    
	    /* Init model representation of pixels */
	    char pixels[][] = getCanvasWithColor(ColorCodeEnum.transformRgbToCharCode(
				(int)( Color.LIGHTBLUE.getRed() * 255 ),
				(int)( Color.LIGHTBLUE.getGreen() * 255 ),
				(int)( Color.LIGHTBLUE.getBlue() * 255 )
				)
	    		, 32, 32);
		bucketFill = new BucketFill(pixels);	
	}

	/**
	 * 
	 * Response to click on pixel event
	 * 
	 * @param x
	 * @param y
	 * @param tool
	 */
	private void actionClickOnPixel(int x, int y, ToolEnum tool) {
		Color color = (Color)selectedColor.getFill();
		if(tool == ToolEnum.BUCKET) {
			bucketFill.fill(y, x, ColorCodeEnum.transformRgbToCharCode(
					(int)( color.getRed() * 255 ),
					(int)( color.getGreen() * 255 ),
					(int)( color.getBlue() * 255 )
					));
		} else if(tool == ToolEnum.PEN) {
			bucketFill.paintPixel(y, x, ColorCodeEnum.transformRgbToCharCode(
					(int)( color.getRed() * 255 ),
					(int)( color.getGreen() * 255 ),
					(int)( color.getBlue() * 255 )
					));
		}
		
		printPixelsToScreen(bucketFill.getPixels());
	}
	
	/**
	 * 
	 * Response on dragging over pixels
	 * 
	 * @param x
	 * @param y
	 * @param tool
	 */
	private void actionDragOverPixel(int x, int y, ToolEnum tool) {
		Color color = (Color)selectedColor.getFill();
		if(tool == ToolEnum.PEN) {		
			bucketFill.paintPixel(y, x, ColorCodeEnum.transformRgbToCharCode(
					(int)( color.getRed() * 255 ),
					(int)( color.getGreen() * 255 ),
					(int)( color.getBlue() * 255 )
					));
			printPixelsToScreen(bucketFill.getPixels());
		}

	}

	/**
	 * Refresh canvas
	 * 
	 * @param pixels
	 */
	private void printPixelsToScreen(char[][] pixels) {
		for (int y = 0; y < pixels.length; y++) {
			for (int x = 0; x < pixels[y].length; x++) {
				
				Node node = tile.getChildren().get(y * pixels[y].length + x);
				if(node instanceof Rectangle){
					
					ColorCodeEnum code = ColorCodeEnum.getColor(pixels[y][x]);
					
                    ((Rectangle)node).setFill(Color.rgb(code.getRed(), code.getGreen(), code.getBlue()));
                }
			}
		}
	}

	/**
	 * Set color picker and actions for picking color
	 */
	private void setColorPickers() {
		this.red.setFill(Color.RED);
		this.orange.setFill(Color.ORANGE);
		this.yellow.setFill(Color.YELLOW);
		this.green.setFill(Color.GREEN);
		this.lightBlue.setFill(Color.LIGHTBLUE);
		this.darkBlue.setFill(Color.DARKBLUE);
		this.purple.setFill(Color.PURPLE);
		this.rose.setFill(Color.PINK);
		this.white.setFill(Color.WHITE);
		this.black.setFill(Color.BLACK);
		
		this.selectedColor.setFill(Color.RED);
		
		this.red.setOnMouseClicked(event -> this.selectedColor.setFill(this.red.getFill()));
		this.orange.setOnMouseClicked(event -> this.selectedColor.setFill(this.orange.getFill()));
		this.yellow.setOnMouseClicked(event -> this.selectedColor.setFill(this.yellow.getFill()));
		this.green.setOnMouseClicked(event -> this.selectedColor.setFill(this.green.getFill()));
		this.lightBlue.setOnMouseClicked(event -> this.selectedColor.setFill(this.lightBlue.getFill()));
		this.darkBlue.setOnMouseClicked(event -> this.selectedColor.setFill(this.darkBlue.getFill()));
		this.purple.setOnMouseClicked(event -> this.selectedColor.setFill(this.purple.getFill()));
		this.rose.setOnMouseClicked(event -> this.selectedColor.setFill(this.rose.getFill()));
		this.white.setOnMouseClicked(event -> this.selectedColor.setFill(this.white.getFill()));
		this.black.setOnMouseClicked(event -> this.selectedColor.setFill(this.black.getFill()));
	}
    
	/**
	 * 
	 * Paint whole canvas with one color
	 * 
	 * @param color
	 * @param dimX
	 * @param dimY
	 * @return
	 */
	private char[][] getCanvasWithColor (char color, int dimX, int dimY) {
		char pixels[][] = new char[dimX][dimY];
		for (int y = 0; y < pixels.length; y++) {
			for (int x = 0; x < pixels[y].length; x++) {
				pixels[y][x] = color;
			}
		}
		return pixels;
	}
	
	public Button getPenButton() {
		return penButton;
	}

	public void setPenButton(Button penButton) {
		this.penButton = penButton;
	}

	public Main getMain() {
		return main;
	}

	public void setMain(Main main) {
		this.main = main;
	}
}
