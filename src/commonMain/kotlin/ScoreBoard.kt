import com.soywiz.klock.TimeSpan
import com.soywiz.klock.microseconds
import com.soywiz.klock.milliseconds
import com.soywiz.klock.timesPerSecond
import com.soywiz.klogger.AnsiEscape
import com.soywiz.klogger.Console.color
import com.soywiz.korge.service.storage.storage
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.async.ObservableProperty
import com.soywiz.korio.async.delay
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Rectangle
import com.soywiz.korui.layout.Length



var score: Int=0
class ScoreBoard(private val game: GameManager): Container() {
    private var bestScore: Int = 0
    val bgScore = roundRect(300.0, 60.0, 5.0, fill = Colors["#2F4F4F"]).xy(30,30)
    val bgbestScore = roundRect(350.0, 60.0, 5.0, fill = Colors["#2F4F4F"]).xy(30,100) // Colors.DARKSLATEBLUE
    private var scoreView: Text = text("СЧЁТ $score", textSize = 30.0).xy(40.0,37.0)
    private var bestScoreView: Text = text("ЛУЧШИЙ СЧЁТ", textSize = 30.0).xy(40.0,107.0)




    init {
        addFixedUpdater(30000.microseconds) {
            if(game.status == GameStatus.RESTARTED) {
                score = 0
                val sky = SolidRect(10000,10000,Colors["#6495ed"]).xy(0,0)
                addChild(sky)
                addScore()
                doDay()
            }
            else if(game.status == GameStatus.RUNNING) {
                score++
                if(score % 700 == 0){
                    val sky = SolidRect(10000,10000,Colors.DARKBLUE).xy(0,0)
                    addChild(sky)
                    addScore()
                    doNight()
                }
                if(score % 1400 == 0){
                    val sky = SolidRect(10000,10000,Colors["#6495ed"]).xy(0,0)
                    addChild(sky)
                    addScore()
                    doDay()
                }
                if(score>bestScore)
                    bestScore=score
                scoreView.text = "СЧЁТ\t$score"
                bestScoreView.text = "ЛУЧШИЙ СЧЁТ\t"
            }
        }
    }

    fun addScore(){
        addChild(bgScore)
        addChild(bgbestScore)
        addChild(scoreView)
        addChild(bestScoreView)
    }


    fun doDay() {
        val grass = SolidRect(10000, 10000, Colors.DARKGREEN).xy(0, 1500)
        val soil = SolidRect(10000, 10000, Colors["#996600"]).xy(0, 1550)

        val sun = Circle(30.0, Colors.GOLD).xy(800, 50)
        val luch6 = Line(830.0,80.0,800.0,50.0,Colors.GOLD).scale(1.3)
        val luch2 = Line(830.0,80.0,800.0,80.0,Colors.GOLD).scale(2.0)
        val luch8 = Line(830.0,80.0,800.0,110.0,Colors.GOLD).scale(1.3)
        val luch3 = Line(830.0,80.0,830.0,50.0,Colors.GOLD).scale(2.0)
        val luch4 = Line(830.0,80.0,830.0,110.0,Colors.GOLD).scale(2.0)
        val luch7 = Line(830.0,80.0,860.0,50.0,Colors.GOLD).scale(1.3)
        val luch1 = Line(830.0,80.0,860.0,80.0,Colors.GOLD).scale(2.0)
        val luch5 = Line(830.0,80.0,860.0,110.0,Colors.GOLD).scale(1.3)
        addChild(grass)
        addChild(soil)
        addChild(sun)
        addChild(luch1)
        addChild(luch2)
        addChild(luch3)
        addChild(luch4)
        addChild(luch5)
        addChild(luch6)
        addChild(luch7)
        addChild(luch8)
    }

    fun doNight(){
        val grass = SolidRect(10000,1000,Colors.DARKGREEN).xy(0,1500)
        val soil = SolidRect(10000,1000,Colors["#996600"]).xy(0,1550)
        val luna = Circle(40.0, Colors["#f0ffff"]).xy(800,50)
        val luna1 = Circle(8.0, Colors.LIGHTGRAY).xy(815,70)
        val luna2 = Circle(6.0, Colors["#a9a9a9"]).xy(835,70)
        val luna3 = Circle(7.0, Colors["#808080"]).xy(850,90)
        val luna4 = Circle(5.0, Colors["#808080"]).xy(850,65)
        val luna5 = Circle(10.0, Colors["#a9a9a9"]).xy(820,100)
        addChild(grass)
        addChild(soil)
        addChild(luna)
        addChild(luna1)
        addChild(luna2)
        addChild(luna3)
        addChild(luna4)
        addChild(luna5)
    }

}