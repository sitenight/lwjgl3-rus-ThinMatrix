package tutorial18.entities;

import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import tutorial18.io.Keyboard;
import tutorial18.models.TexturedModel;
import tutorial18.renderEngine.DisplayManager;

/**
 * Класс игрока
 */
public class Player extends Entity {

    /** скорость передвижения */
    private static final float RUN_SPEED = 20;
    /** градусы в секунду */
    private static final float TURN_SPEED = 160;   
    /** гравитация, сила притяжения */
    private static final float GRAVITY = -50;
    /** сила прыжка */
    private static final float JUMP_POWER = 30;

    private Vector3f position;
    /** текущая скорость */
    private float currentSpeed;
    private float currentTurnSpeed;
    private float upwardsSpeed;
    
    public Player(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale) {
        super(model, position, rotationX, rotationY, rotationZ, scale);
    }

    /**
     * Движение игрока
     */
    public void move() {
        checkInputs(); // обработка нажатия клавиш

        // расчет движения
        // рассчитываем поворот игрока
        super.increaseRotation(0, currentTurnSpeed * (float) DisplayManager.getDeltaInSeconds(), 0);
        // рассчитываем дистанцию в зависимости скорости к пройденому времени
        float distance = currentSpeed * (float) DisplayManager.getDeltaInSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotationY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotationY())));
        super.increasePosition(dx, 0, dz);
        /*
        // Расчет прыжка
        upwardsSpeed += GRAVITY * DisplayManager.getDeltaInSeconds();
        super.increasePosition(0, (float) (upwardsSpeed * DisplayManager.getDeltaInSeconds()), 0);
        */
    }
    
    /**
     * Прыжок игрока
     */
    private void jump() {
        if (upwardsSpeed == 0) {
            upwardsSpeed = JUMP_POWER;
        }
    }
    
    /**
     * Проверка и обработка нажатия клавиш
     */
    private void checkInputs() {
        // Движение вперед и назад
        if (Keyboard.isKeyDown(GLFW_KEY_W) || Keyboard.isKeyDown(GLFW_KEY_UP)) {
            currentSpeed = RUN_SPEED;
        } else if (Keyboard.isKeyDown(GLFW_KEY_S) || Keyboard.isKeyDown(GLFW_KEY_DOWN)) {
            currentSpeed = -RUN_SPEED;
        } else {
            currentSpeed = 0;
        }

        // движение влево и вправо
        if (Keyboard.isKeyDown(GLFW_KEY_D) || Keyboard.isKeyDown(GLFW_KEY_RIGHT)) {
            currentTurnSpeed = -TURN_SPEED;
        } else if (Keyboard.isKeyDown(GLFW_KEY_A) || Keyboard.isKeyDown(GLFW_KEY_LEFT)) {
            currentTurnSpeed = TURN_SPEED;
        } else {
            currentTurnSpeed = 0;
        }

        // прыжок
        if (Keyboard.isKeyDown(GLFW_KEY_SPACE)) {
            jump();
        }
    }
}
