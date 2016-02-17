package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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
        map = mapLoader.load("MapaDeTierra.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        //camara.position.set(vista.getScreenWidth(), vista.getScreenHeight(), 0);
        camara.position.set(640, 355, 0);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
    }

    public void handleInput(float dt){
        if(Gdx.input.isTouched())
            camara.position.x += 50*dt;
    }

    public void update(float dt){
        handleInput(dt);
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


    }


}
