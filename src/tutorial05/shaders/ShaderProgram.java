package tutorial05.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * Общая шейдерная программа содержащая все атрибуты и методы, которые
 * каждый шейдер будет иметь
 */
public abstract class ShaderProgram {

    /** идентификатор программы */
    private int programId;
    /** идентификатор вершинного шейдера */
    private int vertexShaderId;
    /** идентификатор фрагментного шейдера */
    private int fragmentShaderId;
    
    /**
     * Метод загрузки шейдерной программы
     * @param file путь к файлу
     * @param type тип шейдера
     * @return 
     */
    private static int loadShader(String file, int type) {
        String shaderSource = null;
        try (InputStream in = Class.forName(ShaderProgram.class.getName()).getResourceAsStream(file);
                Scanner scanner = new Scanner(in, "UTF-8")) {
            shaderSource = scanner.useDelimiter("\\A").next();
        } catch (Exception ex) { 
            ex.printStackTrace();
        }
        
        int shaderId = GL20.glCreateShader(type);
        if (shaderId == 0) 
            throw new RuntimeException("Ошибка создания шейдера. Тип: " + type);

        GL20.glShaderSource(shaderId, shaderSource);
        GL20.glCompileShader(shaderId);

        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) 
            throw new RuntimeException("Ошибка компиляции шейдерного кода: " 
                    + GL20.glGetShaderInfoLog(shaderId, 1024));
        
        return shaderId;
    }
}
