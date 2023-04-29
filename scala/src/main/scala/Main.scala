import scala.sys.process._
import scala.util.matching.Regex


val colorGrey: String = "\u001b[1;30m"
val colorReset: String = "\u001b[0m"

def grey(s: String): String =
  colorGrey + s + colorReset

val animation: List[String] = List("-", "\\", "|", "/")

class NthFrame(var nthFrame: Int)

def refresh(nthFrame: NthFrame): String =
  nthFrame.nthFrame = nthFrame.nthFrame + 1
  if nthFrame.nthFrame == 4 then {
    nthFrame.nthFrame = 0
    animation(nthFrame.nthFrame)
  } else {
    animation(nthFrame.nthFrame)
  }

def result(cmd: List[String]): String =
  cmd.!!.trim

def getClipboardBuffer: String = result("pbpaste" :: Nil)

def copyToClipboard(toCopy: String): Int =
  val cmd = "bash" :: "-c" :: s"echo '$toCopy' | pbcopy" :: Nil
  Process(cmd).run().exitValue()

def removeNewLines(str: String): String =
  val pattern: Regex = "\\n([a-zA-Z\"])".r
  pattern.replaceAllIn(str, m => s" ${m.group(1)}")

@main def hello: Unit =
  println("Hello world!")
  println(msg)
  var frameBegin = NthFrame(0)
  while true do {
    Thread.sleep(1000)
    val textWithoutNewLine = removeNewLines(getClipboardBuffer)
    val _ = copyToClipboard(textWithoutNewLine)
  }

def msg = "I was compiled by Scala 3. :)"
