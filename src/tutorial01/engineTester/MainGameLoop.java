package tutorial01.engineTester;

import tutorial01.renderEngine.DisplayManager;

/**
 * Основной цикл игры
 */
public class MainGameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        
        // запускаем цикл пока пользователь не закроет окно
        while (DisplayManager.shouldDisplayClose()) {            
            
            // игровая логика
            // рендер/визуализация
            
            DisplayManager.updateDisplay();
        }
        
        DisplayManager.closeDisplay();
    }
}
