package com.dt181g.laboration_3.model;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;


/**
 * Model class for Sprite editor.
 * Contains logic to handle IO.
 * @author Henrik Oskarsson
 */
public class Model {


    /**
     * Constructor of Model.
     */
    public Model() {
    }

    /**
     * Method for creating a sprite.
     * @param polygon polygon base for the sprite.
     * @param recursivePoint recursive blueprint for the sprite.
     * @return a new sprite.
     */
    public Sprite createSprite(Polygon polygon, List<Point2D.Float> recursivePoint) {
        return new Sprite(polygon,recursivePoint);
    }

    /**
     * Method for storing a sprite.
     * @param s sprite to be stored.
     */
    public void storeSprite(Sprite s) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("sprite.obj"))) {
            oos.writeObject(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for reading sprite.
     * @return a sprite if available, otherwise null.
     */
    public Sprite readSprite() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("sprite.obj"))) {
            return (Sprite) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
