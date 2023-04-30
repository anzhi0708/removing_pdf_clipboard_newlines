#include <algorithm>
#include <array>
#include <chrono>
#include <iostream>
#include <regex>
#include <string>
#include <thread>

// Run a command and get the console output
auto
result(const std::string& cmd)
{
  std::array<char, 128> buffer;
  std::string result;
  auto pipe = popen(cmd.c_str(), "r");

  if (!pipe) {
    throw std::runtime_error("popen() failed!");
  }

  while (fgets(buffer.data(), buffer.size(), pipe) != nullptr) {
    result += buffer.data();
  }

  auto returnCode = pclose(pipe);

  if (returnCode != EXIT_SUCCESS) {
    throw std::runtime_error("Error while closing pipe");
  }

  return result;
}

auto
get_clipboard_buffer()
{
  return result("pbpaste");
}

auto
copy_to_clipboard(const std::string& string_to_copy)
{

  std::string escaped_string = string_to_copy;

  std::regex double_quote_regex("\"");
  escaped_string =
    std::regex_replace(escaped_string, double_quote_regex, "\\\"");

  std::regex backtick_regex("`");
  escaped_string = std::regex_replace(escaped_string, backtick_regex, "\\`");

  std::string cli_cmd = "echo \"" + escaped_string + "\" | pbcopy";
  auto returnCode = std::system(cli_cmd.c_str());

  if (returnCode != EXIT_SUCCESS) {
    throw std::runtime_error("Error while copying to clipboard");
  }
}

auto
remove_newlines(const std::string& str)
{
  std::regex regex(R"(\n([a-zA-Z\"]))");
  std::string result = std::regex_replace(str, regex, " $1");
  return result;
}

int
main()
{
  std::cout << "Running..." << std::endl;

  while (true) {
    std::this_thread::sleep_for(std::chrono::seconds(1));
    auto text_without_newline = remove_newlines(get_clipboard_buffer());
    copy_to_clipboard(text_without_newline);
  }

  return 0;
}
