package tutorial06.shaders;

/**
 * Шейдер для создания всех статических моделей
 */
public class StaticShader extends ShaderProgram {

    // указываем пути в константы для загрузки шейдеров
    private static final String VERTEX_FILE = "src/tutorial06/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/tutorial06/shaders/fragmentShader.txt";
    
    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        // связываем списки атрибутов VAO с шейдером 
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }
}

