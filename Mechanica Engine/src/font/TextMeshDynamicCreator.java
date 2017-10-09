package font;

import statics.G;

/**
 * Created by domin on 29 Mar 2017.
 */

public class TextMeshDynamicCreator {

    protected static final double LINE_HEIGHT = 0.03f;
    protected static final int SPACE_ASCII = 32;
    protected static final int RETURN = (int) '\n';
    private float[] translatedPoints;
    private TextBufferData bufferData;

    private Line[] savedLines = new Line[32];

    private MetaFile metaData;

    public TextMeshDynamicCreator() {
        translatedPoints = new float[12];
        bufferData = new TextBufferData();
        for (int i = 0; i < savedLines.length; i++) {
            savedLines[i] = new Line();
        }
    }

    public TextBufferData createTextMesh(GUIText text) {
        metaData = text.getMetaFile();
        bufferData.set(text.getCharArrayString().length);
        createQuadVertices(text, updateStructure(text));
        bufferData.flip();
        return bufferData;
    }

    private int updateStructure(GUIText text){
        int currentLine = 0;
        savedLines[currentLine].set(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
        savedLines[currentLine].setCurrentWord();

        for (char c: text.getCharArrayString()){
            int ascii = (int) c;
            boolean returnCharacter = (ascii == RETURN);
            if (ascii == SPACE_ASCII || returnCharacter) {

                boolean added = false;
                if (!returnCharacter)
                    added = savedLines[currentLine].attemptToAddWord();
                if (!added || returnCharacter){
                    currentLine++;
                    savedLines[currentLine].set(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
                    savedLines[currentLine].attemptToAddWord();
                }
                continue;
            }

            savedLines[currentLine]
                    .getCurrentWord()
                    .addCharacter(metaData.getCharacter(ascii));
        }
        savedLines[currentLine].attemptToAddWord();
        return currentLine+1;
    }

    private void createQuadVertices(GUIText text, int NoOflines) {
        text.setNumberOfLines(NoOflines);
        double curserX = 0f;
        double curserY = 0f;
        for (int line = 0; line < NoOflines; line++) {
            if (text.isCentered()) {
                curserX = (savedLines[line].getMaxLength() - savedLines[line].getLineLength())/2;
            }
            for (int word = 0; word <savedLines[line].getNumberOfWords(); word++) {
                for (Character letter : savedLines[line].getWord(word).getCharacters()) {
                    addVerticesForCharacter(curserX, curserY, letter, text.getFontSize());
                    addTexCoords(letter.getxTextureCoord(), letter.getyTextureCoord(),
                            letter.getXMaxTextureCoord(), letter.getYMaxTextureCoord());
                    curserX += letter.getxAdvance()*text.getFontSize()* G.RATIO;
                }
                curserX += metaData.getSpaceWidth()*text.getFontSize()* G.RATIO;
            }
            curserX = 0;
            curserY += LINE_HEIGHT*text.getFontSize();
        }
    }

    private void addVerticesForCharacter(double curserX, double curserY, Character character, double fontSize) {
        double x = curserX + (character.getxOffset() * fontSize * G.RATIO);
        double y = curserY + ((character.getyOffset() - LINE_HEIGHT) * fontSize);
        double maxX = x + (character.getSizeX() * fontSize * G.RATIO);
        double maxY = y + (character.getSizeY() * fontSize);
        double properX = (2 * x);
        double properY = (-2 * y) ;
        double properMaxX = (2 * maxX);
        double properMaxY = (-2 * maxY);
        addVertices(properX, properY, properMaxX, properMaxY);
    }

    private void addVertices(double x, double y, double maxX, double maxY) {
        translatedPoints[0]  = (float) x;
        translatedPoints[1]  = (float) y;
        translatedPoints[2]  = (float) x;
        translatedPoints[3]  = (float) maxY;
        translatedPoints[4]  = (float) maxX;
        translatedPoints[5]  = (float) maxY;
        translatedPoints[6]  = (float) maxX;
        translatedPoints[7]  = (float) maxY;
        translatedPoints[8]  = (float) maxX;
        translatedPoints[9]  = (float) y;
        translatedPoints[10] = (float) x;
        translatedPoints[11] = (float) y;
        bufferData.putVertices(translatedPoints);
    }

    private void addTexCoords(double x, double y, double maxX, double maxY) {
        translatedPoints[0]  = (float) x;
        translatedPoints[1]  = (float) y;
        translatedPoints[2]  = (float) x;
        translatedPoints[3]  = (float) maxY;
        translatedPoints[4]  = (float) maxX;
        translatedPoints[5]  = (float) maxY;
        translatedPoints[6]  = (float) maxX;
        translatedPoints[7]  = (float) maxY;
        translatedPoints[8]  = (float) maxX;
        translatedPoints[9]  = (float) y;
        translatedPoints[10] = (float) x;
        translatedPoints[11] = (float) y;
        bufferData.putTextureCoords(translatedPoints);
    }
}
