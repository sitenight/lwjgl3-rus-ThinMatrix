package tutorial14.textures;

/**
 * Класс для текстурирования нашей модели
 */
public class ModelTexture {

    /** Идентификатор текстуры */
    private int textureId;
    
    /** коэффициент блеска материала */
    private float shineDamper = 1;
    /** отражательная способность(0 - нет отражения) */
    private float reflectivity = 0;

    public ModelTexture(int textureId) {
        this.textureId = textureId;
    }

    public int getId() {
        return textureId;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
    
    
}

