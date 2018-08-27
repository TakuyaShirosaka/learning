/*
	パッケージを作る際、柔軟性を持たせるためにオプションを持たせたい時がしばしばあります。
	しかしオプションは知っての通り設定しないことが少なくありません。
	JavaなどではOptional Parameterなどのように、デフォルト値が指定できる機能があります。
	go言語では"Self Referential Functions Design"というテクニックがあり、
	オプションと相性が非常に良いため、合わせて"Functional Option Pattern"とも呼ばれています。

	<メリット>
	１．デフォルト値 (完全に独立したデフォルト値が指定できる)
	２．設定自由度 (自由な組み合わせが指定できる)
	３．拡張性 (オプションが増えても関数を増やすだけ)
	４．自己説明能力 (パラメータの名称が明示されます)
	５．安全性 (コンフィグをポインタで渡したような挙動の推測しづらい弄り方ができません)
	６．不要な引数の完全な排除 (設定しない項目はもちろん、全て設定しない場合も隠蔽できます)
*/

package main

import (
	"flag"
	"fmt"
	"log"
	"net/http"
	"strconv"
)

// これは、ユーザーが設定できるオプションを持つstructです。
type Server struct {
	Host string
	Port int
}

// オプションhostの値を取る
func Host(host string) func(*Server) {
	return func(s *Server) {
		fmt.Printf("server host: %s \n", host)
		s.Host = host
	}
}

// オプションportの値を取る
func Port(port int) func(*Server) {
	return func(s *Server) {
		fmt.Printf("port: %d \n", port)
		s.Port = port
		http.ListenAndServe(":"+strconv.Itoa(s.Port), nil)
	}
}

// ルーティングの登録
func HandleAddGrp() {
	fmt.Println("HandleAddGrp start")
	http.HandleFunc("/", HelloServer)
	http.HandleFunc("/test", TestServer)
}

// これは、オプション関数のリストを受け取る関数です。
// Serverが持つ要素に紐づく関数を実行していきます。
func NewServer(opts ...func(*Server)) *Server {
	s := &Server{}

	// 渡されたオプション関数を実行していく
	for _, opt := range opts {
		opt(s)
	}

	return s
}

// http.HandleFuncに登録する関数 ,http.ResponseWriterとhttp.Requestを受ける
func HelloServer(w http.ResponseWriter, r *http.Request) {
	fmt.Println("Hello, World")
}

func TestServer(w http.ResponseWriter, r *http.Request) {
	fmt.Printf("%s :TEST\n", w)
}

func main() {

	//flagパッケージはコマンドパラメータの解析を行う
	//第一引数はオプション名です
	//第二引数はオプションのデフォルト値です。オプションを指定しない場合は、デフォルト値がオプションの値に入ります。
	//第三引数は、オプションの説明を記述しています。記述したメッセージは、flagパッケージの機能であるhelpオプションを使用した際に扱われます。※後述の「helpオプション」を参照

	var host = flag.String("host", "127.0.0.1", "host")
	var port = flag.Int("port", 8080, "port")

	//パラメータの解析、flagを使用する場合は必須
	flag.Parse()

	// ログ出力
	log.Printf("Start Go HTTP Server")

	// ルーティングの登録関数
	HandleAddGrp()

	// structに入力値を値渡し、NewServer側でオプションの設定を実行する
	s := NewServer(
		Host(*host),
		Port(*port),
	)

	// エラー処理
	if s != nil {
		log.Fatal("ListenAndServe: ", s)
	}

}
