package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Atem on 18/02/2016.
 */
public class Box2Creador {
    public Box2Creador(World world, TiledMap map){

        BodyDef bodef =new BodyDef();
        PolygonShape shape= new PolygonShape();
        FixtureDef fidef= new FixtureDef();
        Body dody;

        //Darle un cuerpo a cada objeto del mapa
        for (MapObject object:map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject)object).getRectangle();

            bodef.type=BodyDef.BodyType.StaticBody;//estaticos, no se mueven al ser plataformas
            bodef.position.set((rect.getX()+rect.getWidth()/2), (rect.getY()+rect.getHeight()/2));

            dody=world.createBody(bodef);

            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fidef.shape=shape;
            dody.createFixture(fidef);
        }
    }
}
