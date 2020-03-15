package tutorial22.engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.joml.Vector3f;
import tutorial22.entities.Camera;
import tutorial22.entities.Entity;
import tutorial22.entities.Light;
import tutorial22.entities.Player;
import tutorial22.renderEngine.DisplayManager;
import tutorial22.renderEngine.Loader;
import tutorial22.models.RawModel;
import tutorial22.models.TexturedModel;
import tutorial22.objConverter.ModelData;
import tutorial22.objConverter.OBJFileLoader;
import tutorial22.renderEngine.MasterRenderer;
import tutorial22.renderEngine.OBJLoader;
import tutorial22.terrains.Terrain;
import tutorial22.textures.ModelTexture;
import tutorial22.textures.TerrainTexture;
import tutorial22.textures.TerrainTexturePack;

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
        
        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap,"res/tutorial21/heightmap.png");
        
        List<Entity> entities = new ArrayList<>();
        Random random = new Random(676452);
        
        float x, y, z;
        for(int i=0;i<400;i++){            
            if(i % 7 == 0) {
                x = random.nextFloat() * 400 - 200;
                z = random.nextFloat() * -400;
                y = terrain.getHeightsOfTerrain(x, z);
                entities.add(new Entity(grass, new Vector3f(x, y, z),0,0,0,1.8f));
                
                x = random.nextFloat() * 400 - 200;
                z = random.nextFloat() * -400;
                y = terrain.getHeightsOfTerrain(x, z);
                entities.add(new Entity(flower, new Vector3f(x, y, z),0,0,0,2.3f));
            }
            if(i % 3 == 0) {                
                x = random.nextFloat() * 400 - 200;
                z = random.nextFloat() * -400;
                y = terrain.getHeightsOfTerrain(x, z);
                entities.add(new Entity(fern, new Vector3f(x, y, z),0,random.nextFloat() * 360,0,0.9f));
                
                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightsOfTerrain(x, z);
                entities.add(new Entity(bobble, new Vector3f(x, y, z),0,random.nextFloat() * 360,0,
                        random.nextFloat() * 0.1f + 0.6f));
                
                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightsOfTerrain(x, z);
                entities.add(new Entity(staticModel, new Vector3f(x, y, z),0,0,0,random.nextFloat() * 1f + 4));
            }
        }
        // создание источника света
        Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));
                
        MasterRenderer renderer = new MasterRenderer();
        
        // Игрок
        ModelData bunny = OBJFileLoader.loadOBJ("res/tutorial18/stanfordBunny.obj");
        RawModel bunnyModel = loader.loadToVao(bunny.getVertices(), bunny.getTextureCoords(), 
                bunny.getNormals(), bunny.getIndices());
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, 
                new ModelTexture(loader.loadTexture("res/tutorial18/white.png")));
        Player player = new Player(stanfordBunny, new Vector3f(130, 0, -100), 0, 180, 0, 1);
        
        Camera camera = new Camera(player);        
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) { 
            renderer.renderOptions(); // обработка разной визуализации
                    
            player.move(terrain); // двигаем игрока
            camera.move(); // двигаем камеру     
            renderer.processEntity(player); // рисуем игрока
            // рисуем объекты
            renderer.processTerrain(terrain);
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
