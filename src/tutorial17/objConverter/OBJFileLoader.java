package tutorial17.objConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class OBJFileLoader {
 
    /**
     * Загрузка OBJ файла
     * @param filename путь к файлу
     * @return загруженую модель
     */
    public static ModelData loadOBJ(String filename) {
        FileReader fileReader = null;
        
        try {
            fileReader = new FileReader(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Немогу загрузить файл: " + filename);
        }
        
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        List<Vertex> vertices = new ArrayList<>(); // список вершин
        List<Vector2f> textures = new ArrayList<>(); // список текстурных координат
        List<Vector3f> normals = new ArrayList<>(); // список нормалей
        List<Integer> indices = new ArrayList<>(); // список индексов
        try {
            while (true) {
                line = reader.readLine(); // читаем каждую строчку
                if (line.startsWith("v ")) { // вершины
                    String[] currentLine = line.split(" ");
                    Vector3f vertex = new Vector3f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]),
                            (float) Float.valueOf(currentLine[3]));
                    Vertex newVertex = new Vertex(vertices.size(), vertex);
                    vertices.add(newVertex);
 
                } else if (line.startsWith("vt ")) { // текстурные координаты
                    String[] currentLine = line.split(" ");
                    Vector2f texture = new Vector2f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) { // нормали
                    String[] currentLine = line.split(" ");
                    Vector3f normal = new Vector3f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]),
                            (float) Float.valueOf(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) { // поверхности, индексы вершин, текстур и нормалей
                    break;
                }
            }
            // обработка поверхностей в OBJ файле
            while (line != null && line.startsWith("f ")) {
                String[] currentLine = line.split(" "); // блоки разделенные пробелами
                // вытягиваем числа(v/vt/vn) с каждого блока(3 блока), до "/"
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");
                
                // сортируем индексы под один номер, для каждого блока(3 блока)
                processVertex(vertex1, vertices, indices);
                processVertex(vertex2, vertices, indices);
                processVertex(vertex3, vertices, indices);
                
                line = reader.readLine(); // читаем следующую строку
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        removeUnusedVertices(vertices);
        // иничиализация массивов вершин, текстурных координат и нормалей
        float[] verticesArray = new float[vertices.size() * 3];
        float[] texturesArray = new float[vertices.size() * 2];
        float[] normalsArray = new float[vertices.size() * 3];
        // преобразование списка вершин в массивы
        float furthest = convertDataToArrays(vertices, textures, normals, verticesArray,
                texturesArray, normalsArray);
        // преобразование списка индексов в массив индексов
        int[] indicesArray = convertIndicesListToArray(indices);
        ModelData data = new ModelData(verticesArray, texturesArray, normalsArray, indicesArray,
                furthest);
        return data;
    }
 
    /**
     * Упорядочивание к индексам вершин, индексы текстурных координат и нормалей.
     * Своеобразная сортировка индексов.
     * Когда индексы не совпадают, этот метод переносит данные на нужные позиции, 
     * чтобы индексы были одинаковыми.
     * @param vertex данные вершины (v/vt/vn)
     * @param vertices список вершин
     * @param indices список индексов
     */
    private static void processVertex(String[] vertex, List<Vertex> vertices, 
            List<Integer> indices) {
        // текущий индекс вершины
        int index = Integer.parseInt(vertex[0]) - 1;
        Vertex currentVertex = vertices.get(index);
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;
        if (!currentVertex.isSet()) {
            currentVertex.setTextureIndex(textureIndex);
            currentVertex.setNormalIndex(normalIndex);
            indices.add(index);
        } else {
            dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
                    vertices);
        }
    }
 
    /**
     * Конвертируем список индексов в массив
     * @param indices список индексов
     * @return массив индексов
     */
    private static int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }
 
    private static float convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures,
            List<Vector3f> normals, float[] verticesArray, float[] texturesArray,
            float[] normalsArray) {
        float furthestPoint = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex currentVertex = vertices.get(i);
            if (currentVertex.getLength() > furthestPoint) {
                furthestPoint = currentVertex.getLength();
            }
            // текущий индекс вершины
            Vector3f position = currentVertex.getPosition();
            // текущий индекс текстурных координат
            Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
            // текущий индекс нормалей
            Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
            
            // вытягиваем данные вершин
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            // вытягиваем данные текстурных координат и переносим в индекс вершин
            texturesArray[i * 2] = textureCoord.x;
            texturesArray[i * 2 + 1] = 1 - textureCoord.y;
            // вытягиваем данные нормалей и переносим в индекс вершин
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
        }
        return furthestPoint;
    }
 
    private static void dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex,
            int newNormalIndex, List<Integer> indices, List<Vertex> vertices) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
        } else {
            Vertex anotherVertex = previousVertex.getDuplicateVertex();
            if (anotherVertex != null) {
                dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex,
                        indices, vertices);
            } else {
                Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition());
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
            }
 
        }
    }
     
    /**
     * Удаление неиспользованых вершин
     * @param vertices  список вершин
     */
    private static void removeUnusedVertices(List<Vertex> vertices){
        for(Vertex vertex:vertices){
            if(!vertex.isSet()){
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }
}
