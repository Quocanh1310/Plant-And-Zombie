import javax.swing.*;
import java.awt.event.ActionEvent;


public class GameWindow extends JFrame {

    enum PlantType{
        None,
        Sunflower,
        Peashooter,
        FreezePeashooter,
        Wallnut,
        Shovel
    }

    public GameWindow(){
        setSize(1012,785);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel sun = new JLabel("SUN");
        sun.setLocation(37,80);
        sun.setSize(60,20);

        GamePanel gp = new GamePanel(sun);
        gp.setLocation(0,0);
        getLayeredPane().add(gp, Integer.valueOf(0));

        PlantCard sunflower = new PlantCard(new ImageIcon(this.getClass().getResource("images/cards/card_sunflower.png")).getImage());
        sunflower.setLocation(110,8);
        sunflower.setAction((ActionEvent e) -> {
            if (gp.activePlantingBrush != PlantType.Sunflower){
                gp.activePlantingBrush = PlantType.Sunflower;
            } 
            else gp.activePlantingBrush = PlantType.None;
        });
        getLayeredPane().add(sunflower, Integer.valueOf(3));

        PlantCard peashooter = new PlantCard(new ImageIcon(this.getClass().getResource("images/cards/card_peashooter.png")).getImage());
        peashooter.setLocation(175,8);
        peashooter.setAction((ActionEvent e) -> {
            if (gp.activePlantingBrush != PlantType.Peashooter){
                gp.activePlantingBrush = PlantType.Peashooter;
            } 
            else gp.activePlantingBrush = PlantType.None; //if already choose sunflower and click on that again deselect it
        });
        getLayeredPane().add(peashooter, Integer.valueOf(3));

        PlantCard freezepeashooter = new PlantCard(new ImageIcon(this.getClass().getResource("images/cards/card_freezepeashooter.png")).getImage());
        freezepeashooter.setLocation(240,8);
        freezepeashooter.setAction((ActionEvent e) -> {
            if (gp.activePlantingBrush != PlantType.FreezePeashooter){
                gp.activePlantingBrush = PlantType.FreezePeashooter;
            } 
            else gp.activePlantingBrush = PlantType.None;
        });
        getLayeredPane().add(freezepeashooter, Integer.valueOf(3));

        PlantCard wallnut = new PlantCard(new ImageIcon(this.getClass().getResource("images/cards/card_wallnut.png")).getImage());
        wallnut.setLocation(305,8);
        wallnut.setAction((ActionEvent e) -> {
            if (gp.activePlantingBrush != PlantType.Wallnut){
                gp.activePlantingBrush = PlantType.Wallnut;
            } 
            else gp.activePlantingBrush = PlantType.None;
        });
        getLayeredPane().add(wallnut, Integer.valueOf(3));

        PlantCard shovel = new PlantCard(new ImageIcon(this.getClass().getResource("images/cards/card_shovel.png")).getImage());
        shovel.setLocation(600,8);
        shovel.setSize(92, 45);
        shovel.setAction((ActionEvent e) -> {
            if (gp.activePlantingBrush != PlantType.Shovel){
                gp.activePlantingBrush = PlantType.Shovel;
            } 
            else gp.activePlantingBrush = PlantType.None;
        });
        getLayeredPane().add(shovel, Integer.valueOf(3));


        getLayeredPane().add(sun, Integer.valueOf(2));
        setResizable(false);
        setVisible(true);
        
    }
    public GameWindow(boolean b) {
        Menu menu = new Menu();
        menu.setLocation(0,0);
        setSize(1012,675);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getLayeredPane().add(menu, Integer.valueOf(0));
        menu.repaint();
        setResizable(false);
        setVisible(true);
    }
    static GameWindow gw;
    public static void begin() {
        gw.dispose();
        gw = new GameWindow();
    }
    public static void main(String[] args) {
        gw = new GameWindow(true);
    }

}
