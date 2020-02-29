package tutorial16.engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.joml.Vector3f;
import tutorial16.entities.Camera;
import tutorial16.entities.Entity;
import tutorial16.entities.Light;
import tutorial16.renderEngine.DisplayManager;
import tutorial16.renderEngine.Loader;
import tutorial16.models.RawModel;
import tutorial16.models.TexturedModel;
import tutorial16.renderEngine.MasterRenderer;
import tutorial16.renderEngine.OBJLoader;
import tutorial16.terrains.Terrain;
import tutorial16.textures.ModelTexture;

/**
 * Основной цикл игры
 */
public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        
        Loader loader = new Loader(); // загрузчик моделей
       
        // Загрузка текстурных моделей
        // дерево
        TexturedModel staticModel = new TexturedModel(OBJLoader.loadObjModel("res/tutorial15/tree.obj", loader), 
                new ModelTexture(loader.loadTexture("res/tutorial15/tree.png")));
        // трава
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("res/tutorial15/grassModel.obj", loader), new ModelTexture(
                loader.loadTexture("res/tutorial15/grassTexture.png")));
        grass.getTexture().setHasTransparency(true); // включаем прозрачность текстур
        grass.getTexture().setUseFakeLighting(true); // включаем фальшивое освещение
        // папоротник
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("res/tutorial15/fern.obj", loader), new ModelTexture(
                loader.loadTexture("res/tutorial15/fern.png")));
        fern.getTexture().setHasTransparency(true); // включаем прозрачность текстур
        
        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<500;i++){
            entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,
                    random.nextFloat() * -600),0,0,0,3));
            entities.add(new Entity(grass, new Vector3f(random.nextFloat()*800 - 400,0,
                    random.nextFloat() * -600),0,0,0,1));
            entities.add(new Entity(fern, new Vector3f(random.nextFloat()*800 - 400,0,
                    random.nextFloat() * -600),0,0,0,0.6f));
        }
        // создание источника света
        Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));
        
        Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("res/tutorial14/grass.png")));
        Terrain terrain2 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("res/tutorial14/grass.png")));
        
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
