package tutorial14.engineTester;

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
import tutorial14.terrains.Terrain;
import tutorial14.textures.ModelTexture;

/**
 * Основной цикл игры
 */
public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        
        Loader loader = new Loader(); // загрузчик моделей
        
        // загружаем модель в память OpenGL
        RawModel model = OBJLoader.loadObjModel("res/tutorial13/box.obj", loader);
        // загрузим текстуру используя загрузчик
        ModelTexture texture = new ModelTexture(loader.loadTexture("res/tutorial13/box.png"));
        // Установка переменных блеска
        texture.setShineDamper(20);
        texture.setReflectivity(0f);

        // Создание текстурной модели
        TexturedModel staticModel = new TexturedModel(model, texture);
        
        Entity entity = new Entity(staticModel, new Vector3f(0, 0, -25), 0, 0, 0, 1f);
        // создание источника света
        Light light = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1));
        
        Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("res/tutorial14/grass.png")));
        Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("res/tutorial14/grassFlowers.png")));
        
        Camera camera = new Camera();
        
        MasterRenderer renderer = new MasterRenderer();
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) {  
            camera.move(); // двигаем камеру            
            // рисуем объекты
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processEntity(entity);
            
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }
        
        renderer.cleanUp(); // очищаем визуализацию
        loader.cleanUp(); // очищаем память от загруженной модели
        DisplayManager.closeDisplay();
    }
}
