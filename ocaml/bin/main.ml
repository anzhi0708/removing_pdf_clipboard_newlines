(* Run a command and get the console output *)
let result (cmd : string) : string =
  let fd = Unix.open_process_in cmd in
  let text = In_channel.input_all fd in
  let _ = Unix.close_process_in fd in
  text

let get_clipboard_buffer () : string = result "pbpaste"

let copy_to_clipboard (string_to_copy : string) : unit =
  let cli_cmd = Filename.quote_command "echo" [ string_to_copy ] in
  let _ = Unix.system (cli_cmd ^ "| pbcopy") in
  ()

let remove_newlines (str : string) : string =
  let regex = Str.regexp "\n\\([a-zA-Z\"(]\\)" in
  Str.global_replace regex " \\1" str

let main () : unit =
  let () = Printf.printf "Running...%!" in
  while true do
    let () = Unix.sleep 1 in
    let text_without_newline =
      get_clipboard_buffer () |> remove_newlines |> String.trim
    in
    copy_to_clipboard text_without_newline
  done

let () = main ()
