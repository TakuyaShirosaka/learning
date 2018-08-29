package main

import (
	"fmt"
	"os"

	"github.com/dropbox/dropbox-sdk-go-unofficial/dropbox"
	"github.com/dropbox/dropbox-sdk-go-unofficial/dropbox/files"
)

func main() {
	//Token:DropBoxの開発者登録の際に生成できる値
	config := dropbox.Config{
		Token:    "アクセスToken",
		LogLevel: dropbox.LogInfo, // if needed, set the desired logging level. Default is off
	}

	//configを使用して、clientを生成
	cli := files.New(config)

	//NewCommitInfoを使用して、格納先を指定する。
	/*
		NewCommitInfo()
		"/" + ファイルパス
		上記の指定で
		DropBox/アプリ/アプリ名/(指定したファイルパス)
		の場所に格納される、ルートフォルダは指定しないこと、サポートされてません。
	*/
	req := files.NewCommitInfo("/log/log.txt")

	/*
		Open()
		実際にアップロードするファイルを指定する。
		こちらは通常のパスの指定でよい
	*/
	f, _ := os.Open("./log.txt")

	//アップロード処理実行、レスポンスとエラーが戻り値として返却されるため受ける
	res, err := cli.Upload(req, f)

	//エラー処理
	if err != nil {
		fmt.Printf("\n\n*** Error %v\n\n", err)
		return
	}
	fmt.Printf("\n\n%#v\n\n", res)
}
