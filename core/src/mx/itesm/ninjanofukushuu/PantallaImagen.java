package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/*
 PantallaImagen
 Autor: Irvin Emmanuel Trujillo Díaz
 Descripcion: Pantalla que muestra imagenes, botones para mover las imagenes y un boton accion(regresar o jugar, dependiendo de donde se encuentre el usuario) ...
 Profesor: Roberto Martinez Román
 */
public class PantallaImagen implements Screen {

    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;

    private int indicadorImagenes; //Para saber que texturas se van a cargar...
    /*de 1 a 3: son las texturas de la galeria de arte, 4 a 6 son de la historia..
    1: galeria nivel tierra
    2: galeria nivel agua
    3: galeria nivel fuego
    4: historia nivel tierra
    5: historia nivel agua
    6: historia nivel fuego
    */


    //Imagenes que son para mostrar el arte, son 6 imagenes por cada seccion (arte)...
    //en caso de que sea para mostrar la historia, son 5 imagenes, se va determinar que texturas cargar con el int indicadorImagenes...
    private Fondo imagen;
    private Texture  texturaImagen;
    //las texturas van a cambiar cuando el usuario presione  <- y ->

    //Botones para que el usuario pueda mover las imagenes...
    private Boton btnDerecha;
    private Texture texturaBtnDerecha;

    private Boton btnIzquierda;
    private Texture texturaBtnIzquierda;


    private Boton btnAccion;
    // la textura cargada y el comportamiento del boton dependera del indicadorImagenes
    //de 1 a 3: son las texturas de la galeria de arte por lo que la textura es la del boton regresar
    //4 a 6 son de la historia por lo que el boton dirá "play"...
    private Texture texturaBtnAccion;

    private AssetManager assetManager;


    //tamañoBotones
    private static int TAMANIO_BOTON_FLECHAS_ANCHO = 151;
    private static int TAMANIO_BOTON_FELCHAS_ALTO = 141;
    private static int TAMANIO_BOTON = 200;//BOTON RETURN-ACCION



    //Efectos
    private Sound efectoHoja, efectoClick;

    public PantallaImagen(Principal principal, int indicadorImagenes) {
        this.principal = principal;
        //Indicador de imagenes que va servir para saber que imagenes se van a mostrar dependiendo de donde se accede...
        this.indicadorImagenes = indicadorImagenes;
    }


    @Override
    public void show() {
        //Crear camara
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();

        vista = new StretchViewport(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO,camara);

        this.crearObjetos(); //se crean los objetos depsues de haber cargado de pantalla juego..

        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        //Crear fondo/imagenes que se van a mostra
        this.imagen = new Fondo(this.texturaImagen);


        //Batch
        this.imagen.getSprite().setCenter(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2);
        this.imagen.getSprite().setOrigin(1500 / 2, 1500 / 2);
        batch = new SpriteBatch();
    }



