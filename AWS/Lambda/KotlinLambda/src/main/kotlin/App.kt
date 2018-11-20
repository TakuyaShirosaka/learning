import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger


public class App {
	public fun handler(count: Int, context: Context): String {
		val lambdaLogger = context.getLogger()
		lambdaLogger.log("Kotlinモジュールですよ！！！\n")
		lambdaLogger.log("count = " + count +"\n")
		lambdaLogger.log("インプットの3倍の値が出てきますよ！、テストパラメタは全て第一引数に入ってくるようです。\n")
		return "${count * 3}"
	}
}