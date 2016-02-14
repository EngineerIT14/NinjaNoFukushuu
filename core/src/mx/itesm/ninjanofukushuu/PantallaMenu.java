package mx.itesm.ninjanofukushuu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/*
Desarrollador: Irvin Emmanuel Trujillo Díaz
Descripción: Esta clase es la encargada de mostrar el menu principal y sus determinados botones.
Profesor: Roberto Martinez Román.
*/

public class PantallaMenu implements Screen {

    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;
    //Fondo
    private Fondo fondo;
    private Texture texturaFondo;
    //Botones
    private Boton btnPLay,btnInstructions,btnGallery,btnAbout;
    private Texture texturaBtnPlay,texturaBtnInstructions,texturaBtnGallery,texturaBtnAbout; //Textura, se administran los recursos...
    private static final int anchoBoton = 480 , altoBoton = 160;
    private static final float posicionCentradaBotonX = 440;



    //LOGO
    private Logotipo logo;
    private Texture texturaLogo;
    private static final int anchoLogo = 700 , altoLogo = 350;
    private static final int posicionCentradaXLogo = 330 , posicionCentradaYLogo = 460;

    public PantallaMenu(Principal principal) {
        this.principal = principal;
    }

    //Metodo de la clase Screen, ya fue implementado
    @Override
    public void show() { //Se ejecuta cuando la pantalla se muestra en el juego
        // Crear la camara/vista
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();

        vista = new StretchViewport(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO,camara); //Se va ajustar la vista
        cargarTexturas();
        //Creando objetos...
        fondo = new Fondo(texturaFondo);
        btnPLay = new Boton(texturaBtnPlay);
        btnInstructions  = new Boton(texturaBtnInstructions);
        btnGallery = new Boton(texturaBtnGallery);
        btnAbout = new Boton(texturaBtnAbout);
        logo = new Logotipo(texturaLogo);

        //Colocando posición de botones.
        btnPLay.setPosicion(posicionCentradaBotonX,Principal.ALTO_MUNDO/2 - 30); //Se va restando de 120 en 120... para dar un espacio.
        btnInstructions.setPosicion(posicionCentradaBotonX,Principal.ALTO_MUNDO/2 - 150);
        btnGallery.setPosicion(posicionCentradaBotonX,Principal.ALTO_MUNDO/2 - 270);
        btnAbout.setPosicion(posicionCentradaBotonX,Principal.ALTO_MUNDO / 2 - 390);
        logo.setPosicion( posicionCentradaXLogo, posicionCentradaYLogo );

        //ajustando el tamaño
        fondo.setTamanio(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO);
        btnPLay.setTamanio(anchoBoton, altoBoton);
        btnInstructions.setTamanio(anchoBoton, altoBoton);
        btnGallery.setTamanio(anchoBoton, altoBoton);
        btnAbout.setTamanio(anchoBoton, altoBoton);
        logo.setTamanio(anchoLogo, altoLogo);


        batch = new SpriteBatch();
    }

    private void cargarTexturas() {

        texturaFondo = new Texture(Gdx.files.internal("fondoMenu.png"));
        texturaBtnPlay = new Texture(Gdx.files.internal("botonPlay.png"));
        texturaBtnInstructions = new Texture(Gdx.files.internal("botonInstructions.png"));
        texturaBtnGallery = new Texture(Gdx.files.internal("botonGallery.png"));
        texturaBtnAbout =  new Texture(Gdx.files.internal("botonAbout.png"));
        texturaLogo = new Texture(Gdx.files.internal("logo.png"));
    }

    //Metodo de la clase Screen, ya fue implementado
    @Override
    public void render(float delta) { //ese delta es el tiempo, 60fps entonces el float seria 1/60
        //Borrar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0); //Con este color vas a borrar
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Instruccion para borrar

        batch.setProjectionMatrix(camara.combined); //Con este ajustas el batch...,en este caso, el boton, se ajusta.

        leerEntrada(); //Pata revisar touch


        //DIBUJAR, primero las cosas que van atras....
        batch.begin(); //comienza a dibujar
        fondo.render(batch); //SE DIBUJAN LAS COSAS AQUI EN MEDIO...
        logo.render(batch);
        btnPLay.render(batch);
        btnInstructions.render(batch);
        btnGallery.render(batch);
        btnAbout.render(batch);
        batch.end();

    }



    private void leerEntrada() {
        if (Gdx.input.justTouched()){ //Si el usuario toco la pantalla...
            Vector3 coordernadas = new Vector3();
            coordernadas.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camara.unproject(coordernadas); //Traduce las coordenadas
            float x = coordernadas.x;
            float y = coordernadas.y;
            if ( verifcarBoton(x,y)==true){
                Gdx.app.log("leerEntrada","HAY UN TAP!"); //cuando le apretan va decir esto..
            }
        }
    }

    private boolean verifcarBoton(float x, float y) {
        Sprite spritebtnPlay = btnPLay.getSprite();
        Sprite spritebtnInstructions = btnInstructions.getSprite();
        Sprite spritebtnGallery = btnGallery.getSprite();
        Sprite spritebtnAbout = btnAbout.getSprite();
        //Verificar que la x y La y esten dentro de las dimensiones del sprite...
        if(x>=spritebtnPlay.getX() && x<=spritebtnPlay.getX()+spritebtnPlay.getWidth() && y>=spritebtnPlay.getY() && y<=spritebtnPlay.getY()+spritebtnPlay.getHeight()){
            return true;
        }
        else if(x>=spritebtnInstructions.getX() && x<=spritebtnInstructions.getX()+spritebtnInstructions.getWidth() && y>=spritebtnInstructions.getY() && y<=spritebtnInstructions.getY()+spritebtnInstructions.getHeight()){
            return true;
        }
        else if(x>=spritebtnGallery.getX() && x<=spritebtnGallery.getX()+spritebtnGallery.getWidth() && y>=spritebtnGallery.getY() && y<=spritebtnGallery.getY()+spritebtnInstructions.getHeight()){
            return true;
        }
        else if(x>=spritebtnAbout.getX() && x<=spritebtnAbout.getX()+spritebtnAbout.getWidth() && y>=spritebtnAbout.getY() && y<=spritebtnAbout.getY()+spritebtnAbout.getHeight()){
            return true;
        }
        else{
           return false;
        }

    }

    //Métodos de la clase screen.
    @Override
    public void resize(int width, int height) {
        vista.update(width,height); //Actualizate!!!!
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

    }
}