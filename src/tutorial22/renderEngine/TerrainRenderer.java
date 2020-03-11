package tutorial22.renderEngine;

import java.util.List;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import tutorial22.entities.Entity;
import tutorial22.models.RawModel;
import tutorial22.models.TexturedModel;
import tutorial22.shaders.TerrainShader;
import tutorial22.terrains.Terrain;
import tutorial22.textures.ModelTexture;
import tutorial22.textures.TerrainTexturePack;
import tutorial22.toolbox.Maths;

/**
 * Визуализация ландшафта
 */
public class TerrainRenderer {

    /** Шейдер ландшафта */
    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix); // загружаем матрицу проекции
        shader.connectTextureUnits(); // соединяем текстуры с текстурными блокаим
        shader.stop();
    }
    
    /**
     * Визуализация ландшафта
     * @param terrains список ландшафтов
     */
    public void render(List<Terrain> terrains) {
        for (Terrain terrain : terrains) {
            prepareTerrain(terrain); // подготовка к визуализации
            loadModelMatrix(terrain); // загрузка матрицы преобразования
            // рисуем ландшафт
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTexturedModel(); // открепляем модель
        }
    }
    
    /**
     * Подготовка текстурированной модели к визуализации
     * @param model текстурированная модель
     */
    private void prepareTerrain(Terrain terrain) {
        RawModel rawModel = terrain.getModel(); // данные модели
        
        // Связываем VAO модели, указываем что будем работать с этими данными
        GL30.glBindVertexArray(rawModel.getVaoId());
        // Активируем нулевой списки атрибутов
        GL20.glEnableVertexAttribArray(0); // координаты вершин
        GL20.glEnableVertexAttribArray(1); // текстурные координаты
        GL20.glEnableVertexAttribArray(2); // векторы нормали
        
        bindTexture(terrain);
        // загрузка переменных отражения        
        shader.loadShineVariables(1, 0); // пока небудет блеска на ландшафте
        
    }
    
    /**
     * Привязываем текстуры ландшафта 
     * @param terrain ландшафт с заданными текстурами
     */
    private void bindTexture(Terrain terrain) {
        TerrainTexturePack texturePack = terrain.getTexturePack();         
        // Активируем текстурный блок перед привязкой текстуры
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        // Привяжем к текстурному блоку 0 фоновую текстуру
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
        // Привяжем к текстурному блоку 1 текстуру красного цвета
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
        // Привяжем к текстурному блоку 2 текстуру зеленого цвета
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
        // Привяжем к текстурному блоку 3 текстуру синего цвета
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());
        // Привяжем к текстурному блоку 4 текстуру карты смешевания текстур
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
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
     * Загрузка модели матрицы
     * @param terrain экземпляр объекта
     */
    private void loadModelMatrix(Terrain terrain) {
        // создадим матрицу преобразования  и передадим преобразования 
        Matrix4f transformationMatrix = Maths.getTransformationMatrix(
                new Vector3f(terrain.getX(), 0, terrain.getZ()),
                0,0,0,1);
        // передача преобразований в шейдер
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
