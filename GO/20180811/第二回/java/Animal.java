abstract class Animal {
    private String name;
    
    Animal(String name) {
        this.name = name;
    }
    
    public String getName() {
        return "I'm " + this.name;
    }

    abstract public String say();
}

class Dog extends Animal {
    Dog(String name) {
        super(name);
    }
    
    public String say() {
        return "ワン";
    }
}

class Cat extends Animal {
    Cat(String name) {
        super(name);
    }
    
    public String say() {
        return "ニャー";
    }
}