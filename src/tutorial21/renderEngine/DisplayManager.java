package tutorial21.renderEngine;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import tutorial21.io.Keyboard;
import tutorial21.io.Mouse;

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
    
    private static int frames; // количество кадров
    private static long time; // время
    private static boolean showFPSTitle; // показывать ли FPS в заголовке окна
    private static double lastFrameTime; // прошло времени между кадрами
    private static double deltaInSeconds; // разница времени в секундах
    
    private static Keyboard keyboard;
    private static Mouse mouse;

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

        keyboard = new Keyboard();
        mouse = new Mouse();
        
        // регистрируем обратный вызов ввода с клавиатуры и мыши
        glfwSetKeyCallback(window, keyboard);
        glfwSetCursorPosCallback(window, mouse.getMouseMoveCallback());
        glfwSetMouseButtonCallback(window, mouse.getMouseButtonsCallback());
        glfwSetScrollCallback(window, mouse.getMouseScrollCallback());
        
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

        lastFrameTime = getCurrentTime();
    }
    
    /**
     * Для обновления окна каждый кадр
     */
    public static void updateDisplay() {
        glfwSwapBuffers(window); // поменяем цветовые буферы
        // Gроверяет были ли вызваны какие либо события (вроде ввода с клавиатуры или перемещение мыши)
        glfwPollEvents(); 
        
        if (showFPSTitle) {
            frames++;

            if (System.currentTimeMillis() > time + 1000) {
                glfwSetWindowTitle(window, TITLE + " | FPS: " + frames);
                time = System.currentTimeMillis();
                frames = 0;
            }
        }

        double currentFrameTime = getCurrentTime(); // текущее время
        // затраченое время на 1 кадр (в секунду)
        deltaInSeconds = (currentFrameTime - lastFrameTime) / 1000; 
        lastFrameTime = currentFrameTime; // время окончания рендеринга кадра
    }
    
    /**
     * Закрытие окна по завершении игры.
     * Освободить обратные вызовы окна и уничтожить окно
     */
    public static void closeDisplay() {
        mouse.destroy();
        keyboard.close();
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
    
    public static int getWindowWidth() {
        return WINDOW_WIDTH;
    }

    public static int getWindowHeight() {
        return WINDOW_HEIGHT;
    }
    
    public static void setShowFPSTitle(boolean showFPSTitle) {
        DisplayManager.showFPSTitle = showFPSTitle;

        if (!showFPSTitle) {
            frames = 0;
            time = 0;
        }
    }
    
    /**
     * Получение затраченого времени на 1 кадр
     * @return затраченое время на 1 кадр (в секунду)
     */
    public static double getDeltaInSeconds() {
        return deltaInSeconds;
    }

    /**
     * Получение текущего времени
     * @return текущее время в секундах
     */
    private static double getCurrentTime() {
        return glfwGetTime() * 1000;
    }
}
