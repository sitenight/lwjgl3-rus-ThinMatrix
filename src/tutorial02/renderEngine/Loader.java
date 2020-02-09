package tutorial02.renderEngine;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Загружает 3д модели в память, путем сохранения позиционных данных вершин модели в VAO
 */
public class Loader {
    
    /** Список VAO для отслеживания в памяти */
    private List<Integer> vaos = new ArrayList<>();
    /** Список VBO для отслеживания в памяти */
    private List<Integer> vbos = new ArrayList<>();

    /**
     * Загрузка координат вершин в VAO
     * @param positions положение вершин модели
     * @return загруженную модель
     */
    public RawModel loadToVao(float[] positions) {
        int vaoId = createVao(); // получаем новый идетификатор VAO
        // сохраняем в нулевой список атрибутов VAO данные
        storeDataInAttributeList(0, positions);
        unbindVao(); // отвязываем VAO
        // возвращаем загруженную модель: id и количество вершин(x,y,z)
        return new RawModel(vaoId, positions.length/3);
    }
    
    /**
     * Создание пустого Объекта вершинного массива (VAO)
     * @return идентификатор созданного VAO
     */
    private int createVao() {
        int vaoId = GL30.glGenVertexArrays(); // инициализация пустого VAO
        vaos.add(vaoId); // добавляем в список очередной идетификатор VAO
        GL30.glBindVertexArray(vaoId); // связываем vao по id c OpenGL
        return vaoId;
    }
    
    /**
     * Хранение данных в одном списке атрибутов VAO
     * @param attributeNumber номер в списке атрибутов VAO
     * @param data массив данных
     */
    private void storeDataInAttributeList(int attributeNumber, float[] data) {
        int vboId = GL15.glGenBuffers(); // инициализация пустого VBO
        vbos.add(vboId); // добавляем в список очередной идетификатор VBO
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
        // - размер компонента (в данном случае 3 float)
        // - тип компонента
        // - остальные normalized, stride и pointer
        GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);   
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // отвязываем VAO
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
     * Удаление данных модели из памяти GPU
     */
    public void cleanUp() {
        for (int vaoId : vaos) 
            GL30.glDeleteVertexArrays(vaoId);
        for (int vboId : vbos) 
            GL15.glDeleteBuffers(vboId);
    }
}
