package tutorial06.renderEngine;

import tutorial06.models.RawModel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import tutorial06.textures.Texture;

/**
 * Загружает 3д модели в память, путем сохранения позиционных данных вершин модели в VAO
 */
public class Loader {
    
    /** Список VAO для отслеживания в памяти */
    private List<Integer> vaos = new ArrayList<>();
    /** Список VBO для отслеживания в памяти */
    private List<Integer> vbos = new ArrayList<>();
    /** Список текстур для отслеживания в памяти */
    private List<Integer> textures = new ArrayList<>();

    /**
     * Загрузка координат вершин в VAO
     * @param positions положение вершин модели
     * @param textureCoords текстурные координаты
     * @param indices индексы вершин
     * @return загруженную модель
     */
    public RawModel loadToVao(float[] positions, float[] textureCoords, int[] indices) {
        int vaoId = createVao(); // получаем новый идетификатор VAO
        bindIndicesBuffer(indices); // загружаем и привязываем наши индексы вершин
        // сохраняем список атрибутов VAO данные
        // VAO #0 имеет 3х мерные Векторы(xyz), позиции вершин
        storeDataInAttributeList(0, 3, positions); 
        // VAO #1 имеет 2х мерные Векторы(uv), текстурные координаты
        storeDataInAttributeList(1, 2, textureCoords); 
        unbindVao(); // отвязываем VAO
        // возвращаем загруженную модель: id и количество вершин
        return new RawModel(vaoId, indices.length);
    }
    
    /**
     * Загрузка текстуры
     * @param fileName имя файла текстуры
     * @return идентификатор текстуры
     */
    public int loadTexture(String fileName) {
        int textureId = 0;
        try {
            Texture texture = new Texture(fileName);
            textureId = texture.getId();
            textures.add(textureId);
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        return textureId;
    }
    
    /**
     * Создание пустого Объекта вершинного массива (VAO)
     * @return идентификатор созданного VAO
     */
    private int createVao() {
        int vaoId = GL30.glGenVertexArrays(); // инициализация пустого VAO
        vaos.add(vaoId); // добавляем в список очередной идентификатор VAO
        GL30.glBindVertexArray(vaoId); // связываем vao по id c OpenGL
        return vaoId;
    }
    
    /**
     * Хранение данных в одном списке атрибутов VAO
     * @param attributeNumber номер в списке атрибутов VAO
     * @param coordinateSize размер компонента (для вершины нужно 3(xyz), для тектурной координаты 2(uv))
     * @param data массив данных
     */
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboId = GL15.glGenBuffers(); // инициализация пустого VBO
        vbos.add(vboId); // добавляем в список очередной идентификатор VBO
        // Указываем OpenGL, что сейчас мы будем что-то делать вот с этим VBO
        // для этого используем точку связывания GL_ARRAY_BUFFER
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId); 
        // Создаем FloatBuffer наших данных
        FloatBuffer buffer = storeDataInFloatBuffer(data);  
        // Выделяем память GPU и загружаем в нее наши данные
        // STATIC говорит о том, что мы не собираемся модифицировать данные, 
        // а DRAW — о том, что данные будут использованы для отрисовки чего-то
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);  
        // Откуда брать данные для массива атрибутов, а также в каком формате эти данные находятся
        // Аргументы: 
        // - номер списка атрибутов VAO
        // - размер компонента (для вершины нужно 3(xyz), для тектурной координаты 2(uv))
        // - тип компонента
        // - остальные normalized, stride и pointer
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);   
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // отвязываем VBO
    }
    
    /**
     * Загрузка индексов вершин в память GPU
     * @param indices массив индексов вершин
     */
    public void bindIndicesBuffer(int[] indices) {
        int vboId = GL15.glGenBuffers(); // инициализация нового VBO
        vbos.add(vboId); // добавляем в список очередной идентификатор VBO
        // Указываем тип данных который будем загружать
        // в этот раз Массив Элементов - буфер индексов
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        // Создаем IntBuffer наших данных
        IntBuffer buffer = storeDataIntBuffer(indices);
         // Выделяем память GPU и загружаем в нее наши данные
        // STATIC говорит о том, что мы не собираемся модифицировать данные, 
        // а DRAW — о том, что данные будут использованы для отрисовки чего-то
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);  
    }

    /**
     * Отвязывание VAO, когда завершаем работу с ним
     */
    private void unbindVao() {
        GL30.glBindVertexArray(0);
    }
    
    /**
     * Преобразование массива float в FloatBuffer
     * @param data данные массива float
     * @return данные в FloatBuffer
     */
    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data).flip();
        return buffer;
    }
    
    /**
     * Преобразование массива int в IntBuffer
     * @param data данные массива int
     * @return данные в IntBuffer
     */
    private IntBuffer storeDataIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data).flip();
        return buffer;
    }
    
    
    /**
     * Удаление данных модели из памяти GPU
     */
    public void cleanUp() {
        for (int vaoId : vaos) 
            GL30.glDeleteVertexArrays(vaoId);
        for (int vboId : vbos) 
            GL15.glDeleteBuffers(vboId);
        for (int texture : textures) 
            GL11.glDeleteTextures(texture);
    }
}
