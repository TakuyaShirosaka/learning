package main

// Webサーバ APIサーバ 拡張 - Web サーバから API を叩いて値を返す

import (
    "html/template"
    "io"
    "net/http"
    "github.com/labstack/echo"
)

type(
    Response struct{
        Name string
    }
    ErrorResponse struct{
        ErrorCode int
        Message string
    }
    AnswerRequest struct {
        Name string `json:"name" query:"name"`
    }
    AnswerResponse struct {
        MyName string `json:"my_name"`
        YourName string `json:"your_name"`
    }
)

type TemplateRenderer struct{
    templates *template.Template
}

func (t *TemplateRenderer) Render(w io.Writer, name string, data interface{}, c echo.Context) error {
    if viewContext, isMap := data.(map[string]interface{}); isMap {
        viewContext["reverse"] = c.Echo().Reverse
    }
    return t.templates.ExecuteTemplate(w, name, data)
}

func main() {
    e := echo.New()

    renderer := &TemplateRenderer{
             templates: template.Must(template.ParseGlob("../public/views/*.html")),
    }
    e.Renderer = renderer

    //下記のURLでサーバーを立ち上げる
    //http://localhost:1323/api-test
    
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
    e.GET("/api-test", func(c echo.Context) error {
             return c.Render(http.StatusOK, "api-test.html", map[string]interface{}{
                 "content-name": "GO Sample",
             })
    })
    
    e.POST("/api/users/request", func(c echo.Context) error {
        request := new(AnswerRequest)
        if err := c.Bind(request); err == nil {
            return c.JSON(http.StatusBadRequest, ErrorResponse{100, "エラーです"})
        }

        response := new(AnswerResponse)
        response.MyName = "XXXXX"
        response.YourName = request.Name
           
        //ip := net.ParseIP(c.RealIP()).To4()  
        //log.Infof("貴方のサーバにアクセスがありました: {name: %s, IPAddr: %s", request.Name, ip)
           
        return c.JSON(http.StatusOK, response)
    })

    e.Logger.Fatal(e.Start(":1323"))
}











