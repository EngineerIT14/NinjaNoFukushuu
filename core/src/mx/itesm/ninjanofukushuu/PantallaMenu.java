package mx.itesm.ninjanofukushuu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/*
Desarrolladores: Irvin Emmanuel Trujillo Díaz, Luis Fernando
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
    private static final int anchoBoton = 400 , altoBoton = 160;
    private static final float posicionYBotonJugarInstrucciones = Principal.ALTO_MUNDO/2-150, posicionYBotonGalleryAbout = Principal.ALTO_MUNDO/2 -310   ; //Posicion en y...

    //LOGO
    private Logotipo logo;
    private Texture texturaLogo;
    private static final int anchoLogo = 700 , altoLogo = 350;
    private static final int posicionCentradaXLogo = 330 , posicionCentradaYLogo = 380;

    //Efectos y musica de fondo
    private Sound efectoClick;
    private Music musicaFondo;
    private boolean banderaCancionJuego; //Esta bandera sirve para que se vuelva a crear el objeto de musicaFondo cuando regresas al menu principal y se interrumpa la que esta actualmente.


    public PantallaMenu(Principal principal,boolean banderaCancionJuego) { //Constructor
        this.principal = principal;
        this.banderaCancionJuego = banderaCancionJuego;
    }

    //Metodo de la clase Screen, ya fue implementado
    @Override
    public void show() { //Se ejecuta cuando la pantalla se muestra en el juego
        // Crear la camara/vista
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();

        vista = new StretchViewport(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO,camara); //Se va ajustar la vista

        this.cargarAudioJuego();
        this.crearObjetos();

        //Creando objetos...
        fondo = new Fondo(texturaFondo);
        btnPLay = new Boton(texturaBtnPlay);
        btnInstructions  = new Boton(texturaBtnInstructions);
        btnGallery = new Boton(texturaBtnGallery);
        btnAbout = new Boton(texturaBtnAbout);
        logo = new Logotipo(texturaLogo);

        //Colocando posición de botones.
        btnInstructions.setPosicion(295,PantallaMenu.posicionYBotonJugarInstrucciones);
        btnPLay.setPosicion(675, PantallaMenu.posicionYBotonJugarInstrucciones);
        btnGallery.setPosicion(15,PantallaMenu.posicionYBotonGalleryAbout);
        btnAbout.setPosicion(880,PantallaMenu.posicionYBotonGalleryAbout);
        logo.setPosicion( PantallaMenu.posicionCentradaXLogo, PantallaMenu.posicionCentradaYLogo );

        //ajustando el tamaño
        fondo.setTamanio(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO);
        btnPLay.setTamanio(anchoBoton, altoBoton);
        btnInstructions.setTamanio(anchoBoton-10, altoBoton);
        btnGallery.setTamanio(anchoBoton+10, altoBoton); //Se le modifico el ancho a gallery para que se vea estético..
        btnAbout.setTamanio(anchoBoton+10, altoBoton);
        logo.setTamanio(anchoLogo-10, altoLogo);

        batch = new SpriteBatch();
    }


    //Obtener las texturas cargadas en la pantallaCargando.java
    private void crearObjetos(){
        AssetManager assetManager = principal.getAssetManager();   // Referencia al assetManager

        texturaFondo = assetManager.get("fondoMenu.png");
        texturaBtnPlay =  assetManager.get("botonPlay.png");
        texturaBtnInstructions =  assetManager.get("botonInstructions.png");
        texturaBtnGallery = assetManager.get("botonGallery.png");
        texturaBtnAbout =  assetManager.get("botonAbout.png");
        texturaLogo = assetManager.get("Title.png");
        //sonido
        this.efectoClick = assetManager.get("sonidoVentana.wav");
        this.efectoClick.setVolume(70,70);

    }


    //Metodo para cargar la musica del juego
    private void cargarAudioJuego() {
        //Musica de fondo
        if(!this.banderaCancionJuego) { //si es falsa, entonces no se esta escuchando nada....
            this.musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musicaJuegoPrincipal.wav"));
            this.musicaFondo.setLooping(true);
            this.musicaFondo.setVolume(70);
            this.musicaFondo.play();
        }
    }


    //Metodo de la clase Screen, ya fue implementado
    @Override//Recordar que render es automatico..
    public void render(float delta) { //ese delta es el tiempo, 60fps entonces el float seria 1/60
        //Borrar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0); //Con este color vas a borrar
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Instruccion para borrar

        batch.setProjectionMatrix(camara.combined); //Con este ajustas el batch...,en este caso, el boton, se ajusta.

        leerEntrada(); //Pata revisar touch
        //fondo.getSprite().rotate(.1f);
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

            switch ( verifcarBoton(x,y)){
                //en la pantalla cargando determinara que cargar... se manda el numero correspondiente para saber que se va cargar en esa clase..
                case 1:
                    Gdx.app.log("leerEntrada", "HAY UN TAP EN PLAY!"); //cuando le apretan va decir esto..
                    this.efectoClick.play();
                    principal.setScreen(new PantallaCargando(1,principal,true));  //se manda true porque ya esta la cancion reproduciendose
                    break;
                case 2:
                    Gdx.app.log("leerEntrada", "HAY UN TAP EN INSTRUCCIONES!"); //cuando le apretan va decir esto..
                   // this.btnInstructions.setAlfa(.5f);  //al presionarse se hace transparente
                    this.efectoClick.play();
                    principal.setScreen(new PantallaCargando(2,principal,true));


                    break;
                case 3:
                    Gdx.app.log("leerEntrada", "HAY UN TAP EN GALLERIA!"); //cuando le apretan va decir esto..
                    //this.btnGallery.setAlfa(.5f);  //al presionarse se hace transparente
                    this.efectoClick.play();
                    principal.setScreen(new PantallaCargando(3,principal,true));

                    break;
                case 4:
                    //this.btnAbout.setAlfa(.5f);
                    Gdx.app.log("leerEntrada", "HAY UN TAP! EN ABOUT"); //cuando le apretan va decir esto..
                    this.efectoClick.play();
                    principal.setScreen(new PantallaCargando(4,principal,true));
                    break;
                default:
                    break;
            }
        }
    }

    private int verifcarBoton(float x, float y) {
        Sprite spritebtnPlay = btnPLay.getSprite();
        Sprite spritebtnInstructions = btnInstructions.getSprite();
        Sprite spritebtnGallery = btnGallery.getSprite();
        Sprite spritebtnAbout = btnAbout.getSprite();
        //Verificar que la x y La y esten dentro de las dimensiones del sprite...
        /*caso 1 = boton play, caso 2 = boton instrucciones, caso 3 = boton galería, caso 4 = boton acercaDe, caso0 = ningunBoton*/
        if(x>=spritebtnPlay.getX() && x<=spritebtnPlay.getX()+spritebtnPlay.getWidth() && y>=spritebtnPlay.getY() && y<=spritebtnPlay.getY()+spritebtnPlay.getHeight()){
            return 1;
        }
        else if(x>=spritebtnInstructions.getX() && x<=spritebtnInstructions.getX()+spritebtnInstructions.getWidth() && y>=spritebtnInstructions.getY() && y<=spritebtnInstructions.getY()+spritebtnInstructions.getHeight()){
            return 2;
        }
        else if(x>=spritebtnGallery.getX() && x<=spritebtnGallery.getX()+spritebtnGallery.getWidth() && y>=spritebtnGallery.getY() && y<=spritebtnGallery.getY()+spritebtnInstructions.getHeight()){
            return 3;
        }
        else if(x>=spritebtnAbout.getX() && x<=spritebtnAbout.getX()+spritebtnAbout.getWidth() && y>=spritebtnAbout.getY() && y<=spritebtnAbout.getY()+spritebtnAbout.getHeight()){
            return 4;
        }
        else{
           return 0;
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







    //estos metodos sde ejecutan cuando se pasa a la otra pantalla


    @Override
    public void hide() {

    }

    @Override //Se ejecuta cuanto se pasa a otra pantalla, aqui se deben de liberar los recursos **
    public void dispose() {

        principal.dispose();
        batch.dispose();
        //Fondo
        texturaFondo.dispose();
        //Botones
        texturaBtnPlay.dispose();
        texturaBtnInstructions.dispose();
        texturaBtnGallery.dispose();
        texturaBtnAbout.dispose();
        //LOGO
        texturaLogo.dispose();
        //sonido
        this.efectoClick.dispose();
        //NOTA: LA MUSICA DE FONDO NO SE LIBER, YA QUE QUEREMOS QUE SE SIGA ESCUCHANDO..
    }
}
