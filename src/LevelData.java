
import java.io.*;
import java.util.logging.Logger;

public class LevelData {
    static String Lvl = "1"; //inital level = 1
    static String [][] Level = {{"NormalZombie","ConeHeadZombie"},{"NormalZombie", "ConeHeadZombie", "FootballZombie"},};
    static int [][][] LevelValue = {{{0,49},{50,99}},{{0,25},{26,69},{70,99}}};
    public LevelData() {
        try {
            File f = new File("Level.txt");

            if(!f.exists()) {/*neu file Level.txt khong ton tai*/
                BufferedWriter bwr = new BufferedWriter(new FileWriter(f));//tao ra file Level.txt
                bwr.write("1");//ghi gia tri 1 cho file
                bwr.close();
                Lvl = "1";
            } else { //if the file already exist
                BufferedReader br = new BufferedReader(new FileReader(f));
                Lvl = br.readLine(); //lvl = level stored in the file
                br.close();
            }
        } catch (Exception ex) {
                System.out.println("Can't find or create file Level.txt, Please try again!");
        }
    }
    public static void write(String lvl) {
        File f = new File("Level.txt");
        try {
            BufferedWriter bwr = new BufferedWriter(new FileWriter(f));
            bwr.write(lvl);
            bwr.close();
            Lvl = lvl;
        } catch (IOException ex) {
            Logger.getLogger(LevelData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }
}
