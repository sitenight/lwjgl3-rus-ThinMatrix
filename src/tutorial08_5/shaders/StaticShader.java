package tutorial08_5.shaders;

import org.joml.Matrix4f;
import tutorial08_5.entities.Camera;
import tutorial08_5.toolbox.Maths;

/**
 * Шейдер для создания всех статических моделей
 */
public class StaticShader extends ShaderProgram {

    // указываем пути в константы для загрузки шейдеров
    private static final String VERTEX_FILE = "src/tutorial08_5/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/tutorial08_5/shaders/fragmentShader.txt";
    
    // идентификатор юниформы матрицы трансформации
    private int location_transformationMatrix;
    // идентификатор юниформы матрицы проекции
    private int location_projectionMatrix;
    // идентификатор юниформы матрицы вида
    private int location_viewMatrix;
    
    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        // связываем списки атрибутов VAO с шейдером 
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocations() {
        // регестрируем юниформы
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }
    
    /**
     * Загрузка данных, матрицы трансформации, в юниформу
     * @param matrix матрица преобразования
     */
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }
    
    /**
     * Загрузка данных, матрицы вида, в юниформу
     * @param camera камера
     */
    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }
    
    /**
     * Загрузка данных, матрицы проекции, в юниформу
     * @param matrix матрица проекции
     */
    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }
}

