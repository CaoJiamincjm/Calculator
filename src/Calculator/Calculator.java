package Calculator;

import java.awt.*;
import java.awt.event.*;//此包定义了事件和事件侦听器，以及事件侦听器适配器，它是让事件侦听器的编写过程更为轻松的便捷类。
import java.util.*;
import javax.swing.*;
public class Calculator implements ActionListener {
	private final int BTN_NUM = 1, BTN_EQUAL = 2, BTN_OTHER = 3;
	private String btnName[] = { "0", ".", "负", "+", "=", "1", "2", "3", "-", "C", "4", "5", "6", "*", ")", "7",
			"8", "9", "/", "(" }; // 共19
	private JButton[] btn;
	private JLabel showLabel1, showLabel2;
	private Queue<Object> queue;//队列
	private double result;
	private String in_num = "", strLabel1;
	private int lastPressed = 0;

	public Calculator() {
		JFrame frame = new JFrame("计算器");	
		frame.setLocation(550, 240);
		frame.setSize(256, 240);
		frame.setResizable(false);
		frame.setLayout(null);	//layout: 绝度布局
		
		//上部分
		//表达式
		showLabel1 = new JLabel("0",SwingConstants.RIGHT);							
		showLabel1.setFont(new Font(showLabel1.getFont().getName(), showLabel1.getFont().getStyle(), 16));//??????
		showLabel1.setSize(246, 20);
		showLabel1.setLocation(0, 0);
		frame.add(showLabel1);
		//输入
		showLabel2 = new JLabel("0", SwingConstants.RIGHT);
		showLabel2.setFont(new Font(showLabel2.getFont().getName(), showLabel2.getFont().getStyle(), 20));
		showLabel2.setSize(246, 20);
		showLabel2.setLocation(0, 20);
		frame.add(showLabel2);
		
		//下部分
		btn = new JButton[btnName.length];
		for (int i = 0, y = 0; i < btnName.length; i++) {
			y = i / 5 + 2;//?
			btn[i] = new JButton(btnName[i]);
			btn[i].setSize(50, 40);
			btn[i].setLocation(50 * (i % 5), 250 - 40 * y);
			frame.add(btn[i]);
			btn[i].addActionListener(this);
		}
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//*必须放在按钮后面

		queue = new LinkedList<Object>();		//用队列储存输入的表达式,包含double和string
	}

	public void actionPerformed(ActionEvent e) {
		if (lastPressed == BTN_EQUAL){
			while (!queue.isEmpty())
				queue.poll();//得到队列
			showLabel1.setText("");
		}
		Object o = e.getSource();//鼠标点击的
		if (o == btn[0] || o == btn[5] || o == btn[6] || o == btn[7] || o == btn[10] || o == btn[11] || o == btn[12]
				|| o == btn[15] || o == btn[16] || o == btn[17]) {	//是数字时
			if (lastPressed != BTN_NUM)
				in_num = "";
			in_num = in_num + ((JButton) o).getText();
			showLabel2.setText(in_num);
			lastPressed = BTN_NUM;
		} 
		else if (o == btn[1]) {	//是小数点时
			if (!in_num.contains(".")){
				if (lastPressed != BTN_NUM)
					in_num = "0";
				in_num += '.';
				showLabel2.setText(in_num);
				lastPressed = BTN_NUM;
			}
		}
		else if (o == btn[2]) {	//是负号时
			if (lastPressed != BTN_NUM)
				in_num = "0";
			if (in_num.charAt(0) == '-')//charAt(int index)方法是一个能够用来检索特定索引下的字符的String实例的方法.charAt()方法返回指定索引位置的char值。索引范围为0~length()-1.如: str.charAt(0)检索str中的第一个字符,str.charAt(str.length()-1)检索最后一个字符.
				in_num = in_num.substring(1);//public String substring(int beginIndex)返回一个新的字符串，它是此字符串的一个子字符串。该子字符串始于指定索引处的字符，一直到此字符串末尾。
			else
				in_num = '-' + in_num;
			showLabel2.setText(in_num);
			lastPressed = BTN_NUM;
		}
		else if (o == btn[3] || o == btn[8] || o == btn[13] || o == btn[18]) { //运算符
			strLabel1 = ((JButton) o).getText();
			if (lastPressed == BTN_NUM) {
				showLabel1.setText(showLabel1.getText() + in_num);//下面的数字上去
				queue.add(Double.valueOf(in_num));//Double.valueOf方法是把数字类型的字符串，转换成Double类型
			}
			showLabel1.setText(showLabel1.getText() + strLabel1);//符号上去
			queue.add(strLabel1);
			lastPressed = BTN_OTHER;
		}
		else if (o == btn[4]) {	//等号
			if (lastPressed == BTN_NUM) {
				showLabel1.setText(showLabel1.getText() + in_num);
				queue.add(Double.valueOf(in_num));
			}
			GetResult gr = new GetResult(queue);
			result = gr.getResult();
			if (gr.hasError()){
				showLabel2.setText("表达式有误！");
			} 
			else {
				//实现中国式四舍五入！！！！！！！！！！！！！！！！
				if (Math.round(result) - result == 0)
					showLabel2.setText(String.valueOf((long) result));
				else
					showLabel2.setText(String.valueOf(result));
			}
			lastPressed = BTN_EQUAL;
		}
		else if (o == btn[9]) {	//清除
			while (!queue.isEmpty())
				queue.poll();
			in_num = "0";
			showLabel1.setText("");
			showLabel2.setText("0");
			lastPressed = BTN_OTHER;
		}
		else if (o == btn[14]) {	//右括号
			strLabel1 = ((JButton) o).getText();
			if (lastPressed == BTN_NUM) {
				showLabel1.setText(showLabel1.getText() + in_num);
				queue.add(Double.valueOf(in_num));
			}
			//----------------------------------------------------？？？？？？？？？？？？？？？？？？？？
			showLabel1.setText(showLabel1.getText() + strLabel1);
			queue.add(strLabel1);
			lastPressed = BTN_OTHER;
		}
		else if (o == btn[19]) {	//左括号
			strLabel1 = ((JButton) o).getText();
			showLabel1.setText(showLabel1.getText() + strLabel1);
			queue.add(strLabel1);
			lastPressed = BTN_OTHER;
		}
	}

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//设置windows系统外观
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		new Calculator();
	}
}


