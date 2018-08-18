package food

// strings converter ライブラリ
import "strconv"

type (
	InterfaceFood interface {
		Get() string
	}

	Food struct {
		Volume int
	}

	Meat struct {
		Food
	}

	Fish struct {
		Food
	}

	Mud struct {
		Food
	}
)

func (m Meat) Get() string {
	// strconv.Itoa で  int型 から string型に変換する
	return "this is a meat! volume: " + strconv.Itoa(m.Volume) + "g"
}

func (f Fish) Get() string {
	return "this is a fish! volume: " + strconv.Itoa(f.Volume) + "g"
}

func (m Mud) Get() string {
	return "this is a mud.... volume: " + strconv.Itoa(m.Volume) + "g"
}
