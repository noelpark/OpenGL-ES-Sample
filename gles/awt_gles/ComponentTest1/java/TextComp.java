import java.awt.Component;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

public class TextComp extends Component /*implements Runnable*/ {
    String name;
    public TextComp(String name) {
        super();
        this.name = name;
        System.out.println(" TextComp constructor");
    }

    public void paint(Graphics g) {
        Dimension dim = getSize();
        g.setColor(Color.red);
        g.drawRect(0, 0, dim.width - 1, dim.height - 1);
        g.setColor(Color.blue);
        g.drawLine(0, 0, dim.width - 1, dim.height - 1);
        g.setColor(Color.green);
        g.drawLine(dim.width - 1, 0, 0, dim.height - 1);
        g.setColor(Color.orange);
        g.drawLine(0, dim.height / 2, dim.width - 1, dim.height / 2);
        g.setColor(Color.white);
        g.drawString(name, 0, dim.height / 2);
    }

    public static void main(String[] args) {
        Container scene = new Container();
        TextComp t1 = new TextComp("How are you?");
        t1.setBounds(100, 100, 300, 100);
        TextComp t2 = new TextComp("Fine. Thank you.");
        t2.setBounds(100, 300, 300, 100);
        scene.add(t1);
        scene.add(t2);
        scene.setVisible(true);
        ComponentTest1 t = new ComponentTest1();
        t.startXlet();
    }
}
}
