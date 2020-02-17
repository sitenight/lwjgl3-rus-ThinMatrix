 package tutorial08_5.engineTester;

import org.joml.Vector3f;
import tutorial08_5.entities.Camera;
import tutorial08_5.entities.Entity;
import tutorial08_5.renderEngine.DisplayManager;
import tutorial08_5.renderEngine.Loader;
import tutorial08_5.models.RawModel;
import tutorial08_5.models.TexturedModel;
import tutorial08_5.renderEngine.Renderer;
import tutorial08_5.shaders.StaticShader;
import tutorial08_5.textures.ModelTexture;

/**
 * Основной цикл игры
 */
public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        
        Loader loader = new Loader(); // загрузчик моделей
        StaticShader shader = new StaticShader(); // шейдер статических моделей
        Renderer renderer = new Renderer(shader); // визуализатор моделей
        
        float[] vertices = {            
            -0.5f,0.5f,0,   
            -0.5f,-0.5f,0,  
            0.5f,-0.5f,0,   
            0.5f,0.5f,0,        

            -0.5f,0.5f,1,   
            -0.5f,-0.5f,1,  
            0.5f,-0.5f,1,   
            0.5f,0.5f,1,

            0.5f,0.5f,0,    
            0.5f,-0.5f,0,   
            0.5f,-0.5f,1,   
            0.5f,0.5f,1,

            -0.5f,0.5f,0,   
            -0.5f,-0.5f,0,  
            -0.5f,-0.5f,1,  
            -0.5f,0.5f,1,

            -0.5f,0.5f,1,
            -0.5f,0.5f,0,
            0.5f,0.5f,0,
            0.5f,0.5f,1,

            -0.5f,-0.5f,1,
            -0.5f,-0.5f,0,
            0.5f,-0.5f,0,
            0.5f,-0.5f,1                 
        };
        
        int[] indices = {
            0,1,3,  
            3,1,2,  
            4,5,7,
            7,5,6,
            8,9,11,
            11,9,10,
            12,13,15,
            15,13,14,   
            16,17,19,
            19,17,18,
            20,21,23,
            23,21,22
        };
        
        float[] textureCoords = {
            0,0,
            0,1,
            1,1,
            1,0,            
            0,0,
            0,1,
            1,1,
            1,0,            
            0,0,
            0,1,
            1,1,
            1,0,
            0,0,
            0,1,
            1,1,
            1,0,
            0,0,
            0,1,
            1,1,
            1,0,
            0,0,
            0,1,
            1,1,
            1,0
        };
        
        // загружаем массив вершин, текстурных координат и индексов в память GPU
        RawModel model = loader.loadToVao(vertices, textureCoords, indices);
        // загрузим текстуру используя загрузчик
        ModelTexture texture = new ModelTexture(loader.loadTexture("res/tutorial06/image.png"));
        // Создание текстурной модели
        TexturedModel staticModel = new TexturedModel(model, texture);
        
        Entity entity = new Entity(staticModel, 
                new Vector3f(0, 0, -5f), 
                0, 0, 0, 
                1.0f);
        
        Camera camera = new Camera();
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) {  
            entity.increaseRotation(1, 1, 0);
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
