import scala.sys.process._
import scala.util.matching.Regex


// Run a command and get the output.
def result(cmd: List[String]): String =
  cmd.!!.trim

def getClipboardBuffer: String = result("pbpaste" :: Nil)

// Copy a string to clipboard.
def copyToClipboard(toCopy: String): Int =
  val string  = toCopy.replace("\"", "\\\"").replace("'", "\'")
  val payload = s"""echo "$string" | pbcopy"""
  val cmd     = "bash" :: "-c" :: payload :: Nil
  Process(cmd).run().exitValue()

def removeNewLines(str: String): String =
  val pattern: Regex = "\n([a-zA-Z\"])".r
  pattern.replaceAllIn(str, m => s" ${m.group(1)}")

@main def hello: Unit =
  println("Hello world!")
  println(msg)
  while true do {
    Thread.sleep(1000)
    val textWithoutNewLine = removeNewLines(getClipboardBuffer)
    val _                  = copyToClipboard(textWithoutNewLine)
  }

def msg = "I was compiled by Scala 3. :)"


