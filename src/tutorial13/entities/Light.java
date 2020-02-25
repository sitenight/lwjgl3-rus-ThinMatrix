package tutorial13.entities;

import org.joml.Vector3f;

/**
 * Представление источника света
 */
public class Light {

    /** Позиция источника света */
    private Vector3f position;
    /** Цвет свечения */
    private Vector3f colour;

    public Light(Vector3f position, Vector3f colour) {
        this.position = position;
        this.colour = colour;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }
}
