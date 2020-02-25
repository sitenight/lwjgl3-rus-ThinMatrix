package tutorial14.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tutorial14.entities.Camera;
import tutorial14.entities.Entity;
import tutorial14.entities.Light;
import tutorial14.models.TexturedModel;
import tutorial14.shaders.StaticShader;

public class MasterRenderer {

    /** Статический шейдер */
    private StaticShader shader = new StaticShader();
    /** Визуализация */
    private Renderer renderer = new Renderer(shader);
    
    /** содержит все текстурированные модели для визуализации */
    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    
    /**
     * Визуализация объектов
     * @param sun источник света
     * @param camera камера
     */
    public void render(Light sun, Camera camera) {
        renderer.prepare(); // подготовка окна для рисования кадра
        shader.start(); // запускаем шейдер статических моделей
        shader.loadLight(sun); //загружаем в шейдер источник света
        shader.loadViewMatrix(camera); // обновляем матрицу вида относительно положения камеры
        renderer.render(entities); // визуализация списка моделей
        shader.stop(); // останавливаем шейдер статических моделей
        entities.clear();
    }
    
    /**
     * Сортировка экземпляров объекта
     * @param entity экземпляр объекта
     */
    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        // Если эта модель существует
        if(batch != null) 
            batch.add(entity);
        else { // создание новой текстурированной модели
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }
    
    /**
     * Очистка данных
     */
    public void cleanUp() {
        shader.cleanUp();
    }
}
