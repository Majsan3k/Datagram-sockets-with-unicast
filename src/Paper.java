import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Iterator;

class Paper extends JPanel implements Runnable {
    private HashSet points = new HashSet();
    private DatagramSocket socket;
    private int portIn;
    private int portOut;
    private InetAddress address;
    private byte[] buf = new byte[256];
    private JComboBox<Integer> btnSizeList = new JComboBox<>();

    public Paper(int portIn, String host, int portOut) throws IOException{
        socket = new DatagramSocket(portIn);
        this.portIn = portIn;
        this.portOut = portOut;
        address = InetAddress.getByName(host);
        setBackground(Color.white);

        for(int i = 2; i <= 12; i++){
            btnSizeList.addItem(i);
        }

        JLabel changeSizeLbl = new JLabel("Change font size");

        add(changeSizeLbl);
        add(btnSizeList);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                drawPoint(e.getPoint());
            }
        });

        addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e) {
                drawPoint(e.getPoint());
            }
        });
    }

    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Iterator i = points.iterator();
        while(i.hasNext()) {
            ColorPoint p = (ColorPoint)i.next();
            g.setColor(p.getColor());
            g.fillOval(p.x, p.y, p.getSize(), p.getSize());
        }
    }

    private synchronized void drawPoint(Point p){
        int pointSize = (int) btnSizeList.getSelectedItem();
        System.out.println(pointSize);
        ColorPoint newPoint = new ColorPoint((int)p.getX(), (int)p.getY(), Color.green, pointSize);
        points.add(newPoint);
        repaint();
        send(p);
    }

    private synchronized void addPoint(Point p){
        int pointSize = (int) btnSizeList.getSelectedItem();
        ColorPoint newPoint = new ColorPoint((int)p.getX(), (int)p.getY(), Color.blue, pointSize);
        points.add(newPoint);
        repaint();
    }

    private synchronized void send(Point point){
        String pointToSend = Integer.toString(point.x) + " " + Integer.toString(point.y);
        buf = pointToSend.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, portOut);
        try {
            socket.send(sendPacket);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                DatagramPacket receivePacket = new DatagramPacket(buf, buf.length, address, portIn);
                socket.receive(receivePacket);
                String points = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String[] xy = points.split(" ");
                Point receivedPoint = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
                addPoint(receivedPoint);
            }catch (SocketTimeoutException timeOut){
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}