import javax.swing.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Stack;

public class InterfaceGui extends JPanel
        implements TreeSelectionListener {
    private JEditorPane htmlPane;
    private JTree tree;
    private URL helpURL;
    private static boolean DEBUG = false;
    static Node rootnode = new Node();
    private static String _string = "";
    //Optionally play with line styles.  Possible values are
    //"Angled" (the default), "Horizontal", and "None".
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";

    //Optionally set the look and feel
    private static boolean useSystemLookAndFeel = false;

    public InterfaceGui() {
        super(new GridLayout(1,0));
        String list = "251-*32*+";
        Stack<Node> _list = new Stack();
        //Create the nodes.
        rootnode = Infix(list, _list);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(rootnode.key);
        createNodes(top,rootnode);

        //Create a tree that allows one selection at a time.
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);

        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        //Create the scroll pane and add the tree to it. 
        JScrollPane treeView = new JScrollPane(tree);

        //Create the HTML viewing pane.
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);

        JScrollPane htmlView = new JScrollPane(htmlPane);

        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(htmlView);

        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100);
        splitPane.setPreferredSize(new Dimension(500, 300));

        ImageIcon leafIcon = createImageIcon("middle.gif");
        if (leafIcon != null) {
            DefaultTreeCellRenderer renderer =
                    new DefaultTreeCellRenderer();
            renderer.setOpenIcon(leafIcon);
            renderer.setClosedIcon(leafIcon);
            tree.setCellRenderer(renderer);
        }
        //Add the split pane to this panel
        add(splitPane);
    }
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = InterfaceGui.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    private static Node Infix(String stringtext, Stack stacklist) {
        for(int i = 0; i < stringtext.length(); ++i) {
            Node tree = new Node(stringtext.charAt(i));
            if (!_checknum(stringtext.charAt(i)) && !stacklist.empty()) {
                tree.right = (Node)stacklist.pop();
                tree.left = (Node)stacklist.pop();
                tree.right.parent = tree.left.parent = tree;
                stacklist.push(tree);
            } else {
                stacklist.push(tree);
            }
        }

        return (Node)stacklist.pop();
    }
    public static boolean checknum(String _check) {
         Character check =  _check.charAt(0) ;
        if (check != '+' && check != '-' && check != '*' && check != '/') {
            return check != '1' && check != '2' && check != '3' && check != '4' && check != '5' && check != '6' && check != '7' && check != '8' && check != '9' && check != '0' ? true : true;
        } else {
            return false;
        }
    }
    public static boolean _checknum(char check) {
        if (check != '+' && check != '-' && check != '*' && check != '/') {
            return check != '1' && check != '2' && check != '3' && check != '4' && check != '5' && check != '6' && check != '7' && check != '8' && check != '9' && check != '0' ? true : true;
        } else {
            return false;
        }
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        _string = "";
        num =0 ;
        Inorder(node);
        htmlPane.setText(_string + " = " + Total(node));
       // htmlPane.setText("= "+Total(node));
    }


    private void createNodes(DefaultMutableTreeNode top,Node index) {
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;
        if(Homework1._complete(index)==false){
            category = new DefaultMutableTreeNode(index.left.key);
            book = new DefaultMutableTreeNode(index.right.key);
            createNodes(category,index.left);
            top.add(category);
            createNodes(book,index.right);
            top.add(book);
        }


    }

    private static int Total(DefaultMutableTreeNode _root) {
        char a = _root.toString().charAt(0);
        if (_checknum(a)) {
            return Integer.parseInt(_root.toString());
        } else {
            int num1 = Total((DefaultMutableTreeNode)_root.getChildAt(0));
            char _Ope = _root.toString().charAt(0);
            int num2 = Total((DefaultMutableTreeNode)_root.getChildAt(1));
            return calculate(num1, num2, _Ope);
        }
    }
    public static int calculate(int num1, int num2, Character opera) {
        switch(opera) {
            case '*':
                return num1 * num2;
            case '+':
                return num1 + num2;
            case ',':
            case '.':
            default:
                return num1;
            case '-':
                return num1 - num2;
            case '/':
                return num1 / num2;
        }
    }
    static int num = 0;
    private static void Inorder(DefaultMutableTreeNode index ) {
        char a = index.toString().charAt(0);
        if (_checknum(a)){
           _string =_string + index.toString();
        } else if (num > 0) {
           _string = _string + "(";
            Inorder((DefaultMutableTreeNode)index.getChildAt(0));
            _string = _string + index.toString();
            Inorder((DefaultMutableTreeNode)index.getChildAt(1));
            _string = _string +")";
        } else {
            num++;
            Inorder((DefaultMutableTreeNode)index.getChildAt(0));
            _string = _string + index.toString();
            Inorder((DefaultMutableTreeNode)index.getChildAt(1));
        }

    }
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        //Create and set up the window.
        JFrame frame = new JFrame("TreeDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new InterfaceGui());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }
}