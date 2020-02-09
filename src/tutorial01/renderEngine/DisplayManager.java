package tutorial01.renderEngine;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;

/**
 * Управление дисплеем
 */
public class DisplayManager {
    
    /** ссылка на окно */
    private static long window;
    /** ширина окна */
    private static final int WINDOW_WIDTH = 800;
    /** высота окна */
    private static final int WINDOW_HEIGHT = 600;
    /** заголовок окна*/
    private static final String TITLE = "Game";

    /**
     * Создание окна, объявляем до начала самой игры
     */
    public static void createDisplay() {
        // Инициализация GLFW. Большинство функций GLFW не будут работать перед этим
        if (!glfwInit())
            throw new RuntimeException("Не могу инициализировать GLFW");

        // Настройка GLFW
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // окно будет скрыто после создания
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // окно будет изменяемого размера
        // Задается минимальная требуемая версия OpenGL. 
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4); // Мажорная
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2); // Минорная
        // Установка профайла для которого создается контекст
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // создание окна
        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, TITLE, 0, 0);
        if (window == 0) 
            throw new RuntimeException("Ошибка создания GLFW окна");

        // Получаем разрешение основного монитора/экрана 
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidMode != null;
        // устанавливаем окно в центр экрана
        glfwSetWindowPos(window, (vidMode.width() - WINDOW_WIDTH) / 2, (vidMode.height() - WINDOW_HEIGHT) / 2);

        // Делаем контекст OpenGL текущим
        glfwMakeContextCurrent(window);
        // устанавливаем значение 1 чтобы ограничивать до 60 FPS
        glfwSwapInterval(1);
        // показываем окно
        glfwShowWindow(window);
        
        // Эта строка критически важна для взаимодействия LWJGL с контекстом GLGW OpenGL 
        // или любым контекстом, который управляется извне. LWJGL обнаруживает 
        // текущий контекст в текущем потоке, создает экземпляр GLCapabilities 
        // и делает привязки OpenGL доступными для использования.
        GL.createCapabilities();        
    }
    
    /**
     * Для обновления окна каждый кадр
     */
    public static void updateDisplay() {
        glfwSwapBuffers(window); // поменяем цветовые буферы
        // Gроверяет были ли вызваны какие либо события (вроде ввода с клавиатуры или перемещение мыши)
        glfwPollEvents(); 
    }
    
    /**
     * Закрытие окна по завершении игры.
     * Освободить обратные вызовы окна и уничтожить окно
     */
    public static void closeDisplay() {
        glfwWindowShouldClose(window); // Освобождаем обратные вызовы окна
        glfwDestroyWindow(window); // Уничтожаем окно
        glfwTerminate(); // Завершаем GLFW. Очистка выделенных нам ресурсов
    }
    
    /**
     * Проверяет в начале каждой итерации цикла, получил ли GLFW инструкцию к закрытию, 
     * @return если закрыто окно, то функция вернет false
     */
    public static boolean shouldDisplayClose() {
        return !glfwWindowShouldClose(window);
    }
}