    //crea los objetos de textura y audio
    private void crearObjetos(){
        assetManager = principal.getAssetManager();   // Referencia al assetManager



        // aqui se evalua si se debe de mostrar la primera  imagen de la historia o de la galeria de arte...
        switch(this.indicadorImagenes){
            case 1: //arte de nivel tierra
                this.texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/arteMenu1.png");
                break;
            case 2: //arte de nivel agua
                this.texturaImagen = assetManager.get("imagenesGaleriaArte/arteAgua/arteMenu2.jpg");
                break;
            case 3: //arte de nivel de fuego
                this.texturaImagen = assetManager.get("imagenesGaleriaArte/arteFuego/vidaArte3.png");
                break;
            case 4: //primer imagen de la historia de nivel de tierra
                this.texturaImagen = assetManager.get("seleccionNivel/recursosNivelTierra/historiaTierra1.png");
                break;
            case 5: //primer imagen de la historia de nivel de agua
                this.texturaImagen = assetManager.get("seleccionNivel/recursosNivelAgua/historiaAgua1.png");
                break;
            case 6:
                this.texturaImagen = assetManager.get("seleccionNivel/recursosNivelAgua/historiaFuego1.png");
                break; //primer imagen de la historia de nivel del fuego

        }


        // la textura cargada y el comportamiento del botonAccion dependera del indicadorImagenes
        //de 1 a 3: son las texturas de la galeria de arte por lo que la textura es la del boton regresar
        //4 a 6 son de la historia por lo que el boton dirá "play"...
        if(this.indicadorImagenes<4)
            this.texturaBtnAccion = assetManager.get("return.png");

        else
            this.texturaBtnAccion = assetManager.get("botonPlay.png");


        this.efectoClick = assetManager.get("sonidoVentana.wav");
        this.efectoHoja = assetManager.get("seleccionNivel/sonidosGameplay/efectoPergamino.wav"); //para cuando cambias de hoja..



        //botones para mover las imagenes..
        this.texturaBtnIzquierda =  assetManager.get("seleccionNivel/botonesFlechas/izquierdaImagenes.png");
        this.texturaBtnDerecha = assetManager.get("seleccionNivel/botonesFlechas/derechaImagenes.png");

        //creandoBotones

        this.btnAccion = new Boton(texturaBtnAccion);
        this.btnAccion.setPosicion(Principal.ANCHO_MUNDO-200, Principal.ALTO_MUNDO * 1 / 5 - 150);
        this.btnAccion.setAlfa(0.7f); // Un poco de transparencia
        this.btnAccion.setTamanio(PantallaImagen.TAMANIO_BOTON, PantallaImagen.TAMANIO_BOTON);


        btnIzquierda = new Boton(texturaBtnIzquierda);
        btnIzquierda.setPosicion(50 , 16 / 5 +30);
        btnIzquierda.setAlfa(0.7f); // Un poco de transparencia
        btnIzquierda.setTamanio(PantallaImagen.TAMANIO_BOTON_FLECHAS_ANCHO , PantallaImagen.TAMANIO_BOTON_FELCHAS_ALTO);

        btnDerecha = new Boton(texturaBtnDerecha);
        btnDerecha.setPosicion(Principal.ANCHO_MUNDO / 2 + 200, 16 / 5+20);
        btnDerecha.setAlfa(0.7f); // Un poco de transparencia
        btnDerecha.setTamanio(PantallaImagen.TAMANIO_BOTON_FLECHAS_ANCHO, PantallaImagen.TAMANIO_BOTON_FELCHAS_ALTO );
    }

