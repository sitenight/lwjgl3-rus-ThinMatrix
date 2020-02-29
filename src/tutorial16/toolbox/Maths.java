package tutorial16.toolbox;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import tutorial16.entities.Camera;

/**
 * Математический класс
 */
public class Maths {

    /**
     * Создание матрицы трансформации
     * @param translation вектор смещения
     * @param rx поворот по оси X
     * @param ry поворот по оси Y
     * @param rz поворот по оси Z
     * @param scale масштаб
     * @return матрицу трансформации
     */
    public static Matrix4f getTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        // смещение
        matrix.translate(translation.x, translation.y, translation.z);
        // поворот
        matrix.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0));
        matrix.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0));
        matrix.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1));
        // масштаб
        matrix.scale(new Vector3f(scale, scale, scale));
        return matrix;
    }
    
    /**
     * Создание матрицы виды для камеры
     * @param camera камера
     * @return матрицу вида
     */
    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        // поворачиваем относительно камеры
        viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0,0, 1));
        // смещаем относительно камеры
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        viewMatrix.translate(negativeCameraPos);

        return viewMatrix;
    }
}

