
import com.soywiz.klock.TimeSpan
import com.soywiz.korge.service.storage.storage
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.delay
import com.soywiz.korio.async.runBlockingNoJs
import com.soywiz.korma.geom.Rectangle

class GameManager(private val container: Container) {



    var isRunning = false
    var status = GameStatus.NOT_STARTED
    private var mess: Text? = null
    private var mes: Text? = null

    fun goStartMessage(){
        if(mes == null){
            val t = container.text("Чтобы начать игру нажмите на экран!", textSize = 30.0)
            t.centerOnStage()
            container.addChild(t)
            mes = t
        }
    }

    fun stopStartMessage(){
        container.removeChild(mes)
        mes = null
    }

    fun start(){
        isRunning = true
        status = GameStatus.RUNNING
    }




    fun finish() {
        isRunning = false
        status = GameStatus.FINISHED
        if(mess == null) {
            val text = container.text("\t\t\t\t\t\t\t\t\t\tИгра окончена\nНажмите, чтобы играть снова\n\t\t\t\t\t\t\t\t\t\tВаш счёт: $score", textSize = 30.0)
            text.centerOnStage()
            container.addChild(text)
            mess = text
        }
    }

    suspend fun restart() {
        status = GameStatus.RESTARTED
        container.removeChild(mess)
        mess = null
        delay(TimeSpan(300.0))
        start()
    }




}