package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
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
 Pantalla galeria donde se mostrara el arte desbloqueado mediante los pergaminos...
 Autores: Javier e Irvin Emmanuel Trujillo Díaz
 preferencias para guardar las banderas.. diccionario objeto
 */
public class PantallaGaleria implements Screen {
    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;
    //Fondo
    private Fondo fondo;
    private Texture texturaFondo;
    //Botones
    private Boton btnGaleriaTierra;
    private Texture texturaGaleriaTierraDesbloqueada;
    private Texture texturaGaleriaTierraBloqueada;
    private boolean banderaTierra; //Bandera para saber que textura y el comprotamiendo del boton hacer...

    private Boton btnGaleriaAgua;
    private Texture texturaGaleriaAguaDesbloqueada;
    private Texture texturaGaleriaAguaBloqueada;
    private boolean banderaAgua; //Bandera para saber que textura y el comprotamiendo del boton hacer...

    private Boton btnGaleriaFuego;
    private Texture texturaGaleriaFuegoDesbloqueada;
    private Texture texturaGaleriaFuegoBloqueada;
    private boolean banderaFuego; //Bandera para saber que textura y el comprotamiendo del boton hacer...

    private Boton btnRegresar;
    private Texture texturaRegresar;

    private static final int anchoBoton = 180, altoBoton = 200; //anchoBoton1 = 480 , altoBoton1 = 160;
    //Efectos
    private Sound efectoClick;

    public PantallaGaleria(Principal principal, boolean banderaTierra, boolean banderaAgua, boolean banderaFuego) {
        this.principal = principal;
        //LAS BANDERAS SON FUDAMENTALES YA QUE INDICAN QUE TEXTURA CARGAR (BLOQUEDO O DESBLOQUEADO) Y EL COMPROTAMIENTO DEL BOTON...
        this.banderaTierra = banderaTierra;
        this.banderaAgua = banderaAgua;
        this.banderaFuego = banderaFuego;
    }

    @Override
    public void show() {
        //Crear camara
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();

        vista = new StretchViewport(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO,camara);
        this.crearObjetos();
        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        //Crear fondo
        fondo = new Fondo(texturaFondo);

        //botones
        //NOTA: AQUI  VA DEPENDER DE LA BANDERA, PARA SABER QUE TEXTURA CARGAR...
        if(banderaTierra) //entonces el usuario ya junto los 3 pergaminos del nivel tierra
            this.btnGaleriaTierra = new Boton(texturaGaleriaTierraDesbloqueada);
        else
           this.btnGaleriaTierra =  new Boton(texturaGaleriaTierraBloqueada);

        btnGaleriaTierra.setPosicion(Principal.ANCHO_MUNDO / 4 - 100, Principal.ALTO_MUNDO * 2 / 3 - 200);

        if(banderaAgua) //entonces el usuario ya junto los 3 pergaminos del nivel agua
            this.btnGaleriaAgua = new Boton(texturaGaleriaAguaDesbloqueada);
        else
            this.btnGaleriaAgua = new Boton(texturaGaleriaAguaBloqueada);

        btnGaleriaAgua.setPosicion(Principal.ANCHO_MUNDO / 2 - 100, Principal.ALTO_MUNDO * 2 / 3 - 200);



        if(banderaFuego)  //entonces el usuario ya junto los 3 pergaminos del nivel fuego
            this.btnGaleriaFuego =  new Boton(texturaGaleriaFuegoDesbloqueada);
        else
            this.btnGaleriaFuego =  new Boton(texturaGaleriaFuegoBloqueada);

        btnGaleriaFuego.setPosicion(Principal.ANCHO_MUNDO * 3 / 4 - 100, Principal.ALTO_MUNDO * 2 / 3 - 200);

        btnRegresar = new Boton(texturaRegresar);

        //ajustando tamaño y posiciones del boton reresar...
        btnRegresar.setPosicion(Principal.ANCHO_MUNDO * 7 / 8 , Principal.ALTO_MUNDO * 1 / 5 -150);
        btnRegresar.setTamanio(anchoBoton, altoBoton );
        //Batch
        fondo.getSprite().setCenter(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2);
        fondo.getSprite().setOrigin(1500 / 2, 1500 / 2);
        batch = new SpriteBatch();
    }



    //crea los objetos de textura y audio
    private void crearObjetos(){
        AssetManager assetManager = principal.getAssetManager();   // Referencia al assetManager
        //Fondo
        this.texturaFondo = assetManager.get("M.jpg");
        this.texturaGaleriaTierraDesbloqueada = assetManager.get("galeriaEarth.png");
        this.texturaGaleriaTierraBloqueada =  assetManager.get("galeriaEarthLock.png");
        this.texturaGaleriaAguaDesbloqueada = assetManager.get("galeriaWater.png");
        this.texturaGaleriaAguaBloqueada =  assetManager.get("galeriaWaterLock.png");
        this.texturaGaleriaFuegoDesbloqueada =  assetManager.get("galeriaFire.png");
        this.texturaGaleriaFuegoBloqueada =  assetManager.get("galeriaFireLock.png");
        this.texturaRegresar = assetManager.get("return.png");
        this.efectoClick = assetManager.get("sonidoVentana.wav");
    }

    @Override
    public void render(float delta) {
        //Borrar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camara.combined);
        //DIBUJAR
        batch.begin();
        fondo.render(batch);
        this.btnGaleriaTierra.render(batch);
        this.btnGaleriaAgua.render(batch);
        this.btnGaleriaFuego.render(batch);
        btnRegresar.render(batch);
        batch.end();
    }

