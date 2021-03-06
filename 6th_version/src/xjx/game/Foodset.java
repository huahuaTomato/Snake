package xjx.game;

import java.awt.Image;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Foodset {
    private Scene GameUI;//母窗体,即游戏主界面
    private List<Food> food = new LinkedList<>();//用于描述食物的数组
    private Vector<Coordinate> food_coors = new Vector<>();
    private Vector<JLabel> food_labels = new Vector<>();
    private static final int FOODKIND = 6;
    private int[] point = new int[]{50, 40, 30, 20, 10, 0};//6中食物各自对应的得分
    private ImageIcon[] foodIcon = new ImageIcon[FOODKIND];//6种食物各自对应的图标

    public Foodset(Scene GameUI){
        this.GameUI = GameUI;
        //加载6张食物的图片
        for(int i = 0;i < FOODKIND;i++) {
            foodIcon[i] = new ImageIcon("food//food" + i + ".png");
            foodIcon[i].setImage(foodIcon[i].getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));//保持图片的清晰
        }

        produceFood();
//        produceFood_test();
        show();
    }

    public int getFoodPoint(Coordinate coor){
        /*给定界面上的一个点，判断该点是否有食物存在，若有，则返回对应食物的得分，否则返回-1
         * 注意coor.x代表横向的序号，从左到右依次为[0,WIDTH-1]
         * coor.y代表纵向的序号，从上到下依次为[0,HEIGHT-1]
         */
        for (Iterator<Food> iter = food.iterator(); iter.hasNext();) {
            Food node = iter.next();
            if(node.coor.x == coor.x && node.coor.y == coor.y) {
                node.label.setVisible(false);//从界面上移除食物
                GameUI.remove(node.label);
                iter.remove();//从food数组中移除被吃掉的食物

                return point[node.kind];//返回该食物对应的分数
            }
        }
        return -1;
    }

    public void produceFood_test(){
        int amount = 2, foodtag = 0;
        Food newfood;

        for(int i = 0; i < amount; i++) {
            Coordinate coor;
            if (i == 0) coor = new Coordinate(8, 4);
            else coor = new Coordinate(8, 0);
            food_coors.add(coor);
            Coordinate _coor = new Coordinate(coor.y,coor.x);//置换行和列
            GameUI.setMap(coor.x, coor.y, 2);
            newfood = new Food(foodtag, _coor, foodIcon[foodtag]);
            food.add(newfood);
            GameUI.add(newfood.label);
            food_labels.add(newfood.label);
        }

        GameUI.updateInfos("Amount", food.size() + "");//刷新GameUI界面上显示食物数量的Label
        show();
        System.out.println("产生" + amount + "个食物\t" + GameUI.getSysTime());
        for (Coordinate node : food_coors) {
            System.out.print("(" + node.x + "," + node.y + ") ");
        }
        System.out.println();
        GameUI.PrintMap(GameUI.getMap(), "debug//map.txt");
    }

    public void produceFood(){
        Random rand = new Random();
        int amount = rand.nextInt(3) + 2;
//        int amount = 1;
        double prob;
        int foodtag = 2;
        Food newfood;

        for(int i = 0; i < amount; i++) {
            Coordinate coor = GameUI.randomCoor();//注意，coor.x是数组的行，coor.y是数组的列，和界面上的行序号和列序号正好相反
            food_coors.add(coor);

            Coordinate _coor = new Coordinate(coor.y,coor.x);//置换行和列
            prob = rand.nextDouble();
            if(prob >= 0 && prob <0.1) 		    foodtag = 0;//10%
            else if(prob >= 0.1  && prob <0.25) foodtag = 4;//15%
            else if(prob >= 0.25 && prob <0.5)  foodtag = 3;//25%
            else if(prob >= 0.5  && prob <0.8)  foodtag = 2;//30%
            else if(prob >= 0.8 && prob <0.95)  foodtag = 1;//15%
            else if(prob >= 0.95 && prob <1) 	foodtag = 5;//5%

            GameUI.setMap(coor.x, coor.y, 2);

            newfood = new Food(foodtag, _coor, foodIcon[foodtag]);
            food.add(newfood);
            GameUI.add(newfood.label);
            food_labels.add(newfood.label);
        }

        GameUI.updateInfos("Amount", food.size() + "");//刷新GameUI界面上显示食物数量的Label
        show();
        System.out.println("产生" + amount + "个食物\t" + GameUI.getSysTime());
        for (Coordinate node : food_coors) {
            System.out.print("(" + node.x + "," + node.y + ") ");
        }
        System.out.println();

        GameUI.PrintMap(GameUI.getMap(), "debug//map.txt");
    }

    public Vector<Coordinate> getFoodCoors(){
        return food_coors;
    }

    public void removeFoodCoor(Coordinate coor){
        for(int i = 0; i < food_coors.size(); i++) {
            if(food_coors.get(i).x == coor.x && food_coors.get(i).y == coor.y){
                food_coors.remove(i);
                food_labels.get(i).setVisible(false);
                GameUI.remove(food_labels.get(i));
                food_labels.remove(i);

                if(food_coors.isEmpty()){
                    food_labels.clear();
                    produceFood();
                }
                return;
            }
        }
    }

    public void show(){
        for (Food node : food) {
            node.label.setBounds(GameUI.getPixel(node.coor.x, 5, 22),
                    GameUI.getPixel(node.coor.y, 5, 22), 20, 20);
            node.label.setVisible(true);
        }
    }

    public void removeAll(){//移除界面上的所有食物图片
        for (Food node : food) {
            GameUI.setMap(node.coor.y, node.coor.x, 0);//地图上的该点重新标记为0
            node.label.setVisible(false);
            GameUI.remove(node.label);
        }
        food.clear();
    }

    //食物的数据结构
    public class Food {
        int kind;//食物种类，0-5对应5种不同的食物，见文档说明
        JLabel label;
        Coordinate coor;//坐标

        public Food(int kind,Coordinate coor,ImageIcon icon){
            this.kind = kind;
            label = new JLabel(icon);
            this.coor = coor;
        }
    }
}
