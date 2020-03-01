package tutorial18.entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import tutorial18.io.Keyboard;

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

    public Camera() {
        this.position = new Vector3f(100, 35, 40);
        this.pitch = 10;
        this.yaw = 0;
    }
    
    /**
     * Смещение камеры
     */
    public void move() {
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