     /*REVISION DE TOUCH*/
    //se elimino metodo leer entrada, ahora eso es de touchDown
    //Se eliminó el método de verificarBoton... ahora se usa touchUp, touchDown...

    //Clase utilizada para manejar los eventos de touch en la pantalla
    public class ProcesadorEntrada extends InputAdapter {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla virtual

        private boolean banderaBotonGaleriaTierra = false, banderaBotonGaleriaAgua = false, banderaBotonGaleriaFuego = false,  banderaBotonRegresar = false;
        /*
        Se ejecuta cuando el usuario pone un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse
         */

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            if (btnGaleriaTierra.contiene(x,y)){
                btnGaleriaTierra.setAlfa(.5f);
                btnGaleriaTierra.setTamanio(300, 285); //Lo hago más pequeño
                this.banderaBotonGaleriaTierra = true;
            }

            if (btnGaleriaAgua.contiene(x,y)){
                btnGaleriaAgua.setAlfa(.5f);
                btnGaleriaAgua.setTamanio(300, 285); //Lo hago más pequeño
                this.banderaBotonGaleriaAgua = true;
            }

            if (btnGaleriaFuego.contiene(x,y)){
                btnGaleriaFuego.setAlfa(.5f);
                btnGaleriaFuego.setTamanio(300, 285); //Lo hago más pequeño
                this.banderaBotonGaleriaFuego = true;
            }


            if (btnRegresar.contiene(x,y)){
                btnRegresar.setAlfa(.5f);
                btnRegresar.setTamanio(anchoBoton, altoBoton - 15); //Lo hago más pequeño
                this.banderaBotonRegresar = true;
            }



            return true;    // Indica que ya procesó el evento
        }

        //Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            // Preguntar si las coordenadas son de cierto lugar de donde se quito el dedo

            if (btnGaleriaTierra.contiene(x,y) && this.banderaBotonGaleriaTierra && banderaTierra ){
                //banderaTierra indica que el boton esta desbloqueado porque el usuario ya junto los 3 pergaminos.. en el nivel tierra
                // si no entra al if el boton está bloqueado, no hace ningun sonido
                banderaBotonGaleriaTierra = false;
                btnGaleriaTierra.setAlfa(1);
                btnGaleriaTierra.setTamanio(300, 300); //tamaño original
                efectoClick.play(); //efecto de sonido
            }

            else if (btnGaleriaAgua.contiene(x,y) && this.banderaBotonGaleriaAgua && banderaAgua){
                // bandera agua indica que el boton esta desbloqueado porque el usuario ya junto los 3 pergaminos.. en el nivel agua
                // si no entra al if el boton está bloqueado, no hace ningun sonido
                banderaBotonGaleriaAgua = false;
                btnGaleriaAgua.setAlfa(1);
                btnGaleriaAgua.setTamanio(300, 300); //tamaño original
                efectoClick.play(); //efecto de sonido

            }

            else if (btnGaleriaFuego.contiene(x,y) && this.banderaBotonGaleriaFuego && banderaFuego ){
                //banderaFuego indica que el boton esta desbloqueado porque el usuario ya junto los 3 pergaminos.. en el nivel fuego
                // si no entra al if el boton está bloqueado, no hace ningun sonido
                efectoClick.play(); //efecto de sonido
                banderaBotonGaleriaFuego = false;
                btnGaleriaFuego.setAlfa(1);
                btnGaleriaFuego.setTamanio(300, 300); //tamaño orginal

            }


            //en la pantalla cargando determinara que cargar... se manda el numero correspondiente para saber que se va cargar en esa clase..
            else if (btnRegresar.contiene(x, y) &&  this.banderaBotonRegresar) {
                efectoClick.play(); //efecto de sonido
                principal.setScreen(new PantallaCargando(0,principal,true));  //se manda true porque ya esta la cancion reproduciendose
            }

            else{ //entonces el usuario despego el dedo de la pantalla en otra parte que no sean los botones...
                // se le quita la transparencia y se regresa a su tamaño original
                banderaBotonGaleriaTierra = false;
                btnGaleriaTierra.setAlfa(1);
                btnGaleriaTierra.setTamanio(300, 300); //tamaño original
                banderaBotonGaleriaAgua = false;
                btnGaleriaAgua.setAlfa(1);
                btnGaleriaAgua.setTamanio(300, 300); //tamaño original
                banderaBotonGaleriaFuego = false;
                btnGaleriaFuego.setAlfa(1);
                btnGaleriaFuego.setTamanio(300, 300); //tamaño orginal
                banderaBotonRegresar = false;
                btnRegresar.setAlfa(1);
                btnRegresar.setTamanio(anchoBoton,altoBoton); //tamaño orginal
            }
            return true;    // Indica que ya procesó el evento
        }

        private void transformarCoordenadas(int screenX, int screenY) {
            // Transformar las coordenadas de la pantalla física a la cámara
            coordenadas.set(screenX, screenY, 0);
            camara.unproject(coordenadas); //camaraHUD es para los botones
            // Obtiene las coordenadas relativas a la pantalla virtual
            x = coordenadas.x;
            y = coordenadas.y;
        }
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
        texturaGaleriaTierraDesbloqueada.dispose();
        texturaGaleriaTierraBloqueada.dispose();
        texturaGaleriaAguaDesbloqueada.dispose();
        texturaGaleriaAguaBloqueada.dispose();
        texturaGaleriaFuegoBloqueada.dispose();
        texturaGaleriaFuegoDesbloqueada.dispose();

        texturaRegresar.dispose();
        texturaFondo.dispose();

    }


}
