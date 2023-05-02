use regex;
use std::io;
use std::process::{Command, Stdio};
use std::thread::sleep;
use std::time::Duration;

fn result(cmd: &str) -> String {
    let output = Command::new(cmd).output().expect("Failed to execute command");
    String::from_utf8(output.stdout).expect("Failed to convert output to UTF-8")
}

fn get_clipboard_buffer() -> String {
    result("pbpaste")
}

fn copy_to_clipboard(string_to_copy: &str) {
    let mut echo = Command::new("echo")
        .arg(string_to_copy)
        .stdout(Stdio::piped())
        .spawn()
        .expect("Failed to start echo process");
    let mut pbcopy = Command::new("pbcopy")
        .stdin(Stdio::piped())
        .spawn()
        .expect("Failed to start pbcopy process");

    if let Some(ref mut stdin) = pbcopy.stdin {
        let mut echo_stdout = echo.stdout.take().unwrap();
        io::copy(&mut echo_stdout, stdin).expect("Failed to copy data");
    }

    echo.wait().expect("Failed to wait on echo process");
    pbcopy.wait().expect("Failed to wait on pbcopy process");
}

fn remove_newlines(s: &str) -> String {
    let re = regex::Regex::new("\n([a-zA-Z\"])").unwrap();
    re.replace_all(s, " $1").to_string()
}

fn main() {
    println!("Running...");
    loop {
        sleep(Duration::from_secs(1));
        let text_without_newline = remove_newlines(&get_clipboard_buffer()).trim().to_string();
        copy_to_clipboard(&text_without_newline);
    }
}
