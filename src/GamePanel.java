import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;


public class GamePanel extends JLayeredPane implements MouseMotionListener {

    Image bgImage;
    Image peashooterImage;
    Image freezePeashooterImage;
    Image sunflowerImage;
    Image peaImage;
    Image freezePeaImage;
    Image wallnutImage;
    Image shovelImage;
    Image normalZombieImage;
    Image coneHeadZombieImage;
    Image footballZombieImage;
    Collider[] colliders;

    ArrayList<ArrayList<Zombie>> laneZombies;
    ArrayList<ArrayList<Pea>> lanePeas;
    ArrayList<Sun> activeSuns;

    Timer redrawTimer;
    Timer advancerTimer;
    Timer sunProducer;
    Timer zombieProducer;
    JLabel sunScoreboard;

    GameWindow.PlantType activePlantingBrush = GameWindow.PlantType.None;

    int mouseX , mouseY;

    private int sunScore;

    public int getSunScore() {
        return sunScore;
    }

    public void setSunScore(int sunScore) {
        this.sunScore = sunScore;
        sunScoreboard.setText(String.valueOf(sunScore));
    }

    public GamePanel(JLabel sunScoreboard){
        setSize(1000,752);
        setLayout(null);
        addMouseMotionListener(this);
        this.sunScoreboard = sunScoreboard;
        setSunScore(150);  //chinh mat troi ban dau

        bgImage  = new ImageIcon(this.getClass().getResource("images/mainBG.png")).getImage();
        shovelImage = new ImageIcon(this.getClass().getResource("images/shovel.gif")).getImage();
        peashooterImage = new ImageIcon(this.getClass().getResource("images/plants/peashooter.gif")).getImage();
        freezePeashooterImage = new ImageIcon(this.getClass().getResource("images/plants/freezepeashooter.gif")).getImage();
        sunflowerImage = new ImageIcon(this.getClass().getResource("images/plants/sunflower.gif")).getImage();
        peaImage = new ImageIcon(this.getClass().getResource("images/pea.png")).getImage();
        freezePeaImage = new ImageIcon(this.getClass().getResource("images/freezepea.png")).getImage();
        wallnutImage = new ImageIcon(this.getClass().getResource("images/plants/wallnut.gif")).getImage();

        normalZombieImage = new ImageIcon(this.getClass().getResource("images/zombies/zombie1.gif")).getImage();
        coneHeadZombieImage = new ImageIcon(this.getClass().getResource("images/zombies/zombie2.gif")).getImage();
        footballZombieImage = new ImageIcon(this.getClass().getResource("images/zombies/zombie3.gif")).getImage();

        laneZombies = new ArrayList<>();
        laneZombies.add(new ArrayList<>()); //line 1
        laneZombies.add(new ArrayList<>()); //line 2
        laneZombies.add(new ArrayList<>()); //line 3
        laneZombies.add(new ArrayList<>()); //line 4
        laneZombies.add(new ArrayList<>()); //line 5

        lanePeas = new ArrayList<>();
        lanePeas.add(new ArrayList<>()); //line 1
        lanePeas.add(new ArrayList<>()); //line 2
        lanePeas.add(new ArrayList<>()); //line 3
        lanePeas.add(new ArrayList<>()); //line 4
        lanePeas.add(new ArrayList<>()); //line 5

        colliders = new Collider[45];
        for (int i = 0; i < 45; i++) { //create 45 colliders which represent for 45 blanks of the game field
            Collider a = new Collider();
            a.setLocation(44 + (i%9)*100,109 + (i/9)*120); //set the x-axis, y-axis for colliders
            a.setAction(new PlantActionListener((i%9),(i/9))); //every collider has a action listener
            colliders[i] = a; //add to the collider array
            add(a, Integer.valueOf( 0)); //make the collider zone avaible on screen at layer 0(under the plant vs zom,..)
        }


        activeSuns = new ArrayList<>();

        redrawTimer = new Timer(25,(ActionEvent e) -> {
            repaint();
        }); //25ms redraw the game it the graphics that you see
        redrawTimer.start();

        advancerTimer = new Timer(60,(ActionEvent e) -> advance()); 
        advancerTimer.start(); //60ms advance() method will be called to update the status of zom, plan like moving or shooting and they are the calculation

        sunProducer = new Timer(5000,(ActionEvent e) -> {
            Random rnd = new Random();
            Sun sta = new Sun(this,rnd.nextInt(800)+100,0,rnd.nextInt(300)+200); //sun will creat at initial postion (100-899, 0) then drop down (initial x, 200-499);
            activeSuns.add(sta);
            add(sta, Integer.valueOf(1)); //add to layer 1
        });
        sunProducer.start();
        
        zombieProducer = new Timer(9000,(ActionEvent e) -> {
            Random rnd = new Random();
            LevelData lvl = new LevelData();
            String [] Level = lvl.Level[Integer.parseInt(lvl.Lvl)-1];// get the level, you have to convert it to int in order to access the element of the array
            System.out.println("Avaible zom type: " + Level);
            int [][] LevelValue = lvl.LevelValue[Integer.parseInt(lvl.Lvl)-1]; //get the type of zombie
            System.out.println();
            int l = rnd.nextInt(5); // random zombie's lane
            int t = rnd.nextInt(100);
            Zombie z = null;
            for(int i = 0;i<LevelValue.length;i++) { //tra ve kqua la so mang chinh
                if(t>=LevelValue[i][0]&&t<=LevelValue[i][1]) { //lay gia tri dau va cuoi cua tung loai zom theo hang va cot
                                                                //so cot luon la 2 va so hang la i
                    z = Zombie.getZombie(Level[i],GamePanel.this,l);
                }
            }
            laneZombies.get(l).add(z);
        });
        zombieProducer.start();

    }

