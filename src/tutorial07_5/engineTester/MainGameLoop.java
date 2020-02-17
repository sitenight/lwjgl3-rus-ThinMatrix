 package tutorial07_5.engineTester;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import tutorial07_5.entities.Entity;
import tutorial07_5.io.Keyboard;
import tutorial07_5.io.Mouse;
import tutorial07_5.renderEngine.DisplayManager;
import tutorial07_5.renderEngine.Loader;
import tutorial07_5.models.RawModel;
import tutorial07_5.models.TexturedModel;
import tutorial07_5.renderEngine.Renderer;
import tutorial07_5.shaders.StaticShader;
import tutorial07_5.textures.ModelTexture;

/**
 * Основной цикл игры
 */
public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        
        Loader loader = new Loader(); // загрузчик моделей
        Renderer renderer = new Renderer(); // визуализатор моделей
        StaticShader shader = new StaticShader(); // шейдер статических моделей
        
        float[] vertices = {
            -0.5f,  0.5f, 0f, // V0
            -0.5f, -0.5f, 0f, // V1
             0.5f, -0.5f, 0f, // V2
             0.5f,  0.5f, 0f, // V3
        };
        
        int[] indices = {
            0, 1, 3, // Верхний левый треугольник 
            3, 1, 2, // Нижний правый треугольник
        };
        
        float[] textureCoords = {
            0.0f, 0.0f, // V0
            0.0f, 1.0f, // V1
            1.0f, 1.0f, // V2
            1.0f, 0.0f, // V3
        };
        
        // загружаем массив вершин, текстурных координат и индексов в память GPU
        RawModel model = loader.loadToVao(vertices, textureCoords, indices);
        // загрузим текстуру используя загрузчик
        ModelTexture texture = new ModelTexture(loader.loadTexture("res/tutorial06/image.png"));
        // Создание текстурной модели
        TexturedModel staticModel = new TexturedModel(model, texture);
        
        Entity entity = new Entity(staticModel, 
                new Vector3f(-0.4f, 0, 0), 
                0, 0, 30, 
                1.0f);
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) {   
            if(Keyboard.isKeyDown(GLFW.GLFW_KEY_A)) 
                entity.increacePosition(-0.1f, 0, 0);
            else if(Keyboard.isKeyDown(GLFW.GLFW_KEY_D)) 
                entity.increacePosition(0.1f, 0, 0);
            if (Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) 
                entity.increaseRotation(0, 0, 1);
            else if (Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) 
                entity.increaseRotation(0, 0, -1);
            
            renderer.prepare(); // подготовка окна для рисования кадра
            
            shader.start(); // запускаем шейдер статических моделей
            renderer.render(entity, shader); // рисуем объект
            shader.stop(); // останавливаем шейдер статических моделей
            
            DisplayManager.updateDisplay();
        }
        
        shader.cleanUp(); // очищаем шейдер статических моделей
        loader.cleanUp(); // очищаем память от загруженной модели
        DisplayManager.closeDisplay();
    }
}
