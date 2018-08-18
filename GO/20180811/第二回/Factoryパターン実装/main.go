package main

//importは環境に合わせて修正
import (
	"fmt"

	"./animal"
	"./factory"
	"./food"
)

func main() {
	// petType, foodType が引数で与えられたとします
	petType := animal.Dog{}
	foodType := food.Meat{}

	// factory の instance を取得します
	fa := factory.NewFactory(petType, foodType)

	// factory から Animal型 の instance を取得します
	a := fa.AnimalFactoryMethod()

	// factory から Food型 の instance を取得します
	f := fa.FoodFactoryMethod()

	// Animal の名前を取得します
	fmt.Println(a.GetName())

	// 鳴き声です
	fmt.Println(a.Say())

	// 今日の食べ物です
	fmt.Println(f.Get())
}
