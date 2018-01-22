//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.util.Stack;

public class Homework1 {
	static String checknum = "123456789";
	static String checkope = "+-*/";
	static boolean _complete = false;
	static boolean _completeright = false;
	static Node rootnode = new Node();
	static Node rootwant = new Node();



	public static void main(String[] args) {
		String list = "251-*32*+";
		if (args.length > 0) {
			list = args[0];
		}

		Stack<Node> _list = new Stack();
		rootnode = Infix(list, _list);
		rootwant = rootnode;
		Inorder(rootwant);
		System.out.println(" = " + Total(rootwant));
	}

	private static Node Infix(String stringtext, Stack stacklist) {
		for(int i = 0; i < stringtext.length(); ++i) {
			Node tree = new Node(stringtext.charAt(i));
			if (!checknum(stringtext.charAt(i)) && !stacklist.empty()) {
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

	private static boolean checknum(char check) {
		if (check != '+' && check != '-' && check != '*' && check != '/') {
			return check != '1' && check != '2' && check != '3' && check != '4' && check != '5' && check != '6' && check != '7' && check != '8' && check != '9' && check != '0' ? true : true;
		} else {
			return false;
		}
	}

	private static int calculate(int num1, int num2, Character opera) {
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

	private static void Inorder(Node index) {
		if (checknum(index.key)) {
			System.out.print(index.key);
		} else if (rootwant.key != index.key) {
			System.out.print("(");
			Inorder(index.left);
			System.out.print(index.key);
			Inorder(index.right);
			System.out.print(")");
		} else {
			Inorder(index.left);
			System.out.print(index.key);
			Inorder(index.right);
		}

	}

	private static int Total(Node _root) {
		if (checknum(_root.key)) {
			return Integer.parseInt(_root.key.toString());
		} else {
			int num1 = Total(_root.left);
			char _Ope = _root.key;
			int num2 = Total(_root.right);
			return calculate(num1, num2, _Ope);
		}
	}
}
