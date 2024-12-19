import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Collider extends JPanel implements MouseListener {

    ActionListener al;

    public Collider(){
        setOpaque(false); //to make the interact zone transparent
        addMouseListener(this);
        setSize(100,100);
    }

    public Plant assignedPlant;

    public void setPlant(Plant p){
        assignedPlant = p;
    }

    public void removePlant(){
        assignedPlant.stop();
        assignedPlant = null;
    }

    public boolean isInsideCollider(int tx){ //check for collision in the range from x to x + 100
        return (tx > getLocation().x) && (tx < getLocation().x + 100);
    }

    public void setAction(ActionListener al){
        this.al = al;
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(al != null){
            al.actionPerformed(new ActionEvent(this,ActionEvent.RESERVED_ID_MAX+1,""));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
