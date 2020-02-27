package tutorial15.engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.joml.Vector3f;
import tutorial15.entities.Camera;
import tutorial15.entities.Entity;
import tutorial15.entities.Light;
import tutorial15.renderEngine.DisplayManager;
import tutorial15.renderEngine.Loader;
import tutorial15.models.RawModel;
import tutorial15.models.TexturedModel;
import tutorial15.renderEngine.MasterRenderer;
import tutorial15.renderEngine.OBJLoader;
import tutorial15.terrains.Terrain;
import tutorial15.textures.ModelTexture;

/**
 * Основной цикл игры
 */
public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        
        Loader loader = new Loader(); // загрузчик моделей
        
        // загружаем модель в память OpenGL
        RawModel model = OBJLoader.loadObjModel("res/tutorial14/tree.obj", loader);
        // загрузим текстуру используя загрузчик
        ModelTexture texture = new ModelTexture(loader.loadTexture("res/tutorial14/tree.png"));
        // Установка переменных блеска
        texture.setShineDamper(20);
        texture.setReflectivity(0f);

        // Создание текстурной модели
        TexturedModel staticModel = new TexturedModel(model, texture);
        
        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for(int i=0;i<500;i++){
            entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
        }
        // создание источника света
        Light light = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1));
        
        Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("res/tutorial14/grass.png")));
        Terrain terrain2 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("res/tutorial14/grassFlowers.png")));
        
        Camera camera = new Camera();
        
        MasterRenderer renderer = new MasterRenderer();
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) {  
            camera.move(); // двигаем камеру            
            // рисуем объекты
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            for(Entity entity:entities){
                renderer.processEntity(entity);
            }            
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }
        
        renderer.cleanUp(); // очищаем визуализацию
        loader.cleanUp(); // очищаем память от загруженной модели
        DisplayManager.closeDisplay();
    }
}
