package MineSweeper;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static java.lang.Math.round;
import static java.lang.Math.pow;

public class Game extends JFrame implements Serializable {

	private static final long serialVersionUID = 1L;
	// a bombák száma miatt
    private JLabel label;
    	
    public Game() throws InterruptedException, ClassNotFoundException, IOException {
        EventQueue.invokeLater(() -> {
        setLayout(new BorderLayout());
        setTitle("Minesweeper");
        //inicializálás
        label = new JLabel("");
            try {
                add(new Minesweeper(label));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        //szerepe azonos a smiley ikonéval
        JMenuItem newGame = new JMenuItem("New Game");
        file.add(newGame);
        newGame.addActionListener((ActionListener) new NewGameActionListener());
        
        //egyesével megadhatók a pálya magassága, szélessége, bombák száma
        JMenuItem side = new JMenuItem("Side length");
        file.add(side);
        side.addActionListener(new SideActionListener());
        
        JMenuItem bombNumber = new JMenuItem("Number of mines");
        file.add(bombNumber);
        bombNumber.addActionListener(new BombNumberActionListener());

        JMenuItem saveMenu = new JMenuItem("Save");
        file.add(saveMenu);
        saveMenu.addActionListener(e -> save( "xd.xd"));

        JMenuItem loadMenu = new JMenuItem("Load");
        file.add(loadMenu);
        loadMenu.addActionListener(e -> {
            load("xd.xd");
            repaint();
        });
        
        menu.add(file);
        JMenu info = new JMenu("Info");
        menu.add(info);
        JMenuItem descr = new JMenuItem("Description");
        info.add(descr);
        
        //megváltoztatható a pálya színe
        JMenu colors = new JMenu("Colors");
        menu.add(colors);
        
        //az eredeti aknakereső színei
        JMenuItem standard = new JMenuItem("Standard");
        colors.add(standard);
        standard.addActionListener(new stColorActionListener());
        
        //az általam megálmodott dark aknakereső
        JMenuItem anti = new JMenuItem("Anti Minesweeper");
        colors.add(anti);
        anti.addActionListener(new anColorActionListener());

        descr.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Scanner sc = null;
                try { sc = new Scanner(new File("src/descr.txt") ); } catch (FileNotFoundException e) { e.printStackTrace(); }
                String text = sc.useDelimiter("\\A").next();
                sc.close();
                JTextArea ta = new JTextArea(text);
                ta.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(ta);
                scrollPane.setPreferredSize(new Dimension(300, 200));
                //ezzel szavak nem lesznek kettéválasztva a kiíratásban
                ta.setWrapStyleWord(true);
                //a függőleges görgethetőségért
                ta.setLineWrap(true);
                //kiíratom a leírást JOptionPane segítségével
                JOptionPane.showMessageDialog(null, scrollPane, "How-to", JOptionPane.YES_NO_OPTION);
            }
        });

        add(menu, BorderLayout.NORTH);
        JPanel jp=new JPanel();
        JPanel tp = new TimerPanel();
        jp.add(tp, BorderLayout.WEST);
        JButton smiley = new JButton();
        try {
            //13.png kép beállítása JButton-on
            smiley.setIcon(new ImageIcon((new ImageIcon("src/icons/13.png")).getImage()));
        } catch (Exception ex) { System.out.println(ex); }
        jp.add(smiley);
        //ugyanazt csinálja, mint a New Game menüpont
        smiley.addActionListener(new NewGameActionListener());
        //Jbutton ráigazítása a képre
        smiley.setBorder(BorderFactory.createEmptyBorder());
        //a bombaszámláló hozzáadása a panelhez
        jp.add(label);
        add(jp, BorderLayout.SOUTH);
        JTextField name = new JTextField("Name: ");
        name.setEditable(false);
        ///jp.add(name);
        //JTextField user_name=new JTextField();
        ///jp.add(user_name);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        // komponenshez igazítja az ablakot
        setLocationRelativeTo(null);
        System.out.println(Minesweeper.inGame);
        });
    }

    public static void save(String fileName) {
        try {
            FileOutputStream f = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(f);
            out.writeObject(Minesweeper.field);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load(String fileName) {
        try {
            FileInputStream f = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(f);
            Minesweeper.field = (int[]) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public class TimerPanel extends JPanel {

        private Timer timer;
        private long startTime = System.currentTimeMillis();

        private JLabel label= new JLabel();

        public TimerPanel() {
            setLayout(new GridBagLayout());
            timer = new Timer(10, e -> {
                long now = System.currentTimeMillis();
                if(Minesweeper.inGame) {
                    long clockTime = now - startTime;
                    SimpleDateFormat df = new SimpleDateFormat("mm:ss");
                    label.setText(df.format(clockTime));
                }
                else
                    startTime = System.currentTimeMillis();
            });
            timer.start();
            add(label);
        }
    }

    class NewGameActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            try {
				Minesweeper.newGame();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            repaint();
        }
    }

    class stColorActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            Minesweeper.setColor("st");
            try {
				Minesweeper.newGame();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            repaint();
        }
    }
    
    class anColorActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            Minesweeper.setColor("an");
            try {
				Minesweeper.newGame();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            repaint();
        }
    }

    class SideActionListener implements ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            String Side=JOptionPane.showInputDialog(null, "What size (NxN) do you want?", null);
            //konvertálás
            int side=Integer.parseInt(Side);
            Minesweeper.setSide(side);
            //bombák számának arányos illesztése a pályához
            int bomb=(int)round(pow(side,2)/6);
            Minesweeper.setMines(bomb);
            try {
				Minesweeper.newGame();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            setPreferredSize(new Dimension(side, side));
            repaint();
        }
    }

    class BombNumberActionListener implements ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            String Side=JOptionPane.showInputDialog(null,"How much bomb do you want?"+"\n"+"(Suggested: 1 mine/6 field)",null);
            Minesweeper.setMines(Integer.parseInt(Side));
            try {
				Minesweeper.newGame();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            repaint();
        }
    }

    static WindowListener listener = new WindowAdapter() {
    	public void windowClosing(WindowEvent evt) {
    		Frame frame = (Frame) evt.getSource();
    		try {
				Seri.save("Serialized");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    };

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, IOException {
        
        Game game=new Game();
        try {
			game.addWindowListener(listener);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        game.setVisible(true);

        /*try {
            FileOutputStream f = new FileOutputStream("src/output.txt");
            ObjectOutputStream out = new ObjectOutputStream(f);
            out.writeObject(ser);
            out.close();
        } catch(IOException ex) { } */
    }
}