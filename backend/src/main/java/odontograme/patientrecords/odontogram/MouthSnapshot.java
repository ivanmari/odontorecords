package odontograme.patientrecords.odontogram;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



//Represents the current status of the mouth at a particular point in time. After practices were executed and Statuses forced on the teeth. It is used to create the view of the odontogram.
public class MouthSnapshot {

    /*Takes an encoded string with teeth status
    * Encoding is as follows:
    *
    *   {piece number ISO 3950 notation}{Stat}[letters faces affected]
    *
    *   Stat is a one letter code:
    *
    *   Healthy - H
    *   Removed - X
    *   Filling - F
    *   Bridge  - B
    *   Crown   - C
    *   Implant - I
    *   Root C. - R
    *
    *   Tooth faces names are written with the first letter (See enum ToothFaceName)
    *
    *  Eg.:
    *
    * Tooth# 48 with Filling in oclusal: 48FO
    * Tooth# 48 with Filling in vestibular and messial: 48FVM
    * Tooth# 17 removed: 17X
    * Tooth# 11 Healthy: 11H
    * Tooth# 34 Implant: 34I
    * Tooth# 41-44 Bridge: 41BS,42BI,43BI,44BE
    * where BS means Bridge Start, BI means Bridge Intermediate and BE means Bridge End
    *
    * */
    public MouthSnapshot(Iterable<String> encodedStatusList)
    {
        //Parses comma separated string into list
        //Arrays.asList(encodedStatus.split("\\s*,\\s*"));

        //Obtain tooth# ^\d\d

        for( String element : encodedStatusList)
        {
            String mydata = "some string with 'the data i want' inside";
            Pattern pattern = Pattern.compile("'(.*?)'");
            Matcher matcher = pattern.matcher(mydata);
            if (matcher.find())
            {
                System.out.println(matcher.group(1));
            }

        }
    }

    private List<String> teethStatus;
    private Date timestamp;
}
