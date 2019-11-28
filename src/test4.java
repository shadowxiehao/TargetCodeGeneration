import java.util.ArrayList;
import java.util.Scanner;


public class test4 {

	static int [][] E_LR = {{ -1, -1, -1, -1, -1, -1,  2, -1,  1, -1},
		 					{ -1, -1, -1, -1, -1, -1, -1,  0, -1, -1},
		 					{  3, -1, -1, -1, -1, -1, -1, -1, -1, -1},
		 			        { -1,  5, -1, -1,  6, -1,  7, -1, -1,  4},
		 					{ -1, -1,  8,  9, -1, -1, -1,101, -1, -1},
		 					{ -1,  5, -1, -1,  6, -1,  7, -1, -1, 10},
		 					{ -1,  5, -1, -1,  6, -1,  7, -1, -1, 11},
		 					{ -1, -1,106,106, -1,106, -1,106, -1, -1},
		 					{ -1,  5, -1, -1,  6, -1,  7, -1, -1, 12},
		 					{ -1,  5, -1, -1,  6, -1,  7, -1, -1, 13},
		 				    { -1, -1,102,102, -1,102, -1,102, -1, -1},
		 				    { -1, -1,  8,  9, -1, 14, -1, -1, -1, -1},
		 				    { -1, -1,103,103, -1,103, -1,103, -1, -1},
		 				    { -1, -1,  8,104, -1,104, -1,104, -1, -1},
		 				    { -1, -1,105,105, -1,105, -1,105, -1, -1}};
	static String str = "=-*+()i#AE";
	static ArrayList<String> state = new ArrayList<String>();
	static ArrayList<String> fuhao = new ArrayList<String>();
	static ArrayList<String> data = new ArrayList<String>();
	static ArrayList<String> check = new ArrayList<String>();
	static ArrayList<String> supply = new ArrayList<String>();
	static int max = data.size();
	static int table = 0;
	static String dutou = "";
	static String zhizhen = "";
	static String string = "";
	static int n = 0;
	static String op1 = "";
	static String T = "T0";
	static int index = 0;
	public static void main(String[] args) {
		System.out.println("请输入一个赋值表达式(不包含减法与除法):");
		Scanner input = new Scanner(System.in);
		String str = input.nextLine();
		string = str;
		str += " #";
		str = str.replace("=", " = ");
		str = str.replace("-", " - ");
		str = str.replace("(", " ( ");
		str = str.replace(")", " ) ");
		str = str.replace("+", " + ");
		str = str.replace("*", " * ");
		str = str.replace("#", " # ");
		String s = "";
		for(int i=0;i<str.length();i++){
			if(str.charAt(i) != ' '){
				s += str.charAt(i);
			}else{
				data.add(s);
				s="";
				continue;
			}
		}
	deleteBlack(data);
	check.addAll(data);
	for(int i=0;i<data.size();i++){
		if(!data.get(i).equals("(")&&!data.get(i).equals(")")&&!data.get(i).equals("+")
				&&!data.get(i).equals("*")&&!data.get(i).equals("#")
				&&!data.get(i).equals("=")&&!data.get(i).equals("-")){
			if(judge1(data.get(i))||judge2(data.get(i))){
				data.set(i, "i");
			}else{
				System.out.println("标识符或数字"+data.get(i)+"有误！");
				System.exit(0);
			}
		}
	}
	System.out.println("结果如下：");
	state.add("0");
	fuhao.add("#");
	supply.add("#");
	dutou = data.get(n); 
	zhizhen = check.get(n);
	
	if(!dutou.equals("E")&&!dutou.equals("(")&&!dutou.equals("i")){
		System.out.println("表达式无第一个操作数");
		System.exit(0);
	}
	while(true){
		int x = getindex1();//获取state栈顶元素在表中位置
		int y = getindex2(dutou);//获取读头在表中位置位置
		table = E_LR[x][y];
		if(table == 0){
			System.exit(0);
		}else if(table<100){
			fuhao.add(dutou);
			supply.add(zhizhen);
			state.add(String.valueOf(table));
			n++;
			dutou = data.get(n); 
			zhizhen = check.get(n); 
			switch(table){
			case 0:
				if(!dutou.equals("A")&&!dutou.equals("i")){
					System.out.println("0");
					System.exit(0);
				}
				break;
			case 1:
				if(!dutou.equals("#")){
					System.out.println("1");
					System.exit(0);
				}
				break;
			case 2:
				if(!dutou.equals("=")){
					System.out.println("缺少操作数");
					System.exit(0);
				}
				break;
			case 3:
			case 5:
			case 6:
			case 8:
			case 9:
				if(!dutou.equals("-")&&!dutou.equals("(")&&!dutou.equals("i")&&!dutou.equals("E")){
					System.out.println("不完整的四元式");
					System.exit(0);
				}
				break;
			case 4:
				if(!dutou.equals("*")&&!dutou.equals("+")&&!dutou.equals("#")){
					System.out.println("4");
					System.exit(0);
				}
				break;
			case 7:
			case 10:
			case 12:
			case 13:
			case 14:
				if(!dutou.equals("*")&&!dutou.equals("+")&&!dutou.equals(")")&&!dutou.equals("#")){
					System.out.println("请不要输入‘-’");
					System.exit(0);
				}
				break;
			case 11:
				if(!dutou.equals("*")&&!dutou.equals("+")&&!dutou.equals(")")){
					System.out.println("11");
					System.exit(0);
				}
				break;
			}
		}else{
			int x1 = 0;
			int y1 = 0;
			switch(table){
				case 101:
					index = supply.size()-3;
					op1 = supply.get(index);
					System.out.println("mov "+op1+" "+T);
					supply.remove(gettop(supply));
					supply.remove(gettop(supply));
					supply.remove(gettop(supply));
					fuhao.remove(gettop(fuhao));
					fuhao.remove(gettop(fuhao));
					fuhao.remove(gettop(fuhao));
					state.remove(gettop(state));
					state.remove(gettop(state));
					state.remove(gettop(state));
					fuhao.add("A");
					supply.add(T);
					x1 = getindex1();
					y1 = getindex2("A");
					table = E_LR[x1][y1];
					state.add(String.valueOf(table));
					break;
				case 102:
//					System.out.println("*****102*****");
					index = supply.size()-1;
					op1 = supply.get(index);
					System.out.println("mov r0 "+op1);
					System.out.println("neg r0");
					change(T);
					System.out.println("mov "+T+" r0");
					supply.remove(gettop(supply));
					supply.remove(gettop(supply));
					fuhao.remove(gettop(fuhao));
					fuhao.remove(gettop(fuhao));
					state.remove(gettop(state));
					state.remove(gettop(state));
					fuhao.add("E");
					supply.add(T);
					x1 = getindex1();
					y1 = getindex2("E");
					table = E_LR[x1][y1];
					state.add(String.valueOf(table));
					break;
				case 103:
//					System.out.println("*****103*****");
					index = supply.size()-1;
					op1 = supply.get(index);
					System.out.println("mov r0 "+op1);
					index -= 2;
					op1 = supply.get(index);
					System.out.println("mul r0 "+op1);
					change(T);
					System.out.println("mov "+T+" r0");
					supply.remove(gettop(supply));
					supply.remove(gettop(supply));
					supply.remove(gettop(supply));
					fuhao.remove(gettop(fuhao));
					fuhao.remove(gettop(fuhao));
					fuhao.remove(gettop(fuhao));
					state.remove(gettop(state));
					state.remove(gettop(state));
					state.remove(gettop(state));
					fuhao.add("E");
					supply.add(T);
					x1 = getindex1();
					y1 = getindex2("E");
					table = E_LR[x1][y1];
					state.add(String.valueOf(table));
					break;
				case 104:
//					System.out.println("*****104*****");
					index = supply.size()-1;
					op1 = supply.get(index);
					System.out.println("mov r0 "+op1);
					index -= 2;
					op1 = supply.get(index);
					System.out.println("add r0 "+op1);
					change(T);
					System.out.println("mov "+T+" r0");
					supply.remove(gettop(supply));
					supply.remove(gettop(supply));
					supply.remove(gettop(supply));
					fuhao.remove(gettop(fuhao));
					fuhao.remove(gettop(fuhao));
					fuhao.remove(gettop(fuhao));
					state.remove(gettop(state));
					state.remove(gettop(state));
					state.remove(gettop(state));
					fuhao.add("E");
					supply.add(T);
					x1 = getindex1();
					y1 = getindex2("E");
					table = E_LR[x1][y1];
					state.add(String.valueOf(table));
					break;
				case 105:
//					System.out.println("*****105*****");
					index = supply.size()-1;
					supply.remove(index);
					index -=2;
					supply.remove(index);
					fuhao.remove(gettop(fuhao));
					fuhao.remove(gettop(fuhao));
					fuhao.remove(gettop(fuhao));
					state.remove(gettop(state));
					state.remove(gettop(state));
					state.remove(gettop(state));
					fuhao.add("E");
					x1 = getindex1();
					y1 = getindex2("E");
					table = E_LR[x1][y1];
					state.add(String.valueOf(table));
					break;
				case 106:
//					System.out.println("*****106*****");
					fuhao.remove(gettop(fuhao));
					state.remove(gettop(state));
					fuhao.add("E");
					x1 = getindex1();
					y1 = getindex2("E");
					table = E_LR[x1][y1];
					state.add(String.valueOf(table));
					break;
			}
		}
	}
	

	}
	private static void change(String T) {
		String ss = T.charAt(1)+"";
		int kk = Integer.parseInt(ss)+1;
		test4.T="T"+String.valueOf(kk);
	}
	private static int gettop(ArrayList<String> list) {
		int num = list.size()-1;
		return num;
	}


