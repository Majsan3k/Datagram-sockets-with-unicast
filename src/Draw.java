import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Draw extends JFrame {

    public Draw(Paper paper) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(paper, BorderLayout.CENTER);
        setSize(640, 480);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException{

        if(args.length != 3){
            System.out.println("Need port numbers and hostname in format: <port in> hostname <port out>");
            return;
        }

        int portIn = Integer.parseInt(args[0]);
        String hostName = args[1];
        int portOut = Integer.parseInt(args[2]);

        Paper paper = new Paper(portIn, hostName, portOut);
        new Draw(paper);
        paper.run();
    }
}