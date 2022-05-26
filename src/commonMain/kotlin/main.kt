import basic.*
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.microseconds
import com.soywiz.klock.seconds
import com.soywiz.korau.sound.readMusic
import com.soywiz.korau.sound.readSound
import com.soywiz.korge.*
import com.soywiz.korge.service.storage.storage
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.async.ObservableProperty
import com.soywiz.korio.async.delay
import com.soywiz.korio.async.launch
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Rectangle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay

val score1 = ObservableProperty(0)
val best = ObservableProperty(0)

suspend fun main() = Korge(width = 990, height = 2040, bgcolor = Colors["#6495ed"]){

	/*val sound = resourcesVfs["backgroundmusic.mp3"].readMusic()
	sound.play()*/

	/*runBlockingNoJs {
		GlobalScope.launch {
			delay(1.seconds)

		}
	}*/


	val game = GameManager(this)
	val player = Player(game)
	val world = GameWorld(game)

	val scoreBoard = ScoreBoard(game)
	addChild(scoreBoard)
	scoreBoard.doDay()
	addChild(world)
	addChild(player)
	world.init()
	player.init()


	// Сохранение лучшего счёта в игре
	val storage = views.storage
	best.update(storage.getOrNull("key")?.toInt() ?: 0) // достаем по ключику

	score1.observe {
		if (score > best.value) best.update(score)
	}

	best.observe {
		storage["key"] = it.toString() // убираем по ключику
	}

	text(best.value.toString(), 30.0, Colors.WHITE) {
		setTextBounds(Rectangle(0.0, 0.0,150.0, 10.0))
		alignment = TextAlignment.MIDDLE_CENTER
		position(200.0,117.0) // 40 107
		best.observe {
			text = it.toString()
		}
	}

	var score2 = 0
	addFixedUpdater(30000.microseconds) {
		score2++
		score1.update(score)
	}
}

