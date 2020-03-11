package tutorial21.entities;

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
import tutorial21.io.Keyboard;
import tutorial21.models.TexturedModel;
import tutorial21.renderEngine.DisplayManager;

/**
 * Класс игрока
 */
public class Player extends Entity {

    /** скорость передвижения */
    private static final float RUN_SPEED = 20;
    /** градусы в секунду, скорость поворота */
    private static final float TURN_SPEED = 160;   
    /** гравитация, сила притяжения */
    private static final float GRAVITY = -50;
    /** сила прыжка, насколько высоко прыгает */
    private static final float JUMP_POWER = 30;
    
    /** Высота ландшафта */
    private static final float TERRAIN_HEIGHT = 0;

    /** текущая скорость, которая будет плавать */
    private float currentSpeed;
    /** текущая скорость поворота */
    private float currentTurnSpeed;
    /** скорость игрока вверх при прыжке, уменьшается каждую секунду */
    private float upwardsSpeed;
    
    /** игрок в воздухе, например когда прыгает*/
    private boolean isInAir;
    
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
        // по оси Х смещение
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotationY())));
        // по оси Z смещение
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotationY())));
        super.increasePosition(dx, 0, dz); // смещаем игрока
        
        // Расчет прыжка
        // уменьшение скорости прыжка каждую секунду
        upwardsSpeed += GRAVITY * DisplayManager.getDeltaInSeconds();
        // смещаем игрока по оси Y
        super.increasePosition(0, (float) (upwardsSpeed * DisplayManager.getDeltaInSeconds()), 0);
        // проверка позиции игрока, не ниже рельефа
        if(super.getPosition().y < TERRAIN_HEIGHT) {
            upwardsSpeed = 0; // сбрасываем скорость падения
            isInAir = false; // игрок приземлился
            super.getPosition().y = TERRAIN_HEIGHT; // устанавливаем игрока на землю 
        }
    }
    
    /**
     * Прыжок игрока когда стоит на земле
     */
    private void jump() {
        if (!isInAir) {
            upwardsSpeed = JUMP_POWER; // задаем начальную скорость прыжка
            isInAir = true; // игрок в воздухе
        }
    }
    
    /**
     * Проверка и обработка ввода из клавиатуры
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

        // поворот влево и вправо
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
