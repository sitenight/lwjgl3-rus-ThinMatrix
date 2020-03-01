package tutorial18.objConverter;

public class ModelData {

    /** массив вершин */
    private float[] vertices;
    /** массив текстурных координат */
    private float[] textureCoords;
    /** массив векторов нормалей */
    private float[] normals;
    /** массив индексов */
    private int[] indices;
    /** дальняя точка ?? */
    private float furthestPoint;
 
    public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
            float furthestPoint) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
    }
 
    public float[] getVertices() {
        return vertices;
    }
 
    public float[] getTextureCoords() {
        return textureCoords;
    }
 
    public float[] getNormals() {
        return normals;
    }
 
    public int[] getIndices() {
        return indices;
    }
 
    public float getFurthestPoint() {
        return furthestPoint;
    }
}
