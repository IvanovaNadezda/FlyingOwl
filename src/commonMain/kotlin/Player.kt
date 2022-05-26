import com.soywiz.klock.TimeSpan
import com.soywiz.korau.sound.Sound
import com.soywiz.korau.sound.readSound
import com.soywiz.korev.Key
import com.soywiz.korge.input.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs


class Player(private val game: GameManager): Container() {

    var status = PlayerStatus.RUNNING
    private var speedOwl = 7.0
    private val upperBound = 1090.0
    private val appearanceY = 1350.0
    //private lateinit var protivnik:Protivnik
    private lateinit var owl: Sprite


    private suspend fun buildOwl(): Sprite {
        val img = resourcesVfs["sovi_.png"].readBitmap()
        val fly = SpriteAnimation(img,140,90, columns = 2)
        val sprite = sprite(fly)
        sprite.xy(40.0,appearanceY)
        return sprite.scale(0.5)
        //return image(img).xy(40.0,initialY).scale(0.12)
    }

    suspend fun init() {
        val prep_sound = resourcesVfs["coin.mp3"].readSound()
        //val dead_sound = resourcesVfs["dead.mp3"].readSound()
        val sky = SolidRect(10000,10000).xy(0,0).alpha(0.0)
        addChild(sky)
        owl = buildOwl()
        //protivnik.buildProtivnik()
        game.goStartMessage()

        sky.touch{
            onDown{
                if (game.status == GameStatus.NOT_STARTED) {
                    game.stopStartMessage()
                    game.start()
                }

                if(game.status == GameStatus.RUNNING) {
                    val owlY = owl.y
                    if (owlY >= 0) {
                        status = PlayerStatus.JUMPING_UP
                        prep_sound.play()
                    }
                }
                if (game.status == GameStatus.FINISHED) {
                    game.restart()
                }
            }
        }

        owl.addUpdater {
            if (game.isRunning == true) {
                owl.position(getCoordinates(x,y).x,getCoordinates(x,y).y)
            }
        }

        owl.onCollision({ it.name == "trees" }) {

            game.finish()
            stopAnimation()
            //val finishGame = SolidRect(10000,1000, Colors.DARKORCHID).xy(0,0)
            //addChild(finishGame)
        }


    }

    private fun goAnimation() {
        owl.playAnimationLooped(spriteDisplayTime = TimeSpan(200.0))
        //protivnik.goAnimation()
    }

    private fun stopAnimation() {
        owl.stopAnimation()
        //protivnik.stopAnimation()
    }

    private fun getCoordinates(startingX: Double, startingY: Double): Coordinates {
        val x: Double = startingX
        var y: Double = startingY

        if (status == PlayerStatus.JUMPING_UP) {
            y -= speedOwl
            if (y <= upperBound) {
                status = PlayerStatus.JUMPING_DOWN
            }
        }
        else if(status == PlayerStatus.JUMPING_DOWN){
            y += speedOwl
            if (y >= appearanceY) {
                status = PlayerStatus.RUNNING
                goAnimation()
            }
        }

        return Coordinates(x, y)
    }
}

class Coordinates(val x: Double, val y: Double)



