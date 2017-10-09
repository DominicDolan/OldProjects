package font;

import statics.Loader;

/**
 * Represents a font. It holds the font's texture atlas as well as having the
 * ability to create the quad polygons for any text using this font.
 * 
 * @author Karl
 *
 */
public class FontType {

	private int textureAtlas;
	private MetaFile metaFile;

	/**
	 * Creates a new font and loads up the data about each character from the
	 * font file.
	 * 
	 * @param textureAtlas
	 *            - the ID of the font atlas texture.
	 * @param fontFile
	 *            - the font file containing information about each character in
	 *            the texture atlas.
	 */
	public FontType(int textureAtlas, String fontFile) {
		this.textureAtlas = textureAtlas;
		this.metaFile = new MetaFile(Loader.loadBufferedReader(fontFile));
	}

	/**
	 * @return The font texture atlas.
	 */
	public int getTextureAtlas() {
		return textureAtlas;
	}


	public MetaFile getMetaFile() {
		return metaFile;
	}
}
