package tutorial13.engineTester;

import org.joml.Vector3f;
import tutorial13.entities.Camera;
import tutorial13.entities.Entity;
import tutorial13.entities.Light;
import tutorial13.renderEngine.DisplayManager;
import tutorial13.renderEngine.Loader;
import tutorial13.models.RawModel;
import tutorial13.models.TexturedModel;
import tutorial13.renderEngine.OBJLoader;
import tutorial13.renderEngine.Renderer;
import tutorial13.shaders.StaticShader;
import tutorial13.textures.ModelTexture;

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
        // Установка переменных блеска
        texture.setShineDamper(20);
        texture.setReflectivity(0f);

        // Создание текстурной модели
        TexturedModel staticModel = new TexturedModel(model, texture);
        
        Entity entity = new Entity(staticModel, 
                new Vector3f(0, -2f, -5f), 
                0, 200, 0, 
                1.0f);
        // создание источника света
        Light light = new Light(new Vector3f(0, 10, 0), new Vector3f(1, 1, 1));
        
        Camera camera = new Camera();
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) {  
           // entity.increaseRotation(0, 1, 0);
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