    private void advance(){
        for (int i = 0; i < 5 ; i++) {
            for(Zombie z : laneZombies.get(i)){
                z.advance();
            }

            for (int j = 0; j < lanePeas.get(i).size(); j++) {
                Pea p = lanePeas.get(i).get(j);
                p.advance();
            }

        }

        for (int i = 0; i < activeSuns.size() ; i++) {
            activeSuns.get(i).advance();
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage,0,0,null);
        //real time choice
        if (activePlantingBrush != GameWindow.PlantType.None) {
            Image selectedImage = null;
            switch (activePlantingBrush) {
                case Sunflower:
                if(getSunScore()>=50) {
                    selectedImage = sunflowerImage;}
                    break;
                case Peashooter:
                if(getSunScore()>=100) {
                    selectedImage = peashooterImage;}
                    break;
                case FreezePeashooter:
                if(getSunScore()>=175) {
                    selectedImage = freezePeashooterImage;}
                    break;
                case Wallnut:
                if(getSunScore()>=50) {
                    selectedImage = wallnutImage;}
                    break;
                default:
                    break;
            }

            if(activePlantingBrush == GameWindow.PlantType.Shovel){
                g.drawImage(shovelImage, mouseX -50, mouseY-50, null);
                repaint();
            }
            if (selectedImage != null) {
                g.drawImage(selectedImage, mouseX -50, mouseY-50, null);
                repaint();
            }
        }

        //Draw Plants
        for (int i = 0; i < 45; i++) {
            Collider c = colliders[i];
            if(c.assignedPlant != null){
                Plant p = c.assignedPlant;
                if(p instanceof Peashooter){
                    g.drawImage(peashooterImage,50 + (i%9)*100,119 + (i/9)*120,null);
                }
                if(p instanceof FreezePeashooter){
                    g.drawImage(freezePeashooterImage,55 + (i%9)*100,134 + (i/9)*120,null);
                }
                if(p instanceof Sunflower){
                    g.drawImage(sunflowerImage,50 + (i%9)*100,109 + (i/9)*120,null);
                }
                if(p instanceof Wallnut){
                    g.drawImage(wallnutImage,55 + (i%9)*100,134 + (i/9)*120,null);
            }
        }
    }
//set position of the zom image
        for (int i = 0; i < 5 ; i++) {
            for(Zombie z : laneZombies.get(i)){
                if(z instanceof NormalZombie){
                    g.drawImage(normalZombieImage,z.posX,92+(i*120),null);
                }else if(z instanceof ConeHeadZombie){
                    g.drawImage(coneHeadZombieImage,z.posX,94+(i*120),null);
                }else if(z instanceof FootballZombie){
                    g.drawImage(footballZombieImage,z.posX,60+(i*120),null);
                }
            }
//set position of the pea image
            for (int j = 0; j < lanePeas.get(i).size(); j++) {
                Pea p = lanePeas.get(i).get(j);
                if(p instanceof FreezePea){
                    g.drawImage(freezePeaImage, p.posX - 10, 140 + (i * 120), null);
                }else {
                    g.drawImage(peaImage, p.posX, 140 + (i * 120), null);
                }
            }

        }

        


    }
    class PlantActionListener implements ActionListener {

