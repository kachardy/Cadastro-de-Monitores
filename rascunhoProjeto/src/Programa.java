import controllers.AuthController;
import services.CentralFacade;

public class Programa {

    public static void main(String[] args) {
        CentralFacade central = new CentralFacade();

        // O AuthController só precisa da central (facade) para iniciar o programa
        AuthController auth = new AuthController(central);
        auth.iniciar();
    }
}