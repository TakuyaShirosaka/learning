abstract class Food {
    Number volume;
    
    Food(Number volume) {
        this.volume = volume;
    }
    
    abstract String get();
}

class Meat extends Food {
    Meat(Number volume) {
        super(volume);
    }
    
    String get() {
        return "this is a meat! volume: " + this.volume.toString() + "g";
    }
}

class Fish extends Food {
    Fish(Number volume) {
        super(volume);
    }
    
    String get() {
        return "this is a fish! volume: " + this.volume.toString() + "g";
    }
}

class Mud extends Food {
    Mud(Number volume) {
        super(volume);
    }

    String get() {
        return "this is a mud.... volume: " + this.volume.toString() + "g";
    }
}