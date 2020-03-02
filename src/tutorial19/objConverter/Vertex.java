package tutorial19.objConverter;

import org.joml.Vector3f;

/**
 * Класс вершины
 */
public class Vertex {

    private static final int NO_INDEX = -1;
    
    /** Позиция вершины */
    private Vector3f position;
    /** индекст текстуры */
    private int textureIndex = NO_INDEX;
    /** Индекс нормали */
    private int normalIndex = NO_INDEX;
    /** Дубликат вершины */
    private Vertex duplicateVertex = null;
    /** индек */
    private int index;
    /** длина вектора вершины */
    private float length;
     
    /**
     * Конструктор 3д вершины
     * @param index индекс вершины
     * @param position позиция вершины
     */
    public Vertex(int index,Vector3f position){
        this.index = index;
        this.position = position;
        this.length = position.length();
    }
     
    /**
     * Получение индекса 3д вершины 
     * @return индекс вершины
     */
    public int getIndex(){
        return index;
    }
     
    /**
     * Получение длины вектора вершины
     * @return длина вершины
     */
    public float getLength(){
        return length;
    }
     
    /**
     * Установлено ли индекс текстуры и индекс нормали
     * @return true если присутствует индекс вершины и индекс нормали, иначе false
     */
    public boolean isSet(){
        return textureIndex!=NO_INDEX && normalIndex!=NO_INDEX;
    }
     
    /**
     * Сравнение соответствование в данной вершине индекса текстуры и
     * индекса нормали с проверяемыми
     * @param textureIndexOther проверяемый индекст текстуры
     * @param normalIndexOther проверяемый индекст нормали
     * @return true если соответствует
     */
    public boolean hasSameTextureAndNormal(int textureIndexOther,int normalIndexOther){
        return textureIndexOther==textureIndex && normalIndexOther==normalIndex;
    }
     
    public void setTextureIndex(int textureIndex){
        this.textureIndex = textureIndex;
    }
     
    public void setNormalIndex(int normalIndex){
        this.normalIndex = normalIndex;
    }
 
    public Vector3f getPosition() {
        return position;
    }
 
    public int getTextureIndex() {
        return textureIndex;
    }
 
    public int getNormalIndex() {
        return normalIndex;
    }
 
    public Vertex getDuplicateVertex() {
        return duplicateVertex;
    }
 
    public void setDuplicateVertex(Vertex duplicateVertex) {
        this.duplicateVertex = duplicateVertex;
    }
}
