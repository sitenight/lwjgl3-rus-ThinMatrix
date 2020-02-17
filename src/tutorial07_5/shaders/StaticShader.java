package tutorial07_5.shaders;

import org.joml.Matrix4f;

/**
 * Шейдер для создания всех статических моделей
 */
public class StaticShader extends ShaderProgram {

    // указываем пути в константы для загрузки шейдеров
    private static final String VERTEX_FILE = "src/tutorial07_5/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/tutorial07_5/shaders/fragmentShader.txt";
    
    // идентификатор юниформы матрицы трансформации
    private int location_transformationMatrix;
    
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
        // регестрируем юниформу
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }
    
    /**
     * Загрузка данных, матрицы трансформации, в юниформу
     * @param matrix матрица
     */
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }
}

