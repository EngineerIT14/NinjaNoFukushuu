package mx.itesm.ninjanofukushuu;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/*
Desarrollador: Irvin Emmanuel Trujillo Díaz y Javier Antonio García Roque
Descripcion: clase representa un objeto juego.
Profesor: Roberto Martinez Román.
*/


public class Principal extends Game  {

    public static final float ANCHO_MUNDO = 1280;
    public static final float ALTO_MUNDO = 720;
    public SpriteBatch batch; //Se utiliza este batch en PantallaJuego, por esta razón es pública.




    @Override
    public void create () { //Se crea la pantalla
        batch = new SpriteBatch();
        setScreen(new PantallaMenu(this)); //Se manda el objeto...
    }

    @Override
    public void render () {  //Le delega el render a la pantalla necesaria
        super.render();

    }






}