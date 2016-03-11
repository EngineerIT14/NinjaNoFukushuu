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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by DELL on 10/03/2016.
 */
public class PantallaInstrucciones implements Screen {
    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;
    //Fondo
    private Fondo fondo;
    private Texture texturaFondo;

    private Boton btnRegresar;
    private Texture texturaRegresar;
    private static final int anchoBoton = 180, altoBoton = 200; //anchoBoton1 = 480 , altoBoton1 = 160;

    public PantallaInstrucciones(Principal principal) {
        this.principal = principal;
    }

    @Override
    public void show() {
        //Crear camara
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();

        vista = new StretchViewport(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO,camara);
        this.crearObjetos();



        //Crear fondo
        fondo = new Fondo(texturaFondo);

        btnRegresar = new Boton(texturaRegresar);
        btnRegresar.setPosicion(Principal.ANCHO_MUNDO * 7 / 8 , Principal.ALTO_MUNDO * 1 / 5 -150);
        btnRegresar.setTamanio(anchoBoton - 50, altoBoton - 50);
        //Batch
        fondo.getSprite().setCenter(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2);
        fondo.getSprite().setOrigin(1500 / 2, 1500 / 2);
        batch = new SpriteBatch();
    }



    //crea los objetos de textura y audio
    private void crearObjetos(){
        AssetManager assetManager = principal.getAssetManager();   // Referencia al assetManager
        //Fondo
        texturaFondo = assetManager.get("N.jpg");
        texturaRegresar = assetManager.get("return.png");
    }

    @Override
    public void render(float delta) {
        //Borrar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camara.combined);
        this.leerEntrada();
        //DIBUJAR
        batch.begin();
        fondo.render(batch);
        btnRegresar.render(batch);
        batch.end();
    }

    private void leerEntrada() {
        //Hubo toque
        if (Gdx.input.justTouched()) {
            Vector3 coordernadas = new Vector3();
            coordernadas.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camara.unproject(coordernadas);
            float x = coordernadas.x;
            float y = coordernadas.y;
            switch (verifcarBoton(x, y)) {
                case 1: //Caso para aparecer la presentación de Irvin
                    principal.setScreen(new PantallaMenu(principal, true)); //se manda true porque se esta escuchando la cancion, es decir, ya hay un objeto cancion reproduciendose..
                    break;
            }
        }}

    private int verifcarBoton(float x, float y) {
        Sprite spriteBtnRegresar = btnRegresar.getSprite();
        //Verificar si se toco algun botón y no hay una prsentación mostrada
        if (x>=spriteBtnRegresar.getX() && x<=spriteBtnRegresar.getX()+spriteBtnRegresar.getWidth() &&
                y>=spriteBtnRegresar.getY() && y<=spriteBtnRegresar.getY()+spriteBtnRegresar.getHeight()){
                return 1;
            }
        return 0;
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
        //Eliminar basura
        principal.dispose();
        batch.dispose();
        texturaRegresar.dispose();
        texturaFondo.dispose();

    }


}