package echo;

/**
 * Created by Mark on 22/02/2017.
 */
public class EchoApp{
    //TODO stop the button working when it is in answer mode

    //TODO testing
    //TODO more javadoc
    public static void main(String[] argc){
        EchoApp EA = new EchoApp();
        EA.createEcho();
    }

    public Echo createEcho() {
        return new Echo(this);
    }

    public Echo restartEcho() {
        return new Echo(this);
    }
}
