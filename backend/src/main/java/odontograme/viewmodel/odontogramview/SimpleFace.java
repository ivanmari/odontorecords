package odontograme.viewmodel.odontogramview;

import org.springframework.context.annotation.ComponentScan;

/**
 * Created by immari on 12/22/2016.
 *
 * SimpleFace has a face name and a color. The color is obtained from the practice date and code.
 * If a practice code is associated to a filling color it is checked to see if it was done after or before the closing date.
 * Recent practices have priority over previous ones when deciding the color of a face. The face can be red, if it is recent or
 * blue if it was previous to the closing practice, or if it was a preexisting one.
 * If no practice was done to that face, its color defaults to white.
 */
@ComponentScan(basePackages = "odontograme.viewmodel.odontogramview")
class SimpleFace {

    public SimpleFace(String faceName, String color) {

        this.name = faceName;
        this.color = color;

    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    private String name;
    private String color;
}
