package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
Desarrolladores: Irvin Emmanuel Trujillo Díaz, Javier Antonio García Roque
Descripción: Esta clase es la encargada de mostrar el juego y su comportamiento...
Profesor: Roberto Martinez Román.
*/
public class PantallaJuego implements Screen{
    private final Principal principal; //Pantalla que se muestra...
    Texture texture;
    private OrthographicCamera camara;
    private Viewport vista;
    private Hud hud;
    private SpriteBatch batch;


    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer bodytodr;

    private NinjaPrincipal jugador;

    //creamos constructor por default
    public PantallaJuego(Principal principal){
        this.principal= principal;

        texture = new Texture("badlogic.jpg");
        camara = new OrthographicCamera();
        //vista=new StretchViewport(1200,720, camara); //imagen se apalasta o estira con la pantalla
        vista = new StretchViewport(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO,camara);//Imagen permanece en su lugar estatica
        //vista=new FitViewport(NinjaNoFukushuu.V_WIDTH,NinjaNoFukushuu.V_WIDTH, camara);//La imagen crece y se hace pequeña
        hud = new Hud(principal.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("MapaDeTierraV2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        //camara.position.set(vista.getScreenWidth(), vista.getScreenHeight(), 0);
        camara.position.set(640, 355, 0);
        world=new World(new Vector2(0,-10), true);//gravedad hacía abajo (obvio)
        bodytodr=new Box2DDebugRenderer();
        new Box2Creador(world, map);
        jugador = new NinjaPrincipal(world);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
    }

    public void handleInput(float dt){
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            jugador.b2Body.applyLinearImpulse(new Vector2(0, 4f), jugador.b2Body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && jugador.b2Body.getLinearVelocity().x <= 2)
            jugador.b2Body.applyLinearImpulse(new Vector2(3f, 0), jugador.b2Body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && jugador.b2Body.getLinearVelocity().x >= -2)
            jugador.b2Body.applyLinearImpulse(new Vector2(-3f, 0), jugador.b2Body.getWorldCenter(), true);
    }

    public void update(float dt){
        handleInput(dt);

        world.step(1/60f, 6, 2);//ajustamos el tiempo de colision
        camara.position.x=jugador.b2Body.getPosition().x;
        camara.update();
        renderer.setView(camara);
    }

    @Override
    public void render(float delta) {
        update(delta);

        //Limpiar pantalla
        Gdx.gl.glClearColor(60/255.0f, 181/255.0f, 0, .6f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camara.combined);  //Con este ajustas el batch..., si no, no va aparecer la imagen
        renderer.render();
        bodytodr.render(world, camara.combined);

        principal.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    //estos metodos sde ejecutan cuando se pasa a la otra pantalla


    @Override
    public void hide() {

    }

    @Override //Se ejecuta cuanto se pasa a otra pantalla, aqui se deben de liberar los recursos **
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        bodytodr.dispose();
    }


}
