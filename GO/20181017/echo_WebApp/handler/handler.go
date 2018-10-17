package handler

import (
	"net/http"

	"github.com/labstack/echo"
)

//c をいじって Request, Responseを色々する
func MainPage() echo.HandlerFunc {
	return func(c echo.Context) error {
		return c.String(http.StatusOK, "Hello World")
	}
}
