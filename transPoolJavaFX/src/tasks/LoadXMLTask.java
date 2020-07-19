package tasks;

import controller.MainController;
import controller.exceptions.*;
import javafx.concurrent.Task;

import javax.xml.bind.JAXBException;
import java.io.File;

public class LoadXMLTask extends Task<Boolean> {
    private File file;
    private MainController mainController;
    private final int SLEEP_TIME = 300;


    public LoadXMLTask(File file, MainController mainController)
    {
        this.file = file;
        this.mainController = mainController;
    }

    @Override
    protected Boolean call() throws Exception {

        updateProgress(0,1);

        updateMessage("Loading file");
        updateProgress(0.3,1);
        sleepForAWhile(SLEEP_TIME);

        updateMessage("Validating file data");
        updateProgress(0.6,1);
        sleepForAWhile(SLEEP_TIME);

//        try {
            this.mainController.loadInfoFromXML(this.file);
//        }catch(RuntimeException e)
//        {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }

        updateMessage("Loading data to system");
        updateProgress(1,1);
        sleepForAWhile(SLEEP_TIME);

//        updateMessage("Creating the map");
//        updateProgress(1,1);
//        sleepForAWhile(SLEEP_TIME);

        //this.mainController.buildMap();

        updateMessage("Done");

        return true;
    }

    public static void sleepForAWhile(long sleepTime) {
        if (sleepTime != 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
