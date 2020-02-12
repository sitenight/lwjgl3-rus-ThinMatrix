 package tutorial07.engineTester;

import tutorial07.renderEngine.DisplayManager;
import tutorial07.renderEngine.Loader;
import tutorial07.models.RawModel;
import tutorial07.models.TexturedModel;
import tutorial07.renderEngine.Renderer;
import tutorial07.shaders.StaticShader;
import tutorial07.textures.ModelTexture;

/**
 * Основной цикл игры
 */
public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        
        Loader loader = new Loader(); // загрузчик моделей
        Renderer renderer = new Renderer(); // визуализатор моделей
        StaticShader shader = new StaticShader(); // шейдер статических моделей
        
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
        TexturedModel texturedModel = new TexturedModel(model, texture);
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) {            
            renderer.prepare(); // подготовка окна для рисования кадра
            
            shader.start(); // запускаем шейдер статических моделей
            renderer.render(texturedModel); // рисуем модель
            shader.stop(); // останавливаем шейдер статических моделей
            
            DisplayManager.updateDisplay();
        }
        
        shader.cleanUp(); // очищаем шейдер статических моделей
        loader.cleanUp(); // очищаем память от загруженной модели
        DisplayManager.closeDisplay();
    }
}
