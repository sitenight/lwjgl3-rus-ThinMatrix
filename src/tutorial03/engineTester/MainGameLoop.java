package tutorial03.engineTester;

import tutorial03.renderEngine.DisplayManager;
import tutorial03.renderEngine.Loader;
import tutorial03.renderEngine.RawModel;
import tutorial03.renderEngine.Renderer;

/**
 * Основной цикл игры
 */
public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        
        Loader loader = new Loader(); // загрузчик моделей
        Renderer renderer = new Renderer(); // визуализатор моделей
        
        float[] vertices = {
            -0.5f,  0.5f, 0f, // V0
            -0.5f, -0.5f, 0f, // V1
             0.5f,  0.5f, 0f, // V2
             0.5f, -0.5f, 0f, // V3
        };
        
        int[] indices = {
            0, 1, 3, // Верхний левый треугольник 
            3, 1, 2, // Нижний правый треугольник
        };
        
        // загружаем массив вершин и индексов в модель
        RawModel model = loader.loadToVao(vertices, indices);
        
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