    @Override
    public void render(float delta) {
        //Borrar la pantalla
        Gdx.gl.glClearColor(.5f, 0, 0, 0); //color rojo oscuro
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camara.combined);
        //DIBUJAR
        batch.begin();
        this.imagen.render(batch);
        this.btnAccion.render(batch);
        this.btnIzquierda.render(batch);
        this.btnDerecha.render(batch);
        batch.end();
    }

     /*REVISION DE TOUCH*/
    //se elimino metodo leer entrada, ahora eso es de touchDown
    //Se eliminó el método de verificarBoton... ahora se usa touchUp, touchDown...

    //Clase utilizada para manejar los eventos de touch en la pantalla
    public class ProcesadorEntrada extends InputAdapter {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla virtual
        //son 6 imagenes en la galeria de arte y 5 imagenes para contar la historia, por lo que habra un contador para saber que imagenes colocar..
        int numeroImagen = 1;

        //Banderas que nos sirven para saber si el boton está transparente y  pequeño o  no además que nos ayudan para la segunda condicion de touchUp.
        private boolean banderaBotonDerecha = false, banderaBotonIzquierda = false, banderaBotonAccion = false;
        /*
        Se ejecuta cuando el usuario pone un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse
         */

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            if (btnIzquierda.contiene(x,y)){
                btnIzquierda.setAlfa(.5f);
                btnIzquierda.setTamanio(PantallaImagen.TAMANIO_BOTON_FLECHAS_ANCHO, PantallaImagen.TAMANIO_BOTON_FELCHAS_ALTO-5); //Lo hago más pequeño
                this.banderaBotonIzquierda = true;

            }

            if (btnDerecha.contiene(x,y)){
                btnDerecha.setAlfa(.5f);
                btnDerecha.setTamanio(PantallaImagen.TAMANIO_BOTON_FLECHAS_ANCHO, PantallaImagen.TAMANIO_BOTON_FELCHAS_ALTO-5); //Lo hago más pequeño
                this.banderaBotonDerecha = true;

            }

            if (btnAccion.contiene(x,y)){
                btnAccion.setAlfa(.5f);
                btnAccion.setTamanio(PantallaImagen.TAMANIO_BOTON, PantallaImagen.TAMANIO_BOTON-5); //Lo hago más pequeño
                this.banderaBotonAccion = true;

            }

            return true;    // Indica que ya procesó el evento
        }

        //Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            // Preguntar si las coordenadas son de cierto lugar de donde se quito el dedo

            if (btnIzquierda.contiene(x,y) && this.banderaBotonIzquierda){
                //banderaPergaminosTierra indica que el boton esta desbloqueado porque el usuario ya junto los 3 pergaminos.. en el nivel tierra
                // si no entra al if el boton está bloqueado, no hace ningun sonido
                banderaBotonIzquierda = false;
                btnIzquierda.setAlfa(1);
                btnIzquierda.setTamanio(PantallaImagen.TAMANIO_BOTON_FLECHAS_ANCHO, PantallaImagen.TAMANIO_BOTON_FELCHAS_ALTO); //tamaño original
                efectoHoja.play(); //efecto de sonido
                numeroImagen -= 1; //para cambiar de imagen..
                if(numeroImagen<=0){ //el usuario esta en la primer imagen, no debe de avanzar..
                   numeroImagen = 1;
                }
                else{ //el usuario no esta en la primer imagen, hay que mover la imagen...
                //como son 5 imagenes para la historia y 6 para la galera de arte, hay que comparar el indicadorImagenes, recordad que si es menor que 4, se esta viendo la galeria de arte...

                    if(indicadorImagenes<4) {
                    /*
                    1: galeria nivel tierra
                    2: galeria nivel agua
                    3: galeria nivel fuego
                    4: historia nivel tierra
                    5: historia nivel agua
                    6: historia nivel fuego*/
                       switch(indicadorImagenes) {
                           case 1: //galeria de arte nivel tierra

                           switch (numeroImagen) {
                               case 1:
                                   texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/arteMenu1.png");
                                   imagen.cambiarFondo(texturaImagen);
                                   break;
                               case 2:
                                   texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/hatakuArte.png");
                                   imagen.cambiarFondo(texturaImagen);
                                   break;
                               case 3:
                                   texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/mapaArte1.png");
                                   imagen.cambiarFondo(texturaImagen);
                                   break;
                               case 4:
                                   texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/mapaArte2.png");
                                   imagen.cambiarFondo(texturaImagen);
                                   break;
                               case 5:
                                   texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/samuraiArte.png");
                                   imagen.cambiarFondo(texturaImagen);
                                   break;
                               default:break;
                           }
                               break;

                           case 2: // galeria de arte nivel de agua
                               switch (numeroImagen) {
                                   case 1:
                                       texturaImagen = assetManager.get("imagenesGaleriaArte/arteAgua/arteMenu2.jpg");
                                       imagen.cambiarFondo(texturaImagen);
                                       break;
                                   case 2:
                                       texturaImagen = assetManager.get("imagenesGaleriaArte/arteAgua/arteMenu3.png");
                                       imagen.cambiarFondo(texturaImagen);
                                       break;
                                   case 3:
                                       texturaImagen = assetManager.get("imagenesGaleriaArte/arteAgua/pocionArte.png");
                                       imagen.cambiarFondo(texturaImagen);
                                       break;
                                   case 4:
                                       texturaImagen = assetManager.get("imagenesGaleriaArte/arteAgua/vidaArte2.png");
                                       imagen.cambiarFondo(texturaImagen);
                                       break;
                                   case 5:
                                       texturaImagen = assetManager.get("imagenesGaleriaArte/arteAgua/vidaArte2Color.png");
                                       imagen.cambiarFondo(texturaImagen);
                                       break;
                                   default:break;
                               }
                               break;
                           case 3: //galeria de arte nivel del fuego
                               break;
                           default:break;
                       }
                   }

                    else{ //entonces son las imagenes de la historia, son 5 imagenes entonces...
                        switch(indicadorImagenes) {
                            case 4: // historia nivel de tierra
                                switch(numeroImagen){
                                    case 1:
                                        texturaImagen = assetManager.get("seleccionNivel/recursosNivelTierra/historiaTierra1.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 2:
                                        texturaImagen = assetManager.get("seleccionNivel/recursosNivelTierra/historiaTierra2.jpg");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 3:
                                        texturaImagen = assetManager.get("seleccionNivel/recursosNivelTierra/historiaTierra3.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 4:
                                        texturaImagen = assetManager.get("seleccionNivel/recursosNivelTierra/historiaTierra4.jpg");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case 5: //historia de nivel de agua
                                /*switch(numeroImagen){
                                    case 1:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/arteMenu1.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 2:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/hatakuArte.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 3:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/mapaArte1.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 4:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/mapaArte2.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    default:break;
                                }*/
                                break;
                            case 6: //historia de nivel de fuego
                                /*switch(numeroImagen){
                                    case 1:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/arteMenu1.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 2:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/hatakuArte.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 3:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/mapaArte1.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 4:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/mapaArte2.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    default:break;
                                }*/
                                break;
                            default:break;
                        }
                   }
                }
            }

            else if (btnDerecha.contiene(x,y) && this.banderaBotonDerecha ){
                // bandera agua indica que el boton esta desbloqueado porque el usuario ya junto los 3 pergaminos.. en el nivel agua
                // si no entra al if el boton está bloqueado, no hace ningun sonido
                banderaBotonDerecha= false;
                btnDerecha.setAlfa(1);
                btnDerecha.setTamanio(PantallaImagen.TAMANIO_BOTON_FLECHAS_ANCHO, PantallaImagen.TAMANIO_BOTON_FELCHAS_ALTO);  //tamaño original
                efectoHoja.play(); //efecto de sonido
                //cambiando imagen
                numeroImagen += 1; //para cambiar de imagen..
                boolean banderaCambiarImagen = true;
                //recordar que si el usuario esta viendo la historia son 5 imagenes, si no, son 6...
                if(indicadorImagenes<4){ //esta viendo la galeria de arte son 6 imagenes entonces.. .
                    if(numeroImagen>=7){ //el usuario esta en la ultima hoja no debe de avanzar.
                        numeroImagen = 6;
                        banderaCambiarImagen = false;
                    }
                }
                else{ //entonces el usuario esta viendo la historia, son 5 imagenes..
                    if(numeroImagen>=6){ //el usuario esta en la ultima hoja no debe de avanzar.
                        numeroImagen = 5;
                        banderaCambiarImagen = false;
                    }
                }


                if(banderaCambiarImagen){ //el usuario no esta en la ultima imagen, hay que moverla..
                    //como son 5 imagenes para la historia y un numero diferente  para la galera de arte, hay que comparar el indicadorImagenes, recordar que si es menor que 4, se esta viendo la galeria de arte...
                    if(indicadorImagenes<4) {
                    /*
                    1: galeria nivel tierra
                    2: galeria nivel agua
                    3: galeria nivel fuego
                    4: historia nivel tierra
                    5: historia nivel agua
                    6: historia nivel fuego*/
                        switch(indicadorImagenes) {
                            case 1: //galeria de arte nivel tierra

                                switch (numeroImagen) {
                                    case 1:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/arteMenu1.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 2:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/hatakuArte.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 3:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/mapaArte1.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 4:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/mapaArte2.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 5:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/samuraiArte.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 6:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/vidaArte.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    default:break;
                                }
                                break;

                            case 2: // galeria de arte nivel de agua
                                switch (numeroImagen) {
                                    case 1:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteAgua/arteMenu2.jpg");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 2:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteAgua/arteMenu3.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 3:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteAgua/pocionArte.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 4:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteAgua/vidaArte2.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 5:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteAgua/vidaArte2Color.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    /*case 6:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/vidaArte.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;*/
                                    default:break;
                                }
                                break;
                            case 3: //galeria de arte nivel del fuego
                                break;
                            default:break;
                        }
                    }

                    else{ //entonces son las imagenes de la historia, son 5 imagenes entonces...
                        switch(indicadorImagenes) {
                            case 4: // historia nivel de tierra
                                switch(numeroImagen){
                                    case 1:
                                        texturaImagen = assetManager.get("seleccionNivel/recursosNivelTierra/historiaTierra1.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 2:
                                        texturaImagen = assetManager.get("seleccionNivel/recursosNivelTierra/historiaTierra2.jpg");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 3:
                                        texturaImagen = assetManager.get("seleccionNivel/recursosNivelTierra/historiaTierra3.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 4:
                                        texturaImagen = assetManager.get("seleccionNivel/recursosNivelTierra/historiaTierra4.jpg");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 5:
                                        texturaImagen = assetManager.get("seleccionNivel/recursosNivelTierra/historiaTierra5.jpg");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;

                                    default:
                                        break;
                                }
                                break;
                            case 5: //historia de nivel de agua
                                /*switch(numeroImagen){
                                    case 1:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/arteMenu1.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 2:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/hatakuArte.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 3:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/mapaArte1.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 4:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/mapaArte2.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    default:break;
                                }*/
                                break;
                            case 6: //historia de nivel de fuego
                                /*switch(numeroImagen){
                                    case 1:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/arteMenu1.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 2:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/hatakuArte.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 3:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/mapaArte1.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    case 4:
                                        texturaImagen = assetManager.get("imagenesGaleriaArte/arteTierra/mapaArte2.png");
                                        imagen.cambiarFondo(texturaImagen);
                                        break;
                                    default:break;
                                }*/
                                break;
                            default:break;
                        }
                    }
                }

            }




            else if (btnAccion.contiene(x,y) && this.banderaBotonAccion){

                efectoClick.play(); //efecto de sonido
                banderaBotonAccion = false;
                btnAccion.setAlfa(1);
                btnAccion.setTamanio(PantallaImagen.TAMANIO_BOTON, PantallaImagen.TAMANIO_BOTON); //tamaño orginal

                //aqui definimos el comportamiento del boton, ya que puede ser el boton play o el return entonces:
                if(indicadorImagenes<4)//es el boton return entonces...
                    principal.setScreen(new PantallaGaleria(principal,PantallaCargando.banderaArteTierra,PantallaCargando.banderaArteAgua,PantallaCargando.banderaArteFuego));// regresas a la galeria...

                else //es el boton play
                    principal.setScreen(new PantallaJuego(principal)); //nivel de tierra..
            }

            else{ //entonces el usuario despego el dedo de la pantalla en otra parte que no sean los botones...
                // se le quita la transparencia y se regresa a su tamaño original
                //niveles bloqueados
                banderaBotonIzquierda = false;
                btnIzquierda.setAlfa(1);
                btnIzquierda.setTamanio(PantallaImagen.TAMANIO_BOTON_FLECHAS_ANCHO, PantallaImagen.TAMANIO_BOTON_FELCHAS_ALTO); //tamaño original
                banderaBotonDerecha = false;
                btnDerecha.setAlfa(1);
                btnDerecha.setTamanio(PantallaImagen.TAMANIO_BOTON_FLECHAS_ANCHO, PantallaImagen.TAMANIO_BOTON_FELCHAS_ALTO); //tamaño original
                banderaBotonAccion = false;
                btnAccion.setAlfa(1);
                btnAccion.setTamanio(PantallaImagen.TAMANIO_BOTON, PantallaImagen.TAMANIO_BOTON); //tamaño orginal
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
        texturaImagen.dispose();
        texturaBtnAccion.dispose();
        texturaBtnIzquierda.dispose();
        texturaBtnDerecha.dispose();
        efectoClick.dispose();
        efectoHoja.dispose();

    }


}
