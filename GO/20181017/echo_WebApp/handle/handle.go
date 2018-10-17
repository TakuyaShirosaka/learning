package handle

import (
	"net/http"

	"github.com/labstack/echo"
)

func MainPage() echo.HandlerFunc {
	return func(c echo.Context) error {
		//プレースホルダusernameの値取り出し
		username := c.Param("username")
		day := c.Param("day")
		return c.String(http.StatusOK, "Hello World "+username+" "+day)
	}
}
