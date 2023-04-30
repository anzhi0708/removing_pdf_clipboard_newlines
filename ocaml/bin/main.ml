let color_grey : string = "\027[1;30m"
let color_reset : string = "\027[0m"
let grey (s : string) : string = color_grey ^ s ^ color_reset
let animation : string list = [ "-"; "\\"; "|"; "/" ]

let refresh (frame : int ref) : string =
  frame := !frame + 1;
  if !frame = 4 then
    let () = frame := 0 in
    List.nth animation 0
  else List.nth animation !frame

(*
let escape (s : string) (origin : string) (replacement : string) =
  (* Replace every {origin} with {replacement}. *)
  let regex = Str.regexp origin in
  let result = Str.global_replace regex replacement s in
  result
*)

let result (cmd : string) : string =
  (* Run a command and get the console output *)
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
  let regex = Str.regexp "\n\\([a-zA-Z\"]\\)" in
  Str.global_replace regex " \\1" str

let main () : unit =
  let frame_begin : int ref = ref 0 in
  while true do
    let () =
      Printf.printf "%s%!"
        ("\r" ^ grey (refresh frame_begin) ^ grey " Running...")
    in
    let () = Unix.sleep 1 in
    let text_without_newline =
      get_clipboard_buffer () |> remove_newlines |> String.trim
    in
    copy_to_clipboard text_without_newline
  done

let () = main ()
