package tutorial12.engineTester;

import org.joml.Vector3f;
import tutorial12.entities.Camera;
import tutorial12.entities.Entity;
import tutorial12.entities.Light;
import tutorial12.renderEngine.DisplayManager;
import tutorial12.renderEngine.Loader;
import tutorial12.models.RawModel;
import tutorial12.models.TexturedModel;
import tutorial12.renderEngine.OBJLoader;
import tutorial12.renderEngine.Renderer;
import tutorial12.shaders.StaticShader;
import tutorial12.textures.ModelTexture;

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
        RawModel model = OBJLoader.loadObjModel("res/tutorial12/dragon.obj", loader);
        // загрузим текстуру используя загрузчик
        ModelTexture texture = new ModelTexture(loader.loadTexture("res/tutorial12/dragon.png"));
        // Создание текстурной модели
        TexturedModel staticModel = new TexturedModel(model, texture);
        
        Entity entity = new Entity(staticModel, 
                new Vector3f(0, -5f, -20f), 
                0, 0, 0, 
                1.0f);
        // создание источника света
        Light light = new Light(new Vector3f(0, 10, -20), new Vector3f(1, 1, 1));
        
        Camera camera = new Camera();
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) {  
            entity.increaseRotation(0, 1, 0);
            camera.move(); // двигаем камеру
            
            renderer.prepare(); // подготовка окна для рисования кадра
            
            shader.start(); // запускаем шейдер статических моделей
            shader.loadLight(light); //загружаем в шейдер источник света
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
