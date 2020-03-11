package tutorial21.models;

import tutorial21.textures.ModelTexture;

/**
 * Класс Текстурированной модели.
 * Этот класс объединит данные модели и текстуры.
 */
public class TexturedModel {

    /** загруженная модель */
    private RawModel model;
    
    /** загруженная текстура */
    private ModelTexture texture;

    public TexturedModel(RawModel model, ModelTexture texture) {
        this.model = model;
        this.texture = texture;
    }

    /**
     * Получение данных модели
     * @return модель
     */
    public RawModel getRawModel() {
        return model;
    }

    /**
     * Получение данных текстуры
     * @return текстуру
     */
    public ModelTexture getTexture() {
        return texture;
    }
}

