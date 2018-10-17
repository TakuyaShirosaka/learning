package main

import (
	"fmt"
	"os"
	"time"

	"github.com/dropbox/dropbox-sdk-go-unofficial/dropbox"
	"github.com/dropbox/dropbox-sdk-go-unofficial/dropbox/files"
)

func main() {
	config := dropbox.Config{
		Token:    "zBlS15C0FXAAAAAAAAAAU8Yrei65ZE33HrgZ3KijHOriTGIlPgvbIBGXm1kMHGjo",
		LogLevel: dropbox.LogInfo, // if needed, set the desired logging level. Default is off
	}

	//Configからクライアント生成
	cli := files.New(config)

	//↓APIの実行
	//プログラムからは*アプリケーションフォルダしか見れないので注意
	//ルートフォルダだとブランクを指定
	arg := files.NewListFolderArg("")

	//再帰読み込みを行う
	arg.Recursive = true

	//実行
	res, err := cli.ListFolder(arg)

	//エラー処理
	if err != nil {
		fmt.Println("err: %s", err)
		os.Exit(0)
	}

	//結果を出力
	printFiles(res.Entries)

}

func printFiles(entries []files.IsMetadata) {
	for _, entry := range entries {
		switch f := entry.(type) {
		case *files.FileMetadata:
			printFile(f)
		case *files.FolderMetadata:
			printFolder(f)
		}
	}
}

func printFile(f *files.FileMetadata) {
	fmt.Printf("-rw-r--r--\t%d\t%s\t%s\n",
		f.Size,
		f.ServerModified.Format(time.RubyDate),
		f.PathDisplay)
}

func printFolder(f *files.FolderMetadata) {
	fmt.Printf("drw-r--r--\t%d\t%s\t%s\n",
		0,
		"",
		f.PathDisplay)
}
