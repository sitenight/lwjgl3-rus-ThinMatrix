package tutorial17.textures;

public class TerrainTexturePack {

    /** основная текстура, черный цвет */
    private TerrainTexture backgroundTexture; 
    /** текстура для красного цвета */
    private TerrainTexture rTexture;
    /** текстура для зеленого цвета */
    private TerrainTexture gTexture;
    /** текстура для синего цвета */
    private TerrainTexture bTexture;

    public TerrainTexturePack(TerrainTexture backgroundTexture, 
            TerrainTexture rTexture, TerrainTexture gTexture, TerrainTexture bTexture) {
        this.backgroundTexture = backgroundTexture;
        this.rTexture = rTexture;
        this.gTexture = gTexture;
        this.bTexture = bTexture;
    }

    public TerrainTexture getBackgroundTexture() {
        return backgroundTexture;
    }

    public TerrainTexture getrTexture() {
        return rTexture;
    }

    public TerrainTexture getgTexture() {
        return gTexture;
    }

    public TerrainTexture getbTexture() {
        return bTexture;
    }
}