        int x,y;

        public PlantActionListener(int x, int y){
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (colliders[x + y * 9].assignedPlant == null){/* Chỉ cho phép trồng cây nếu không có cây trong collider*/     
                if(activePlantingBrush == GameWindow.PlantType.Sunflower){
                    if(getSunScore()>=50) {
                        colliders[x + y * 9].setPlant(new Sunflower(GamePanel.this, x, y));
                        setSunScore(getSunScore()-50);
                    }
                }
                if(activePlantingBrush == GameWindow.PlantType.Peashooter){
                    if(getSunScore() >= 100) {
                        colliders[x + y * 9].setPlant(new Peashooter(GamePanel.this, x, y));
                        setSunScore(getSunScore()-100);
                    }
                }

                if(activePlantingBrush == GameWindow.PlantType.FreezePeashooter){
                    if(getSunScore() >= 175) {
                        colliders[x + y * 9].setPlant(new FreezePeashooter(GamePanel.this, x, y));
                        setSunScore(getSunScore()-175);
                    }
                }
                if (activePlantingBrush == GameWindow.PlantType.Wallnut) {
                    if (getSunScore() >= 50) {
                        colliders[x + y * 9].setPlant(new Wallnut(GamePanel.this, x, y)); //the pharse x + y * 9 cho biết vị trí của mạng 1 chiều 
                        setSunScore(getSunScore() - 50);                                // nơi mà cây được trồng dựa trên tọa độ (x, y) của mảng 2 chiều
                    }
                }
            }
            if(activePlantingBrush == GameWindow.PlantType.Shovel){
                colliders[x + y * 9].removePlant(); // Correctly remove the plant
            }
            activePlantingBrush = GameWindow.PlantType.None;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
    static int progress = 0;
    public void setProgress(int num) {
        progress += num; // Mỗi khi giết một zombie, progress tăng lên 10
        System.out.println(progress);
    
        if (progress >= 150) {
            // Xóa tất cả zombie còn lại trên sân
            for (int i = 0; i < 5; i++) {
                laneZombies.get(i).clear(); // Xóa toàn bộ danh sách zombie trong từng làn
                lanePeas.get(i).clear();
            }
            activeSuns.clear();
            redrawTimer.stop();
            advancerTimer.stop();
            sunProducer.stop();
            zombieProducer.stop();
            
            for (Collider collider : colliders) {
                if (collider.assignedPlant != null) {
                    collider.removePlant(); // Xóa cây trong từng ô
                }
            }

    
            if ("1".equals(LevelData.Lvl)) {
                JOptionPane.showMessageDialog(null, "Level Completed !!!" + '\n' + "Starting next Level");
                progress = 0;
                GameWindow.gw.dispose();
                LevelData.write("2"); // Ghi đè file level.txt thành 2
                GameWindow.gw = new GameWindow();
            } else {
                JOptionPane.showMessageDialog(null, "Level Completed !!!" + '\n' +
                        "More Levels will come soon !!!" + '\n' + "Resetting data");
                LevelData.write("1"); // Ghi đè file level.txt thành 1
                System.exit(0);
            }
    
            progress = 0; // Đặt lại progress
        }
    }
    
}
