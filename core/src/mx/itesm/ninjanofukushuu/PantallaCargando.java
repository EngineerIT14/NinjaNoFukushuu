package mx.itesm.ninjanofukushuu;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Descripción: Pantala intermedia entre el menú y el juego
 * Profesor: Roberto Martinez Román
 */
public class PantallaCargando implements Screen
{
    private Principal plataforma;

    // La cámara y vista principal
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;

    // Imagen cargando
    private Texture texturaCargando;
    private Sprite spriteCargando;

    private AssetManager assetManager;  // Asset manager principal

    private int pantallaCargar; //aqui se va almacenar un numero que indicara que pantalla va cargar
                                /*
                                * 1: elementos juego
                                * 2: elementos instrucciones
                                * 3: elementos galeria de arte
                                * 4: elementos acerca de
                                * */

    public PantallaCargando(Integer nivel,Principal plataforma) {
        this.plataforma = plataforma;
        this.pantallaCargar = nivel;
        this.assetManager = plataforma.getAssetManager();
    }

    @Override
    public void show() {
        // Crea la cámara/vista
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();
        vista = new StretchViewport(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO, camara);

        batch = new SpriteBatch();

        // Cargar recursos
        assetManager.load("cargando.png", Texture.class);
        assetManager.finishLoading();
        texturaCargando = assetManager.get("cargando.png");
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(Principal.ANCHO_MUNDO / 2 - spriteCargando.getWidth() / 2,
                Principal.ALTO_MUNDO / 2 - spriteCargando.getHeight() / 2);

        cargarRecursos();
    }

    // Carga los recursos a través del administrador de assets (siguiente pantalla)
    private void cargarRecursos() {
        // Carga los recursos de la siguiente pantalla
           /*       * 0: Menu principal.
                     * 1: elementos juego
                       * 2: elementos instrucciones
                            * 3: elementos galeria de arte
                                * 4: elementos acerca de
            */
        switch(pantallaCargar) {
            case 0: break;
            case 1: //Pantalla Juego
                    assetManager.load("MapaDeTierraV2.tmx", TiledMap.class);  // Cargar info del mapa
                    assetManager.load("marioSprite.png", Texture.class);    // Cargar imagen
                    // Texturas de los botones
                    assetManager.load("derecha.png", Texture.class);
                    assetManager.load("izquierda.png", Texture.class);
                    assetManager.load("salto.png", Texture.class);

                    //Textura objetos
                    assetManager.load("scroll.png", Texture.class);
                    assetManager.load("pocion.png", Texture.class);

                    //Textura enemigo
                    assetManager.load("TierraE.png", Texture.class);
                    //Textura templo
                    assetManager.load("temploVerde.png", Texture.class);
                    //caritaVida
                    assetManager.load("life1.png", Texture.class);
                    //sonidos

                    assetManager.load("efectoSaltoHataku.wav",Sound.class);
                    assetManager.load("efectoVida.wav",Sound.class);
                    assetManager.load("efectoPergamino.wav",Sound.class);
                    break;

            case 4: //Pantalla acerca de.
                    assetManager.load("Fondo.jpg",Texture.class);
                    assetManager.load("M.png",Texture.class);
                    assetManager.load("N.png", Texture.class);
                    assetManager.load("I.png",Texture.class);
                    assetManager.load("J.png", Texture.class);
                    assetManager.load("F.png", Texture.class);
                    assetManager.load("return.png",Texture.class);
                    assetManager.load("Irvi.png",Texture.class);
                    assetManager.load("Mare.png",Texture.class);
                    assetManager.load("Javo.png",Texture.class);
                    assetManager.load("Nuria.png",Texture.class);
                    assetManager.load("Fercho.png",Texture.class);
                    assetManager.load("sonidoVentana.wav",Sound.class);
                    break;
            default: break;
        }
    }

    @Override
    public void render(float delta) {

        // Actualizar carga
        actualizar();

        // Dibujar
        borrarPantalla();
        //gira la imagen cargando
        spriteCargando.setRotation(spriteCargando.getRotation() + 15);

        batch.setProjectionMatrix(camara.combined);

        // Entre begin-end dibujamos nuestros objetos en pantalla
        batch.begin();
        spriteCargando.draw(batch);
        batch.end();
    }

    private void actualizar() {

        if (assetManager.update()) {// Terminó la carga, cambiar de pantalla
            switch(this.pantallaCargar){//dependiendo que se cargo, cambiamos la pantalla...
                case 0: break;
                case 1:
                    plataforma.setScreen(new PantallaJuego(plataforma));
                    break;
                case 4:
                    plataforma.setScreen(new PantallaAcerca(plataforma));
                    break;
                default: break;
            }
        } else {
            // Aún no termina la carga de assets, leer el avance
            float avance = assetManager.getProgress()*100;
            Gdx.app.log("Cargando",avance+"%");
        }
    }

    private void borrarPantalla() {
        Gdx.gl.glClearColor(0.42f, 0.55f, 1, 1);    // r, g, b, alpha
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }


    @Override
    public void resize(int width, int height) {
        vista.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        texturaCargando.dispose();
        // Los assets de PantallaJuego se liberan en el método dispose de PantallaJuego
    }
}
