/*
 GNU Lesser General Public License

 Copyright (c) 2017 Wimmer, Simon-Justus

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package chart.settings;

import chart.ChartIndicatorBox;
import chart.PropertiesManager;
import chart.parameters.IndicatorParameters;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IndicatorPopUpWindow extends PopupControl {

    private static IndicatorPopUpWindow open;

    private final PropertiesManager propertiesManager;
    private final String key;
    private final ChartIndicatorBox indicatorBox;


    private  BorderPane borderPane;
    @FXML private HBox hBox;
    @FXML private Label title;
    @FXML private Button btnSave;
    @FXML private Button btnDuplicate;
    @FXML private Button btnRemove;

    private Map<String, Property> nameValue = new HashMap<>();

    private IndicatorPopUpWindow(String key, ChartIndicatorBox indicatorBox){
        this.propertiesManager = indicatorBox.getPropertiesManager();
        this.key = key;
        this.indicatorBox = indicatorBox;
        borderPane = new BorderPane();
        setAutoHide(true);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(("fxml/charting-IndicatorPopUpWindow.fxml")));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(borderPane);
        borderPane.getStylesheets().add("fxml/charting-IndicatorPopUpWindow.css");

        try {
            fxmlLoader.load();
            String[] el = key.split("_");
            title.setText(el[0]);
            title.getStyleClass().add("title");
            if(el.length > 1){
                Map<String, String> parameters = propertiesManager.getParametersFor(key);

                VBox first = new VBox(3);
                Iterator<Map.Entry<String, String>> it = parameters.entrySet().iterator();
                if(!it.hasNext()){
                    hide();
                }
                // iterate over all parameters and add label and a value setter
                while(it.hasNext()){
                    first.setId("vbox");
                    Map.Entry entry = it.next();
                    String parameterName = (String) entry.getKey();
                    String parameterValue = (String) entry.getValue();
                    String parameterType = propertiesManager.getParameterType(key, parameterName);

                    Control valueSetter = IndicatorParameters.getComponent(parameterType, parameterValue);
                    valueSetter.setId(parameterName);
                    Property value = new SimpleObjectProperty();
                    if (valueSetter instanceof javafx.scene.control.TextField){
                        value.bind(((javafx.scene.control.TextField)valueSetter).textProperty());
                        nameValue.put(parameterName,value);
                    } else if (valueSetter instanceof ComboBox){
                        value.bind(((ComboBox)valueSetter).valueProperty());
                        nameValue.put(parameterName,value);
                    } else if( valueSetter instanceof ColorPicker){
                        value.bind(((ColorPicker)valueSetter).valueProperty());
                        nameValue.put(parameterName,value);
                    }
                    Label name = new Label(parameterName);
                    name.getStyleClass().add("plable");
                    first.getChildren().add(name);
                    first.getChildren().add(valueSetter);
                    first.getChildren().add(new Separator(Orientation.HORIZONTAL));
                    if(first.getChildren().size() > 13){
                        hBox.getChildren().add(first);
                        first = new VBox(3);
                    }
                }
                if(first.getChildren().size()>0){
                    hBox.getChildren().add(first);
                }

                addButtonAction(true, true, true);
                setOnAutoHide(event -> {hide();});
                getScene().setRoot(borderPane);
            } else {
                borderPane.setCenter(new Label("No settings available"));
                addButtonAction(false, false, true);
            }

        } catch (XPathExpressionException | IOException xpe){
            xpe.printStackTrace();
            borderPane.setCenter(new Label("No settings available"));
            addButtonAction(false, false, true);
        }
    }

    private void addButtonAction (boolean save, boolean duplicate, boolean remove){
        if(save){
            btnSave.setOnAction(event -> {

                Iterator<Map.Entry<String, Property>> it = this.nameValue.entrySet().iterator();
                while (it.hasNext()){
                    try{
                        Map.Entry<String, Property> entry = it.next();
                        try{
                            if(propertiesManager.getParameterType(key,entry.getKey()).equals("Color")){
                                Color color = ((Color)entry.getValue().getValue());
                                propertiesManager.setParameter(key, entry.getKey(),
                                        String.format("%s,%s,%s,%s",color.getRed(),color.getGreen(),color.getBlue(),color.getBrightness()*255));
                            } else {
                                propertiesManager.setParameter(key, entry.getKey(), entry.getValue().getValue().toString());
                            }
                            indicatorBox.reloadIndicator(key);
                            hide();
                        } catch (XPathExpressionException xpe){
                            //TODO handle..
                            xpe.printStackTrace();
                        }
                    } catch (Exception io){
                        //TODO handle..
                        io.printStackTrace();
                    }
                }
            });
        } else {
            btnSave.setDisable(true);
        }

        if(duplicate){
            btnDuplicate.setOnAction(event->{
                try {
                    propertiesManager.duplicate(key);
                    hide();
                    //TODO inform new entry in menu
                } catch (Exception e){
                    //TODO handle..
                    e.printStackTrace();
                }
            });
        } else {
            btnDuplicate.setDisable(true);
        }

        if (remove){
            btnRemove.setOnAction(event -> {
                indicatorBox.removeIndicator(key);
                hide();
            });
        } else {
            btnRemove.setDisable(true);
        }
    }

    public static IndicatorPopUpWindow getPopUpWindow(String key, ChartIndicatorBox chartIndicatorBox){
        if (open != null) {
            open.hide();
        }
        open = new IndicatorPopUpWindow(key, chartIndicatorBox);
        return open;
    }

    @Override
    public void hide(){
        super.hide();

    }


    @Override
    protected Skin<?> createDefaultSkin() {
        return new IndicatorPupUpWindwoSkin(this, borderPane);
    }

    class IndicatorPupUpWindwoSkin implements Skin<PopupControl>{

        private PopupControl popup;
        private Pane content;

        public IndicatorPupUpWindwoSkin(final PopupControl control, final Pane content){
            this.popup = control;
            this.content = content;
            content.idProperty().bind(popup.idProperty());
            content.styleProperty().bind(popup.styleProperty());
            content.getStyleClass().addAll(popup.getStyleClass());
        }

        @Override
        public PopupControl getSkinnable() {
            return popup;
        }

        @Override
        public Node getNode() {
            return content;
        }

        @Override
        public void dispose() {

        }
    }
}
