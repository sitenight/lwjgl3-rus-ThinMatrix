package tutorial14.engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.joml.Vector3f;
import tutorial14.entities.Camera;
import tutorial14.entities.Entity;
import tutorial14.entities.Light;
import tutorial14.renderEngine.DisplayManager;
import tutorial14.renderEngine.Loader;
import tutorial14.models.RawModel;
import tutorial14.models.TexturedModel;
import tutorial14.renderEngine.MasterRenderer;
import tutorial14.renderEngine.OBJLoader;
import tutorial14.renderEngine.EntityRenderer;
import tutorial14.shaders.StaticShader;
import tutorial14.textures.ModelTexture;

/**
 * Основной цикл игры
 */
public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        
        Loader loader = new Loader(); // загрузчик моделей
        
        // загружаем модель в память OpenGL
        RawModel model = OBJLoader.loadObjModel("res/tutorial14/box.obj", loader);
        // загрузим текстуру используя загрузчик
        ModelTexture texture = new ModelTexture(loader.loadTexture("res/tutorial14/box.png"));
        // Установка переменных блеска
        texture.setShineDamper(20);
        texture.setReflectivity(0f);

        // Создание текстурной модели
        TexturedModel staticModel = new TexturedModel(model, texture);
        // создание источника света
        Light light = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1));
        
        Camera camera = new Camera();
        
        List<Entity> allBox = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 100 - 50;
            float y = random.nextFloat() * 100 - 50;
            float z = random.nextFloat() * -300;
            allBox.add(new Entity( staticModel,
                    new Vector3f(x, y, z), 
                    random.nextFloat() * 180f,
                    random.nextFloat() * 180f, 0f, 1f));
        }
        
        MasterRenderer renderer = new MasterRenderer();
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) {  
            camera.move(); // двигаем камеру            
            // рисуем объекты
            for (Entity box : allBox) 
                renderer.processEntity(box);
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }
        
        renderer.cleanUp(); // очищаем визуализацию
        loader.cleanUp(); // очищаем память от загруженной модели
        DisplayManager.closeDisplay();
    }
}
