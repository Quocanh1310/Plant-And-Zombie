import javax.swing.*;
import java.awt.event.ActionEvent;



public class Zombie {
    private Timer eatTimer;
    public int health = 1000;
    public int speed = 1;
    private GamePanel gp;

    public int posX = 1000;
    public int myLane;
    public boolean isMoving = true;

    public Zombie(GamePanel parent,int lane){
        this.gp = parent;
        myLane = lane;
    }
   

    public void advance(){
        if(isMoving)/*zombie chi tien len phia truoc neu isMoving co gia tri true */ {
            boolean isCollides = false;
            final Collider[] collided = {null};
            for (int i = myLane * 9; i < (myLane + 1) * 9; i++)/*xac dinh cac collider trong duong ngang cua zom */ {
                if (gp.colliders[i].assignedPlant != null && gp.colliders[i].isInsideCollider(posX))
                //neu 1 collider co cay va zombie nam trong vung cua o co (collider) 
                {
                    isCollides = true;
                    collided[0] = gp.colliders[i];//luu lai collider hien tai
                    break;
                }
            }
            if (!isCollides)/*neu zombie khong va cham voi cay */ {
                if(slowInt>0)/*neu zombie di cham do bi dinh hieu ung cua cay bang*/{
                    if(slowInt % 2 == 0){//neu bi slow thi moi nghi slowduration la so le thi moi di chuyen sang trai 1 lan
                        posX-=speed;//su dung speed de xac dinh toc do cua tung loai zombie
                    }
                    slowInt--;
                }else {
                    posX -= speed;
                }
                if(eatTimer != null){
                    eatTimer.stop();
                    eatTimer = null;
                }
            } else {
                if (collided != null && collided[0].assignedPlant != null) {
                    if (eatTimer == null) {
                        eatTimer = new Timer(200, (ActionEvent e) -> {
                        if (collided[0].assignedPlant != null) {
                            collided[0].assignedPlant.health -= 10;
                            if (collided[0].assignedPlant.health < 0) {
                                collided[0].removePlant();
                                System.out.println("Plant die");
                                eatTimer.stop();
                                eatTimer = null;
                            }
                        }
                        });
                        eatTimer.start();    
                    }
}}
            if (posX < 0) {
                isMoving = false;
                JOptionPane.showMessageDialog(gp,"ZOMBIES ATE YOUR BRAIN !" + '\n' + "Starting the level again");
                GameWindow.gw.dispose();
                GameWindow.gw = new GameWindow();
            }
        }
    }

    int slowInt = 0;
    public void slow(){
        slowInt = 1000;
    }
    public static Zombie getZombie(String type,GamePanel parent, int lane) {
        Zombie z = new Zombie(parent,lane);
        switch(type) {
            case "NormalZombie" : z = new NormalZombie(parent,lane);
                break;
            case "ConeHeadZombie" : z = new ConeHeadZombie(parent,lane);
                break;
            case "FootballZombie" : z = new FootballZombie(parent,lane);
                break;
        }
        return z;
    }
}


