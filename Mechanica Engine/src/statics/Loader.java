package statics;

import font.FontType;
import font.GUIText;
import font.TextBufferData;
import font.TextMeshDynamicCreator;
import models.RawModel;
import models.TexturedModel;
import org.jbox2d.common.Vec2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import world.descriptor.WorldDescriptor;
import world.descriptor.WorldGraphics;
import world.descriptor.WorldPolygons;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by domin on 22 Mar 2017.
 */

public class Loader {
    private static List<Integer> vaos = new ArrayList<Integer>();
    private static List<Integer> vbos = new ArrayList<Integer>();
    private static List<Integer> textures = new ArrayList<Integer>();

    private static Map<String, Integer> textureList = new HashMap<>();

    private static TextMeshDynamicCreator dynamicCreator = new TextMeshDynamicCreator();

    private static int textureNumber = -1;
    private static int[] textureNames;

    private static final float SCALE = 1.0f/100f;

    private static float docHeight;

    public static RawModel loadRawModel(float[] positions, short[] indices){
//        int indexID = bindIndicesBuffer(indices);
//        int vertexID = storeDataInAttributeList(positions);
//        RawModel model = new RawModel(indices.length, indexID, vertexID);
//        return model;

        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, positions);
        unbindVAO();

        RawModel model = new RawModel(vaoID, indices.length);
        model.drawType = GL11.GL_TRIANGLES;
        return model;
    }


    public static RawModel loadRawModel(float[] positions){
        int vaoID = createVAO();
        storeDataInAttributeList(0, positions);
        unbindVAO();
        RawModel model = new RawModel(vaoID, positions.length/3);
        return model;
    }

    public static TexturedModel loadTextureModel(float[] positions, short[] indices, float[] textureCoords, int texture){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        unbindVAO();

        TexturedModel model = new TexturedModel(vaoID, indices.length, texture);
        model.drawType = GL11.GL_TRIANGLES;
        return model;
    }

    public static TexturedModel loadTextureModel(float[] positions, float[] textureCoords, int texture){
        int vaoID = createVAO();
        storeDataInAttributeList(0, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        unbindVAO();

        TexturedModel model = new TexturedModel(vaoID, positions.length/3, texture);
        model.drawType = GL11.GL_TRIANGLES;
        return model;
    }

    public static TexturedModel loadTextureModel(FloatBuffer positions, FloatBuffer textureCoords, int vertexCount, int texture){
        int vaoID = createVAO();
        storeDataInAttributeList(0, 2, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        unbindVAO();

        TexturedModel model = new TexturedModel(vaoID, vertexCount, texture);
        model.drawType = GL11.GL_TRIANGLES;
        return model;
    }

    public static TexturedModel loadTexturedQuad(float left, float top, float right, float bottom){
        float[] vertices = new float[]
                {
                        left,  top,    0.0f,
                        left,  bottom, 0.0f,
                        right, bottom, 0.0f,
                        right, top,    0.0f
                };

        short[] indices = new short[] {0, 1, 2, 0, 2, 3}; // The order of vertexrendering.

        float[] textureCoords = {
                0,0, //V0
                0,1, //V1
                1,1, //V2
                1,0  //V3
        };

        return loadTextureModel(vertices, indices, textureCoords, 0);
    }

//    public static TextBufferData loadTextBuffers(GUIText text){
//        return dynamicCreator.createTextMesh(text);
//    }


    public static String loadShaderCode(String filename){
        return loadTextFile("shadercode/" + filename + ".txt");
    }

    public static String loadTextFile(String filename){
        StringBuilder text = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while((line = reader.readLine())!=null){
                text.append(line);
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

        return text.toString();
    }

    public static FontType loadFont(String name){
        return new FontType(loadTexture("fonts/" + name), "res/fonts/" + name + ".fnt");
    }

    public static BufferedReader loadBufferedReader(String file) {
        try {
            return new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't read font meta file!");
            return null;
        }
    }

    public static int loadTexture(String fileName){
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }


    public static WorldDescriptor loadWorldDescriptor(String fileName, Vec2 location){
        Document doc = loadSVGDocument(fileName);

        Element info =  doc.getElementsByTag("svg").first();
        docHeight = getSVGHeight(info);

        return new WorldDescriptor(loadWorldGraphics(doc, location), loadWorldObjects(doc, location));

    }
    public interface OtherLoadings{
        void load(Document doc, Vec2 location);
    }
    public static WorldDescriptor loadWorldDescriptor(String fileName, Vec2 location, OtherLoadings... others){
        Document doc = loadSVGDocument(fileName);

        Element info =  doc.getElementsByTag("svg").first();
        docHeight = getSVGHeight(info);
        for (OtherLoadings other : others) {
            other.load(doc, location);
        }

        return new WorldDescriptor(loadWorldGraphics(doc, location), loadWorldObjects(doc, location));

    }

    private static WorldGraphics loadWorldGraphics(Document doc, Vec2 location){
        WorldGraphics graphics = new WorldGraphics(location);

        Element graphicsElement = doc.getElementById("Graphics");
        List<Vec2[]> graphicsPolygons;

        if (graphicsElement != null) {
            Elements imageElements = graphicsElement.getElementsByTag("img");

            if (imageElements.size() > 0)
                for (Element imageElement : imageElements) {
                    graphics.addImage(
                            loadMatrixFromSVG(imageElement),
                            loadTextureIDFromSVG(imageElement)
                    );
                }

            graphicsPolygons = loadPolygonsFromSVG(graphicsElement);
            Elements polygonElements = graphicsElement.getElementsByTag("polygon");
            polygonElements.addAll(graphicsElement.getElementsByTag("rect"));
            int i = 0;
            for (Element element : polygonElements) {
                graphics.addPolygon(
                        graphicsPolygons.get(i),
                        Long.decode(element.attr("fill") + "FF")
                );
                i++;
            }
        }
        return graphics;
    }

    private static WorldPolygons loadWorldObjects(Document doc, Vec2 location){
        WorldPolygons objects = new WorldPolygons(location);
        List<Vec2[]> polygons = loadPolygonsFromSVG(doc.getElementById("EdgeShape"));
        objects.setPolygons(polygons);


        Element keyElements = doc.getElementById("KeyLocations");
        Elements textElements = keyElements.getElementsByTag("text");
        for (Element element : textElements) {
            String name = element.html();

            String transform = element.attr("transform")
                    .replace("matrix", "")
                    .replace("(", "")
                    .replace(")", "")
                    .trim();

            String[] data = transform.split(" ");
            float x = Float.parseFloat(data[data.length - 2]);
            float y = Float.parseFloat(data[data.length - 1]);
            x *= SCALE;
            y *= SCALE;
            y = docHeight*SCALE - y;

            objects.addKeyLocation(name, new Vec2(x,y));
        }
        return objects;
    }

    private static float getSVGWidth(Element element){
        String width = element.attr("width").replace("px","");
        return Float.parseFloat(width);
    }

    private static float getSVGHeight(Element element){
        String height = element.attr("height").replace("px","");
        return Float.parseFloat(height);
    }

    public static Document loadSVGDocument(String fileName){
        String file = loadTextFile("res/svg/" + fileName + ".svg");

        return Jsoup.parse(file);
    }



    public static List<Vec2[]> loadPolygonsFromSVG(Element element){
        Elements polygonElements = element.getElementsByTag("polygon");
        Elements rectElements = element.getElementsByTag("rect");
        List<Vec2[]> polygons = new ArrayList<>();
        for (Element polygon : polygonElements) {
            Vec2[] vertices = parseVertices(polygon.attr("points"));

            F.multiplyAll(vertices, SCALE);
            for (Vec2 vertex : vertices) {
                vertex.y = docHeight*SCALE - vertex.y;
            }
            polygons.add(vertices);
        }
        for (Element rect : rectElements){
            float x = Float.parseFloat(rect.attr("x"));
            float y = Float.parseFloat(rect.attr("y"));
            float width = Float.parseFloat(rect.attr("width"));
            float height = Float.parseFloat(rect.attr("height"));
            Vec2[] vertices = new Vec2[5];
            vertices[0] = new Vec2(x,y);
            vertices[1] = new Vec2(x,y + height);
            vertices[2] = new Vec2(x + width,y + height);
            vertices[3] = new Vec2(x + width,y);
            vertices[4] = new Vec2(x,y);

            F.multiplyAll(vertices, SCALE);
            for (Vec2 vertex : vertices) {
                vertex.y = docHeight*SCALE - vertex.y;
            }

            polygons.add(vertices);
        }
        return polygons;
    }

    private static Vec2[] parseVertices(String pointsString){
        pointsString = pointsString.trim().replace("  ", " ").replace("  ", " ");
        String[] pointsArray = pointsString.split(" ");
        int count = pointsArray.length;
        Vec2[] vertices = new Vec2[count];
        for (int i = 0; i < pointsArray.length; i++) {
            String[] xy = pointsArray[i].split(",");
            vertices[i] = new Vec2(Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
        }
        return vertices;
//        float x = Float.parseFloat(pointsString.substring(0,pointsString.indexOf(",")));
//        float y = Float.parseFloat(pointsString.substring(pointsString.indexOf(",")+1,pointsString.indexOf(" ")));
//        vertices[0] = new Vec2(x,y);
//
//        int start = pointsString.indexOf(" ") +1;
//        for (int i = 1; i < count; i++) {
//            x = Float.parseFloat(pointsString.substring(start, pointsString.indexOf(",",start)));
//            y = Float.parseFloat(pointsString.substring(pointsString.indexOf(",",start) + 1, pointsString.indexOf(" ",start)));
//            vertices[i] = new Vec2(x,y);
//            start = pointsString.indexOf(" ",start)+1;
//        }
//
//        return vertices;
    }

    public static int loadTextureIDFromSVG(Element element){
        String imageName = element.attr("xlink:href");
        imageName = imageName.replace(".png", "").replace("../images/","");
        return loadTexture("images/" + imageName);
    }

    public static Matrix4f loadMatrixFromSVG(Element element){
        float imageHeight = Float.parseFloat(element.attr("height"));
        float imageWidth = Float.parseFloat(element.attr("width"));

        String strMatrix = element.attr("transform");
        strMatrix = strMatrix.replaceAll("matrix", "").replace("(", "").replace(")", "");
        String[] strValues = strMatrix.split(" ");
        float[] values = new float[strValues.length];
        for (int i = 0; i < strValues.length; i++) {
            values[i] = Float.parseFloat(strValues[i]);
        }

        return svgToMatrix(values, imageWidth, imageHeight, docHeight);
    }

    public static RawModel loadObjModel(String filename){
        FileReader fr = null;

        try {
            fr = new FileReader(new File("res/"+filename+".obj"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fr);
        String line;

        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Short> indices = new ArrayList<>();

        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] textureArray = null;
        short[] indicesArray = null;

        try {

            while (true){
                line = reader.readLine();
                String[] currentLine = line.split(" ");

                if (line.startsWith("v ")){
                    Vector3f vertex = new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")){
                    Vector2f texture = new Vector2f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")){
                    Vector3f normal = new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")){
                    textureArray = new float[vertices.size()*2];
                    normalsArray = new float[vertices.size()*3];
                    break;
                }
            }

            while(line!=null){
                if (!line.startsWith("f ")){
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
                line = reader.readLine();

            }
            reader.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size()*3];
        indicesArray = new short[indices.size()];

        int vertexPointer = 0;

        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);


        }

        return Loader.loadTextureModel(verticesArray, indicesArray, textureArray, Loader.loadTexture("stallTexture"));
    }

    private static void processVertex(String[] vertexData, List<Short> indices,
                                      List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,
                                      float[] normalsArray){
        short currentVertexPointer = (short) (Short.parseShort(vertexData[0]) - 1);
        indices.add(currentVertexPointer);
        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertexPointer*2] = currentTex.x;
        textureArray[currentVertexPointer*2+1] = 1 - currentTex.y;

        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
        normalsArray[currentVertexPointer*3] = currentNorm.x;
        normalsArray[currentVertexPointer*3 + 1] = currentNorm.y;
        normalsArray[currentVertexPointer*3 + 2] = currentNorm.z;


    }

    private static Matrix4f svgToMatrix(float[] svgFloats, float imageWidth, float imageHeight, float docHeight){
        Matrix4f matrixFloats = new Matrix4f();
        Matrix4f matrixTemp = new Matrix4f();
        matrixFloats.setIdentity();
        matrixTemp.setIdentity();

        Matrix4f.scale(new Vector3f(SCALE, SCALE, 0), matrixTemp, matrixTemp);
        Matrix4f.translate(new Vector3f(0, imageHeight, 0), matrixTemp, matrixTemp);

//        matrixFloats[0]  = svgFloats[0];
//        matrixFloats[1]  = -svgFloats[1];
//        matrixFloats[4]  = -svgFloats[2];
//        matrixFloats[5]  = svgFloats[3];
//        matrixFloats[12] = svgFloats[4];
//        matrixFloats[13] = (docHeight - imageHeight-svgFloats[5]);

        //      0   1   2   3
        //  0 [ 0   1   2   3 ]
        //  1 [ 4   5   6   7 ]
        //  2 [ 8   9   10  11]
        //  3 [ 12  13  14  15]
        //

        matrixFloats.m00  = svgFloats[0];
        matrixFloats.m01  = -svgFloats[1];
        matrixFloats.m10  = -svgFloats[2];
        matrixFloats.m11  = svgFloats[3];
        matrixFloats.m30 = svgFloats[4];
        matrixFloats.m31 = (docHeight - imageHeight-svgFloats[5]);

        Matrix4f.mul(matrixTemp, matrixFloats, matrixFloats);

        Matrix4f.scale(new Vector3f(imageWidth, imageHeight, 0), matrixFloats, matrixFloats);
        Matrix4f.translate(new Vector3f(0, -1, 0), matrixFloats, matrixFloats);

        return matrixFloats;
    }


    public static TextBufferData loadTextBuffers(GUIText text){
        return dynamicCreator.createTextMesh(text);
    }

    private static int[] listToArray(List<Integer> list){
        int[] array = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }


    public static void cleanUp(){
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }

        for (int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }

        for (int texture : textures) {
            GL15.glDeleteBuffers(texture);
        }

    }

    private static int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private static void storeDataInAttributeList(int attributeNumber, int coordinateSize, FloatBuffer data){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private static void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private static void storeDataInAttributeList(int attributeNumber, float[] data){
        storeDataInAttributeList(attributeNumber, 3, data);
    }

    private static void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    private static void bindIndicesBuffer(short[] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        ShortBuffer buffer = storeDataInShortBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

    }

    private static IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }


    private static ShortBuffer storeDataInShortBuffer(short[] data){
        ShortBuffer buffer = BufferUtils.createShortBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public static FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }


    public static float getSCALE() {
        return SCALE;
    }

    public static float getDocHeight() {
        return docHeight;
    }


    public static boolean doesFileExist(String file) {
        return new File(file).exists();
    }
//
//    private static int storeDataInAttributeList(float[] data){
//        FloatBuffer buffer = storeDataInFloatBuffer(data);
//        buffer.position(0);
//        return VAO.add(buffer);
//    }
//
//
//    public static int storeDataInAttributeList(float[] data, int dataLength){
////        data = Arrays.copyOf(data, dataLength);
//        FloatBuffer buffer = storeDataInFloatBuffer(data, dataLength);
//        buffer.position(0);
//        return VAO.add(buffer);
//    }
//
//    private static int bindIndicesBuffer(short[] indices){
//        ShortBuffer buffer = storeDataInShortBuffer(indices);
//        buffer.position(0);
//        return VAO.add(buffer);
//    }
//
//    private static IntBuffer storeDataInIntBuffer(int[] data){
//        ByteBuffer bb = ByteBuffer.allocateDirect(data.length*4);
//        bb.order(ByteOrder.nativeOrder());
//        IntBuffer buffer = bb.asIntBuffer();
//        buffer.put(data);
//        buffer.flip();
//        return buffer;
//    }
//
//    public static FloatBuffer storeDataInFloatBuffer(float[] data){
//        ByteBuffer bb = ByteBuffer.allocateDirect(data.length*4);
//        bb.order(ByteOrder.nativeOrder());
//        FloatBuffer buffer = bb.asFloatBuffer();
//        buffer.put(data);
//        buffer.flip();
//        return buffer;
//    }
//
//    public static FloatBuffer storeDataInFloatBuffer(float[] data, int dataLength){
//        ByteBuffer bb = ByteBuffer.allocateDirect(dataLength*4);
//        bb.order(ByteOrder.nativeOrder());
//        FloatBuffer buffer = bb.asFloatBuffer();
//        buffer.put(data, 0, dataLength);
//        buffer.flip();
//        return buffer;
//    }
//

}
