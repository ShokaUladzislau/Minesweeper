import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val array = Array(11) { CharArray(11) }
    val marks = Array(11) { CharArray(11) }
    val printField = Array(11) { BooleanArray(11) }
    var markCount = 0
    var n = 0
    var minesSet = false
    initialize(array, marks, printField)
    println("How many mines do you want on the field?")
    n = scanner.nextLine().toInt()
    printEmptyField()
    while (true) {
        println("Set/unset mines marks or claim a cell as free: ")
        val input = scanner.nextLine().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (input.size != 3) {
            println("bad input")
            continue
        }
        val a = input[0].toInt()
        val b = input[1].toInt()
        if (a > 9 || a < 1 || b > 9 || b < 1) {
            println("Indexes out of range")
            continue
        }
        if (input[2] == "free") {
            if (!minesSet) {
                setMines(array, n, a, b)
                minesSet = true
            }
            updatePrintFields(array, printField, marks, a, b)
        }
        if (input[2] == "mine") {
            if (marks[b][a] == '*') {
                marks[b][a] = '.'
                markCount--
                printField[b][a] = false
            } else {
                marks[b][a] = '*'
                markCount++
                printField[b][a] = true
            }
        }
        printField(array, marks, printField, false)
        if (markCount == n && match(array, marks) || cleared(array, printField)) {
            println("Congratulations! You found all mines!")
            break
        }
    }
}

fun cleared(array: Array<CharArray>, printField: Array<BooleanArray>): Boolean {
    for (i in array.indices) {
        for (j in array[i].indices) {
            if (array[i][j] == '.' && !printField[i][j]) return false
        }
    }
    return true
}

fun initialize(array: Array<CharArray>, marks: Array<CharArray>, printField: Array<BooleanArray>) {
    for (i in array.indices) {
        for (j in array[i].indices) {
            array[i][j] = '.'
            marks[i][j] = '.'
            printField[i][j] = false
        }
    }
}

fun updatePrintFields(array: Array<CharArray>, printField: Array<BooleanArray>, marks: Array<CharArray>, a: Int, b: Int) {
    if (printField[b][a]) {
        if (marks[b][a] == '*' && (array[b][a] == '.' || array[b][a] >= '0') && array[b][a] <= '9') {
            marks[b][a] = '.'
        } else return
    }
    if (array[b][a] == 'X') {
        printField(array, Array(11) { CharArray(11) }, printField, true)
        println("You stepped on a mine and failed!")
        System.exit(0)
    } else {
        printField[b][a] = true
        if (array[b][a] >= '1' && array[b][a] <= '8') return else {
            if (b < 9) updatePrintFields(array, printField, marks, a, b + 1)
            if (a < 9) updatePrintFields(array, printField, marks, a + 1, b)
            if (a < 9 && b < 9) updatePrintFields(array, printField, marks, a + 1, b + 1)
            if (b > 1) updatePrintFields(array, printField, marks, a, b - 1)
            if (a > 1) updatePrintFields(array, printField, marks, a - 1, b)
            if (a > 1 && b > 1) updatePrintFields(array, printField, marks, a - 1, b - 1)
            if (a > 1 && b < 9) updatePrintFields(array, printField, marks, a - 1, b + 1)
            if (a < 9 && b > 1) updatePrintFields(array, printField, marks, a + 1, b - 1)
        }
    }
}

fun printField(array: Array<CharArray>, marks: Array<CharArray>, printFields: Array<BooleanArray>, dead: Boolean) {
    println(" |123456789|\n-|---------|")
    for (i in 1..9) {
        print("$i|")
        for (j in 1..9) {
            if (!printFields[i][j]) {
                if (dead && array[i][j] == 'X') { //bombs when dead
                    print('X')
                } else print('.') //hidden fields
            } else if (marks[i][j] == '*') print('*') //marks
            else if (array[i][j] == '.') print('/') //slashes
            else  //numbers
                print(array[i][j])
        }
        println("|")
    }
    println("-|---------|")
}

fun printEmptyField() {
    println(" |123456789|\n-|---------|")
    for (i in 1..9) {
        print("$i|")
        for (j in 1..9) {
            print('.')
        }
        println("|")
    }
    println("-|---------|")
}

fun match(array: Array<CharArray>, mines: Array<CharArray>): Boolean {
    for (i in 1..9) {
        for (j in 1..9) {
            if (array[i][j] == 'X' && mines[i][j] != '*') return false
        }
    }
    return true
}

fun setMines(array: Array<CharArray>, n: Int, c: Int, d: Int) {
    var n = n
    val random = Random()
    while (n > 0) {
        val a = random.nextInt(9) + 1
        val b = random.nextInt(9) + 1
        if (array[a][b] != 'X' && !(a == d && b == c)) {
            array[a][b] = 'X'
            n--
        }
    }
    for (i in 1..9) {
        for (j in 1..9) {
            if (array[i][j] == 'X') continue
            var count = 0
            count += if (array[i - 1][j] == 'X') 1 else 0
            count += if (array[i + 1][j] == 'X') 1 else 0
            count += if (array[i][j - 1] == 'X') 1 else 0
            count += if (array[i][j + 1] == 'X') 1 else 0
            count += if (array[i - 1][j + 1] == 'X') 1 else 0
            count += if (array[i - 1][j - 1] == 'X') 1 else 0
            count += if (array[i + 1][j - 1] == 'X') 1 else 0
            count += if (array[i + 1][j + 1] == 'X') 1 else 0
            if (count > 0) array[i][j] = ('0'.code + count).toChar()
        }
    }
}