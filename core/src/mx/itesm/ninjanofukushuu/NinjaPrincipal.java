package mx.itesm.ninjanofukushuu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
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
    private Estado estado; //Reacionado con movimiento

    public NinjaPrincipal(World world){
        this.world=world;
        defineNinja();
        Ninja = new Texture(Gdx.files.internal("SN.png"));
        estado = Estado.DERECHA;
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

    //Estado ninja, indica que hace
    public void actualizar() {
        switch (estado) {
            case DERECHA:
                b2Body.applyLinearImpulse(new Vector2(3f, 0), b2Body.getWorldCenter(), true);
                break;
            case IZQUIERDA:
                b2Body.applyLinearImpulse(new Vector2(3f, 0), b2Body.getWorldCenter(), true);
                break;
            case SALTANDO:
                b2Body.applyLinearImpulse(new Vector2(0, 20000), b2Body.getWorldCenter(), true);
                break;
        }
    }

    public void cambiarEstado(){

    }


    public void Saltar(){

    }

    public  enum  Estado{
        DERECHA,
        IZQUIERDA,
        SALTANDO
    }

}
