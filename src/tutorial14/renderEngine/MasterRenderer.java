package tutorial14.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import tutorial14.entities.Camera;
import tutorial14.entities.Entity;
import tutorial14.entities.Light;
import tutorial14.models.TexturedModel;
import tutorial14.shaders.StaticShader;

public class MasterRenderer {
    
    /** Поле зрения: Угол поля зрения в радианах */
    private static final float FOV = (float) Math.toRadians(60.0f);    
    /** Расстояние до ближней плоскости */
    private static final float Z_NEAR = 0.01f;    
    /** Расстояние до дальней плоскости */
    private static final float Z_FAR = 1000.f;
    /** Матрица проэкции */
    private Matrix4f projectionMatrix;

    /** Статический шейдер */
    private StaticShader shader = new StaticShader();
    /** Визуализация */
    private EntityRenderer renderer;
    
    /** содержит все текстурированные модели для визуализации */
    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    public MasterRenderer() {
        GL11.glEnable(GL11.GL_CULL_FACE); // включаем отсечение невидимых поверхностей
        GL11.glCullFace(GL11.GL_BACK); // отсекаем заднюю сторону поверхности
        
        // генерируем матрицу проэкции
        projectionMatrix = new Matrix4f().identity()
            .setPerspective(FOV, DisplayManager.WINDOW_WIDTH/ DisplayManager.WINDOW_HEIGHT, Z_NEAR, Z_FAR);
        
        renderer = new EntityRenderer(shader, projectionMatrix);
    }
    
    /**
     * Визуализация объектов
     * @param sun источник света
     * @param camera камера
     */
    public void render(Light sun, Camera camera) {
        prepare(); // подготовка окна для рисования кадра
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
     * Вызввается каждый кадр данный метод.
     */
    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST); // включаем тест глубины
        // Очистка экрана и буфера глубины, а также рисование цветом в цветовом буфере
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); 
        GL11.glClearColor(0.51f, 0.71f, 0.87f, 0.0f); // Загрузка выбранного цвета в цветовой буфер
    }
    
    /**
     * Очистка данных
     */
    public void cleanUp() {
        shader.cleanUp();
    }
}
