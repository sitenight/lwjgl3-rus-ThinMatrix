package tutorial19.models;

/**
 * Представляет 3д модели хранящиеся в памяти
 */
public class RawModel {
    
    /** Идентификатор VAO (Объект вершинного массива)*/
    private int vaoId;
    /** Сколько вершин хранит модель */
    private int vertexCount;

    public RawModel(int vaoId, int vertexCount) {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
