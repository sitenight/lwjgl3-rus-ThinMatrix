package tutorial06.renderEngine;

import tutorial06.models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import tutorial06.models.TexturedModel;

/**
 * Визуализация данных
 */
public class Renderer {

    /**
     * Вызввается каждый кадр данный метод.
     */
    public void prepare() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); // Очистка экрана и рисование цветом в цветовом буфере
        GL11.glClearColor(1, 0, 0, 1); // Загрузка выбранного цвета в цветовой буфер
    }
    
    /**
     * Визуализация текстурной модели
     * @param texturedModel текстурная модель
     */
    public void render(TexturedModel texturedModel) {
        RawModel model = texturedModel.getModel();
        
        // Связываем VAO модели, указываем что будем работать с этими данными
        GL30.glBindVertexArray(model.getVaoId());
        // Активируем нулевой списки атрибутов
        GL20.glEnableVertexAttribArray(0); // координаты вершин
        GL20.glEnableVertexAttribArray(1); // текстурные координаты
        
        // Активируем текстурный блок перед привязкой текстуры
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        // Привяжем её, чтобы функции, использующие текстуры, знали какую текстуру использовать
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getId());
        
        // Рисуем примитивы. Аргументы:
        // - тип примитива (в данном случаем треугольники)
        // - количество вершин в модели
        // - указываем тип индексов данных в памяти
        // - смещение
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        
        // т.к. закончили использовать нулевой список атрибутов, то отключаем
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        // отвязываем VAO модели
        GL30.glBindVertexArray(0);
    }
}
