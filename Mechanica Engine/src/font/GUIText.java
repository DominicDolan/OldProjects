package font;

import models.TexturedModel;
import statics.Loader;

import java.nio.Buffer;

/**
 * Represents a piece of text in the game.
 * 
 * @author Karl
 *
 */
public class GUIText extends TexturedModel{

	private String textString;

	private char[] charArrayString;
	private float fontSize;

//	private int vertexCount;
	public Buffer vertexBuffer;
	public Buffer textureBuffer;
	private int colour;

	private float positionX, positionY;
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;
    private MetaFile metaFile;

	private boolean centerText = false;

	/**
	 * Creates a new text, loads the text's quads into a VAO, and adds the text
	 * to the screen.
	 * 
	 * @param text
	 *            - the text.
	 * @param fontSize
	 *            - the font size of the text, where a font size of 1 is the
	 *            default size.
	 * @param font
	 *            - the font that this text should use.
	 * @param x
	 *            - the x position on the screen where the top left corner of the
	 *            text should be rendered. The top left corner of the screen is
	 *            (0, 0) and the bottom right is (1, 1).
     *  @param y
     *            - the y position on the screen where the top left corner of the
     *            text should be rendered. The top left corner of the screen is
     *            (0, 0) and the bottom right is (1, 1).
	 * @param maxLineLength
	 *            - basically the width of the virtual page in terms of screen
	 *            width (1 is full screen width, 0.5 is half the width of the
	 *            screen, etc.) Text cannot go off the edge of the page, so if
	 *            the text is longer than this length it will go onto the next
	 *            line. When text is centered it is centered into the middle of
	 *            the line, based on this line length value.
	 * @param centered
	 *            - whether the text should be centered or not.
	 */
	public GUIText(String text, float fontSize, FontType font, float x, float y, float maxLineLength,
                   boolean centered) {
		super();
		this.textString = text;
		this.charArrayString = text.toCharArray();
		this.fontSize = fontSize;
		this.font = font;
        this.metaFile = font.getMetaFile();
		this.positionX = x;
		this.positionY = y;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;

        TextBufferData data = Loader.loadTextBuffers(this);
        set(Loader.loadTextureModel(data.vertices, data.textureCoords, data.vertexCount, font.getTextureAtlas()));
//        setMeshInfo(data.vertices, data.textureCoords, data.vertexCount);
		// preLoad text
	}

	public void set(String text, float fontSize, FontType font, float x, float y, float maxLineLength,
                    boolean centered){
		this.textString = text;
		this.charArrayString = text.toCharArray();
		this.fontSize = fontSize;
		this.font = font;
		this.positionX = x;
		this.positionY = y;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
        set();
	}

	public void set(String text, float x, float y){
		this.textString = text;
		this.charArrayString = text.toCharArray();
		this.positionX = x;
		this.positionY = y;
		set();
	}

	public void set(){
        TextBufferData data = Loader.loadTextBuffers(this);
		set(Loader.loadTextureModel(data.vertices, data.textureCoords, data.vertexCount, font.getTextureAtlas()));
    }

	public char[] getCharArrayString() {
		return charArrayString;
	}

	/**
	 * Remove the text from the screen.
	 */
	public void remove() {
		// remove text
	}

	/**
	 * @return The font used by this text.
	 */
	public FontType getFont() {
		return font;
	}

	/**
	 * Set the colour of the text.
	 * 
	 * @param color
	 *            - red value, between 0 and 1.
	 */
	public void setColour(int color) {
		this.colour = color;
	}

	/**
	 * @return the colour of the text.
	 */
	public int getColour() {
		return colour;
	}

	/**
	 * @return The number of lines of text. This is determined when the text is
	 *         loaded, based on the length of the text and the max line length
	 *         that is set.
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * @return The x position of the top-left corner of the text in screen-space.
	 *         (0, 0) is the top left corner of the screen, (1, 1) is the bottom
	 *         right.
	 */
	public float getPositionX() {
		return positionX;
	}


	/**
	 * @return The y position of the top-left corner of the text in screen-space.
	 *         (0, 0) is the top left corner of the screen, (1, 1) is the bottom
	 *         right.
	 */
	public float getPositionY() {
		return positionY;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	/**
	 * Set the Buffers and vertex count for this text.
	 *
	 * @param verticesCount
	 *            - the total number of polygons in all of the quads.
	 */
	public void setMeshInfo(Buffer vertices, Buffer textureCoords, int verticesCount) {
		vertexBuffer = vertices;
		textureBuffer = textureCoords;
		setVertexCount(verticesCount);
	}

	public int getTextureAtlas(){
		return font.getTextureAtlas();
	}

	/**
	 * @return The total number of polygons of all the text's quads.
	 */
//	public int getVertexCount() {
//		return this.vertexCount;
//	}

	/**
	 * @return the font size of the text (a font size of 1 is normal).
	 */
	protected float getFontSize() {
		return fontSize;
	}

	/**
	 * Sets the number of lines that this text covers (method used only in
	 * loading).
	 * 
	 * @param number
	 */
	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	/**
	 * @return {@code true} if the text should be centered.
	 */
	protected boolean isCentered() {
		return centerText;
	}

	/**
	 * @return The maximum length of a line of this text.
	 */
	protected float getMaxLineSize() {
		return lineMaxSize;
	}

	/**
	 * @return The string of text.
	 */
	protected String getTextString() {
		return textString;
	}


    public void setText(String text) {
        this.textString = text;
        set();
    }

    public MetaFile getMetaFile() {
        return metaFile;
    }
}
