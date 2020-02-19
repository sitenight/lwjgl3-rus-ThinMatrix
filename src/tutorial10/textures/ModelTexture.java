package tutorial10.textures;

/**
 * Класс для текстурирования нашей модели
 */
public class ModelTexture {

    /** Идентификатор текстуры */
    private int textureId;

    public ModelTexture(int textureId) {
        this.textureId = textureId;
    }

    public int getId() {
        return textureId;
    }
}