//计算
class GetResult {
	private Stack<Double> stk_num;//Stack:栈
	private Stack<Character> stk_ch;
	private Queue<Object> queue1;//传进来的
	private Queue<Object> queue2;//新的
	private Object object;
	private char ch;
	private double num1, num2;
	private boolean haserror = false;

	public GetResult(Queue<Object> q) {
		this.queue1 = q;
		haserror = false;
		stk_num = new Stack<Double>();
		stk_ch = new Stack<Character>();
		queue2 = new LinkedList<Object>();
	}

	public void translate() {		//将中缀表达式转为后缀表达式, 存入队列queue2
		while (!queue1.isEmpty()) {
			object = queue1.poll();
			if (object instanceof Double) {	//如果是double直接存入队列
				queue2.add((Double) object);
			} 
			else {
				ch = ((String) object).charAt(0);
				if (ch == '(') {					//如果是'('存入符号栈中
					stk_ch.push(ch);
				} 
				else if (ch == ')') {				//如果是')'将符号栈中的元素取出存入队列，直到遇到'('
					if (stk_ch.isEmpty() || !stk_ch.contains('(')){
						haserror = true;
						return;
					}
					while (stk_ch.peek().charValue() != '(') {//Stack.Peek 与 stack.pop 的区别	相同点：大家都返回栈顶的值。不同点：peek 不改变栈的值(不删除栈顶的值)，pop会把栈顶的值删除。
						queue2.add(stk_ch.pop());
					}
					stk_ch.pop();
				}
				else {							//如果是运算符
					if (!stk_ch.isEmpty()) {
						if (getPriority(ch) <= getPriority(stk_ch.peek().charValue())) {
							while (!stk_ch.isEmpty() && getPriority(ch) <= getPriority(stk_ch.peek().charValue())) {
								if (stk_ch.peek() == '(') {
									break;
								}
								queue2.add(stk_ch.pop());
							}
						}
					}
					stk_ch.push(ch);
				}
			}
		}
		while (!stk_ch.isEmpty()) {		//符号栈中剩余符号加入队列
			queue2.add(stk_ch.pop());
		}
	}

	public Double getResult() {		//计算后缀表达式的值
		Object o;
		translate();
		while (!queue2.isEmpty() && !haserror) {
			o = queue2.poll();
			if (o instanceof Double) {
				stk_num.push((Double) o);
			} 
			else {
				if ((char) o == '(' || (char) o == ')')	//后缀表达式中有括号，说明表达式有误
					haserror = true;
				else
					calculate((char) o);
			}
		}
		if (!haserror && stk_num.size() >= 2)	//数字栈中还有元素
			haserror = true;
		if (stk_num.isEmpty())
			return 0.0;
		return stk_num.peek();
	}

	public void calculate(char c) {	//一次计算
		if (stk_num.size() < 2) {	//队列中有运算符，数字栈少于2个数字，说明表达式有误
			haserror = true;
			return;
		}
		num2 = stk_num.pop();
		num1 = stk_num.pop();
		switch (c) {

		case '+':
			num1 += num2;
			break;
		case '-':
			num1 -= num2;
			break;
		case '*':
			num1 *= num2;
			break;
		case '/':
			num1 /= num2;
			break;
		}
		stk_num.push(num1);		//计算完成存入数字栈

	}

	public int getPriority(char c) {	//获取运算符的优先级
		if (c == ')')
			return 0;
		else if (c == '+' || c == '-')
			return 1;
		else if (c == '*' || c == '/')
			return 2;
		else if (c == '(')
			return 3;
		else
			return 4;
	}

	public boolean hasError(){
		return haserror;
	}
}


