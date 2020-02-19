 package tutorial10.engineTester;

import org.joml.Vector3f;
import tutorial10.entities.Camera;
import tutorial10.entities.Entity;
import tutorial10.renderEngine.DisplayManager;
import tutorial10.renderEngine.Loader;
import tutorial10.models.RawModel;
import tutorial10.models.TexturedModel;
import tutorial10.renderEngine.OBJLoader;
import tutorial10.renderEngine.Renderer;
import tutorial10.shaders.StaticShader;
import tutorial10.textures.ModelTexture;

/**
 * Основной цикл игры
 */
public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        
        Loader loader = new Loader(); // загрузчик моделей
        StaticShader shader = new StaticShader(); // шейдер статических моделей
        Renderer renderer = new Renderer(shader); // визуализатор моделей
        
        // загружаем модель в память OpenGL
        RawModel model = OBJLoader.loadObjModel("res/tutorial10/stall.obj", loader);
        // загрузим текстуру используя загрузчик
        ModelTexture texture = new ModelTexture(loader.loadTexture("res/tutorial10/stallTexture.png"));
        // Создание текстурной модели
        TexturedModel staticModel = new TexturedModel(model, texture);
        
        Entity entity = new Entity(staticModel, 
                new Vector3f(0, 0, -20f), 
                0, 0, 0, 
                1.0f);
        
        Camera camera = new Camera();
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) {  
            entity.increaseRotation(0, 1, 0);
            camera.move(); // двигаем камеру
            
            renderer.prepare(); // подготовка окна для рисования кадра
            
            shader.start(); // запускаем шейдер статических моделей
            shader.loadViewMatrix(camera); // обновляем матрицу вида относительно положения камеры
            renderer.render(entity, shader); // рисуем объект
            shader.stop(); // останавливаем шейдер статических моделей
            
            DisplayManager.updateDisplay();
        }
        
        shader.cleanUp(); // очищаем шейдер статических моделей
        loader.cleanUp(); // очищаем память от загруженной модели
        DisplayManager.closeDisplay();
    }
}
