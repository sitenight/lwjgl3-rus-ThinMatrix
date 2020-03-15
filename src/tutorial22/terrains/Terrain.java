package tutorial22.terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.joml.Vector2f;
import org.joml.Vector3f;
import tutorial22.models.RawModel;
import tutorial22.renderEngine.Loader;
import tutorial22.textures.TerrainTexture;
import tutorial22.textures.TerrainTexturePack;
import tutorial22.toolbox.Maths;

/**
 * Класс который представляет ландшафт в нашей игре
 */
public class Terrain {

    /** Размер ландшафта */
    private static final float SIZE = 800;
    /** Максимальная высота карты, минимальная будет также но со знаком минуса */
    private static final int MAX_HEIGHT = 40;
    /** Цвет пикселя для максимальной высоты */
    private static final int MAX_PIXEL_COLOUR = 256 * 256 * 256;
    
    // координаты позиции
    private float x;
    private float z;
    
    private RawModel model; // сетка ландшафта
    private TerrainTexturePack texturePack; // пак текстур ландшафта
    private TerrainTexture blendMap; // карта смешения текстур
    
    private float[][] heights; // высоты ландшафта

    /**
     * Конструктор ландшафта
     * @param gridX позиция по оси Х в мире
     * @param gridZ позиция по оси Z в мире
     * @param loader загрузчик
     * @param texturePack пак текстур ландшафта
     * @param blendMap карта смешения текстур
     * @param heightMap ссылка на карту высот
     */
    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, 
            TerrainTexture blendMap, String heightMap) {
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain(loader, heightMap);
    }
    
    /**
     * Генератор ландшафта
     * @param loader загрузчик
     * @param heightMap карта высот
     * @return сгенерированую модель ландшафта 
     */
    private RawModel generateTerrain(Loader loader, String heightMap){
        // загрузка файла карты высот
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(heightMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert image != null; // проверка на корректность загрузки файла
        
        int VERTEX_COUNT = image.getHeight(); // кол-во вершин одной стороны ландшафта
        
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];
        
        int count = VERTEX_COUNT * VERTEX_COUNT; // количество вершин
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                // генерируем вершины
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                // высота вершины
                float height = getHeight(j, i, image);
                heights[j][i] = height; // помещаем в массив высот для обнаружения столкновений
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                // генерируем нормали
                Vector3f normal = calculateNormal(j, i, image); // рассчитываем нормаль
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                // генерируем текстурные координаты
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVao(vertices, textureCoords, normals, indices);
    }
    
    /**
     * Расчет высоты ландшафта в зависимости цвета пиксела на карте высот
     * @param x координата Х
     * @param z координата Z
     * @param image карта высот
     * @return высоту ландшафта в определённых координатах
     */
    private float getHeight(int x, int z,  BufferedImage image) {
        if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) {
            return 0;   // out of bounds
        }

        float height = image.getRGB(x, z); // получаем цвет пикселя
        height += MAX_PIXEL_COLOUR / 2f;
        height /= MAX_PIXEL_COLOUR / 2f; // преобразовываем в диапазон от -1 до 1
        height *= MAX_HEIGHT; // получаем конечную высоту вершины
        return height;
    }
    
    /**
     * Расчет вектора нормали
     * @param x координата Х
     * @param z координата Z
     * @param image карта высот
     * @return вектор нормали для ландшафта в определённых координатах
     */
    private Vector3f calculateNormal(int x, int z, BufferedImage image) {
        // вычисляем высоты соседних пикселей
        float heightL = getHeight(x - 1, z, image); // левый пиксель
        float heightR = getHeight(x + 1, z, image); // правый пиксель
        float heightD = getHeight(x, z - 1, image); // нижний пиксель
        float heightU = getHeight(x, z + 1, image); // верхний пиксель
        // рассчитываем вектор нормали
        Vector3f normal= new Vector3f(heightL - heightR, 2.0f, heightD - heightU);
        normal.normalize(); // нормализируем вектор
        return normal;
    }
    
    /**
     * Метод получения высоты ландшафта по заданным координатам
     * @param worldX мировые координаты по Х
     * @param worldZ мировые координаты по Z
     * @return высоту ландшафта
     */
    public float getHeightsOfTerrain(float worldX, float worldZ) {
        // преобразование мировых координа в положение относительно ландшафта
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        // рассчет размера сетки на ландшафте
        float gridSquareSize = SIZE / (float)(heights.length - 1) ;
        // в каком квадрате сетки нужно производить рассчет высоты
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        // проверка не выхода за пределы ландшафта
        if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 ||
           gridX < 0 || gridZ < 0) {
            return 0;
        }
        // координаты на квадрате ландшафта
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        
        float triangleHeight; // результирующая высота
        if(xCoord <= (1 - zCoord)) { // выбор треугольника в квадрате ландшафта
            triangleHeight = Maths.calculateTriangleHeightByBarycentric(
                    new Vector3f(0, heights[gridX][gridZ], 0), 
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoord, zCoord) // позиции проверки
            );
        } else {
            triangleHeight = Maths.calculateTriangleHeightByBarycentric(
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoord, zCoord)
            );
        }

        return triangleHeight;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }
}
