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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
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

    //Scrolls
    private Array<ObjetosJuego> scroll;
    private Texture texturaScroll;

    //Vidas
    private Array<ObjetosJuego> vidas;
    private Texture texturaVidas;

    //Pociones
    private Array<ObjetosJuego> pociones;
    private Texture texturaPocion;


    //Estado para la suma del marcador
    private Estado estado;

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
        camara.position.set(vista.getScreenWidth(), vista.getScreenHeight(), 0);
        camara.position.set(640, 355, 0);
        world=new World(new Vector2(0,-220), true);//gravedad hacía abajo (obvio)
        bodytodr=new Box2DDebugRenderer();
        new Box2Creador(world, map);
        jugador = new NinjaPrincipal(world);
    }

    private void crearObjetos() {
        //Lista scrolles
        scroll = new Array<ObjetosJuego>(3);
        for (int i = 0; i<3;i++) {
            ObjetosJuego nuevo = new ObjetosJuego(texturaScroll);
            nuevo.setTamanio(50,50);
            scroll.add(nuevo);
        }
        scroll.get(0).setPosicion(550,300);
        scroll.get(1).setPosicion(800, 50);
        scroll.get(2).setPosicion(230,630);
        //Vidas
        /* vidas = new Array<ObjetosJuego>(3);
        for(int i =0; i<3 ;i++){
            ObjetosJuego nuevo = new ObjetosJuego(texturaVidas);
            nuevo.setTamanio(50,50);
            nuevo.setPosicion(30+i*50,650);
            vidas.add(nuevo);
        }*/
        //Pociones
        pociones = new Array<ObjetosJuego>(2);
        for(int i =0; i< 2;i++) {
            ObjetosJuego nuevo = new ObjetosJuego(texturaPocion);
            nuevo.setTamanio(50,50);
            pociones.add(nuevo);
        }
        //Se colocan las pociones en el lugar correspondiente
        pociones.get(0).setPosicion(1000, Principal.ALTO_MUNDO / 2);
        pociones.get(1).setPosicion(300, 500);

    }

    @Override
    public void show() {
        cargarTexturas();
        batch = new SpriteBatch();
        crearObjetos();
        this.estado = Estado.SINSUMAR; //Inicialmente el usuario no esta sumando nada
    }

    private void cargarTexturas() {
        texturaVidas = new Texture(Gdx.files.internal("life1.png"));
        texturaScroll = new Texture(Gdx.files.internal("scroll.png"));
        texturaPocion = new Texture(Gdx.files.internal("pocion.png"));
    }

    public void handleInput(float dt){
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            jugador.b2Body.applyLinearImpulse(new Vector2(0, 20000), jugador.b2Body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && jugador.b2Body.getLinearVelocity().x <= 100)
            jugador.b2Body.applyLinearImpulse(new Vector2(3f, 0), jugador.b2Body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && jugador.b2Body.getLinearVelocity().x >= -100)
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
        Gdx.gl.glClearColor(60 / 255.0f, 181 / 255.0f, 0, .6f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camara.combined);  //Con este ajustas el batch..., si no, no va aparecer la imagen
        renderer.render();
        bodytodr.render(world, camara.combined);
        recogerObjeto();
        //jugador.actualizar();
        //leerEntrada();
        principal.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        //dibujando
        hud.stage.draw();
        batch.begin();
        for(ObjetosJuego scrolls:scroll) {
            if(scrolls.actualizar())
                scrolls.render(batch);
        }
       /* for (ObjetosJuego vida:vidas){
            if(vida.actualizar())
                vida.render(batch);
        }*/
        for (ObjetosJuego pocion:pociones){
            if(pocion.actualizar())
                pocion.render(batch);
        }
        batch.end();
    }
    //Metodo para revomer elementos del juego
    private void recogerObjeto() {
        for (ObjetosJuego scrolls : scroll) {

            if (jugador.b2Body.getPosition().x >= scrolls.getSprite().getX() && jugador.b2Body.getPosition().x <= scrolls.getSprite().getX() + scrolls.getSprite().getWidth() &&
                    jugador.b2Body.getPosition().y >= scrolls.getSprite().getY() && jugador.b2Body.getPosition().y <= scrolls.getSprite().getY() + scrolls.getSprite().getHeight() && this.estado == Estado.SINSUMAR) {
                if(this.estado == Estado.SINSUMAR) {
                    this.estado = Estado.SUMANDO;
                    scrolls.quitarElemento();
                    this.hud.contadorPergaminos += 1;
                    System.out.println(this.hud.contadorSaludVidas);
                    this.hud.actualizarTablaLabels(); //Para que en pantalla se vea el cambio de marcador
                    scroll.pop();
                }
            }

            if(this.estado != Estado.SINSUMAR)
                this.estado = Estado.SINSUMAR;






        }
        for (ObjetosJuego pocion : pociones) {
            if (jugador.b2Body.getPosition().x >= pocion.getSprite().getX() && jugador.b2Body.getPosition().x <= pocion.getSprite().getX() + pocion.getSprite().getWidth() &&
                    jugador.b2Body.getPosition().y >= pocion.getSprite().getY() && jugador.b2Body.getPosition().y <= pocion.getSprite().getY() + pocion.getSprite().getHeight() && this.estado == Estado.SINSUMAR) {
                if(this.estado == Estado.SINSUMAR) {
                    this.estado = Estado.SUMANDO;
                    pocion.quitarElemento();
                    this.hud.contadorSaludVidas += 1; //Se modifica la vida, se le suma una unidad a  las actuales..
                    System.out.println(this.hud.contadorSaludVidas);
                    this.hud.actualizarTablaLabels(); //Para que en pantalla se vea el cambio de marcador
                    pociones.pop();
                }
            }
            if(this.estado != Estado.SINSUMAR)
                this.estado = Estado.SINSUMAR;









        }
    }
    /*private void leerEntrada() {
        if(Gdx.input.justTouched()){
            Vector3 coordenadas = new Vector3();
            coordenadas.set(Gdx.input.getX(),Gdx.input.getY(),0);
            camara.unproject(coordenadas); //traduce las coordenadas
            float x = coordenadas.x;
            float y = coordenadas.y;
            if (x >= 0 && x <= Principal.ANCHO_MUNDO && y >= 0 && y <= Principal.ALTO_MUNDO) {
                jugador.Saltar();
            }
        }
    }*/

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


    //Estados
    public  enum  Estado{
        SUMANDO,
        SINSUMAR
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
        texturaPocion.dispose();
        texturaScroll.dispose();
        texturaVidas.dispose();
    }


}
