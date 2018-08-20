package scraping

/*
	スクレイピングツール
	http://metalsucks.net から情報を抜き出す。
	抜き出す情報はAlbumReview
    goquery:jQuery的にスクレイピングやクローリングができるライブラリです。
*/

import (
	"fmt"
	"log"
	"net/http"

	"github.com/PuerkitoBio/goquery"
)

func Scrape() {
	// Get()内のURLにリクエスト
	res, err := http.Get("http://metalsucks.net")
	if err != nil {
		log.Fatal(err)
	}
	defer res.Body.Close()
	if res.StatusCode != 200 {
		log.Fatalf("status code error: %d %s", res.StatusCode, res.Status)
	}

	// 取得したHTMLの読み込み
	doc, err := goquery.NewDocumentFromReader(res.Body)
	if err != nil {
		log.Fatal(err)
	}

	// 下記の要素の中からテキストを取得
	// イメージは.sidebar-reviews内のarticle内の.content-block　class
	doc.Find(".sidebar-reviews article .content-block").Each(func(i int, s *goquery.Selection) {
		// リンク要素のテキストとイタリック要素のテキストを取得
		band := s.Find("a").Text()
		title := s.Find("i").Text()
		//%d :基数10 ,%s :文字列またはスライスそのまま
		fmt.Printf("Review %d: %s - %s\n", i, band, title)
	})
}
