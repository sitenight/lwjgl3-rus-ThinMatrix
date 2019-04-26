package demo02.engine.math;

import engine.math.*;

/**
 * Класс для работы с цветом
 * @author Medved
 */
public class Color {
    
    /** Цвет по-умолчанию */
    public final static Color DEFAULT_COLOR = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    /** красный */
    public final static Color RED =           new Color(1.0f, 0.0f, 0.0f, 1.0f);
    /** бордовый */
    public final static Color MAROON =        new Color(0.5f, 0.0f, 0.0f, 1.0f);
    /** желтый */
    public final static Color YELLOW =        new Color(1.0f, 1.0f, 0.0f, 1.0f);
    /** оливковый  */
    public final static Color OLIVE =         new Color(0.5f, 0.5f, 0.0f, 1.0f);
    /** лайм */
    public final static Color LIME =          new Color(0.0f, 1.0f, 0.0f, 1.0f);
    /** зеленый */
    public final static Color GREEN =         new Color(0.0f, 0.5f, 0.0f, 1.0f);
    /** морская волна */
    public final static Color AQUA =          new Color(0.0f, 1.0f, 1.0f, 1.0f);
    /** сине-зеленый */
    public final static Color TEAL =          new Color(0.0f, 0.5f, 0.5f, 1.0f);
    /** синий */
    public final static Color BLUE =          new Color(0.0f, 0.0f, 1.0f, 1.0f);
    /** темно-синий */
    public final static Color NAVY =          new Color(0.0f, 0.0f, 0.5f, 1.0f);
    /** фуксия */
    public final static Color FUCHISIA =      new Color(1.0f, 0.0f, 1.0f, 1.0f);
    /** фиолетовый */
    public final static Color PURPLE =        new Color(0.5f, 0.0f, 0.5f, 1.0f);
    /** белый */
    public final static Color WHITE =         new Color(1.0f, 1.0f, 1.0f, 1.0f);
    /** серебрянный */
    public final static Color SILVER =        new Color(0.75f, 0.75f, 0.75f, 1.0f);
    /** серый */
    public final static Color GRAY =          new Color(0.5f, 0.5f, 0.5f, 1.0f);
    /** черный */
    public final static Color BLACK =         new Color(0.0f, 0.0f, 0.0f, 1.0f);

    /** красный компонент */
    public float r; 
    
    /** зеленый компонент */
    public float g;
    
    /** синий компонент */
    public float b;
    
    /** альфа компонент */
    public float a;

    /**
     * Конструктор цвета
     * @param r красный компонент 
     * @param g зеленый компонент
     * @param b синий компонент
     * @param a альфа компонент(прозрачность)
     */
    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    /**
     * Конструктор цвета цвета из вектора,
     * альфа канал равен 1.0f
     * @param vector цвет содержащийся в векторе
     */
    public Color(Vector3f vector) {
        this.r = vector.x;
        this.g = vector.y;
        this.b = vector.z;
        this.a = 1.0f;
    }
    
    /**
     * Конструктор цвета из ранее созданного цвета
     * @param color цвет ранее созданный
     */
    public Color(Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
    }
    
    /**
     * Создает новый цвет из одного целого числа.
     * Каждый компонент представлен 1 байтом в следующем порядке: 
     * альфа канал, красный, зеленый, синий.
     * 
     * @param argb все компоненты в одном целом числе
     */
    public Color(int argb) {
        this.a = (argb >>> 24) & 0xff;
        this.r = (argb >>> 16) & 0xff;
        this.g = (argb >>> 8) & 0xff;
        this.b = (argb) & 0xff;
    }
    
    /**
     * Конструктор цвета заданный по-умолчанию
     * В данном случае черный цвет
     */
    public Color() {
        this(DEFAULT_COLOR);
    }
    
    /**
     * Установка нового цвета из вектора,
     * альфа канал равен 1.0f
     * @param vector цвет содержащийся в векторе
     */
    public void set(Vector3f vector) {
        this.r = vector.x;
        this.g = vector.y;
        this.b = vector.z;
        this.a = 1.0f;
    }
    
    /**
     * Возвращает 3ех мерный вектор цвета, без альфа канала
     * @return 3ех мерный вектор цвета
     */
    public Vector3f get() {
        return new Vector3f(r, g, b);
    }
    
    /**
     * Создает целое число, содержащее все компоненты в одном целом числе.
     * Каждый компонент представлен 1 байтом в следующем порядке: 
     * альфа канал, красный, зеленый, синий.
     * @return argb значение
     */
    public int getARGB() {
	return (((int) (a * 255) & 0xff) << 24) | (((int) (r * 255) & 0xff) << 16) 
                | (((int) (g * 255) & 0xff) << 8) | (((int) (b * 255) & 0xff));
    }
    
}
