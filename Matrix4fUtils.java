package demo02.engine.math;

public class Matrix4fUtils {

    /**
     * Установка матрицы позиции
     * @param m матрица в которую будем устанавливать значения новые
     * @param x позиция по x
     * @param y позиция по y
     * @param z позиция по z
     */
    public static void setPositionMatrix(Matrix4f m, float x, float y, float z) {
        m.set(
                1.0f, 0.0f, 0.0f, x,
                0.0f, 1.0f, 0.0f, y,
                0.0f, 0.0f, 1.0f, z,
                0.0f, 0.0f, 0.0f, 1.0f
        );
    }
    
    /**
     * Масштабирование матрицы
     * @param m матрица в которую будем устанавливать значения новые
     * @param scaleX масштаб по Х
     * @param scaleY масштаб по Y
     * @param scaleZ масштаб по Z
     */
    public static void setScaleMatrix(Matrix4f m, float scaleX, float scaleY, float scaleZ) {
        m.set(
                scaleX, 0.0f,   0.0f,   0.0f,
                0.0f,   scaleY, 0.0f,   0.0f,
                0.0f,   0.0f,   scaleY, 0.0f,
                0.0f,   0.0f,   0.0f,   1.0f
        );
    }
    
    /**
     * Поворот матрицы по оси Y
     * @param m матрица в которую вписывается новые значение
     * @param radiants угол поворота в радианах
     */
    public static void setYawMatrix(Matrix4f m, float radiants) {
	m.set(
            (float) Math.cos(radiants),  0.0f, (float) Math.sin(radiants), 0.0f,
            0.0f,			 1.0f, 0.0f,			   0.0f,
            (float) -Math.sin(radiants), 0.0f, (float) Math.cos(radiants), 0.0f,
            0.0f,			 0.0f, 0.0f,			   1.0f
        );
    }

    /**
     * Поворот матрицы по оси Х
     * @param m матрица в которую вписывается новые значение
     * @param radiants угол поворота в радианах
     */
    public static void setPitchMatrix(Matrix4f m, float radiants) {
        m.set(
            1.0f, 0.0f,			       0.0f,			   0.0f,
            0.0f, (float) Math.cos(radiants),  (float) Math.sin(radiants), 0.0f,
            0.0f, (float) -Math.sin(radiants), (float) Math.cos(radiants), 0.0f,
            0.0f, 0.0f,			       0.0f,			   1.0f
        );
    }

    /**
     * Поворот матрицы по оси Z
     * @param m матрица в которую вписывается новые значение
     * @param radiants угол поворота в радианах
     */
    public static void setRollMatrix(Matrix4f m, float radiants) {
	m.set(
            (float) Math.cos(radiants),	 (float) Math.sin(radiants), 0.0f, 0.0f,
            (float) -Math.sin(radiants), (float) Math.cos(radiants), 0.0f, 0.0f,
            0.0f,			 0.0f,			     1.0f, 0.0f,
            0.0f,			 0.0f,			     0.0f, 1.0f
	);
    }
    
    
    public static void setOrthographicProjection(Matrix4f m, float left, float right, float top, float bottom, float near, float far) {
	m.set(
            2f / (right-left),  0.0f,		   0.0f,	    -(right+left) / (right-left),
            0.0f,		2f / (top-bottom), 0.0f,	    -(top+bottom) / (top-bottom),
            0.0f,		0.0f,		   2f / (far-near), -(far+near) / (far-near),
            0.0f,		0.0f,		   0.0f,	    1.0f
	);
    }

    /**
     * Перспективная матрица проекции.
     * 
     * При перспективной проекции используется тот факт, что для человеческий 
     * глаз работает с предметом дальнего типа, размеры которого имеют угловые 
     * размеры. Чем дальше объект, тем меньше он нам кажется. Таким образом, 
     * объём пространства, который визуализируется представляет собой пирамиду.
     * 
     * В OpenGL версии 3.0 и выше отменили работу со стеком матриц OpenGL, 
     * теперь необходимо вручную создавать матрицы и передавать их в шейдер для 
     * дальнейшего использования.
     * 
     * @param m матрица в которую вписывается новые значение
     * @param fovDegree (field of view) определяет поле зрения/Угол обзора
     * @param aspect коэффициент отношения сторон окна OpenGL width/height
     * @param near расстояние от точки зрения до ближней отсекающей рамки
     * @param far расстояние от точки зрения до дальней отсекающей рамки
     */
    public static void setPerspectiveProjection(Matrix4f m, float fovDegree, float aspect, float near, float far) {
	float fovRadiants= (float) (fovDegree * Math.PI / 180.0);
	float f = (float) Math.tan(Math.PI * 0.5 - fovRadiants / 2.0f);

	m.set(
            f / aspect, 0.0f, 0.0f,                        0.0f,
            0.0f,	f,    0.0f,                        0.0f,
            0.0f,	0.0f, (far + near) / (near - far), (2 * far * near) / (near - far),
            0.0f,	0.0f, -1.0f,                       0.0f
	);
    }
    
    // ===================== getter ==================
    
    /**
     * Получение матрицы позиции
     * @param x координата позиции
     * @param y координата позиции
     * @param z координата позиции
     * @return 
     */
    public static Matrix4f getPositionMatrix(float x, float y, float z) {
        Matrix4f matrix = new Matrix4f();
        setPositionMatrix(matrix, x, y, z);
        return matrix;
    }
    
    public static Matrix4f getScaleMatrix(float scaleX, float scaleY, float scaleZ) {
        Matrix4f matrix = new Matrix4f();
        setScaleMatrix(matrix, scaleX, scaleY, scaleZ);
        return matrix;
    }
    
    public static Matrix4f getYawMatrix(float radiants) {
        Matrix4f matrix = new Matrix4f();
        setYawMatrix(matrix, radiants);
        return matrix;
    }
    
    public static Matrix4f getPitchMatrix(float radiants) {
        Matrix4f matrix = new Matrix4f();
        setPitchMatrix(matrix, radiants);
        return matrix;
    }
    
    public static Matrix4f getRollMatrix(float radiants) {
        Matrix4f matrix = new Matrix4f();
        setRollMatrix(matrix, radiants);
        return matrix;
    }
    
    public static Matrix4f getOrthographicProjection(float left, float right, float top, float bottom, float near, float far) {
        Matrix4f matrix = new Matrix4f();
        setOrthographicProjection(matrix, left, right, top, bottom, near, far);
        return matrix;
    }
    
    public static Matrix4f getPerspectiveProjection(float fovDegree, float aspect, float near, float far) {
        Matrix4f matrix = new Matrix4f();
        setPerspectiveProjection(matrix, fovDegree, aspect, near, far);
        return matrix;
    }
    
}

