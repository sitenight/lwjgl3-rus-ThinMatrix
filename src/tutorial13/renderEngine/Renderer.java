package tutorial13.renderEngine;

import java.util.List;
import java.util.Map;
import org.joml.Matrix4f;
import tutorial13.models.RawModel;
import tutorial13.models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import tutorial13.shaders.StaticShader;
import tutorial13.entities.Entity;
import tutorial13.textures.ModelTexture;
import tutorial13.toolbox.Maths;

/**
 * Визуализация данных
 */
public class Renderer {
    
    /** Поле зрения: Угол поля зрения в радианах */
    private static final float FOV = (float) Math.toRadians(60.0f);    
    /** Расстояние до ближней плоскости */
    private static final float Z_NEAR = 0.01f;    
    /** Расстояние до дальней плоскости */
    private static final float Z_FAR = 1000.f;
    
    private StaticShader shader;

    public Renderer(StaticShader shader) {
        this.shader = shader;
        GL11.glEnable(GL11.GL_CULL_FACE); // включаем отсечение невидимых поверхностей
        GL11.glCullFace(GL11.GL_BACK); // отсекаем заднюю сторону поверхности
        shader.start();
        shader.loadProjectionMatrix(new Matrix4f().identity()
                    .setPerspective(FOV, DisplayManager.WINDOW_WIDTH/ DisplayManager.WINDOW_HEIGHT, Z_NEAR, Z_FAR));
        shader.stop();
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
     * Визуализация списка текстурированных моделей
     * @param entities список моделей
     */
    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel model : entities.keySet()) { // для каждой модели
            prepareTexturedModel(model); // подготавливаем текстурированную модель
            List<Entity> batch = entities.get(model); // получаем все сущности
            for (Entity entity : batch) { // проходимся по каждому объекту
                prepareInstance(entity); // подготавливаем объект
                
                // Рисуем примитивы. Аргументы:
                // - тип примитива (в данном случаем треугольники)
                // - количество вершин в модели
                // - указываем тип индексов данных в памяти
                // - смещение
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }
    
    /**
     * Подготовка текстурированной модели к визуализации
     * @param model текстурированная модель
     */
    private void prepareTexturedModel(TexturedModel model) {
        RawModel rawModel = model.getRawModel(); // данные модели
        
        // Связываем VAO модели, указываем что будем работать с этими данными
        GL30.glBindVertexArray(rawModel.getVaoId());
        // Активируем нулевой списки атрибутов
        GL20.glEnableVertexAttribArray(0); // координаты вершин
        GL20.glEnableVertexAttribArray(1); // текстурные координаты
        GL20.glEnableVertexAttribArray(2); // векторы нормали
        
        // загрузка переменных отражения
        ModelTexture texture = model.getTexture();
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        
        // Активируем текстурный блок перед привязкой текстуры
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        // Привяжем её, чтобы функции, использующие текстуры, знали какую текстуру использовать
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
    }
    
    /**
     * Открепление текстурированной модели
     */
    private void unbindTexturedModel() {
        // т.к. закончили использовать нулевой список атрибутов, то отключаем
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        // отвязываем VAO модели
        GL30.glBindVertexArray(0);
    }
    
    /**
     * Подготовка экземпляра объекта
     * @param entity экземпляра объекта
     */
    private void prepareInstance(Entity entity) {
        // создадим матрицу преобразования  и передадим преобразования 
        Matrix4f transformationMatrix = Maths.getTransformationMatrix(
                entity.getPosition(), 
                entity.getRotationX(), 
                entity.getRotationY(), 
                entity.getRotationZ(), 
                entity.getScale());
        // передача преобразований в шейдер
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
