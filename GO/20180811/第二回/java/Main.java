public class Main {
    public static void main(String[] args) throws Exception {
        // petType, foodType が引数で与えられたとします
        String petType = "dog";
        String foodType = "meat";
        
        // animal food の変数を初期化
        Animal animal = null;
        Food food = null;
        
        // pwrType によって Animal,　Food の instane を取得します
        switch(petType) {
        case "dog":
            // petType == "dog" の場合、Dogクラス の instance を取得
            animal = new Dog("dog");
            
            // petType == "dog" の場合の foodType ごとの適量な Foodクラス の instance を取得します
            switch(foodType) {
            case "meat":
                // 100g のお肉
                food = new Meat(100);
                break;
            case "fish":
                // 40g の魚
                food = new Fish(40);
                break;
            default:
                // 適切な Case がない場合、なんと 300g の泥……。
                food = new Mud(300);
            }
            break;
        case "cat":
            // petType == "cat" の場合、Catクラス の instance を取得
            animal = new Cat("cat");
            
            // petType == "cat" の場合の foodType ごとの適量な Foodクラス の instance を取得します
            switch(foodType) {
            case "meat":
                // 20g のお肉
                food = new Meat(20);
                break;
            case "fish":
                // 80g の魚
                food = new Fish(80);
                break;
            default:
                // 適切な Case がない場合、なんと 100g の泥……。
                food = new Mud(100);
            }
            break;
        default:
            // 適切な Case がない場合、なんとモンスターが……
            animal = new Monster("monster");
            switch(foodType) {
            case "meat":
                food = new Meat(0);
                break;
            case "fish":
                food = new Fish(0);
                break;
            default:
                food = new Mud(1000);
            }
        }
        
        // Animal の名前を取得します
        System.out.println(animal.getName());
        
        // 鳴き声です
        System.out.println(animal.say());
        
        // 今日の食べ物です
        System.out.println(food.get());
    }
}