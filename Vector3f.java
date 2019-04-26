package demo02.engine.math;

public class Vector3f {
        
    public float x, y, z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3f(Vector3f vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public Vector3f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    
    public float getSquaredValue() {
	return x * x + y * y + z * z;
    }
    
    /**
     * Вычисление длины вектора
     * @return длина вектора
     */
    public float length() {
        return (float) Math.sqrt(getSquaredValue());
    }
    
    public Vector3f normalized() {
        return divided(length());
    }
       
    /**
     * Установка нового значения элементу вектора
     * @param component элемент вектора. 0 - х элемент, 1 - y элемент, 2 - z элемент
     * @param value новое значение
     * @return текущий вектор новым значением элемента
     */
    public Vector3f set(int component, float value) {
        switch(component) {
            case 0:
                x = value;
                break;
            case 1:
                y = value;
                break;
            case 2:
                z = value;
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
    public Vector3f add(float value) {
        this.x += value;
        this.y += value;
        this.z += value;
        return this;
    }
    
    /**
     * Сложение векторов
     * @param vector вектор который будем добавлять
     * @return текущий вектор суммы векторов
     */
    public Vector3f add(Vector3f vector) {
        this.x += vector.x;
        this.y += vector.y;
        this.z += vector.z;
        return this;
    }
       
    /**
     * Сложение значения с элементом вектора
     * @param component элемент вектора. 0 - х элемент, 1 - y элемент, 2 - z элемент
     * @param value значение на которое прибавляем
     * @return текущий вектор с сумированым элементом
     */
    public Vector3f add(int component, float value) {
        switch(component) {
            case 0:
                x += value;
                break;
            case 1:
                y += value;
                break;
            case 2:
                z += value;
                break;
        }
        return this;
    }
        
    /**
     * Сложение значения на вектор
     * @param value значение которое будем добавлять
     * @return новый суммированный вектор
     */
    public Vector3f added(float value) {
        return clone().add(value);
    }
    
    /**
     * Сложение векторов
     * @param vector вектор который будем добавлять
     * @return новый вектор суммы векторов
     */
    public Vector3f added(Vector3f vector) {
        return clone().add(vector);
    }
    
    //============== subtract =======================
    
    /**
     * Вычитание значения у вектора
     * @param value значение которое будем отнимать
     * @return текущий вектор
     */
    public Vector3f subtract(float value) {
        this.x -= value;
        this.y -= value;
        this.z -= value;
        return this;
    }
    
    /**
     * Вычитание векторов
     * @param vector вектор который будем отнимать
     * @return вычтеный текущий вектор
     */
    public Vector3f subtract(Vector3f vector) {
        this.x -= vector.x;
        this.y -= vector.y;
        this.z -= vector.z;
        return this;
    }
    
    /**
     * Вычитание векторов
     * @param value значение которое будем отнимать
     * @return новый вектор
     */    
    public Vector3f subtracted(float value) {
        return clone().subtract(value);
    }
    
    /**
     * Вычитание векторов
     * @param vector вектор который будем отнимать
     * @return новый вектор
     */
    public Vector3f subtracted(Vector3f vector) {
        return clone().subtract(vector);
    }
    
    //============== multiply =======================
    
    /**
     * Умножение вектора на значение
     * @param value значение
     * @return текущий вектор
     */
    public Vector3f multiply(float value) {
        this.x *= value;
        this.y *= value;
        this.z *= value;
        return this;
    }
    
    /**
     * Умножение векторов
     * @param vector вектор на который будем умножать
     * @return умноженый текущий вектор
     */
    public Vector3f multiply(Vector3f vector) {
        this.x *= vector.x;
        this.y *= vector.y;
        this.z *= vector.z;
        return this;
    }
    
    /**
     * Умножение вектора на значение
     * @param value значение
     * @return новый вектор
     */
    public Vector3f multiplied(float value) {
        return clone().multiply(value);
    }
    
    /**
     * Умножение векторов
     * @param vector вектор на который будем умножать
     * @return умноженый новый вектор
     */
    public Vector3f multiplied(Vector3f vector) {
        return clone().multiply(vector);
    }
    
    //============== divide =======================
        
    /**
     * Деление вектора на значение
     * @param value значение на которое будем делить
     * @return разделенный текущий вектор
     */
    public Vector3f divide(float value) {
        this.x /= value;
        this.y /= value;
        this.z /= value;
        return this;
    }
    
    /**
     * Деление векторов
     * @param vector вектор на который будем делить
     * @return разделенный текущий вектор
     */
    public Vector3f divide(Vector3f vector) {
        this.x /= vector.x;
        this.y /= vector.y;
        this.z /= vector.z;
        return this;
    }
    
    /**
     * Деление вектора на значение
     * @param value значение на которое будем делить
     * @return разделенный новый вектор
     */
    public Vector3f divided(float value) {
        return clone().divide(value);
    }
    
    /**
     * Деление векторов
     * @param vector вектор на который будем делить
     * @return разделенный новый вектор
     */
    public Vector3f divided(Vector3f vector) {
        return clone().divide(vector);
    }
    
    /**
     * Модуль вектора
     * @return новый вектор с положительными значениями
     */
    public Vector3f abs() {
        return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
    }
        
    /**
     * Сравнение векторов
     * @param vector вектор с которым сравниваем
     * @return Одинаковые ли вектора или нет
     */
    public boolean equals(Vector3f vector) {
        return x == vector.x && y == vector.y && z == vector.z;
    }

    protected Vector3f clone() {
        return new Vector3f(this.x, this.y, this.z);
    }
    
    @Override
    public String toString() {
        return "(" + x + "|" + y +  "|" + z + ")";
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) return true;
	if (o == null || getClass() != o.getClass()) return false;

	Vector3f vector3f = (Vector3f) o;

	if (Float.compare(vector3f.x, x) != 0) return false;
        if (Float.compare(vector3f.y, y) != 0) return false;
	return Float.compare(vector3f.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
	result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
	result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
	return result;
    }
}

