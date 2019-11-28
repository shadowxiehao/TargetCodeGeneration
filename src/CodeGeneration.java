import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class CodeGeneration {
    /*
    1、文法如下：
    0) S→A			1) A→i=E		2) E→-E
    3) E→E*E		4) E→E+E		5) E→(E)
    6) E→i

2、对应的LR分析表如下：
状态	 ACTION	                    GOTO
	=	@	*	+	(	)	 i   #  A   E

     */
    private int[][] table =
            //          =   @   *   +   (   )   i   #   A   E
                    {{ -2, -2, -2, -2, -2, -2,  2, -7,  1, -2},
                     { -1, -1, -1, -1, -1, -1, -1,  0, -1, -1},
                     {  3, -3, -3, -3, -3, -3, -3, -3, -3, -3},
                     { -4,  5, -5, -5,  6, -6,  7, -1, -1,  4},
                     { -1, -1,  8,  9, -1, -1, -1,101, -1, -1},
                     { -1,  5, -1, -1,  6, -1,  7, -1, -1, 10},
                     { -1,  5, -1, -1,  6, -1,  7, -1, -1, 11},
                     { -1, -1,106,106, -8,106, -8,106, -1, -1},
                     { -1,  5, -1, -1,  6, -1,  7, -1, -1, 12},
                     { -1,  5, -1, -1,  6, -1,  7, -1, -1, 13},
                     { -1, -1,102,102, -8,102, -8,102, -1, -1},
                     { -1, -1,  8,  9, -1, 14, -1, -1, -1, -1},
                     { -1, -1,103,103, -1,103, -1,103, -1, -1},
                     { -1, -1,  8,104, -1,104, -1,104, -1, -1},
                     { -1, -1,105,105, -1,105, -1,105, -1, -1}};
    // <-1代表出错,出错含义部分是自己写的,不一定对
    // 0代表接受 acc
    // 1~100 代表移进 如1 代表S1
    // >101 代表规约  如101 代表r1

    private Stack<Character> symbol_stack = new Stack<>();//符号栈的表面 如 E,i
    private Stack<String> meaning_stack = new Stack<>();//符号栈的实际内容
    private Stack<Integer> state_stack = new Stack<>();//状态栈
    private String input; //输入串,初始化时默认加上#结尾
    Queue<String> queuei = new LinkedList<String>();

    private int index = 0;//记录当前读到字符串第几个字符
    private HashMap<String, Integer> keywords = new HashMap<>();

    private int state = 0;//记录一次状态
    private int act = -1;//记录对应的表格内的数字

    //记录和 T 有关的值
    private int tnum = 0;

    public CodeGeneration(String input) {//初始化分析
        this.input = input + "#";//输入串加上'#' 初始化输入串
        init_keywords();
        try {
            find();//将初始输入转换成标准格式
            state = 0;
            state_stack.push(state);
            symbol_stack.push('#');//初始化栈
            index = 0;//指向输入串第一个,输入串初始化完成
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private int init_keywords() {//将输入符号 转换成 LR分析表中的对应列数
        keywords.put("=", 0);
        keywords.put("@", 1);
        keywords.put("*", 2);
        keywords.put("+", 3);
        keywords.put("(", 4);
        keywords.put(")", 5);
        keywords.put("i", 6);
        keywords.put("#", 7);
        keywords.put("A", 8);
        keywords.put("E", 9);
        return 0;//正常
    }

    public void show() {

        try {
            while ((act = table[state_stack.peek()][keywords.get(input.charAt(index) + "")]) != 0) {
                process();
            }
            System.out.println("表达式正确,生成成功!");

        } catch (Exception e) {
            if (e.getMessage() != null) System.out.println(e.getMessage());
        }
    }


    private void process() throws Exception {//处理不同的移进或规约
        try {
            if (act < 0) {
                errorMessage();
            } else if (act > 100) {//规约
                convention();//太长了不好看,到别的地方写
            } else if (act > 0) {//移进
                symbol_stack.push(input.charAt(index++));//符号栈进栈一个输入,输入串指向下个字符
                meaning_stack.push(queuei.poll());//语义符号栈进一个内容
                state_stack.push(act);//状态栈进栈要移进的状态
            }
        } catch (Exception e) {
            throw e;//我只是错误的搬运工
        }
    }


    private void convention() {//规约
        switch (act) {
            case 106: {
                //出栈,弹出一个符号和一个状态
                for (int i = 0; i < 1; i++) {
                    symbol_stack.pop();
                }
//                meaning_stack.push(meaning_stack.pop()); //不影响,注释了

                for (int i = 0; i < 1; i++) {
                    state_stack.pop();
                }
                //加入规约'E'
                symbol_stack.push('E');
                //将GOTO的数字推入状态栈
                state = state_stack.peek();
                act = table[state][keywords.get("E")];
                state_stack.push(act);
                break;
            }
            case 105: {
                //弹出(E)
                for (int i = 0; i < 3; i++) {
                    symbol_stack.pop();
                }
                meaning_stack.pop();
                String temp = meaning_stack.pop();
                meaning_stack.pop();
                meaning_stack.push(temp);
                //弹出状态
                for (int i = 0; i < 3; i++) {
                    state_stack.pop();
                }
                //加入规约的'E'
                symbol_stack.push('E');
                //GOTO处理
                state = state_stack.peek();
                act = table[state][keywords.get("E")];
                state_stack.push(act);
                break;
            }
            case 104: {
                //E+E
                System.out.println("mov r0 "+meaning_stack.pop());
                meaning_stack.pop();
                System.out.println("add r0 "+meaning_stack.pop());
                ++tnum;
                meaning_stack.push("T"+tnum);
                System.out.println("mov "+ meaning_stack.peek() +" r0");

                //出栈,弹出n个符号和n个状态
                for (int i = 0; i < 3; i++)
                    symbol_stack.pop();
                for (int i = 0; i < 3; i++)
                    state_stack.pop();
                //加入规约E+E
                symbol_stack.push('E');
                //将GOTO的数字推入状态栈
                state = state_stack.peek();
                act = table[state][keywords.get("E")];
                state_stack.push(act);
                break;
            }
            case 103: {
                //E*E
                System.out.println("mov r0 "+meaning_stack.pop());
                meaning_stack.pop();
                System.out.println("mul r0 "+meaning_stack.pop());
                ++tnum;
                meaning_stack.push("T"+tnum);
                System.out.println("mov "+ meaning_stack.peek() +" r0");

                //出栈,弹出n个符号和n个状态
                for (int i = 0; i < 3; i++)
                    symbol_stack.pop();
                for (int i = 0; i < 3; i++)
                    state_stack.pop();
                //加入规约的 E*E->E
                symbol_stack.push('E');
                //将GOTO的数字推入状态栈
                state = state_stack.peek();
                act = table[state][keywords.get("E")];
                state_stack.push(act);
                break;
            }
            case 102: {
                //-E
                System.out.println("mov r0 "+meaning_stack.pop());
                meaning_stack.pop();
                System.out.println("neg r0");
                ++tnum;
                meaning_stack.push("T"+tnum);
                System.out.println("mov "+ meaning_stack.peek() +" r0");

                //出栈,弹出n个符号和n个状态
                for (int i = 0; i < 2; i++)
                    symbol_stack.pop();
                for (int i = 0; i < 2; i++)
                    state_stack.pop();
                //加入规约的 -E->E
                symbol_stack.push('E');
                //将GOTO的数字推入状态栈
                state = state_stack.peek();
                act = table[state][keywords.get("E")];
                state_stack.push(act);
                break;
            }
            case 101: {
                //A->i=E 世界线的收束!
                meaning_stack.pop();
                meaning_stack.pop();
//                ++tnum;
                System.out.println("mov "+meaning_stack.pop()+" T"+tnum);
                meaning_stack.push("T"+tnum);

                //出栈,弹出n个符号和n个状态
                for (int i = 0; i < 3; i++)
                    symbol_stack.pop();
                for (int i = 0; i < 3; i++)
                    state_stack.pop();
                //加入规约的 -E->E
                symbol_stack.push('A');
                //将GOTO的数字推入状态栈
                state = state_stack.peek();
                act = table[state][keywords.get("A")];
                state_stack.push(act);
                break;
            }
        }
    }

    private void errorMessage() throws Exception {//显示出错原因
        switch (act) {
            case -1:
                throw new Exception("出错!");
            case -2:
                throw new Exception("因为是赋值语句,前面需要加y=之类的式子,当前开头遇到的是:"+input.charAt(index));
            case -3:
                throw new Exception("因为是赋值语句,前面需要加y=之类的式子,当前开头遇到的是:"+meaning_stack.peek());
            case -4:
                throw new Exception("赋值号'='后还有一个'=',重复!");
            case -5:
                throw new Exception("此时期待一个运算对象(如i或'('),而实际遇到的是:"+meaning_stack.peek());
            case -6:
                throw new Exception("没有对应的左括号匹配!");
            case -7:
                throw new Exception("目前无输入,程序终止!");
            case -8:
                throw new Exception("此时期待一个运算符,而实际遇到的是:"+( (input.charAt(index)=='i')?meaning_stack.peek():input.charAt(index)) );
        }
        throw new Exception("error");//程序要体面就让它体面,否则就帮它体面
    }

    /*
    将初始输入转换成标准格式
     */
    private void find() throws Exception {//找到之后是"+","*",...中的哪一个,都不属于则报错
        String out = "";//暂存,每次处理后先存到out,最后让input=out
        String tmp;//暂存找到的字符串
        String tmpnum;//暂存数字
        input = input.replaceAll("\\s", "");
        input = input.replaceAll("(?<!\\w)-(?=\\w)", "@");
        while (index < input.length()) {
            if (isDigit(input.charAt(index))) {
                tmpnum = "";
                tmpnum += input.charAt(index);
                index++;
                while (isDigit(input.charAt(index))) {
                    tmpnum += input.charAt(index);
                    index++;
                }
                queuei.offer(tmpnum);
                out += "i";
            } else if (isLetter(input.charAt(index))) {
                tmp = "";
                int flag = 0;

                while (isLetter(input.charAt(index))) {
                    tmp += input.charAt(index);
                    flag++;
                    index++;
                }
                if (flag != 0) {
                    while (isDigit(input.charAt(index))) {
                        tmp += input.charAt(index);
                        index++;
                    }
                }
                if (keywords.containsKey(tmp) && tmp != "A" && tmp != "E") {
                    queuei.offer(tmp);
                    out += tmp;
                } else {
                    queuei.offer(tmp);
                    out += "i";
                }
            } else if (keywords.containsKey(input.charAt(index) + "")) {
                index++;
                queuei.offer(input.charAt(index - 1) + "");
                out += input.charAt(index - 1) + "";
            } else {
                //因为如果非法输入了,上面也会报错,要把前面报错的屏蔽掉
                throw new ArrayStoreException("第" + (index + 1) + "个输入字符" + input.charAt(index) + "为非法输入!");
            }
        }
        input = out;
    }

    //判断是否是字母
    boolean isLetter(char letter) {
        return (letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z');
    }

    //判断是否是数字
    boolean isDigit(char digit) {
        return digit >= '0' && digit <= '9';
    }
}
