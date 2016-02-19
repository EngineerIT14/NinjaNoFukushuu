package mx.itesm.ninjanofukushuu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Atem on 18/02/2016.
 */
public class NinjaPrincipal {
    public World world;//cargamos el mundo
    public Body b2Body;//Creamos el cuerpo
    private Texture Ninja;

    public NinjaPrincipal(World world){
        this.world=world;
        defineNinja();
        Ninja = new Texture(Gdx.files.internal("SN.png"));
    }

    public void defineNinja(){
        BodyDef bodef=new BodyDef();
        bodef.position.set(32,100);//posicion de arranque
        bodef.type=BodyDef.BodyType.DynamicBody; //Como se va a mover lo creamos como objeto dinamico
        b2Body=world.createBody(bodef);
        FixtureDef fidef=new FixtureDef();
        CircleShape shape=new CircleShape();
        shape.setRadius(5);

        fidef.shape=shape;
        b2Body.createFixture(fidef); //Crea el objeto con lo que definimos en Fixture.
    }
}
