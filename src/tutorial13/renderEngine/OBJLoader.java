package tutorial13.renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import tutorial13.models.RawModel;

/**
 * Загрузчик OBJ файлов
 */
public class OBJLoader {

    /**
     * Загрузка OBJ файла
     * @param filename путь к файлу
     * @param loader загрузчик
     * @return загруженую модель
     */
    public static RawModel loadObjModel(String filename, Loader loader) {
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Немогу загрузить файл: " + filename);
        }

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        List<Vector3f> vertices = new ArrayList<>(); // список вершин
        List<Vector2f> textures = new ArrayList<>(); // список текстурных координат
        List<Vector3f> normals = new ArrayList<>(); // список нормалей
        List<Integer> indices = new ArrayList<>(); // список индексов
        // регистрация массивов
        float[] verticesArray = null;
        float[] texturesArray = null;
        float[] normalsArray = null;
        int[] indicesArray = null;

        try {
            while(true) {
                line = bufferedReader.readLine(); // читаем каждую строчку
                String[] currentLine = line.split(" "); // делим строку по пробелам

                if (line.startsWith("v ")) { // вершины
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), 
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) { // текстурные координаты
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), 
                            Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) { // нормали
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), 
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) { // поверхности, индексы вершин, текстур и нормалей
                    texturesArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while (line != null) {
                // проверка наличия поверхностей в файле и пропуск других данных
                if (!line.startsWith("f ")) {
                    line = bufferedReader.readLine();
                    continue;
                }

                String[] currentLine = line.split(" "); // блоки разделенные пробелами
                // вытягиваем числа(v/vt/vn) с каждого блока(3 блока), до "/"
                String[] vertex1 = currentLine[1].split("/"); 
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                // сортируем индексы под один номер, для каждого блока(3 блока)
                processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);

                line = bufferedReader.readLine(); // читаем следующую строку
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // преобразование списка вершин в массив для передачи загрузчику (Loader)
        // инициализация массивов
        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;

        // заполняем массив данными (координаты вершины)
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        // заполняем массив данными (индексы)
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        return loader.loadToVao(verticesArray, texturesArray, normalsArray, indicesArray);
    }

    /**
     * Упорядочивание к индексам вершин, индексы текстурных координат и нормалей.
     * Своеобразная сортировка индексов.
     * Когда индексы не совпадают, этот метод переносит данные на нужные позиции, 
     * чтобы индексы были одинаковыми.
     * @param vertexData данные вершины (v/vt/vn)
     * @param indices список индексов
     * @param textures список текстурных координат
     * @param normals список нормалей
     * @param textureArray 
     * @param normalsArray 
     */
    private static void processVertex(String[] vertexData, List<Integer> indices, 
            List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, 
            float[] normalsArray) {
        // текущий индекс вершины
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer); // добавляем новый индекс в список
        
        // текущий индекс текстурных координат
        Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) - 1);
        // вытягиваем данные текстурных координат и переносим в индекс вершин
        textureArray[currentVertexPointer * 2] = currentTexture.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;

        // текущий индекс нормалей
        Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
        // вытягиваем данные нормалей и переносим в индекс вершин
        normalsArray[currentVertexPointer * 3] = currentNormal.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
    }
}

