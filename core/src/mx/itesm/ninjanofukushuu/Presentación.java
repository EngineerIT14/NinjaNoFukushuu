package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Atem on 13/02/2016.
 */
public class Presentación {
    private Sprite sprite;
    public Presentación(Texture textura){
        sprite = new Sprite(textura);
    }
    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
}
