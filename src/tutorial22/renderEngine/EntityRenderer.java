package tutorial22.renderEngine;

import java.util.List;
import java.util.Map;
import org.joml.Matrix4f;
import tutorial22.models.RawModel;
import tutorial22.models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import tutorial22.shaders.StaticShader;
import tutorial22.entities.Entity;
import tutorial22.textures.ModelTexture;
import tutorial22.toolbox.Maths;

/**
 * Визуализация данных
 */
public class EntityRenderer {
    
    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
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
        // отключение отсечения задних граней если текстура с прозрачностью
        if(texture.isHasTransparency())
            MasterRenderer.disableCulling();
        // загрузка фальшивого освещения
        shader.loadFakeLightingVariable(texture.isUseFakeLighting());
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
        // включение отсечения задних граней 
        MasterRenderer.enableCulling();
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
