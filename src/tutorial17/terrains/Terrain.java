package tutorial17.terrains;

import tutorial17.models.RawModel;
import tutorial17.renderEngine.Loader;
import tutorial17.textures.ModelTexture;
import tutorial17.textures.TerrainTexture;
import tutorial17.textures.TerrainTexturePack;

/**
 * Класс который представляет ландшафт в нашей игре
 */
public class Terrain {

    /** Размер ландшафта */
    private static final float SIZE = 800;
    /** Количество вершин с каждой стороны */
    private static final int VERTEX_COUNT = 128;
    
    // координаты позиции
    private float x;
    private float z;
    
    private RawModel model; // сетка ландшафта
    private TerrainTexturePack texturePack; // пак текстур ландшафта
    private TerrainTexture blendMap; // карта смешения текстур

    /**
     * Конструктор ландшафта
     * @param gridX позиция по оси Х в мире
     * @param gridZ позиция по оси Z в мире
     * @param loader загрузчик
     * @param texturePack пак текстур ландшафта
     * @param blendMap карта смешения текстур
     */
    public Terrain(int gridX, int gridZ, Loader loader, 
            TerrainTexturePack texturePack, TerrainTexture blendMap) {
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain(loader);
    }
    
    /**
     * Генератор ландшафта
     * @param loader загрузчик
     * @return сгенерированую модель ландшафта 
     */
    private RawModel generateTerrain(Loader loader){
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
                vertices[vertexPointer*3+1] = 0;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                // генерируем нормали
                normals[vertexPointer*3] = 0;
                normals[vertexPointer*3+1] = 1;
                normals[vertexPointer*3+2] = 0;
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
