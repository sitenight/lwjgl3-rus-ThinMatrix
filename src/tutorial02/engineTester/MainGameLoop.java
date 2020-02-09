package tutorial02.engineTester;

import tutorial02.renderEngine.DisplayManager;
import tutorial02.renderEngine.Loader;
import tutorial02.renderEngine.RawModel;
import tutorial02.renderEngine.Renderer;

/**
 * Основной цикл игры
 */
public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        
        Loader loader = new Loader(); // загрузчик моделей
        Renderer renderer = new Renderer(); // визуализатор моделей
        
        // OpenGL ожидает, что вершины будут определены против часовой стрелки по умолчанию
        float[] vertices = {
            // Левый нижний треугольник
            -0.5f,  0.5f, 0f,
            -0.5f, -0.5f, 0f,
             0.5f, -0.5f, 0f,
            // Правый верхний треугольник
             0.5f, -0.5f, 0f,
             0.5f,  0.5f, 0f,
            -0.5f,  0.5f, 0f,
        };
        
        // загружаем массив вершин в модель
        RawModel model = loader.loadToVao(vertices);
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) {            
            renderer.prepare(); // подготовка окна для рисования кадра
            
            // игровая логика
            renderer.render(model); // рисуем модель
            
            DisplayManager.updateDisplay();
        }
        
        loader.cleanUp(); // очищаем память от загруженной модели
        DisplayManager.closeDisplay();
    }
}
