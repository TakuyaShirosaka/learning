package main

//echoフレームワークを使用した。
//HelloWorld

import (
    "net/http"
    "github.com/labstack/echo"
)


type(
    Response struct{
        Name string
    }
)


func main() {
    e := echo.New()
    
    //下記のURLでサーバーを立ち上げる
    //http://localhost:1323/
    
    e.GET("/", func(c echo.Context) error {
        return c.String(http.StatusOK, "Hello, World!")
	})
	e.GET("/test", func(c echo.Context) error {
        return c.String(http.StatusOK, "testを表示するよ！")
    })
    e.GET("/api/users/:name", func(c echo.Context) error {
        name := c.Param("name")
        return c.JSON(http.StatusOK, Response{Name: name})
    })
    e.Logger.Fatal(e.Start(":1323"))
}











