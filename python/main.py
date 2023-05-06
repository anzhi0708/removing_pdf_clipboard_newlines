#!/usr/bin/env python3


import os
import subprocess
import re
import time
import shlex


PATTERN: str      = r"\n([a-zA-Z\"\(“])"
REGEX: re.Pattern = re.compile(PATTERN)


def remove_new_lines(s: str) -> str:
    return REGEX.sub(r" \1", s).strip()


def get_clipboard_buffer() -> str:
    command: str = "pbpaste"
    with os.popen(command, "r") as output:
        return output.read()


def copy_to_clipboard(s: str) -> None:
    command: str = f"echo {shlex.quote(s)} | pbcopy"
    subprocess.run(["bash", "-c", command])


def main() -> None:
    print("Running...")
    while True:
        time.sleep(1)
        buffer: str = get_clipboard_buffer()
        new_string: str = remove_new_lines(buffer)
        copy_to_clipboard(new_string)


def _test_regex(filename: str = "./test.txt") -> str:
    with open(filename, "r") as file:
        return remove_new_lines(file.read())


if __name__ == "__main__":
    main()
