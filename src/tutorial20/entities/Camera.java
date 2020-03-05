package tutorial20.entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import tutorial20.io.Mouse;

/**
 * Класс симуляции камеры
 */
public class Camera {

    /** позиция камеры */
    private Vector3f position;
    /** тангаж - поворот по оси X(Поперечная ось) */
    private float pitch;
    /** рыскание - поворот по оси Y(Вертикальная ось, лежащая в плоскости) */
    private float yaw;
    /** крен - поворот по оси Z(Продольная ось) */
    private float roll;
    
    /** игрок */
    private Player player;
    
    /** расстояние камеры до игрока, зум */
    private float distanceFromPlayer;
    /** угол для поворота вокруг игрока */
    private float angleAroundPlayer;
    
    private boolean mouseLeft = false, mouseRight = false;
    private double mouseLastX, mouseLastY;
    private float scrollLast;
    
    /** начальное смещение камеры от игрока */
    private final static float DEFAULT_OFFSET_DISTANCE = 50;
    /** смещение камеры по высоте в центр модели, а не в ноги */
    private final static float OFFSET_HEIGHT = 3;
    /** минимальное отдаление камеры */
    private final static float MIN_OFFSET_DISTANCE = 12;
    

    public Camera(Player player) {
        this.player = player; // привязываем к игроку
        this.distanceFromPlayer = DEFAULT_OFFSET_DISTANCE; // дистанция до игрока
        this.angleAroundPlayer = 0; // угол поворота вокруг игрока
        this.position = new Vector3f(100, 35, 40); // позиция
        this.pitch = 30; // наклон камеры(верх-низ)
        this.yaw = 0; // поворот камеры вокруг
        this.roll = 0; 
    }
    
    /**
     * Смещение камеры
     */
    public void move() {
        calculateZoom(); // расчет зума камеры
        calculatePitch(); // расчет высоты камеры
        calculateAngleAroundPlayer(); // расчет положение камеры вокруг игрока
        
        // расчет положения камеры
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance() + OFFSET_HEIGHT;
        calculateCameraPosition(horizontalDistance, verticalDistance); 
        
        this.yaw = 180 - (player.getRotationY() + angleAroundPlayer); // поворот камеры вокруг
    }

    /**
     * Расчет зума камеры
     * Получает данные поворота колесика и вычисляет новое расстояние до игрока
     */
    private void calculateZoom() {
        float scrollNew = (float) Mouse.getMouseScrollY();
        
        if(scrollNew == scrollLast)
            return;
        
        float zoomLevel = scrollNew - scrollLast;
        distanceFromPlayer += zoomLevel;
        
        scrollLast = scrollNew;
        
        if(distanceFromPlayer < MIN_OFFSET_DISTANCE)
            distanceFromPlayer = MIN_OFFSET_DISTANCE;
    }
    
    /**
     * Расчет высоты камеры
     * При нажатии ПКМ получает изменение движения мыши по оси Y и 
     * меняет угол камеры вверх-вниз относительно игрока
     */
    private void calculatePitch() {
        boolean button = Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
        if (button) { 
            if(!mouseRight) {
                mouseLastY = Mouse.getMouseY();
                mouseRight = true;
            }
            float pitchChange = (float) (mouseLastY - Mouse.getMouseY()) * 0.1f;
            pitch -= pitchChange;
            
            mouseLastY = Mouse.getMouseY();
        } else if(!button) 
            mouseRight = false;
    }

    /**
     * Расчет положение камеры вокруг игрока
     * При нажатии ЛКМ получает изменение движения мыши по оси Х и 
     * меняет положение камеры вокруг игрока
     */
    private void calculateAngleAroundPlayer() {
        boolean button = Mouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
        if (button) { 
            if(!mouseLeft) {
                mouseLastX = Mouse.getMouseX();
                mouseLeft = true;
            }
            float angleChange = (float) -(mouseLastX - Mouse.getMouseX()) * 0.3f;
            angleAroundPlayer -= angleChange;
            
            mouseLastX = Mouse.getMouseX();
        } else if(!button) 
            mouseLeft = false;
    }
    
    /**
     * Расчет отдаленности камеры от игрока
     * @return расстояние камеры до игрока
     */
    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    /**
     * Расчет высоты камеры относительно игрока
     * @return высота установки камеры
     */
    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }
    
    /**
     * Расчет положения камеры
     * Фактическое положение камеры по всем осям.
     * @param horizDistance раcстояние горизонтальное до камеры
     * @param verticDistance высота камеры
     */
    private void calculateCameraPosition(float horizDistance, float verticDistance) {
        // угол поворота камеры
        float alpha = player.getRotationY() + angleAroundPlayer;
        // смещение камеры по осям X и Z
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(alpha)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(alpha)));
        // обновляем положение камеры
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticDistance;
    }
    
    public Vector3f getPosition() {
        return position;
    }
    
    /** тангаж - поворот по оси X(Поперечная ось) */
    public float getPitch() {
        return pitch;
    }

    /** рыскание - поворот по оси Y(Вертикальная ось, лежащая в плоскости) */
    public float getYaw() {
        return yaw;
    }

    /** крен - поворот по оси Z(Продольная ось) */
    public float getRoll() {
        return roll;
    }
}