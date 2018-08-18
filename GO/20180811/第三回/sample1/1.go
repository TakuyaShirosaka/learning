// +build ignore

package main

import (
    "fmt"
    "time"
)

func main() {
    fmt.Println("Start!")
    // boolの型でchannelを作成する
    ch := make(chan bool)
    // goroutineを生成して、サブスレッドで処理する
    go func() {
        time.Sleep(2 * time.Second)
        // chに対してtrueを投げる（送信）
        ch <- true
    }()
    // chに受信があったらisFinに返事する。
    // 受信があるまで、処理をブロックし続ける（これで同期が取れる）
    isFin := <-ch // <-chだけでもブロック出来る

    // chをクローズする
    close(ch)

    // 受信した値をprintする
    fmt.Println(isFin)
    fmt.Println("Finish!")
}