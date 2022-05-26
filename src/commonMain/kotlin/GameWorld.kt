package basic

import Arrow
import GameManager
import Tree
import com.soywiz.kds.random.FastRandom
import com.soywiz.klock.timesPerSecond
import com.soywiz.korge.view.*
import com.soywiz.korio.async.launch
import kotlinx.coroutines.*

class GameWorld(private val game: GameManager): Container() {
    private var speedWorld: Double = 4.0 // скорость мира без ускорения
    private val treePosX = 1000.0 // откуда появится первое дерево
    private val prPosX = 10000.0 // откуда появится первая стрела
    private val xStartNow = 300.0 // на что умножить рандом препятствие
    private val startY = 1310.0 // 500.0 110.0 // для совы
    private val startY1 = 1090.0 // для стрел
    private val boost = 0.0001 // ускорение игрового поля
    private var tree: MutableList<Image> = mutableListOf()
    private var pr: MutableList<Image> = mutableListOf()

    suspend fun init() {
        initWorld()
        initUpdate()
    }

    private suspend fun initWorld() {
        var prX=prPosX
        var tX = treePosX
        var i = 0
        while(i != 2){
            tree.add(Tree(tX,startY).create())
            pr.add(Arrow(prX,startY1).create())
            tX += 500
            prX+=3000
            i++
        }
        tree.forEach { addChild(it) }
        pr.forEach { addChild(it) }
        /*tree.add(Tree(obstacleX,startY).create())
        pr.add(Protivnik(prX,startY1).create())*/
    }

    private fun initUpdate() {
        addFixedUpdater(60.timesPerSecond) {
            if(game.status == GameStatus.RESTARTED) {
                stopWorld()
                speedWorld = 4.0
            }
            if(game.status == GameStatus.RUNNING) {
                speedWorld += speedWorld*boost

                val treeIterator = tree.iterator()
                while(treeIterator.hasNext()) {
                    val tree = treeIterator.next()
                    val x = tree.x - (1 * speedWorld)
                    tree.position(x, tree.y)
                    if(x < 0) {
                        treeIterator.remove()
                        removeChild(tree)
                        addTree()
                    }
                }
                val prIterator = pr.iterator()
                while(prIterator.hasNext()) {
                    val pr = prIterator.next()
                    val x = pr.x - (2 * speedWorld+boost)
                    pr.position(x, pr.y)
                    if(x < 0) {
                        prIterator.remove()
                        removeChild(pr)
                        addPr()
                    }
                }
            }
        }
    }

    private fun addPr(){
        if (game.status == GameStatus.RUNNING){
            GlobalScope.launch {
                val newPr = Arrow(prPosX+FastRandom.nextDouble() * xStartNow,startY1).create()
                addChild(newPr)
                pr.add(newPr)
            }
        }
    }


    private fun addTree() {
        if(game.status == GameStatus.RUNNING) {
            GlobalScope.launch {
                val newTree = Tree(1000.0 + FastRandom.nextDouble() * xStartNow, startY).create()
                addChild(newTree)
                tree.add(newTree)
            }
        }
    }

    private fun stopWorld() {
        tree.forEach { removeChild(it) }
        pr.forEach { removeChild(it) }
        tree = mutableListOf()
        pr = mutableListOf()

        GlobalScope.launch {
            initWorld()
        }
    }
}