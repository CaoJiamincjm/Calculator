package Calculator;

import java.awt.*;
import java.awt.event.*;//�˰��������¼����¼����������Լ��¼����������������������¼��������ı�д���̸�Ϊ���ɵı���ࡣ
import java.util.*;
import javax.swing.*;
public class Calculator implements ActionListener {
	private final int BTN_NUM = 1, BTN_EQUAL = 2, BTN_OTHER = 3;
	private String btnName[] = { "0", ".", "��", "+", "=", "1", "2", "3", "-", "C", "4", "5", "6", "*", ")", "7",
			"8", "9", "/", "(" }; // ��19
	private JButton[] btn;
	private JLabel showLabel1, showLabel2;
	private Queue<Object> queue;//����
	private double result;
	private String in_num = "", strLabel1;
	private int lastPressed = 0;

	public Calculator() {
		JFrame frame = new JFrame("������");	
		frame.setLocation(550, 240);
		frame.setSize(256, 240);
		frame.setResizable(false);
		frame.setLayout(null);	//layout: ���Ȳ���
		
		//�ϲ���
		//���ʽ
		showLabel1 = new JLabel("0",SwingConstants.RIGHT);							
		showLabel1.setFont(new Font(showLabel1.getFont().getName(), showLabel1.getFont().getStyle(), 16));//??????
		showLabel1.setSize(246, 20);
		showLabel1.setLocation(0, 0);
		frame.add(showLabel1);
		//����
		showLabel2 = new JLabel("0", SwingConstants.RIGHT);
		showLabel2.setFont(new Font(showLabel2.getFont().getName(), showLabel2.getFont().getStyle(), 20));
		showLabel2.setSize(246, 20);
		showLabel2.setLocation(0, 20);
		frame.add(showLabel2);
		
		//�²���
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//*������ڰ�ť����

		queue = new LinkedList<Object>();		//�ö��д�������ı��ʽ,����double��string
	}

	public void actionPerformed(ActionEvent e) {
		if (lastPressed == BTN_EQUAL){
			while (!queue.isEmpty())
				queue.poll();//�õ�����
			showLabel1.setText("");
		}
		Object o = e.getSource();//�������
		if (o == btn[0] || o == btn[5] || o == btn[6] || o == btn[7] || o == btn[10] || o == btn[11] || o == btn[12]
				|| o == btn[15] || o == btn[16] || o == btn[17]) {	//������ʱ
			if (lastPressed != BTN_NUM)
				in_num = "";
			in_num = in_num + ((JButton) o).getText();
			showLabel2.setText(in_num);
			lastPressed = BTN_NUM;
		} 
		else if (o == btn[1]) {	//��С����ʱ
			if (!in_num.contains(".")){
				if (lastPressed != BTN_NUM)
					in_num = "0";
				in_num += '.';
				showLabel2.setText(in_num);
				lastPressed = BTN_NUM;
			}
		}
		else if (o == btn[2]) {	//�Ǹ���ʱ
			if (lastPressed != BTN_NUM)
				in_num = "0";
			if (in_num.charAt(0) == '-')//charAt(int index)������һ���ܹ����������ض������µ��ַ���Stringʵ���ķ���.charAt()��������ָ������λ�õ�charֵ��������ΧΪ0~length()-1.��: str.charAt(0)����str�еĵ�һ���ַ�,str.charAt(str.length()-1)�������һ���ַ�.
				in_num = in_num.substring(1);//public String substring(int beginIndex)����һ���µ��ַ��������Ǵ��ַ�����һ�����ַ����������ַ���ʼ��ָ�����������ַ���һֱ�����ַ���ĩβ��
			else
				in_num = '-' + in_num;
			showLabel2.setText(in_num);
			lastPressed = BTN_NUM;
		}
		else if (o == btn[3] || o == btn[8] || o == btn[13] || o == btn[18]) { //�����
			strLabel1 = ((JButton) o).getText();
			if (lastPressed == BTN_NUM) {
				showLabel1.setText(showLabel1.getText() + in_num);//�����������ȥ
				queue.add(Double.valueOf(in_num));//Double.valueOf�����ǰ��������͵��ַ�����ת����Double����
			}
			showLabel1.setText(showLabel1.getText() + strLabel1);//������ȥ
			queue.add(strLabel1);
			lastPressed = BTN_OTHER;
		}
		else if (o == btn[4]) {	//�Ⱥ�
			if (lastPressed == BTN_NUM) {
				showLabel1.setText(showLabel1.getText() + in_num);
				queue.add(Double.valueOf(in_num));
			}
			GetResult gr = new GetResult(queue);
			result = gr.getResult();
			if (gr.hasError()){
				showLabel2.setText("���ʽ����");
			} 
			else {
				//ʵ���й�ʽ�������룡������������������������������
				if (Math.round(result) - result == 0)
					showLabel2.setText(String.valueOf((long) result));
				else
					showLabel2.setText(String.valueOf(result));
			}
			lastPressed = BTN_EQUAL;
		}
		else if (o == btn[9]) {	//���
			while (!queue.isEmpty())
				queue.poll();
			in_num = "0";
			showLabel1.setText("");
			showLabel2.setText("0");
			lastPressed = BTN_OTHER;
		}
		else if (o == btn[14]) {	//������
			strLabel1 = ((JButton) o).getText();
			if (lastPressed == BTN_NUM) {
				showLabel1.setText(showLabel1.getText() + in_num);
				queue.add(Double.valueOf(in_num));
			}
			//----------------------------------------------------����������������������������������������
			showLabel1.setText(showLabel1.getText() + strLabel1);
			queue.add(strLabel1);
			lastPressed = BTN_OTHER;
		}
		else if (o == btn[19]) {	//������
			strLabel1 = ((JButton) o).getText();
			showLabel1.setText(showLabel1.getText() + strLabel1);
			queue.add(strLabel1);
			lastPressed = BTN_OTHER;
		}
	}

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//����windowsϵͳ���
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		new Calculator();
	}
}


