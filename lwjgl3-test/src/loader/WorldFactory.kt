package loader

import compatibility.Polygon
import org.jbox2d.common.Vec2
import org.joml.Matrix4f
import org.joml.Vector3f
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import util.extensions.timesAssign
import world.WorldGraphics
import world.WorldPolygons
import world.WorldSection
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by domin on 01/11/2017.
 */


class WorldFactory(
        fileName: String,
        private val position: Vec2 = Vec2(0f, 0f),
        private val scale: Double = 1.0) {

    private val docHeight: Float
    private val doc: Document

    private val map: HashMap<String, Int> = HashMap()

    private val graphics = WorldGraphics()
    private val polygons = WorldPolygons()

    val world: WorldSection
        get() = WorldSection(graphics, polygons, doc)

    init {
        doc = loadSVGDocument(fileName)
        docHeight = doc
                .getElementsByTag("svg").first()
                .attr("height")
                .replace("px", "").toFloat()

        loadWorldGraphics()
        loadWorldPolygons()

    }

    private fun loadSVGDocument(fileName: String): Document {

        val filePath = if (fileName.endsWith(".svg")) "res/svg/$fileName"
                       else "res/svg/$fileName.svg"

        return Jsoup.parse(loadTextFile(filePath))
    }


    private fun loadWorldGraphics(): WorldGraphics {

        val graphicsElement: Element? = doc.getElementById("Graphics")

        if (graphicsElement != null) {

            for (element in graphicsElement.allElements){
                if (element.tagName() == "image"){
                    graphics.add(
                            loadTextureIDFromSVG(element),
                            loadMatrixFromSVG(element)
                    )
                }
                if (element.tagName() == "polygon"){
                    val vertices: Array<Vec2> = parseVertices(element.attr("points"))

                    vertices *= scale
                    for (vertex in vertices) {
                        vertex.y = (docHeight * scale - vertex.y).toFloat()
                        vertex.y += position.y
                        vertex.x += position.x
                    }
                    graphics.add(Polygon(vertices))
                }
                if (element.tagName() == "rect"){
                    val x = element.attr("x").toFloat()
                    val y = element.attr("y").toFloat()
                    val width = element.attr("width").toFloat()
                    val height = element.attr("height").toFloat()
                    val vertices = arrayOf(
                        Vec2(x, y),
                        Vec2(x, y + height),
                        Vec2(x + width, y + height),
                        Vec2(x + width, y)
//                        Vec2(x, y)
                    )
                    vertices *= scale
                    for (vertex in vertices) {
                        vertex.y = (docHeight * scale - vertex.y).toFloat()
                        vertex.y += position.y
                        vertex.x += position.x
                    }

                    graphics.add(Polygon(vertices))
                }
            }

        }
        return graphics
    }

    private fun loadWorldPolygons(): WorldPolygons {

        val groundElements: Element? = doc.getElementById("EdgeShape")

        if (groundElements != null) {

            for (element in groundElements.allElements){
                if (element.tagName() == "polygon"){
                    val vertices: Array<Vec2> = parseVertices(element.attr("points"))

                    vertices *= scale
                    for (vertex in vertices) {
                        vertex.y = (docHeight * scale - vertex.y).toFloat()
                        vertex.y += position.y
                        vertex.x += position.x
                    }
                    polygons.add(Polygon(vertices))
                }
                if (element.tagName() == "rect"){
                    val x = element.attr("x").toFloat()
                    val y = element.attr("y").toFloat()
                    val width = element.attr("width").toFloat()
                    val height = element.attr("height").toFloat()
                    val vertices = arrayOf(
                            Vec2(x, y),
                            Vec2(x, y + height),
                            Vec2(x + width, y + height),
                            Vec2(x + width, y)
//                        Vec2(x, y)
                    )
                    vertices *= scale
                    for (vertex in vertices) {
                        vertex.y = (docHeight * scale - vertex.y).toFloat()
                        vertex.y += position.y
                        vertex.x += position.x
                    }

                    polygons.add(Polygon(vertices))
                }
            }

        }
        return polygons
    }

    private fun parseVertices(pointsString: String): Array<Vec2> {
        val pointsArray = pointsString
                .trim()
                .replace(" +".toRegex(), " ")
                .split(" ")

        return Array(pointsArray.size, {
            val xy = pointsArray[it].split(",")
            Vec2(xy[0].toFloat(), xy[1].toFloat())
        })
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

    private fun loadTextureIDFromSVG(element: Element): Int {
        var imageName = element.attr("xlink:href")
        imageName = imageName.replace(".png", "").replace("../images/", "")
        if (!map.containsKey(imageName)){
            map.put(imageName, loadTexture("images/" + imageName))
        }
        return map[imageName]!!
    }

    private fun loadMatrixFromSVG(element: Element): Matrix4f {
        val imageHeight = java.lang.Float.parseFloat(element.attr("height"))
        val imageWidth = java.lang.Float.parseFloat(element.attr("width"))

        var strMatrix = element.attr("transform")
        strMatrix = strMatrix.replace("matrix|[()]".toRegex(), "")
        val strValues = strMatrix.split(" ")//.dropLastWhile { it.isEmpty() }.toTypedArray()
        val values = FloatArray(strValues.size)
        for (i in strValues.indices) {
            values[i] = strValues[i].toFloat()
        }

        return svgToMatrix(values, imageWidth, imageHeight, docHeight)
    }

    private fun svgToMatrix(svgFloats: FloatArray, imageWidth: Float, imageHeight: Float, docHeight: Float): Matrix4f {
        var matrixFloats = Matrix4f()
        var matrixTemp = Matrix4f()
        matrixFloats.identity()
        matrixTemp.identity()

        matrixTemp.scale(Vector3f(scale.toFloat(), scale.toFloat(), 0f))
        matrixTemp.translate(Vector3f(0f, imageHeight, 0f))

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

        matrixFloats.m00(svgFloats[0])
        matrixFloats.m01(-svgFloats[1])
        matrixFloats.m10(-svgFloats[2])
        matrixFloats.m11(svgFloats[3])
        matrixFloats.m30(svgFloats[4])
        matrixFloats.m31(docHeight - imageHeight - svgFloats[5])

        matrixFloats = matrixTemp.mul(matrixFloats)

        matrixFloats.scale(Vector3f(imageWidth, imageHeight, 0f))
        matrixFloats.translate(Vector3f(0f, -1f, 0f))

        matrixTemp = Matrix4f()
        matrixTemp.identity()
        matrixTemp.translate(Vector3f(position.x, position.y, 0f))
        matrixFloats = matrixTemp.mul(matrixFloats)

        return matrixFloats
    }
}