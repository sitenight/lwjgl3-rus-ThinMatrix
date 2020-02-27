package tutorial14.entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import tutorial14.io.Keyboard;

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
        this.position = new Vector3f(0, 5, 0);
    }
    
    /**
     * Смещение камеры
     */
    public void move() {
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
            position.z -= 0.2f;
        } else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
            position.z += 0.2f;
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
            position.x += 0.2f;
        } else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
            position.x -= 0.2f;
        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_Q)) {
            position.y += 0.2f;
        } else if (Keyboard.isKeyDown(GLFW.GLFW_KEY_E)) {
            position.y -= 0.2f;
        }
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