	private static int getindex2(String dutou) {
		String s = dutou;
		int result = str.indexOf(s);
		return result;
	}


	private static int getindex1() {
		ArrayList<String> state2 = new ArrayList<String>(state);
		int size = state2.size();
		String s = state2.get(size-1);
		int result = Integer.parseInt(s);
		return result;
	}


	private static boolean judge2(String string) {
		String s = string;
		char[] ch = s.toCharArray();
		if((ch[0]>='a'&&ch[0]<='z')||(ch[0]>='A'&&ch[0]<='Z')||ch[0]=='_'||ch[0]=='$')
			for(int i=0;i<ch.length;i++)
				if((ch[i]>='a'&&ch[i]<='z')||(ch[0]>='A'&&ch[0]<='Z')||ch[i]=='_'||ch[i]=='$'||judge1(ch[i]+""))
					return true;
		return false;
	}


	private static boolean judge1(String string) {
		String s = string;
		char[] ch = s.toCharArray();
		for(int i=0;i<ch.length;i++){
			if(ch[i]>='0'&&ch[i]<='9')
				continue;
			else
				return false;
		}
		return true;
	}	


	private static void deleteBlack(ArrayList<String> data2) {
		while(data.contains("")){
			int n = data.indexOf("");
			data.remove(n);
		}
	}
}
