package tutorial19.engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.joml.Vector3f;
import tutorial19.entities.Camera;
import tutorial19.entities.Entity;
import tutorial19.entities.Light;
import tutorial19.entities.Player;
import tutorial19.renderEngine.DisplayManager;
import tutorial19.renderEngine.Loader;
import tutorial19.models.RawModel;
import tutorial19.models.TexturedModel;
import tutorial19.objConverter.ModelData;
import tutorial19.objConverter.OBJFileLoader;
import tutorial19.renderEngine.MasterRenderer;
import tutorial19.renderEngine.OBJLoader;
import tutorial19.terrains.Terrain;
import tutorial19.textures.ModelTexture;
import tutorial19.textures.TerrainTexture;
import tutorial19.textures.TerrainTexturePack;

/**
 * Основной цикл игры
 */
public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        
        Loader loader = new Loader(); // загрузчик моделей
        
        // загрузка текстур ландшафта
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("res/tutorial17/grassy2.png"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("res/tutorial17/mud.png"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("res/tutorial17/grassFlowers.png"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("res/tutorial17/path.png"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        // загрузка карты смешивания текстур ландшафта
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("res/tutorial17/blendMap.png"));
        
        
        ModelData data = OBJFileLoader.loadOBJ("res/tutorial15/tree.obj");
        RawModel treeModel = loader.loadToVao(data.getVertices(), data.getTextureCoords(), 
                data.getNormals(), data.getIndices());
       
        // Загрузка текстурных моделей
        // дерево
        TexturedModel staticModel = new TexturedModel(treeModel, 
                new ModelTexture(loader.loadTexture("res/tutorial15/tree.png")));
        // трава
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("res/tutorial15/grassModel.obj", loader), new ModelTexture(
                loader.loadTexture("res/tutorial15/grassTexture.png")));
        grass.getTexture().setHasTransparency(true); // включаем прозрачность текстур
        grass.getTexture().setUseFakeLighting(true); // включаем фальшивое освещение
         // цветы
        TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("res/tutorial15/grassModel.obj", loader), new ModelTexture(
                loader.loadTexture("res/tutorial17/flower.png")));
        flower.getTexture().setHasTransparency(true); // включаем прозрачность текстур
        flower.getTexture().setUseFakeLighting(true); // включаем фальшивое освещение
        // папоротник
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("res/tutorial15/fern.obj", loader), new ModelTexture(
                loader.loadTexture("res/tutorial15/fern.png")));
        fern.getTexture().setHasTransparency(true); // включаем прозрачность текстур
        // дерево
        TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("res/tutorial17/lowPolyTree.obj", loader), new ModelTexture(
                loader.loadTexture("res/tutorial17/lowPolyTree.png")));
        
        List<Entity> entities = new ArrayList<>();
        Random random = new Random(676452);
        for(int i=0;i<400;i++){
            if(i % 7 == 0) {
                entities.add(new Entity(grass, new Vector3f(random.nextFloat()*400 - 200,0,
                    random.nextFloat() * -400),0,0,0,1.8f));
                entities.add(new Entity(flower, new Vector3f(random.nextFloat()*400 - 200,0,
                    random.nextFloat() * -400),0,0,0,2.3f));
            }
            if(i % 3 == 0) {
                entities.add(new Entity(fern, new Vector3f(random.nextFloat()*400 - 200,0,
                        random.nextFloat() * -400),0,random.nextFloat() * 360,0,0.9f));
                entities.add(new Entity(bobble, new Vector3f(random.nextFloat()*800 - 400,0,
                        random.nextFloat() * -600),0,random.nextFloat() * 360,0,
                        random.nextFloat() * 0.1f + 0.6f));
                entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,
                        random.nextFloat() * -600),0,0,0,random.nextFloat() * 1f + 4));
            }
        }
        // создание источника света
        Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));
        
        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
        Terrain terrain2 = new Terrain(-1, -1, loader,texturePack, blendMap);
        
        Camera camera = new Camera();        
        MasterRenderer renderer = new MasterRenderer();
        
        // Игрок
        ModelData bunny = OBJFileLoader.loadOBJ("res/tutorial18/stanfordBunny.obj");
        RawModel bunnyModel = loader.loadToVao(bunny.getVertices(), bunny.getTextureCoords(), 
                bunny.getNormals(), bunny.getIndices());
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, 
                new ModelTexture(loader.loadTexture("res/tutorial18/white.png")));
        Player player = new Player(stanfordBunny, new Vector3f(100, 0, -50), 0, 0, 0, 1);
        
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) {  
            camera.move(); // двигаем камеру     
            player.move(); // двигаем игрока
            renderer.processEntity(player); // рисуем игрока
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
