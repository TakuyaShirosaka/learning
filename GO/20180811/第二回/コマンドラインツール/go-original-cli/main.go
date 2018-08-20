package main

/*
	【Golangで作るコマンドラインツール】

	今回はcliパッケージを使用し、
	NameやAuthor等の基本情報の設定
	独自コマンド"get","metal"の実装をしています。

	*dep init で依存関係の解決を行ってください。

*/

import (
	"fmt"
	"log"
	"os"
	"scraping"

	"github.com/urfave/cli"
)

func main() {

	//appに対して設定する
	app := cli.NewApp()
	app.Name = "HSCLT: Hyper Scraping Command Line Tool!"
	app.Author = "Type YourName Here!"
	app.Email = "Type YourEmail"
	app.Version = "0.10"
	app.Copyright = "Copyright(c) 2018 " + app.Author
	app.Usage = "you'll scraping easy!"

	//app.Comands で独自コマンドを追加できる
	app.Commands = []cli.Command{
		{
			// # get の後に google.com と書くと、scaping google.com と表示されました
			// ./go-original-cli get googel.com
			//コマンド名
			Name: "get",

			//短縮コマンド名
			Aliases: []string{"g"},

			//説明文
			Usage: "scraping website! ",

			//実行処理
			Action: func(c *cli.Context) error {
				// c.Args() で引数取得
				if website := c.Args().First(); website == "" {
					// 引数がなかった場合はエラー
					return cli.NewExitError("get command have to arg website. please read help by --help", 1)
				}

				// 処理結果を出力
				fmt.Printf("scaping %s\n", c.Args().First())

				//nil は空を表す特別なデータと考えてください。
				return nil
			},
		},
		{
			//コマンド名
			Name: "metal",

			//短縮コマンド名
			Aliases: []string{"m"},

			//説明文
			Usage: "scraping website! ",

			//実行処理
			Action: func(c *cli.Context) error {

				//スクレイピング
				scraping.Scrape()

				//nil は空を表す特別なデータと考えてください。
				return nil
			},
		},
	}

	err := app.Run(os.Args)
	if err != nil {
		log.Fatal(err)
	}
}
