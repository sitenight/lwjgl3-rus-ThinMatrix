package demo02.engine.math;

public class Vector2f {
        
    public float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2f(Vector2f vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public Vector2f() {
        this.x = 0;
        this.y = 0;
    }
    
    /**
     * Вычисление длины вектора
     * @return длина вектора
     */
    public float length() {
        return (float) Math.sqrt(getSquaredValue());
    }
    
    public float getSquaredValue() {
	return x * x + y * y;
    }
    
    public Vector2f normalized() {
        return divided(length());
    }
       
    /**
     * Установка нового значения элементу вектора
     * @param component элемент вектора. 0 - х элемент, 1 - y элемент
     * @param value новое значение
     * @return текущий вектор новым значением элемента
     */
    public Vector2f set(int component, float value) {
        switch(component) {
            case 0:
                x = value;
                break;
            case 1:
                y = value;
                break;
        }
        return this;
    }
    
    //============== add =======================
    
    /**
     * Сложение значения на вектор
     * @param value значение которое будем добавлять
     * @return текущий суммированный вектор
     */
    public Vector2f add(float value) {
        this.x += value;
        this.y += value;
        return this;
    }
    
    /**
     * Сложение векторов
     * @param vector вектор который будем добавлять
     * @return текущий вектор суммы векторов
     */
    public Vector2f add(Vector2f vector) {
        this.x += vector.x;
        this.y += vector.y;
        return this;
    }
       
    /**
     * Сложение значения с элементом вектора
     * @param component элемент вектора. 0 - х элемент, 1 - y элемент
     * @param value значение на которое прибавляем
     * @return текущий вектор с сумированым элементом
     */
    public Vector2f add(int component, float value) {
        switch(component) {
            case 0:
                x += value;
                break;
            case 1:
                y += value;
                break;
        }
        return this;
    }
        
    /**
     * Сложение значения на вектор
     * @param value значение которое будем добавлять
     * @return новый суммированный вектор
     */
    public Vector2f added(float value) {
        return clone().add(value);
    }
    
    /**
     * Сложение векторов
     * @param vector вектор который будем добавлять
     * @return новый вектор суммы векторов
     */
    public Vector2f added(Vector2f vector) {
        return clone().add(vector);
    }
    
    //============== subtract =======================
    
    /**
     * Вычитание значения у вектора
     * @param value значение которое будем отнимать
     * @return текущий вектор
     */
    public Vector2f subtract(float value) {
        this.x -= value;
        this.y -= value;
        return this;
    }
    
    /**
     * Вычитание векторов
     * @param vector вектор который будем отнимать
     * @return вычтеный текущий вектор
     */
    public Vector2f subtract(Vector2f vector) {
        this.x -= vector.x;
        this.y -= vector.y;
        return this;
    }
    
    /**
     * Вычитание векторов
     * @param value значение которое будем отнимать
     * @return новый вектор
     */    
    public Vector2f subtracted(float value) {
        return clone().subtract(value);
    }
    
    /**
     * Вычитание векторов
     * @param vector вектор который будем отнимать
     * @return новый вектор
     */
    public Vector2f subtracted(Vector2f vector) {
        return clone().subtract(vector);
    }
    
    //============== multiply =======================
    
    /**
     * Умножение вектора на значение
     * @param value значение
     * @return текущий вектор
     */
    public Vector2f multiply(float value) {
        this.x *= value;
        this.y *= value;
        return this;
    }
    
    /**
     * Умножение векторов
     * @param vector вектор на который будем умножать
     * @return умноженый текущий вектор
     */
    public Vector2f multiply(Vector2f vector) {
        this.x *= vector.x;
        this.y *= vector.y;
        return this;
    }
    
    /**
     * Умножение вектора на значение
     * @param value значение
     * @return новый вектор
     */
    public Vector2f multiplied(float value) {
        return clone().multiply(value);
    }
    
    /**
     * Умножение векторов
     * @param vector вектор на который будем умножать
     * @return умноженый новый вектор
     */
    public Vector2f multiplied(Vector2f vector) {
        return clone().multiply(vector);
    }
    
    //============== divide =======================
        
    /**
     * Деление вектора на значение
     * @param value значение на которое будем делить
     * @return разделенный текущий вектор
     */
    public Vector2f divide(float value) {
        this.x /= value;
        this.y /= value;
        return this;
    }
    
    /**
     * Деление векторов
     * @param vector вектор на который будем делить
     * @return разделенный текущий вектор
     */
    public Vector2f divide(Vector2f vector) {
        this.x /= vector.x;
        this.y /= vector.y;
        return this;
    }
    
    /**
     * Деление вектора на значение
     * @param value значение на которое будем делить
     * @return разделенный новый вектор
     */
    public Vector2f divided(float value) {
        return clone().divide(value);
    }
    
    /**
     * Деление векторов
     * @param vector вектор на который будем делить
     * @return разделенный новый вектор
     */
    public Vector2f divided(Vector2f vector) {
        return clone().divide(vector);
    }
    
    /**
     * Модуль вектора
     * @return новый вектор с положительными значениями
     */
    public Vector2f abs() {
        return new Vector2f(Math.abs(x), Math.abs(y));
    }
        
    /**
     * Сравнение векторов
     * @param vector вектор с которым сравниваем
     * @return Одинаковые ли вектора или нет
     */
    public boolean equals(Vector2f vector) {
        return x == vector.x && y == vector.y;
    }

    protected Vector2f clone() {
        return new Vector2f(this.x, this.y);
    }
    
    @Override
    public String toString() {
        return "(" + x + "|" + y + ")";
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (o == null || getClass() != o.getClass()) return false;

	Vector2f vector2f = (Vector2f) o;

	if (Float.compare(vector2f.x, x) != 0) return false;
	return Float.compare(vector2f.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
	result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
	return result;
    }
}