//����
class GetResult {
	private Stack<Double> stk_num;//Stack:ջ
	private Stack<Character> stk_ch;
	private Queue<Object> queue1;//��������
	private Queue<Object> queue2;//�µ�
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

	public void translate() {		//����׺���ʽתΪ��׺���ʽ, �������queue2
		while (!queue1.isEmpty()) {
			object = queue1.poll();
			if (object instanceof Double) {	//�����doubleֱ�Ӵ������
				queue2.add((Double) object);
			} 
			else {
				ch = ((String) object).charAt(0);
				if (ch == '(') {					//�����'('�������ջ��
					stk_ch.push(ch);
				} 
				else if (ch == ')') {				//�����')'������ջ�е�Ԫ��ȡ��������У�ֱ������'('
					if (stk_ch.isEmpty() || !stk_ch.contains('(')){
						haserror = true;
						return;
					}
					while (stk_ch.peek().charValue() != '(') {//Stack.Peek �� stack.pop ������	��ͬ�㣺��Ҷ�����ջ����ֵ����ͬ�㣺peek ���ı�ջ��ֵ(��ɾ��ջ����ֵ)��pop���ջ����ֵɾ����
						queue2.add(stk_ch.pop());
					}
					stk_ch.pop();
				}
				else {							//����������
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
		while (!stk_ch.isEmpty()) {		//����ջ��ʣ����ż������
			queue2.add(stk_ch.pop());
		}
	}

	public Double getResult() {		//�����׺���ʽ��ֵ
		Object o;
		translate();
		while (!queue2.isEmpty() && !haserror) {
			o = queue2.poll();
			if (o instanceof Double) {
				stk_num.push((Double) o);
			} 
			else {
				if ((char) o == '(' || (char) o == ')')	//��׺���ʽ�������ţ�˵�����ʽ����
					haserror = true;
				else
					calculate((char) o);
			}
		}
		if (!haserror && stk_num.size() >= 2)	//����ջ�л���Ԫ��
			haserror = true;
		if (stk_num.isEmpty())
			return 0.0;
		return stk_num.peek();
	}

	public void calculate(char c) {	//һ�μ���
		if (stk_num.size() < 2) {	//�������������������ջ����2�����֣�˵�����ʽ����
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
		stk_num.push(num1);		//������ɴ�������ջ

	}

	public int getPriority(char c) {	//��ȡ����������ȼ�
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


