import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tictactoe extends JFrame {
    static JButton [] jb = new JButton[9];//九个按钮作为棋盘的落点
    static boolean flag = true;//"true"表示O方，"false"表示X方
    static boolean tag = true;//"true"代表人人对战,"false"代表人机对战
    static final int VALUE = 100;
    static final int WIN = +VALUE; //O方获胜
    static final int LOSE = -VALUE;//X方获胜
    static final int DRAW = 0;//平局

    static final int[][] WON = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    public Tictactoe()//构造方法
    {
        this.setTitle("井字棋小游戏");//标题
        this.setSize(400, 400);//设置棋盘的大小
        this.setLayout(new GridLayout(3, 3));//网格式布局以便作为九宫格棋盘
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//使得关闭弹窗的按钮有效

        //把按钮加到棋盘上
        for(int i = 0; i < 9; i++)
        {
            jb[i] = new JButton("");
            jb[i].addActionListener(new Click());
            this.add(jb[i]);
        }
        this.setVisible(true);
    }

    public static void Init(JButton [] jb)
    {
        Object [] options = {"人人对战", "人机对战"};
        int m = JOptionPane.showOptionDialog(null,"人人对战 or 人机对战?", "提示", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, options,null);
        for(int i = 0; i < 9; i++)
        {
            jb[i].setText("");
            jb[i].setEnabled(true);
        }
        if(m == JOptionPane.YES_OPTION)
        {
            tag = true;
        }
        else
        {
            tag = false;
        }
    }

    public int Judge(JButton[] jb)
    {
        int result = 0;//用来判断哪方获胜或是平局

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                if(jb[WON[i][j]].getText() == "O")
                {
                    result ++;
                }
                else if(jb[WON[i][j]].getText() == "X")
                {
                    result --;
                }
            }
            if(result == 3)
            {
                return WIN;
            }
            else if(result == -3)
            {
                return LOSE;
            }
            else
            {
                result = 0;
            }
        }
        for(int i = 0; i < 9; i++) {
            if (jb[i].getText() == "") return 1;
        }
        return DRAW;
    }

    public int minimax(JButton[] jb)
    {
        int bestValue = VALUE;
        int index = 0;

        for(int i = 0; i < 9; i++)
        {
            if(jb[i].getText() == "")//每个空的位置试探性的填上X
            {
                jb[i].setText("X");
                int value = max(jb);//按最坏的情况搜索，也就是对手总是极大化自己的价值
                if(value < bestValue)//保持不败
                {
                    bestValue = value;
                    index = i;
                }
                jb[i].setText("");
            }
        }
        return index;
    }

    public int min(JButton [] jb)//极小搜索
    {
        int evalValue = Judge(jb);
        if(evalValue == WIN)//由于状态空间较小，可直接搜索到终局
        {
            return WIN;
        }
        else if(evalValue == LOSE)
        {
            return LOSE;
        }
        else if(evalValue == DRAW)
        {
            return DRAW;
        }
        else
        {
            int bestValue = VALUE;
            for(int i = 0; i < 9; i++)
            {
                if(jb[i].getText() == "")
                {
                    jb[i].setText("X");
                    bestValue = Math.min(bestValue, max(jb));//作为X方，会极小化对方的利益
                    jb[i].setText("");
                }
            }
            return bestValue;
        }
    }

    public int max(JButton [] jb)//极大搜索
    {
        int evalValue = Judge(jb);
        if(evalValue == WIN)//由于井字棋搜索空间较小，可以直接搜索到终局
        {
            return WIN;
        }
        else if(evalValue == LOSE)
        {
            return LOSE;
        }
        else if(evalValue == DRAW)
        {
            return DRAW;
        }
        else
        {
            int bestValue = -VALUE;
            for(int i = 0; i < 9; i++)
            {
                if(jb[i].getText() == "") {
                    jb[i].setText("O");
                    bestValue = Math.max(bestValue, min(jb));//作为O方会极大化自己的利益
                    jb[i].setText("");
                }
            }
            return bestValue;
        }
    }

    public class Click implements ActionListener//监听按钮被点击后作出响应
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i = 0;i < 9; i++)
            {
                if(e.getSource() == jb[i])//如果某个按钮被单击，根据flag判断落下O还是X
                {
                    if(tag) {//如果是人人对战
                        if (flag) {
                            jb[i].setText("O");
                            flag = false;
                        } else {
                            jb[i].setText("X");
                            flag = true;
                        }
                        jb[i].setEnabled(false);//被单机过的按钮不可用
                    }
                    else {
                        jb[i].setText("O");
                        jb[i].setEnabled(false);//被单机过的按钮不可用
                        int GameState = Judge(jb);
                        if (GameState == 1) {
                            int Next_Step = minimax(jb);
                            jb[Next_Step].setText("X");
                            jb[Next_Step].setEnabled(false);
                        }
                    }
                }

            }

            int GameState = Judge(jb);
            switch (GameState)
            {
                case WIN:
                    JOptionPane.showMessageDialog(null,"O方获胜","提示",JOptionPane.DEFAULT_OPTION);
                    break;

                case LOSE:
                    JOptionPane.showMessageDialog(null,"X方获胜","提示",JOptionPane.DEFAULT_OPTION);
                    break;

                case DRAW:
                    JOptionPane.showMessageDialog(null,"平局","提示",JOptionPane.DEFAULT_OPTION);
                    break;

                case 1:
                    break;
            }

            if(GameState == WIN || GameState == LOSE || GameState == DRAW)
            {
                int over = JOptionPane.showConfirmDialog(null,"再来一局？", "提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(over == JOptionPane.YES_OPTION)
                {
                    Init(jb);
                }
                else
                {
                    System.exit(0);
                }
            }
        }
    }

    public static void main(String [] args)
    {
        new Tictactoe();
        Init(jb);
    }
}
