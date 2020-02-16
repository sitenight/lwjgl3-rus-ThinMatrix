 package tutorial08.engineTester;

import org.joml.Vector3f;
import tutorial08.entities.Entity;
import tutorial08.renderEngine.DisplayManager;
import tutorial08.renderEngine.Loader;
import tutorial08.models.RawModel;
import tutorial08.models.TexturedModel;
import tutorial08.renderEngine.Renderer;
import tutorial08.shaders.StaticShader;
import tutorial08.textures.ModelTexture;

/**
 * Основной цикл игры
 */
public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        
        Loader loader = new Loader(); // загрузчик моделей
        StaticShader shader = new StaticShader(); // шейдер статических моделей
        Renderer renderer = new Renderer(shader); // визуализатор моделей
        
        float[] vertices = {
            -0.5f,  0.5f, 0f, // V0
            -0.5f, -0.5f, 0f, // V1
             0.5f, -0.5f, 0f, // V2
             0.5f,  0.5f, 0f, // V3
        };
        
        int[] indices = {
            0, 1, 3, // Верхний левый треугольник 
            3, 1, 2, // Нижний правый треугольник
        };
        
        float[] textureCoords = {
            0.0f, 0.0f, // V0
            0.0f, 1.0f, // V1
            1.0f, 1.0f, // V2
            1.0f, 0.0f, // V3
        };
        
        // загружаем массив вершин, текстурных координат и индексов в память GPU
        RawModel model = loader.loadToVao(vertices, textureCoords, indices);
        // загрузим текстуру используя загрузчик
        ModelTexture texture = new ModelTexture(loader.loadTexture("res/tutorial06/image.png"));
        // Создание текстурной модели
        TexturedModel staticModel = new TexturedModel(model, texture);
        
        Entity entity = new Entity(staticModel, 
                new Vector3f(-0.4f, 0, 0), 
                0, 0, 30, 
                1.0f);
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) {     
            entity.increacePosition(0, 0, -0.1f);
            
            renderer.prepare(); // подготовка окна для рисования кадра
            
            shader.start(); // запускаем шейдер статических моделей
            renderer.render(entity, shader); // рисуем объект
            shader.stop(); // останавливаем шейдер статических моделей
            
            DisplayManager.updateDisplay();
        }
        
        shader.cleanUp(); // очищаем шейдер статических моделей
        loader.cleanUp(); // очищаем память от загруженной модели
        DisplayManager.closeDisplay();
    }
}
