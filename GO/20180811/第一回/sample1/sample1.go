package main
import "fmt"

type (
    Animal struct {
        name string
    }
    Dog struct {
        Animal
    }
)

func (a *Animal) GetName() string {
    return "I’m " + a.name
}

func main() {
    animal := Animal{name: "dog"}

    // 出力: I'm Dog
    fmt.Println(animal.GetName())

    dog := new(Dog)
    dog.name = "dog"

    // 出力: I'm Dog
    fmt.Println(dog.GetName())
}