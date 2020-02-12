package tutorial07.textures;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

/**
 * Класс текстуры
 */
public class Texture {

    private final int id;
    
    public Texture(int id) {
        this.id = id;
    }  
    
    public Texture(String filename) throws Exception {
        this(loadTexture(filename));
    }
    
    private static int loadTexture(String fileName) throws Exception {
        int width, height;
        
        // Будет содержать декодированное изображение (так как каждый пиксель 
        // использует четыре байта, его размер будет 4 ширины по высоте)
        ByteBuffer buf; 
        
        // Загрузка файла текстуры
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
            
            buf = STBImage.stbi_load(fileName, w, h, channels, 4);
            if (buf == null)
                throw new Exception("Файл текстуры [" + fileName + "] не загружен: " + STBImage.stbi_failure_reason());
            
            // получение высоты и ширины картинки
            width = w.get();
            height = h.get();
        }
        //создание новой текстуры
        int textureId = GL11.glGenTextures();
        // присоединяем текстуру
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        
        // Говорит OpenGL, как распаковать байты RGBA. Каждый компонент имеет размер 1 байт.
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        
        // Этот параметр в основном говорит о том, что когда пиксель рисуется 
        // без прямой связи один к одному с координатой текстуры, он выбирает 
        // ближайшую точку текстуры.
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        
        // Выгрузка данных текстуры
        // [1] GL_TEXTURE_2D - Определяет целевую текстуру (ее тип)
        // [2] level - указывает номер уровня детализации. Уровень 0 - это базовый уровень изображения. Уровень n - это n-е уменьшенное изображение mipmap
        // [3] internal format - Определяет количество цветовых компонентов в текстуре.
        // [4] width - Определяет ширину текстурного изображения
        // [5] height - Определяет высоту текстурного изображения
        // [6] border - Это значение должно быть нулевым
        // [7] format - Определяет формат данных пикселей: в данном случае RGBA.
        // [8] type - Определяет тип данных данных пикселей. Для этого мы используем неподписанные байты.
        // [9] data - Буфер, в котором хранятся наши данные
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, 
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
        
        // Генерация Мип-карта
        // Мип-карта - это набор изображений с уменьшенным разрешением, 
        // созданный из высокодетализированной текстуры. Эти изображения с 
        // более низким разрешением будут использоваться автоматически при 
        // масштабировании нашего объекта.
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        
        // освобождаем буфер
        STBImage.stbi_image_free(buf);
        
        return textureId;
    }
    
    /**
     * Освобождение памяти GPU
     */
    public void cleanup() {
        GL11.glDeleteTextures(id);
    }

    public int getId() {
        return id;
    }
    
}

